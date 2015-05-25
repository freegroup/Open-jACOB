/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.core.data.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.qbe.QBERelationConstraint;
import de.tif.jacob.core.data.impl.qbe.QBERelationGraph;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataRecordTree/* implements IDataRecordTree*/
{
  static public transient final String        RCS_ID = "$Id: DataRecordTree.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
//  static private final transient Log logger = LogFactory.getLog(DataRecordTree.class);

  private final DataAccessor accessor;
  Class nodeTemplate;
  List propagatedAliases = new ArrayList();
  
	public DataRecordTree(DataAccessor accessor)
	{
    this.accessor = accessor;
	}
	
	// IBIS: kommt dann später in den DataAccessor....denke ich.
	public DataRecordTreeNode propagate(IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection) throws Exception
	{
	  return propagate(record,relationSet,filldirection, DataRecordTreeNode.class);
	}
	
	// IBIS: kommt dann später in den DataAccessor....denke ich.
	/**
	 * nodeTemplate must inherit the interface IDataRecordTreeNode
	 */
	public DataRecordTreeNode propagate(IDataTableRecord record, IRelationSet relationSet, Filldirection filldirection, Class nodeTemplate) throws Exception
	{
	  this.nodeTemplate=nodeTemplate;
	  
		// fetch data of connected records
		QBERelationGraph relationGraph = new QBERelationGraph(relationSet, record.getTableAlias());
		IDataTableRecord tableRecord = accessor.propagateRecord(relationGraph, filldirection, (DataRecord)record);
		if (null == tableRecord)
		{
			// could not fetch data from datasource
			throw new Exception("Could not propagate record: "+record);
		}
		// erstmal den Baum mit dem übergebenen Relationset aufbauen
		// (...ausnutzen was man durch eine einmalige Propagation bekommen kann)
		//
		DataRecordTreeNode rootNode= recordToNode(relationGraph, filldirection,tableRecord);
		
		// und danach die Blätter einzeln propagieren....naja nur eine Ebene erstmal
		//
		phase2Processing(rootNode,relationGraph, filldirection);
		
		return rootNode;
	}
	
  private DataRecordTreeNode recordToNode( QBERelationGraph relationGraph, Filldirection filldirection, IDataTableRecord tableRecord) throws Exception
	{
    DataRecordTreeNode node = (DataRecordTreeNode)nodeTemplate.newInstance();
    node.setNodeRecord(tableRecord);
		ITableAlias alias = tableRecord.getTable().getTableAlias();

		// process connected records
		if (filldirection.isForward())
		{
			Iterator iter2 = relationGraph.getForwardRelations(alias);
			while (iter2.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter2.next();
				ITableAlias forwardTableAlias = relation.getToTableAlias();
				IDataTable forwardTable = tableRecord.getTable().getAccessor().getTable(forwardTableAlias);
				if(forwardTable.recordCount()>0)
					propagatedAliases.add(forwardTableAlias);

				for (int i = 0; i < forwardTable.recordCount(); i++)
				{
					node.addChild(recordToNode(relationGraph, filldirection, forwardTable.getRecord(i)));
				}
			}
		}
		
		if (filldirection.isBackward())
		{
			Iterator iter2 = relationGraph.getBackwardRelations(alias);
			while (iter2.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter2.next();
				ITableAlias backwardTableAlias = relation.getFromTableAlias();

				IDataTable backwardTable = tableRecord.getTable().getAccessor().getTable(backwardTableAlias);
				IDataTableRecord backwardTableRecord = backwardTable.getSelectedRecord();
				if(backwardTable.recordCount()>0)
					propagatedAliases.add(backwardTableAlias);
				if (null != backwardTableRecord)
				{
					node.addChild(recordToNode(relationGraph, filldirection, backwardTableRecord));
				}
			}
		}
		return node;
	}  
  
  
  // IBIS: duplicate Code. Wird von Andreas Sonntag durch eine performatetre Variante mit internal cache Strukturen
  //       angepasst/verbessert.
  //
  // Ich gehe durch jeden Knoten und propagiere diesen und hänge jede Kind dieses Ergebnisses an den Elternknoten
  // TEUER und nicht korrekt in der Ergebnismenge!!!
  //
  private void phase2Processing(DataRecordTreeNode node, QBERelationGraph relationGraph, Filldirection filldirection) throws Exception
	{
      Iterator iter=node.getChildren().iterator();
      while(iter.hasNext())
      {
        DataRecordTreeNode leaf = (DataRecordTreeNode)iter.next();
        accessor.propagateRecord(relationGraph,filldirection,(DataRecord)leaf.getInternalRecord());
        leafRecordToNode(leaf,relationGraph,filldirection);
        phase2Processing(leaf,relationGraph, filldirection);
      }
	}
  
  private DataRecordTreeNode leafRecordToNode(DataRecordTreeNode leafRecord, QBERelationGraph relationGraph, Filldirection filldirection) throws Exception
	{
		ITableAlias alias = leafRecord.getTable().getTableAlias();

		// process connected records
		if (filldirection.isForward())
		{
			Iterator iter2 = relationGraph.getForwardRelations(alias);
			while (iter2.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter2.next();
				ITableAlias forwardTableAlias = relation.getToTableAlias();
				if(!propagatedAliases.contains(forwardTableAlias))
				{
					IDataTable forwardTable = leafRecord.getTable().getAccessor().getTable(forwardTableAlias);
					for (int i = 0; i < forwardTable.recordCount(); i++)
					{
					  leafRecord.addChild(recordToNode(relationGraph, filldirection, forwardTable.getRecord(i)));
					}
				}
			}
		}
		
		if (filldirection.isBackward())
		{
			Iterator iter2 = relationGraph.getBackwardRelations(alias);
			while (iter2.hasNext())
			{
				QBERelationConstraint relation = (QBERelationConstraint) iter2.next();
				ITableAlias backwardTableAlias = relation.getFromTableAlias();

				IDataTable backwardTable = leafRecord.getTable().getAccessor().getTable(backwardTableAlias);
				IDataTableRecord backwardTableRecord = backwardTable.getSelectedRecord();
				if(!propagatedAliases.contains(backwardTableAlias))
				{
					if (null != backwardTableRecord)
					{
					  leafRecord.addChild(recordToNode(relationGraph, filldirection, backwardTableRecord));
					}
				}
			}
		}
		return leafRecord;
	}  
}

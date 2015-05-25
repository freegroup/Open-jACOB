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

package de.tif.jacob.core.data.impl.qbe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.data.impl.misc.InvalidTableAliasException;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableAlias.ITableAliasConditionAdjuster;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class QBESpecification implements IDataFieldConstraints
{
  static public transient final String RCS_ID = "$Id: QBESpecification.java,v 1.3 2009/11/27 21:17:14 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.3 $";
  
  static private final transient Log logger = LogFactory.getLog(QBESpecification.class);

  // Map<QBEField-> Integer(fieldIndex)>
  private final Map fieldsToReturnMap;
  
  // List<QBEField>
	private final List fieldsToReturnList;
	
	private final ITableAlias aliasToSearch;
  
  // Set<ITableAlias>
	private final Set aliasesToQuery;
	
  // List<QBEFieldConstraint>
  private final List fieldConstraints;
  
  // List<QBEFieldConstraint>
  private final List allFieldConstraints;
  
	// Set<QBERelationConstraint>
	private final Set relationConstraints;
	
	// Set<QBERelationConstraint>
	private final Set inverseRelationConstraints;
	
  // List<Integer>
  private final List keyIndices;
  
  // List<QBEUserConstraint>
  private final List userConstraints;
  
  // Set<ITableAlias>
  private Set aliasesOfConditionsAdded;

  // List<QBEField>
  private final List unmodifiableFieldsToReturnList;
  
  private boolean distinct = false;
  
	public QBESpecification(ITableAlias aliasToSearch)
	{
		// init members
    this.aliasToSearch = aliasToSearch;
		this.fieldsToReturnMap = new HashMap();
		this.fieldsToReturnList = new ArrayList();
		this.aliasesToQuery = new HashSet();
		this.fieldConstraints = new ArrayList();
		this.allFieldConstraints = new ArrayList();
		this.relationConstraints = new HashSet();
		this.inverseRelationConstraints = new HashSet();
		this.keyIndices = new ArrayList();
    this.unmodifiableFieldsToReturnList = Collections.unmodifiableList(this.fieldsToReturnList);
    this.userConstraints = new ArrayList();
    this.aliasesToQuery.add(aliasToSearch);
  }
	
	public void addCondition(ITableAlias alias, ITableAliasConditionAdjuster adjuster, QBERelationGraph relationGraph)
  {
    try
    {
      ITableAliasCondition aliasCondition = alias.getCondition(adjuster);
      if (aliasCondition != null)
      {
        if (aliasesOfConditionsAdded == null)
          aliasesOfConditionsAdded = new HashSet();

        // avoid to add the same alias condition multiple times
        if (aliasesOfConditionsAdded.add(alias))
        {
          addCondition(aliasCondition, relationGraph);
        }
      }
    }
    catch (InvalidTableAliasException ex)
    {
      ex.setConditionAlias(alias);
      throw ex;
    }
  }

  public void addCondition(ITableAliasCondition aliasCondition, QBERelationGraph relationGraph)
  {
    try
    {
      this.userConstraints.add(new QBEUserConstraint(aliasCondition.toString()));

      // IBIS : Ein Hack für DataTable, da eigentlich immer ein QBERelationGraph
      // da sein sollte.
      // Probleme zum Bespiel bei DataTable.performRecordCount()
      if (aliasCondition.getForeignAliases().size() > 0 && relationGraph != null)
      {
        Iterator iter = aliasCondition.getForeignAliases().iterator();
        while (iter.hasNext())
        {
          ITableAlias alias = (ITableAlias) iter.next();
          addRelationConstraints(relationGraph.markAsConstrained(alias, aliasCondition.isOptionalForeignAlias(alias)));
        }
      }
    }
    catch (InvalidTableAliasException ex)
    {
      ex.setAliasCondition(aliasCondition);
      throw ex;
    }
  }
  
  /**
   * @return List<QBEField>
   */
  public List getFieldsToQuery()
  {
    return this.unmodifiableFieldsToReturnList; 
  }

  /**
   * @return Iterator<ITableAlias>
   */
  public Iterator getTableAliasesToQuery()
  {
    return this.aliasesToQuery.iterator(); 
  }
  
  /**
   * @return
   */
  public boolean isMultiTableQuery()
  {
    return this.aliasesToQuery.size()>1; 
  }
  
  /**
   * @return
   */
  public boolean hasInverseRelationConstraints()
  {
  	return this.inverseRelationConstraints.size() > 0;
  }

  /**
   * @return Iterator<QBEFieldConstraint>
   */
  public Iterator getFieldConstraints()
  {
    return this.fieldConstraints.iterator(); 
  }

  /**
   * @return Iterator<QBEFieldConstraint>
   */
  public Iterator getAllFieldConstraints()
  {
    return this.allFieldConstraints.iterator(); 
  }

  /**
   * @return Iterator<QBERelationConstraint>
   */
  public Iterator getRelationConstraints()
  {
    return this.relationConstraints.iterator();
  }
  
  /**
   * @return Iterator<QBERelationConstraint>
   */
  public Iterator getAnsiOrderedRelationConstraints()
  {
    if (this.relationConstraints.size() > 1)
    {
      // Return relations in an ordered manner, i.e.
      // return relations starting with alias to search
      // and then connected relations.
      // This is needed to perform proper ANSI joins!
      //

      // IBIS: Make more efficient
      Set aliasesProcessed = new HashSet();
      aliasesProcessed.add(this.aliasToSearch);

      List resultList = new ArrayList(this.relationConstraints.size());

      List workList = new ArrayList(this.relationConstraints.size());
      workList.addAll(this.relationConstraints);

      do
      {
        boolean modified = false;
        ListIterator iter = workList.listIterator();
        while (iter.hasNext())
        {
          QBERelationConstraint relation = (QBERelationConstraint) iter.next();
          if (aliasesProcessed.contains(relation.getToTableAlias()) || //
              aliasesProcessed.contains(relation.getFromTableAlias()))
          {
            modified = true;
            resultList.add(relation);
            aliasesProcessed.add(relation.getToTableAlias());
            aliasesProcessed.add(relation.getFromTableAlias());
            iter.remove();
          }
        }
        if (!modified)
        {
          // should never occur
          throw new IllegalStateException("Inconsistent relations");
        }
      }
      while (workList.size() != 0);

      return resultList.iterator();
    }

    return this.relationConstraints.iterator();
  }

  /**
   * @return Iterator<QBERelationConstraint>
   */
  public Iterator getInverseRelationConstraints()
  {
  	return this.inverseRelationConstraints.iterator(); 
  }

  /**
   * @param field
   */
  public void addKeyField(QBEField field)
  {
    this.keyIndices.add(add(field, false));
  }

  /**
   * @param field
   */
  public void addRecordField(QBEField field)
  {
    add(field, true);
  }
  
  private Integer add(QBEField field, boolean enforceAdd)
	{
		Integer fieldIndex = (Integer) this.fieldsToReturnMap.get(field);

		if (null == fieldIndex)
		{
			// field not added so far
			fieldIndex = new Integer(this.fieldsToReturnList.size());
			this.fieldsToReturnMap.put(field, fieldIndex);
			this.fieldsToReturnList.add(field);
			if (!field.isKeepEmpty())
			{
				this.aliasesToQuery.add(field.getTableAlias());
			}
		}
		else
		{
			if (enforceAdd)
			{
				// to ensure, that similar fields may occur in multiple columns for
				// browser definitions
				this.fieldsToReturnList.add(field);
			}
		}
		return fieldIndex;
	}

	/**
	 * @param fieldConstraint
	 */
	public void add(QBEFieldConstraint fieldConstraint)
	{
    if (!fieldConstraint.getTableField().getType().isConstrainable())
      throw new UnsupportedOperationException();
    
    if (logger.isTraceEnabled())
      logger.trace("QBESpecification.add(QBEFieldConstraint): "+fieldConstraint);
    
    this.fieldConstraints.add(fieldConstraint);
    this.allFieldConstraints.add(fieldConstraint);
		this.aliasesToQuery.add(fieldConstraint.getTableAlias());
	}

  /**
   * @param relationConstraint
   */
  public void addInverse(QBERelationConstraint relationConstraint, QBEFieldConstraint inverseFieldConstraint)
  {
    add(relationConstraint, true);
    this.allFieldConstraints.add(inverseFieldConstraint);
  }
  
  private void add(QBERelationConstraint relationConstraint, boolean isInverseRelation)
  {
    if (logger.isTraceEnabled())
      logger.trace("QBESpecification.add(QBERelationConstraint): "+relationConstraint+", inverse="+isInverseRelation);
    
    if (isInverseRelation)
    {
    	this.inverseRelationConstraints.add(relationConstraint);
    	this.aliasesToQuery.add(relationConstraint.getFromTableAlias());
    }
    else
    {
    	this.relationConstraints.add(relationConstraint);
    	this.aliasesToQuery.add(relationConstraint.getFromTableAlias());
    	this.aliasesToQuery.add(relationConstraint.getToTableAlias());
    	if (relationConstraint.isForwardFlag())
    	{
    		this.distinct = true;
    	}
    }
  }

  public void addRelationConstraints(Iterator relationConstraints)
	{
		while (relationConstraints.hasNext())
		{
			add((QBERelationConstraint) relationConstraints.next(), false);
		}
	}

	/**
	 * @return Returns the userConstraints.
	 */
	public List getUserConstraints()
	{
		return this.userConstraints;
	}

	/**
	 * @return Returns the keyIndices.
	 */
	public int[] getPrimaryKeyIndices()
	{
    int[] primaryKeyIndices = new int [this.keyIndices.size()];
    for (int i=0; i < this.keyIndices.size(); i++)
    {
      primaryKeyIndices [i] = ((Integer) this.keyIndices.get(i)).intValue();
    }
		return primaryKeyIndices;
	}
  
	/**
	 * @return Returns the distinct.
	 */
	public boolean isDistinct()
	{
		return distinct;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataFieldConstraints#getConstraints()
	 */
	public Iterator getConstraints()
	{
		return new ConstraintIterator();
	}

	/**
	 * @return Returns the aliasToSearch.
	 */
	public ITableAlias getAliasToSearch()
	{
		return aliasToSearch;
	}

  private class ConstraintIterator implements Iterator
  {
    private int index = 0;
    private QBEFieldConstraint next = null;
    ConstraintIterator()
    {
      iterate();
    }
    
    private void iterate()
    {
    	if (this.next != null)
      {
    		this.next = this.next.getLinkedConstraint();
      }
      
      if (this.next == null && fieldConstraints.size() > this.index)
      {
      	this.next = (QBEFieldConstraint) fieldConstraints.get(this.index);
        this.index++;
      }
    }
    
  		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return this.next != null;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public Object next()
		{
      Object result = this.next;
      iterate();
			return result;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
      throw new UnsupportedOperationException();
		}
  }
}

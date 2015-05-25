/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on Jul 15, 2004
 */
package de.tif.jacob.designer.editor.relationset.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.util.Trace;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * Command to create a new relationship between tables
 * 
 * @author Andreas Herz
 */
public class CreateRelationCommand extends Command
{
	protected final RelationsetModel relationset;
	protected final TableAliasModel foreignTable; // die 'Besen' Seite, oder auch die 'Start' seite
	protected TableAliasModel  primaryTable;

	/** The relationship between primary and foreign key tables * */
	protected RelationModel relation;
	
	/** The target (primary key) table * */
	private  List possibleKeys = new ArrayList();
	
	public CreateRelationCommand(RelationsetModel relationset, TableAliasModel foreignTable)
	{
	  this.relationset = relationset;
	  this.foreignTable = foreignTable;
	}

  /**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute()
	{
		if (foreignTable.equals(primaryTable))
			return false;

		if (primaryTable == null)
			return false;

		// 1. Möglichkeit
		//
		// Es besteht eine Relation die genau das gewünschte Ausdrückt.
		// Diese wird dann eingefügt.
		//
		relation =relationset.getJacobModel().getRelationModel(primaryTable,foreignTable);
		if(relation!=null)
		  return true;

		// 2. Möglichkeit
		//
		// Es gibt in der Starttabelle einen Foreingkey welche noch auf keine andere Tabelle zeigt
		// und von den Columntypen auf den pkey der Zieltabelle passt.
		//
		possibleKeys.clear();
		KeyModel primKey     = primaryTable.getPrimaryKeyModel();
		List     foreignKeys = foreignTable.getMatchedForeingKeyModels(primKey); 
		Iterator iter = foreignKeys.iterator();
		while(iter.hasNext())
		{
		  KeyModel key = (KeyModel)iter.next();
	    possibleKeys.add(key);
	    for (RelationModel r : primaryTable.getJacobModel().getRelationModelsTo(foreignTable))
      {
        if(r.getToKey()!=key)
        {
  		    //System.out.println("MATCH FOUND (unused)---------------------------------------------------");
        }
        else if(r.getFromTableAlias().getTableModel() == primaryTable.getTableModel())
        {
  		    //System.out.println("MATCH FOUND (gleiche tabelle)------------------------------------------");
        }
        else
        {
  		    //System.out.println("KEY zeigt bereits auf eine andere tabelle -----------------------------");
  		    possibleKeys.remove(key);
  		    break;
        }
      }
		}
		
		return true;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
    Trace.start("CreateRelationCommand");
    try
    {
      if(possibleKeys.size()>=1)
  	  {
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(null, new LabelProvider()
        {
          public Image getImage(Object element)
          {
            if(element instanceof String)
              return JacobDesigner.getImage("new_foreingkey.png");
            return JacobDesigner.getImage(((ObjectModel) element).getImageName());
          }
  
          public String getText(Object element)
          {
            if(element instanceof String)
              return (String)element;
            
            return ((KeyModel) element).getName();
          }
        });
        dialog.setImage(JacobDesigner.getImage("select_foreignkey.gif"));
        dialog.setTitle("Select foreign key.");
        dialog.setMessage("Select foreign key from <"+foreignTable.getName()+"> to use for relation.");
        possibleKeys.add(0,"<<new>>");
        dialog.setElements(possibleKeys.toArray());
        
        dialog.create();
        if(dialog.open()==Window.OK)
        {
          Object obj= dialog.getFirstResult();
          if(!(obj instanceof String))
          {
            KeyModel key = (KeyModel)obj;
            relation = new RelationModel( primaryTable,foreignTable, key);
            relationset.getJacobModel().addElement(relation);
          }
        }
        else
          return;
  	  }
  	  // es wurde keine relations gefunden und auch kein freier fkey welchen man
  	  // verwenden könnte -> neue felder anlegen
  	  //
  	  if(relation==null)
  	  {
        Trace.mark();
  	    KeyModel key= foreignTable.getTableModel().createMatchingForeignKey(primaryTable.getName(),primaryTable.getTableModel().getPrimaryKeyModel()); 
        Trace.print("createMatchingforeignKey");
  	    relation = new RelationModel( primaryTable,foreignTable, key);
        Trace.print("new RelationModel(...)");
  	    relationset.getJacobModel().addElement(relation);
        Trace.print("add relation to jacob model");
  	  }
      Trace.mark();
      relationset.addElement(relation);
      Trace.print("add relation to relationset");
    }
    finally
    {
      Trace.stop("CreateRelationCommand");
    }
	}


	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo()
	{
	  if(relation!=null)
	  {
	    relationset.getJacobModel().addElement(relation);
	    relationset.addElement(relation);
	  }
	}

	/**
	 * Undo version of command
	 */
	public void undo()
	{
	  if(relation!=null)
	  {
			relationset.removeElement(relation);
			relationset.getJacobModel().removeElement(relation);
	  }
	}


	/**
	 * @param primaryTable
	 *            The primaryTable to set.
	 */
	public void setPrimaryTable(TableAliasModel primaryTable)
	{
		this.primaryTable = primaryTable;
	}

}

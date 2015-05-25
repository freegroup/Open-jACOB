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
 * Created on 03.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;

import de.tif.jacob.designer.actions.ShowTableEditorAction;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class TreeTableParent extends TreeParent implements DblClickObject
{
//  private final TableModel model;

  public TreeTableParent(TreeViewer viewer, TreeParent parent, JacobModel jacob, TableModel model)
  {
    super(viewer, parent, jacob, model, model.getName());
    model.getJacobModel().addPropertyChangeListener(this);
  }
  
	public void addChildren()
	{
	  if(getTableModel().getPrimaryKeyModel()!=null)
	    addChild(new TreeKeyObject(viewer, this, jacobModel, getTableModel(), getTableModel().getPrimaryKeyModel()));
	  
    List keys = getTableModel().getKeyModels();
    for (int i = 0; i < keys.size(); i++)
    {
      KeyModel key = (KeyModel) keys.get(i);
      if(key.getType()!= KeyModel.DBTYPE_PRIMARY && key.getType()!= KeyModel.DBTYPE_INDEX&& key.getType()!= KeyModel.DBTYPE_UNIQUE)
        addChild(new TreeKeyObject(viewer, this, jacobModel, getTableModel(), key));
    }

    keys = getTableModel().getKeyModels();
    for (int i = 0; i < keys.size(); i++)
    {
      KeyModel key = (KeyModel) keys.get(i);
      if( key.getType()== KeyModel.DBTYPE_INDEX || key.getType()== KeyModel.DBTYPE_UNIQUE)
        addChild(new TreeKeyObject(viewer, this, jacobModel, getTableModel(), key));
    }

    List fields = getTableModel().getFieldModels();
    for (int i = 0; i < fields.size(); i++)
    {
      this.addChild(new TreeTableFieldObject(viewer, this, jacobModel, getTableModel(), (FieldModel) fields.get(i)));
    }
	}

  public void dispose()
  {
    model.getJacobModel().removePropertyChangeListener(this);
    super.dispose();
  }
  
  public String getName()
  {
    return getTableModel().getName();
  }

  public void setName(String newName) throws Exception
  {
    super.setName(newName);
    getTableModel().setName(newName);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    return true;
  }
  
  public TableModel getTableModel()
  {
    return (TableModel)getModel();
  }

  public void propertyChange(PropertyChangeEvent ev)
  {
    super.propertyChange(ev);

    if (ev.getPropertyName()==ObjectModel.PROPERTY_HISTORY_FIELD_CHANGED)
    {
      refreshVisual();
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
    {
      refreshVisual();
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CHANGED)
    {
		  refreshVisual(true);
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_ADDED)
    {
		  refreshVisual(true);
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_DELETED)
    {
      refreshVisual(true);
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_CHANGED)
    {
		  refreshVisual();
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_KEY_CHANGED)
    {
		  refreshVisual();
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_KEY_ADDED)
    {
		  refreshVisual(true);
    }
    else if (ev.getPropertyName()==ObjectModel.PROPERTY_KEY_DELETED)
    {
		  refreshVisual(true);
    }
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ALIAS_CREATED)
    {
      TableAliasModel alias =(TableAliasModel)ev.getNewValue();
      if(alias.getTableModel()==model)
  		  refreshVisual();
    }
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_ALIAS_DELETED)
    {
      TableAliasModel alias =(TableAliasModel)ev.getOldValue();
      if(alias.getTableModel()==model)
  		  refreshVisual();
    }
  }

  public void dblClick()
  {
    // reuse the object selection contribution to use a common method to oopen
    // the table editor.
    //
    new ShowTableEditorAction()
    {
      public TableModel getTableModel() {return (TableModel)model; }
    }.run(null);
  }
  
  /**
	 * No direct edit in the tree
	 */
	public boolean hasDirectEdit()
	{
	  return true;
	}

  public void dragStart(DragSourceEvent event)
  {
    event.doit=true;
  }
}

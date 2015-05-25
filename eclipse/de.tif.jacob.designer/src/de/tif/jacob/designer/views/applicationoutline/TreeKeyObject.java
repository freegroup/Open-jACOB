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
 * Created on 05.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.tif.jacob.designer.actions.ShowJacobFormEditorAction;
import de.tif.jacob.designer.actions.ShowTableKeyEditorAction;
import de.tif.jacob.designer.editor.table.JacobTableEditor;
import de.tif.jacob.designer.editor.table.JacobTableEditorInput;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.model.UIJacobFormModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreeKeyObject extends TreeParent implements DblClickObject
{
	private TableModel table;

	public TreeKeyObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, TableModel table, KeyModel model) 
	{
		super(viewer, parent, jacob, model,  model.getName());
		
		this.table = table;
	}

  public void addChildren()
  {
    List fields = getKeyModel().getFieldModels();
    for (int i = 0; i < fields.size(); i++)
    {
      this.addChild(new TreeKeyFieldObject(viewer, this, jacobModel, getKeyModel(), (FieldModel) fields.get(i)));
    }
  }


  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName() 
	{
		return getKeyModel().getName();
	}
	
	/**
	 * direct edit in the Tree
	 */
  public void setName(String name) throws Exception
  {
    getKeyModel().setName(name);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    // Keys sollen nicht alphanumerisch sortiert sein, sondern manuell sortiert werden können
    return false;
  }

	/* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getSortingCategory()
   */
  public int getSortingCategory()
  {
    if (KeyModel.DBTYPE_PRIMARY.equals(getKeyModel().getType()))
    {
      // ensure primary key listed first
      return 0;
    }
    
    // all other keys are listed afterwards, but before table fields!
    return 1;
  }

  public void dblClick()
  {
    new ShowTableKeyEditorAction()
    {
      public KeyModel getKeyModel()
      {
        return (KeyModel)model;
      }
    }.run(null);
  }
	
  public void propertyChange(PropertyChangeEvent evt)
  {
    super.propertyChange(evt);

    if (evt.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		  refreshVisual();
		else if (evt.getPropertyName()==ObjectModel.PROPERTY_FIELDS_CHANGED)
		  refreshVisual(true);
		else if (evt.getPropertyName()==ObjectModel.PROPERTY_KEY_CHANGED)
		  refreshVisual();
  }
  
  public KeyModel getKeyModel()
  {
    return (KeyModel)model;
  }
  
  /**
   * No direct edit in the tree
   */
	public boolean hasDirectEdit()
	{
	  return true;
	}
}

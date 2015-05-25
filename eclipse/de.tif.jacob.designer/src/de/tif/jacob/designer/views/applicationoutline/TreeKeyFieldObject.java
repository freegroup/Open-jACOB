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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;

import de.tif.jacob.designer.actions.ShowTableFieldEditorAction;
import de.tif.jacob.designer.actions.ShowTableKeyEditorAction;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.KeyModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreeKeyFieldObject extends TreeModelObject implements DblClickObject
{
  KeyModel keyModel;
  
	public TreeKeyFieldObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, KeyModel key, FieldModel model) 
	{
		super(viewer, parent, jacob, model, model.getName());
		jacobModel.addPropertyChangeListener(this);
    this.keyModel = key;
	}

  public void dispose()
  {
		jacobModel.removePropertyChangeListener(this);
		super.dispose();
  }

  public FieldModel getFieldModel()
  {
    return (FieldModel)model;
  }
  

  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName() 
	{
		return getFieldModel().getName();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    // Fields sollen nicht alphanumerisch sortiert sein, sondern manuell sortiert werden können
    return false;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getSortingString()
   */
  public String getSortingString()
  {
    return getFieldModel().getName();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getSortingCategory()
   */
  public int getSortingCategory()
  {
    // return big number to ensure that table field are listed after table keys
    return 10;
  }
  
	public void dblClick()
  {
    new ShowTableKeyEditorAction()
    {
      public KeyModel getKeyModel()
      {
        return keyModel;
      }
    }.run(null);
  }
	
  @Override
  public boolean hasDirectEdit()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent ev)
  {
    super.propertyChange(ev);

    if (ev.getPropertyName()==FieldModel.PROPERTY_FIELD_DELETED && ev.getOldValue()==getModel())
		{
		  getParent().removeChild(this);
		  refreshVisual();
		}
    else if(ev.getSource() == model)
      refreshVisual();
    else if(getFieldModel().getLabel().startsWith("%"))
		{
		  if (ev.getPropertyName()==FieldModel.PROPERTY_I18NKEY_CREATED && ev.getNewValue().equals(getFieldModel().getLabel().substring(1)))
		    refreshVisual();
		  else if (ev.getPropertyName()==FieldModel.PROPERTY_I18NKEY_REMOVE  && ev.getOldValue().equals(getFieldModel().getLabel().substring(1)))
		    refreshVisual();
		}
  }


}

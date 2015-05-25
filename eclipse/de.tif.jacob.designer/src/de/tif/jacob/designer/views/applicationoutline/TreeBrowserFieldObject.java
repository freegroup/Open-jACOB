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
import java.beans.PropertyChangeListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.actions.ShowBrowserEditorAction;
import de.tif.jacob.designer.actions.ShowTableFieldEditorAction;
import de.tif.jacob.designer.editor.browser.BrowserColumnSelectionDialog;
import de.tif.jacob.designer.model.BrowserFieldModel;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableModel;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreeBrowserFieldObject extends TreeModelObject implements DblClickObject
{
	public TreeBrowserFieldObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, BrowserFieldModel model) 
	{
		super(viewer, parent, jacob,model, model.getName());
		jacobModel.addPropertyChangeListener(this);
	}

	
  public void dispose()
  {
		jacobModel.removePropertyChangeListener(this);

		super.dispose();
  }
  
	public BrowserFieldModel getBrowserFieldModel()
  {
    return (BrowserFieldModel)model;
  }
  

	public void dblClick()
  {
	  new ShowBrowserEditorAction()
    {
      public BrowserModel getBrowserModel()
      {
        return TreeBrowserFieldObject.this.getBrowserFieldModel().getBrowserModel();
      }
    }.run(null);
  }

  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    // Sortierreihenfolge sollte im Baum so angezeigt werden, wie
    // diese dann später auch im Browser dargestellt wird
    return false;
  }
  
  public boolean hasDirectEdit()
  {
    return false;
  }
  
  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent ev)
  {
		if (ev.getPropertyName()==FieldModel.PROPERTY_FIELD_DELETED)
		{
		  getParent().removeChild(this);
		  refreshVisual();
		}
		if(getBrowserFieldModel().getLabel().startsWith("%"))
		{
		  if (ev.getPropertyName()==FieldModel.PROPERTY_I18NKEY_CREATED && ev.getNewValue().equals(getBrowserFieldModel().getLabel().substring(1)))
		    refreshVisual();
		  else if (ev.getPropertyName()==FieldModel.PROPERTY_I18NKEY_REMOVE  && ev.getOldValue().equals(getBrowserFieldModel().getLabel().substring(1)))
		    refreshVisual();
		}
		else if(ev.getSource() == model)
		  refreshVisual();
    super.propertyChange(ev);
  }
}

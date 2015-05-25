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
 * Created on 14.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.actions.ShowTableAliasEditorAction;
import de.tif.jacob.designer.model.BrowserModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.TableModel;
import de.tif.jacob.designer.util.ModelTransfer;

/**
 * @author andreas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeTableAliasObject extends TreeModelObject implements DblClickObject
{
  public TreeTableAliasObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, TableAliasModel model)
  {
    super(viewer, parent, jacob, model, model.getName());
		jacobModel.addPropertyChangeListener(this);
	}

  public void dispose()
  {
		jacobModel.removePropertyChangeListener(this);
		super.dispose();
  }


  public TableAliasModel getTableAliasModel()
  {
    return (TableAliasModel)model;
  }

  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName()
  {
    return getTableAliasModel().getName();
  }

  public void setName(String newName) throws Exception
  {
    getTableAliasModel().setName(newName);
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

  public void propertyChange(PropertyChangeEvent ev)
  {
    super.propertyChange(ev);

    if (ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CREATED)
		  return;
    if (ev.getPropertyName()==ObjectModel.PROPERTY_ALIAS_CREATED)
		  return;
    
    if (ev.getPropertyName()==ObjectModel.PROPERTY_TABLE_CHANGED && ev.getSource()==getModel())
      refreshVisual();
    if (ev.getPropertyName()==ObjectModel.PROPERTY_EVENTHANDLER_CHANGED && ev.getSource()==getModel())
      refreshVisual();
    if (ev.getPropertyName()==ObjectModel.PROPERTY_NAME_CHANGED)
		  refreshVisual();
    if (ev.getPropertyName()==ObjectModel.PROPERTY_CONDITION_CHANGED)
		  refreshVisual();
    if (ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_CREATED && ((BrowserModel)ev.getNewValue()).getTableAliasModel()==model)
		  refreshVisual();
    if (ev.getPropertyName()==ObjectModel.PROPERTY_BROWSER_DELETED  && ((BrowserModel)ev.getOldValue()).getTableAliasModel()==model)
		  refreshVisual();
  }

  public void dblClick()
  {
    // reuse the object selection contribution to use a common method to oopen
    // the table editor.
    //
    new ShowTableAliasEditorAction()
    {
      public TableAliasModel getTableAliasModel() {return (TableAliasModel)model; }
    }.run(null);
  }
  
	/**
	 * No direct edit in the tree
	 */
	public boolean hasDirectEdit()
	{
	  return true;
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
   */
  public void dragStart(DragSourceEvent event)
  {
    event.doit = true;
  }
}

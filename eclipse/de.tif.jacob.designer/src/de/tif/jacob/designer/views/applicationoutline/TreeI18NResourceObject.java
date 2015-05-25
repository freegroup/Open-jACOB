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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.actions.ShowI18NEditorAction;
import de.tif.jacob.designer.model.I18NResourceModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * @author andreas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeI18NResourceObject extends TreeModelObject implements DblClickObject
{
  public TreeI18NResourceObject(TreeViewer viewer, TreeParent parent, JacobModel jacob, I18NResourceModel model)
  {
    super(viewer, parent, jacob,model,  model.getName());
    jacobModel.addPropertyChangeListener(this);
  }

  
  public void dispose()
  {
    super.dispose();
    jacobModel.removePropertyChangeListener(this);
  }


  public I18NResourceModel getI18NResourceModel()
  {
    return (I18NResourceModel)model;
  }

  public final String getLabel()
  {
    return getModel().getExtendedDescriptionLabel();
  }
  
  public String getName()
  {
    return getI18NResourceModel().getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#sortingEnabled()
   */
  public boolean sortingEnabled()
  {
    return false;
  }

  public void propertyChange(PropertyChangeEvent ev)
  {
    super.propertyChange(ev);

    if(ev.getSource()==model)
		  refreshVisual();
    else if(ev.getPropertyName() == ObjectModel.PROPERTY_TESTRESOURCEBUNDLE_CHANGED)
      refreshVisual();
  }

  
	public void dblClick()
  {
	  new ShowI18NEditorAction()
    {
      public JacobModel getJacobModel()
      {
        return jacobModel;
      }
    }.run(null);
  }  

	/**
	 * No direct edit in the tree
	 */
	public boolean hasDirectEdit()
	{
	  return false;
	}
}

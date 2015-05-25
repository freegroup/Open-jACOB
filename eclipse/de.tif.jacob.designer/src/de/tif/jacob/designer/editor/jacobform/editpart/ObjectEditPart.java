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
package de.tif.jacob.designer.editor.jacobform.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.ui.IActionFilter;

import de.tif.jacob.designer.editor.jacobform.editpolicies.ComponentEditPolicyForwardDelete;
import de.tif.jacob.designer.editor.jacobform.figures.ObjectFigure;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.util.UIUtils;

public abstract class ObjectEditPart	extends AbstractGraphicalEditPart	implements PropertyChangeListener, IActionFilter
{

	public ObjectFigure getObjectFigure()
	{
	  return (ObjectFigure)getFigure();
	}

	public ObjectModel getObjectModel()
	{
	  return (ObjectModel)getModel();
	}
  
  public void activate()
  {
    super.activate();
    
    ObjectModel objectModel = (ObjectModel)getModel();
    objectModel.addPropertyChangeListener(this);
    if(objectModel.getJacobModel()!=null)
      objectModel.getJacobModel().addPropertyChangeListener(this);
  }

  public void deactivate()
  {
    super.deactivate();
    ObjectModel objectModel = (ObjectModel)getModel();
    objectModel.removePropertyChangeListener(this);
    if(objectModel.getJacobModel()!=null)
      objectModel.getJacobModel().removePropertyChangeListener(this);
  }

	
	public void propertyChange(PropertyChangeEvent ev)
	{
	}
	
	public void createEditPolicies()
	{
	  installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicyForwardDelete(){});
	}

  public boolean testAttribute(Object target, String name, String value)
  {
    if("KEY_ALT".equals(name))
    {
      return Boolean.parseBoolean(value)==UIUtils.isAltKeyPressed();
    }
    return true;
  }
}

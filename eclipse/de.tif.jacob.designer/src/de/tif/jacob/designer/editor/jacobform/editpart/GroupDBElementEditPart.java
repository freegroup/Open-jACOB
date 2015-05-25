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
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.UIDBLocalInputFieldModel;


public abstract class GroupDBElementEditPart extends GroupElementEditPart
{
  public GroupDBElementEditPart(Class figureClass)
  {
    super(figureClass);
  }
  
	public void activate()
	{
		super.activate();
		
		if(getModel() instanceof UIDBLocalInputFieldModel)
		{
		  UIDBLocalInputFieldModel formElement = (UIDBLocalInputFieldModel)getModel();
		  formElement.getFieldModel().addPropertyChangeListener(this);
		}
	}

	public void deactivate()
	{
		super.deactivate();
		if(getModel() instanceof UIDBLocalInputFieldModel)
		{
		  UIDBLocalInputFieldModel formElement = (UIDBLocalInputFieldModel)getModel();
		  formElement.getFieldModel().removePropertyChangeListener(this);
		}
	}
	
  public void propertyChange(PropertyChangeEvent ev)
  {
    if(ev.getSource() instanceof FieldModel && ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_DELETED)
   		refreshVisuals();
    else if(ev.getPropertyName()==ObjectModel.PROPERTY_FIELD_CHANGED)
      refreshVisuals();
    else
      super.propertyChange(ev);
  }
}


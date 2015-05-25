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
package de.tif.jacob.designer.editor.relationset.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import de.tif.jacob.designer.model.ObjectModel;

/**
 * An abstract EditPart implementation which is property aware and responds to
 * PropertyChangeEvents fired from the model
 * @author Phil Zoio
 */
public abstract class PropertyAwareEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener
{
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate()
	{
		super.activate();
		ObjectModel propertyAwareObject = (ObjectModel) getModel();
		propertyAwareObject.addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate()
	{
		super.deactivate();
		ObjectModel propertyAwareObject = (ObjectModel) getModel();
		propertyAwareObject.removePropertyChangeListener(this);
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
    if (evt.getPropertyName() == ObjectModel.PROPERTY_RELATIONSET_CHANGED)
		{
			refreshTargetConnections();
			refreshSourceConnections();
		}
		else if (evt.getPropertyName() == ObjectModel.PROPERTY_CONSTRAINT_CHANGED)
		{
			handleBoundsChange(evt);
		}
	}

	protected void handleBoundsChange(PropertyChangeEvent evt)
	{
	}
}
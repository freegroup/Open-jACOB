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
package de.tif.jacob.designer.editor.relationset.policy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import de.tif.jacob.designer.editor.relationset.command.AddTableAliasCommand;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.model.RelationsetModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * Handles creation of new tables using drag and drop or point and click from the palette
 * @author Phil Zoio
 */
public class RelationsetEditPolicy extends ContainerEditPolicy
{

	/**
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command getAddCommand(GroupRequest request)
	{
		EditPart host = getTargetEditPart(request);
		return null;
	}

	/**
	 * @see ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request)
	{
		Object newObject = request.getNewObject();
		if (newObject instanceof TableAliasModel)
		{
			Point location = request.getLocation();
			EditPart host = getTargetEditPart(request);
			RelationsetEditPart schemaPart = (RelationsetEditPart)getHost();
			RelationsetModel relationSet = schemaPart.getRelationsetModel();
			TableAliasModel  alias       = (TableAliasModel) newObject;
			return  new AddTableAliasCommand(relationSet, alias, location);
		}
		
		return null;
	}

	/**
	 * @see AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request)
	{
	  if (REQ_CREATE.equals(request.getType()))
			return getHost();
		if (REQ_ADD.equals(request.getType()))
			return getHost();
		if (REQ_MOVE.equals(request.getType()))
			return getHost();
		return super.getTargetEditPart(request);
	}

}
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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import de.tif.jacob.designer.editor.relationset.command.RemoveRelationCommand;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.RelationsetModel;

/**
 * EditPolicy to handle deletion of relationships
 * @author Phil Zoio
 */
public class RelationEditPolicy extends ComponentEditPolicy
{

	protected Command createDeleteCommand(GroupRequest request)
	{
		RelationModel    relation    = (RelationModel) getHost().getModel();
		RelationsetModel relationset = ((RelationsetEditPart)getHost().getRoot().getChildren().get(0)).getRelationsetModel();
		return new RemoveRelationCommand(relationset, relation);
	}
	
}
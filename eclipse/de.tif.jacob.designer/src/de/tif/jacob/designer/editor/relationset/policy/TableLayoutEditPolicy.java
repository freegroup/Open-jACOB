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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import de.tif.jacob.designer.editor.relationset.command.FieldMoveCommand;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableAliasModel;

/**
 * Handles moving of columns within and between tables
 * @author Phil Zoio
 */
public class TableLayoutEditPolicy extends FlowLayoutEditPolicy
{

	public TableLayoutEditPolicy()
	{
		
	}
	/**
	 * Creates command to transfer child column to after column (in another
	 * table)
	 */
	protected Command createAddCommand(EditPart child, EditPart after)
	{
	  return null;
	}

	/**
	 * Creates command to transfer child column to after specified column
	 * (within table)
	 */
	protected Command createMoveChildCommand(EditPart child, EditPart after)
	{
		if (after != null)
		{
			FieldModel childModel = (FieldModel) child.getModel();
			FieldModel afterModel = (FieldModel) after.getModel();
			
			if(childModel==afterModel)
				return null;
			
//			System.out.println(afterModel.getExtendedDescriptionLabel());
//
			TableAliasModel parentTable = (TableAliasModel) getHost().getModel();
			int oldIndex = getHost().getChildren().indexOf(child);
			int newIndex = getHost().getChildren().indexOf(after);
//			System.out.println("Moving from "+oldIndex+"=>"+newIndex);
//
			return new FieldMoveCommand(childModel, oldIndex, newIndex);
//			return command;
		}
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getCreateCommand(CreateRequest request)
	{
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getDeleteDependantCommand(Request request)
	{
		return null;
	}

}
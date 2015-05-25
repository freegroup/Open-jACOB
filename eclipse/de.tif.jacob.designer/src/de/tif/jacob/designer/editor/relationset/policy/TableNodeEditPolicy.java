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
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import de.tif.jacob.designer.editor.relationset.command.CreateRelationCommand;
import de.tif.jacob.designer.editor.relationset.editpart.RelationsetEditPart;
import de.tif.jacob.designer.editor.relationset.editpart.TableAliasEditPart;

/**
 * Handles manipulation of relationships between tables
 * @author Phil Zoio
 */
public class TableNodeEditPolicy extends GraphicalNodeEditPolicy
{

	/**
	 * @see GraphicalNodeEditPolicy#getConnectionCreateCommand(CreateConnectionRequest)
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request)
	{
		TableAliasEditPart             part = (TableAliasEditPart) getHost();
		RelationsetEditPart relationsetPart = (RelationsetEditPart)part.getParent();
		
		CreateRelationCommand cmd = new CreateRelationCommand(relationsetPart.getRelationsetModel(), part.getTableAliasModel());
		request.setStartCommand(cmd);
		return cmd;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getConnectionCompleteCommand(CreateConnectionRequest)
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
	{
		CreateRelationCommand cmd  = (CreateRelationCommand) request.getStartCommand();
		TableAliasEditPart    part = (TableAliasEditPart) request.getTargetEditPart();
		cmd.setPrimaryTable(part.getTableAliasModel());
		
		return cmd;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getReconnectSourceCommand(ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request)
	{
	  return null;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getReconnectTargetCommand(ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request)
	{
	  return null;
	}

}
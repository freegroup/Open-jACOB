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
package de.tif.jacob.designer.editor.jacobform.editpolicies;

import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class ComponentEditPolicyForwardDelete extends ComponentEditPolicy
{
	protected Command getDeleteCommand(GroupRequest request)
	{
		Command childCommand = createDeleteCommand(request);
		Command parentCommand = getParentDeleteCommand( request);
		CompoundCommand com = new CompoundCommand();
		if (childCommand != null)
			com.add(childCommand);
		if (parentCommand != null)
			com.add(parentCommand);

		if (childCommand == null && parentCommand == null)
			return null;
		else
			return com;
	}
	
	protected Command getParentDeleteCommand(GroupRequest request)
	{
		request.setEditParts(getHost());
		request.setType(RequestConstants.REQ_DELETE_DEPENDANT);
		
		return getHost().getParent().getCommand(request);
	}
}

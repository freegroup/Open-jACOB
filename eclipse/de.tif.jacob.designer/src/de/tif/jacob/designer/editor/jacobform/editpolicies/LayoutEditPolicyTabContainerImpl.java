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

import java.util.Iterator;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import de.tif.jacob.designer.editor.jacobform.commands.CreateTabPaneGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteGroupElementCommand;
import de.tif.jacob.designer.model.UIGroupContainer;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITabPanesModel;

public class LayoutEditPolicyTabContainerImpl extends OrderedLayoutEditPolicy
{

	protected EditPolicy createChildEditPolicy(EditPart child) 
	{
		return new ResizableEditPolicy();
	}

	protected Command createAddCommand(EditPart child, EditPart after) 
	{
		return null;
	}

	protected Command createMoveChildCommand(EditPart child, EditPart after) 
	{
		return null;
	}

	protected EditPart getInsertionReference(Request request) 
	{
		return null;
	}

	protected Command getCreateCommand(CreateRequest request) 
	{
		Object newObj = request.getNewObject();
		
		if(newObj instanceof UIGroupModel)
		{
			UIGroupModel newGroup = (UIGroupModel)newObj;
      UITabContainerModel container = (UITabContainerModel)getHost().getModel();
			return new CreateTabPaneGroupCommand(container.getJacobModel(), container, newGroup,-1);
		}
		return null;
	}

	protected Command getDeleteDependantCommand(Request request) 
	{
		CompoundCommand cc = new CompoundCommand();
    UITabContainerModel container = (UITabContainerModel)getHost().getModel();
		Iterator it = ((GroupRequest) request).getEditParts().iterator();
    
		while(it.hasNext())
		{
			Object obj = ((EditPart)it.next()).getModel();
			// das Object löscht nicht sich selbst. Dafür ist der übergeordnete Container zuständig!
			//
			if(obj instanceof UIGroupElementModel && obj != container)
				cc.add(new DeleteGroupElementCommand(container.getGroupModel(),(UIGroupElementModel)obj ));
			else
				System.out.println(obj.getClass().getName());
		}
    if(cc.isEmpty())
    	return null;
		return cc;
	}
}

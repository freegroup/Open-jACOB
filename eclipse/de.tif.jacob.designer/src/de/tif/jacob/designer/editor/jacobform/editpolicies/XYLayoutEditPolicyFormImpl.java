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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import de.tif.jacob.designer.editor.jacobform.commands.CreateGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGroupElementCommand;
import de.tif.jacob.designer.model.UIFormElementModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupModel;

public class XYLayoutEditPolicyFormImpl extends XYLayoutEditPolicy
{
	protected Command createAddCommand(EditPart child, Object constraint)
	{
		return null;
	}

	protected Command createChangeConstraintCommand(EditPart child, Object constraint)
	{
		UIGroupModel model = (UIGroupModel)child.getModel();
		return new MoveGroupCommand(model, (Rectangle)constraint);
	}

	protected Command getCreateCommand(CreateRequest request)
	{
		Object newObj = request.getNewObject();
		
		if(newObj instanceof UIGroupModel)
		{
			UIGroupModel newGroup = (UIGroupModel)newObj;
			UIJacobFormModel form = (UIJacobFormModel)getHost().getModel();
			Rectangle rect = (Rectangle)getConstraintFor(request);
			return new CreateGroupCommand(form.getJacobModel(), form, newGroup, rect.getLocation(), rect.getSize());
		}
		return null;
	}

	protected Command getDeleteDependantCommand(Request request)
	{
		CompoundCommand cc = new CompoundCommand();
		UIJacobFormModel form = (UIJacobFormModel)getHost().getModel();
		Iterator it = ((GroupRequest) request).getEditParts().iterator();
		while(it.hasNext())
		{
			cc.add(new DeleteGroupCommand(form,(UIGroupModel)((EditPart)it.next()).getModel()) );
		}
		return cc;
	}

}

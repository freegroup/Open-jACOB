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
package de.tif.jacob.designer.editor.jacobform.editpolicies;

import java.util.Iterator;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import de.tif.jacob.designer.editor.jacobform.commands.CreateTabPaneGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeletePaneGroupCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveTabCommand;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupElementEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.TabEditPart;
import de.tif.jacob.designer.editor.relationset.command.FieldMoveCommand;
import de.tif.jacob.designer.model.FieldModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UITabContainerModel;
import de.tif.jacob.designer.model.UITabModel;
import de.tif.jacob.designer.model.UITabStripModel;

/**
 * Handles moving of columns within and between tables
 * @author Phil Zoio
 */
public class TabStripLayoutEditPolicy extends FlowLayoutEditPolicy
{

	public TabStripLayoutEditPolicy()
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
      UITabModel childModel = (UITabModel) child.getModel();
      UITabModel afterModel = (UITabModel) after.getModel();
      
      if(childModel==afterModel)
        return null;
      
      int oldIndex = getHost().getChildren().indexOf(child);
      int newIndex = getHost().getChildren().indexOf(after);

      return new MoveTabCommand(childModel, oldIndex, newIndex);
    }
    return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getCreateCommand(CreateRequest request)
	{
    Object newObj = request.getNewObject();
    
    if(newObj instanceof UIGroupModel)
    {
      UIGroupModel newGroup = (UIGroupModel)newObj;
      UITabStripModel strip = (UITabStripModel)getHost().getModel();
      
      return new CreateTabPaneGroupCommand(strip.getJacobModel(), strip.getTabContainerModel(), newGroup, this.getFeedbackIndexFor(request));
    }
    return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getDeleteDependantCommand(Request request)
	{
    CompoundCommand cc = new CompoundCommand();
    Iterator it = ((GroupRequest) request).getEditParts().iterator();
    while(it.hasNext())
    {
      Object object = it.next();
      if(object instanceof TabEditPart)
      {
        TabEditPart elementPart = (TabEditPart)object;
        
        UITabModel  tab  = elementPart.getTabModel();
        cc.add(new DeletePaneGroupCommand(tab.getUITabContainerModel().getPane(), tab.getUIGroupModel()));
      }
    }
    if(cc.isEmpty())
      return null;
    return cc;
	}

}
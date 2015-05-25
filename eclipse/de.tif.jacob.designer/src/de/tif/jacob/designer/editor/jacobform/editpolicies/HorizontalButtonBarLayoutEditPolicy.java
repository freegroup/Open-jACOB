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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.rulers.RulerProvider;
import de.tif.jacob.designer.editor.jacobform.commands.AddButtonBarElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.AddGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.ChangeGuideCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateButtonBarElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteButtonBarElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveButtonBarElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.RemoveButtonBarElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.RemoveGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.editpart.HorizontalButtonBarEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.ButtonEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupElementEditPart;
import de.tif.jacob.designer.model.UIButtonBarModel;
import de.tif.jacob.designer.model.UIButtonModel;
import de.tif.jacob.designer.model.UIFormGuideModel;
import de.tif.jacob.designer.model.UIGroupElementModel;

/**
 * Handles moving of columns within and between tables
 * @author Phil Zoio
 */
public class HorizontalButtonBarLayoutEditPolicy extends FlowLayoutEditPolicy
{

	public HorizontalButtonBarLayoutEditPolicy()
	{
	}
  

  @Override
  protected boolean isHorizontal()
  {
    return true;
  }

  @Override
  protected Command getOrphanChildrenCommand(Request request)
  {
    if(request instanceof GroupRequest)
    {
      GroupRequest r = (GroupRequest)request;
      CompoundCommand cmd = new CompoundCommand();
      
      Iterator iter = r.getEditParts().iterator();
      while(iter.hasNext())
      {
        ButtonEditPart button =(ButtonEditPart)iter.next();
        UIButtonBarModel bar = button.getButtonModel().getButtonBarModel();
        cmd.add(new RemoveButtonBarElementCommand(bar, button.getButtonModel()));
      }
      if(cmd.size()>0)
        return cmd;
    }
    return super.getOrphanChildrenCommand(request);
  }



  /**
	 * Creates command to transfer child column to after column (in another
	 * table)
	 */
  protected Command createAddCommand(EditPart child, EditPart after)
  {
    Object obj = child.getModel();
    HorizontalButtonBarEditPart host = (HorizontalButtonBarEditPart)getHost();
    if(obj instanceof UIButtonModel )
    {
      UIButtonModel model = (UIButtonModel)obj;
      
      return new AddButtonBarElementCommand(host.getButtonBarModel(), model );
    }
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
      UIButtonModel childModel = (UIButtonModel) child.getModel();
      UIButtonModel afterModel = (UIButtonModel) after.getModel();
      
      if(childModel==afterModel)
        return null;
      
      int oldIndex = getHost().getChildren().indexOf(child);
      int newIndex = getHost().getChildren().indexOf(after);
//
      return new MoveButtonBarElementCommand(childModel, oldIndex, newIndex);
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
    
    if(newObj instanceof UIButtonModel)
    {
      UIButtonModel button = (UIButtonModel)newObj;
      UIButtonBarModel bar = (UIButtonBarModel)getHost().getModel();
      
      return new CreateButtonBarElementCommand(bar.getJacobModel(), bar, button, this.getFeedbackIndexFor(request));
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
      if(object instanceof ButtonEditPart)
      {
        ButtonEditPart buttonPart = (ButtonEditPart)object;
        
        UIButtonModel  button  = buttonPart.getButtonModel();
        cc.add(new DeleteButtonBarElementCommand(button.getButtonBarModel(),button));
      }
    }
    if(cc.isEmpty())
      return null;
    return cc;
	}
}
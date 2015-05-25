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
 * Created on 10.12.2004
 *
 */
package de.tif.jacob.designer.editor.jacobform.dnd;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import de.tif.jacob.designer.editor.jacobform.commands.CreateGroupCommand;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.model.TableAliasModel;
import de.tif.jacob.designer.model.UIJacobFormModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.util.ModelTransfer;
import de.tif.jacob.designer.views.applicationoutline.TreeTableAliasObject;

/**
 *  
 */
public class DropListener extends AbstractTransferDropTargetListener
{
  private final CommandStack commandStack;
  private final UIJacobFormModel  formModel;
  
  /**
   * @param viewer
   * @param stack 
   */
  public DropListener(EditPartViewer viewer, CommandStack stack, UIJacobFormModel model)
  {
    super(viewer, TextTransfer.getInstance());
    this.commandStack = stack;
    this.formModel = model;
  }
  
  protected String getCommandName() 
  {
  	return RequestConstants.REQ_ADD;
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
   */
  protected Request createTargetRequest()
  {
  	ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_ADD);
  	return request;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetRequest()
   */
  protected void updateTargetRequest()
  {
  	ChangeBoundsRequest request = (ChangeBoundsRequest)getTargetRequest();
  	request.setLocation(getDropLocation());
  	request.setType(getCommandName());
  }

  /* (non-Javadoc)
   * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDragOver()
   */
  protected void handleDragOver()
  {
    if(!ModelTransfer.areObjectsSameType())
      return;
    
    Object obj = ModelTransfer.getObjects()[0];
  	if(obj instanceof TreeTableAliasObject)
  	{
  		getCurrentEvent().detail = DND.DROP_COPY;
  	}
  	else
  	{
  		getCurrentEvent().detail = DND.DROP_NONE;
  		return;
  	}
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
   */
  protected void handleDrop()
  {
    if(!ModelTransfer.areObjectsSameType())
      return;
    
    if(ModelTransfer.getObjects()[0] instanceof TreeTableAliasObject)
    {
      CompoundCommand cc = new CompoundCommand();
      for (int i = 0; i < ModelTransfer.getObjects().length; i++)
      {
        TreeTableAliasObject obj = (TreeTableAliasObject)ModelTransfer.getObjects()[i];
        TableAliasModel aliasModel = ((TreeTableAliasObject)obj).getTableAliasModel();
        JacobModel jacobModel = formModel.getJacobModel();
        UIGroupModel groupModel = new UIGroupModel();
        Point point = getDropLocation();
        CreateGroupCommand cmd =new CreateGroupCommand(jacobModel,formModel, groupModel, point,(Dimension)null);
        cmd.setAliasModel(aliasModel);
        cc.add(cmd);
      }
      commandStack.execute(cc);

    }
    
  }
}

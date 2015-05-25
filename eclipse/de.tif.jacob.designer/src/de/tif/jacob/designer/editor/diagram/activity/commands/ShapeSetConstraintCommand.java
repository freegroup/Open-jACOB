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
package de.tif.jacob.designer.editor.diagram.activity.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import de.tif.jacob.designer.model.diagram.activity.ActivityModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;

/**
 * A command to resize and/or move a shape. The command can be undone or redone.
 * 
 * @author Elias Volanakis
 */
public class ShapeSetConstraintCommand extends Command
{
  private final Rectangle newBounds;
  private Rectangle oldBounds;
  private final ChangeBoundsRequest request;
  private final DiagramElementModel shape;

  /**
   * Create a command that can resize and/or move a shape.
   * 
   * @param shape
   *          the shape to manipulate
   * @param req
   *          the move and resize request
   * @param newBounds
   *          the new size and location
   * @throws IllegalArgumentException
   *           if any of the parameters is null
   */
  public ShapeSetConstraintCommand(DiagramElementModel shape, ChangeBoundsRequest req, Rectangle newBounds)
  {
    if (shape == null || req == null || newBounds == null)
    {
      throw new IllegalArgumentException();
    }
    this.shape = shape;
    this.request = req;
    this.newBounds = newBounds.getCopy();
    setLabel("move / resize");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canExecute()
   */
  public boolean canExecute()
  {
    Object type = request.getType();
    // make sure the Request is of a type we support:
    return (RequestConstants.REQ_MOVE.equals(type) || RequestConstants.REQ_MOVE_CHILDREN.equals(type) || RequestConstants.REQ_RESIZE.equals(type) || RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute()
  {
    oldBounds = new Rectangle(shape.getLocation(), shape.getSize());
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo()
  {
    // nur eine activit�t kann vergr��ert/verkleinert werden
    if(shape instanceof ActivityModel)
      shape.setSize(newBounds.getSize());
    shape.setLocation(newBounds.getLocation());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    shape.setSize(oldBounds.getSize());
    shape.setLocation(oldBounds.getLocation());
  }
}

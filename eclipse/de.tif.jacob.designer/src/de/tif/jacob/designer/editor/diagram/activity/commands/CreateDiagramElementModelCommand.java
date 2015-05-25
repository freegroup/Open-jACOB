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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;
import de.tif.jacob.designer.model.diagram.activity.ActivityModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;

/**
 * A command to add a Shape to a ShapeDiagram. The command can be undone or
 * redone.
 * 
 * @author Elias Volanakis
 */
public class CreateDiagramElementModelCommand extends Command
{
  private DiagramElementModel newShape;
  private final ActivityDiagramModel parent;
  private final CreateRequest request;
  private boolean shapeAdded;

  /**
   * Create a command that will add a new Shape to a ShapesDiagram.
   * 
   * @param parent
   *          the ShapesDiagram that will hold the new element
   * @param req
   *          a request to create a new Shape
   * @throws IllegalArgumentException
   *           if any parameter is null, or the request does not provide a new
   *           Shape instance
   */
  public CreateDiagramElementModelCommand(ActivityDiagramModel parent, CreateRequest req)
  {
    if (parent == null || req == null || !(req.getNewObject() instanceof DiagramElementModel))
    {
      throw new IllegalArgumentException();
    }
    this.parent = parent;
    this.request = req;
    setLabel("shape creation");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#canUndo()
   */
  public boolean canUndo()
  {
    return shapeAdded;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#execute()
   */
  public void execute()
  {
    newShape = (DiagramElementModel) request.getNewObject();
    // nur eine activit�t kann vergr��ert/verkleinert werden
    if(newShape instanceof ActivityModel)
    {
      Dimension size = request.getSize()!=null?request.getSize():new Dimension(100,75);
      newShape.setSize(size);
    }
    newShape.setLocation(request.getLocation());
    newShape.setJacobModel(parent.getJacobModel());
    newShape.setActivityDiagramModel(parent);
    redo();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#redo()
   */
  public void redo()
  {
    shapeAdded = parent.addElement(newShape);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.commands.Command#undo()
   */
  public void undo()
  {
    parent.removeElement(newShape);
  }
}

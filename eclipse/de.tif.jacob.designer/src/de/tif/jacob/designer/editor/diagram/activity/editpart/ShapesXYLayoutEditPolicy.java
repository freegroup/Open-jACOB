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
 * Created on 22.02.2005
 *
 */
package de.tif.jacob.designer.editor.diagram.activity.editpart;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import de.tif.jacob.designer.editor.diagram.activity.commands.CreateDiagramElementModelCommand;
import de.tif.jacob.designer.editor.diagram.activity.commands.ShapeSetConstraintCommand;
import de.tif.jacob.designer.model.diagram.activity.ActivityModel;
import de.tif.jacob.designer.model.diagram.activity.BranchMergeModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;
import de.tif.jacob.designer.model.diagram.activity.EndModel;
import de.tif.jacob.designer.model.diagram.activity.ForkJoinModel;
import de.tif.jacob.designer.model.diagram.activity.StartModel;


/**
 * EditPolicy for the Figure used by this edit part. Children of
 * XYLayoutEditPolicy can be used in Figures with XYLayout.
 * 
 * @author Elias Volanakis
 */
class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy
{
  private final ActivityDiagramEditPart part;

  /**
   * Create a new instance of this edit policy.
   * 
   * @param layout
   *          a non-null XYLayout instance. This should be the layout of the
   *          editpart's figure where this instance is installed.
   * @param part TODO
   * @throws IllegalArgumentException
   *           if layout is null
   * @see ActivityDiagramEditPart#createEditPolicies()
   */
  ShapesXYLayoutEditPolicy(ActivityDiagramEditPart part, XYLayout layout)
  {
    if (layout == null)
    {
      throw new IllegalArgumentException();
    }
    this.part = part;
    setXyLayout(layout);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
   *      java.lang.Object)
   */
  protected Command createAddCommand(EditPart child, Object constraint)
  {
    // not used in this example
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.requests.ChangeBoundsRequest,
   *      org.eclipse.gef.EditPart, java.lang.Object)
   */
  protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint)
  {
    if (child instanceof DiagramElementEditPart && constraint instanceof Rectangle)
    {
      return new ShapeSetConstraintCommand((DiagramElementModel) child.getModel(), request, (Rectangle) constraint);
    }
    return super.createChangeConstraintCommand(request, child, constraint);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart,
   *      java.lang.Object)
   */
  protected Command createChangeConstraintCommand(EditPart child, Object constraint)
  {
    // not used in this example
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
   */
  protected Command getCreateCommand(CreateRequest request)
  {
    Object childClass = request.getNewObjectType();
    
    if (childClass == ActivityModel.class)
      return new CreateDiagramElementModelCommand(this.part.getActivityDiagramModel(), request);

    if (childClass instanceof DiagramElementModel)
      return new CreateDiagramElementModelCommand(this.part.getActivityDiagramModel(), request);

    if (childClass == ForkJoinModel.class)
      return new CreateDiagramElementModelCommand(this.part.getActivityDiagramModel(), request);

    if (childClass == BranchMergeModel.class)
      return new CreateDiagramElementModelCommand(this.part.getActivityDiagramModel(), request);

    if (childClass == StartModel.class)
      return new CreateDiagramElementModelCommand(this.part.getActivityDiagramModel(), request);

    if (childClass == EndModel.class)
      return new CreateDiagramElementModelCommand(this.part.getActivityDiagramModel(), request);

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
   */
  protected Command getDeleteDependantCommand(Request request)
  {
    // not used in this example
    return null;
  }
}
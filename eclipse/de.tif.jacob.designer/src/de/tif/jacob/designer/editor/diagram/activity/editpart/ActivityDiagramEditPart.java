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
package de.tif.jacob.designer.editor.diagram.activity.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.diagram.activity.ActivityDiagramModel;

/**
 * EditPart for the a ShapesDiagram instance.
 * <p>
 * This edit part server as the main diagram container, the white area where
 * everything else is in. Also responsible for the container's layout (the way
 * the container rearanges is contents) and the container's capabilities (edit
 * policies).
 * </p>
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class ActivityDiagramEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener
{
  /**
   * Upon activation, attach to the model element as a property change listener.
   */
  public void activate()
  {
    if (!isActive())
    {
      super.activate();
      ((ObjectModel) getModel()).addPropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies()
  {
    // disallows the removal of this edit part from its parent
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    // handles constraint changes (e.g. moving and/or resizing) of model
    // elements
    // and creation of new model elements
    XYLayout layout = (XYLayout) getContentPane().getLayoutManager();
    installEditPolicy(EditPolicy.LAYOUT_ROLE, new ShapesXYLayoutEditPolicy(this, layout));
    // disable selection feedback for this edit part
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  protected IFigure createFigure()
  {
    Figure f = new Figure();
    f.setLayoutManager(new FreeformLayout());
    return f;
  }

  /**
   * Upon deactivation, detach from the model element as a property change
   * listener.
   */
  public void deactivate()
  {
    if (isActive())
    {
      super.deactivate();
      ((ObjectModel) getModel()).removePropertyChangeListener(this);
    }
  }

  protected ActivityDiagramModel getActivityDiagramModel()
  {
    return (ActivityDiagramModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
   */
  protected List getModelChildren()
  {
    return getActivityDiagramModel().getChildren(); // return a list of shapes
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if (ObjectModel.PROPERTY_ELEMENT_ADDED==evt.getPropertyName())
      refreshChildren();
    else if (ObjectModel.PROPERTY_ELEMENT_REMOVED==evt.getPropertyName())
      refreshChildren();
  }
}

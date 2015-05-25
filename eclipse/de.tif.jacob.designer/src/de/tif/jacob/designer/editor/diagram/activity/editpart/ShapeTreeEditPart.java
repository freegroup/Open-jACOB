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
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
import de.tif.jacob.designer.editor.diagram.activity.editpolicies.ShapeComponentEditPolicy;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;

/**
 * TreeEditPart used for Shape instances (more specific for EllipticalShape and
 * RectangularShape instances). This is used in the Outline View of the
 * ShapesEditor.
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class ShapeTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener
{

  /**
   * Create a new instance of this edit part using the given model element.
   * 
   * @param model
   *          a non-null Shapes instance
   */
  ShapeTreeEditPart(DiagramElementModel model)
  {
    super(model);
  }

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
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#createEditPolicies()
   */
  protected void createEditPolicies()
  {
    // allow removal of the associated model element
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentEditPolicy());
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

  private DiagramElementModel getCastedModel()
  {
    return (DiagramElementModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
   */
  protected Image getImage()
  {
    return getCastedModel().getImage();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
   */
  protected String getText()
  {
    return getCastedModel().toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    refreshVisuals(); // this will cause an invocation of getImage() and
                      // getText(), see below
  }
}

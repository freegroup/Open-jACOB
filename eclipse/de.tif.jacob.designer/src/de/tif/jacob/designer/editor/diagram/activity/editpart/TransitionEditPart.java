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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.TextCellEditor;
import de.tif.jacob.designer.editor.diagram.activity.commands.ConnectionDeleteCommand;
import de.tif.jacob.designer.editor.diagram.activity.directedit.LabelCellEditorLocator;
import de.tif.jacob.designer.editor.diagram.activity.editpolicies.TransitionDirectEditPolicy;
import de.tif.jacob.designer.editor.diagram.activity.figures.IDiagramElementFigure;
import de.tif.jacob.designer.editor.diagram.activity.figures.TransitionFigure;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;

/**
 * Edit part for Connection model elements.
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
public class TransitionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener
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

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies()
  {
    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new TransitionDirectEditPolicy());
    
    // Selection handle edit policy.
    // Makes the connection show a feedback, when selected by the user.
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    // Allows the removal of the connection model element
    installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy()
    {
      protected Command getDeleteCommand(GroupRequest request)
      {
        TransitionModel t =getTransitionModel();
        return new ConnectionDeleteCommand(t.getJacobModel(),t.getActivityDiagramModel(),t);
      }
    });
  }

	public void performRequest(Request request)
	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		{
		  TransitionFigure figure = (TransitionFigure)getFigure();
		  Label label = figure.getLabel();
		  new ExtendedDirectEditManager(this,TextCellEditor.class,new LabelCellEditorLocator(label),label).show();
		}
		else
		  super.performRequest(request);
	}
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  protected IFigure createFigure()
  {
    return new TransitionFigure(getRoot(),getTransitionModel().getDescription());
  }


  private TransitionModel getTransitionModel()
  {
    return (TransitionModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    String prop = evt.getPropertyName();
    if (ObjectModel.PROPERTY_DESCRIPTION_CHANGED==prop)
      refreshVisuals();
  }
  
  protected void refreshVisuals()
  {
    ((IDiagramElementFigure)figure).setText(getTransitionModel().getDescription());
  }
  
}

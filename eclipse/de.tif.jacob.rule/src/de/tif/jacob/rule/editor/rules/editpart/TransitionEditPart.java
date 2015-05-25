/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 �* All rights reserved. This program and the accompanying materials
 �* are made available under the terms of the Common Public License v1.0
 �* which accompanies this distribution, and is available at
 �* http://www.eclipse.org/legal/cpl-v10.html
 �*
 �* Contributors:
 �*����Elias Volanakis - initial API and implementation
 �*******************************************************************************/
package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import de.tif.jacob.rule.editor.rules.commands.ConnectionDeleteCommand;
import de.tif.jacob.rule.editor.rules.figures.TransitionFigure;
import de.tif.jacob.rule.editor.rules.model.TransitionModel;

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

  public TransitionEditPart()
  {
    
  }
  
  /**
   * Upon activation, attach to the model element as a property change listener.
   */
  public void activate()
  {
    if (!isActive())
    {
      super.activate();
      getTransitionModel().addPropertyChangeListener(this);
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
      getTransitionModel().removePropertyChangeListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies()
  {
    
    // Selection handle edit policy.
    // Makes the connection show a feedback, when selected by the user.
    installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
    // Allows the removal of the connection model element
    
    installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy()
    {
      protected Command getDeleteCommand(GroupRequest request)
      {
        TransitionModel t =getTransitionModel();
        return new ConnectionDeleteCommand(t);
      }
    });
    
  }

  public TransitionModel getTransitionModel()
  {
    return (TransitionModel)getModel();
  }
  
  
	public void performRequest(Request request)
	{
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		{
      System.out.println("direct edit of transition");
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
    return new TransitionFigure();
  }



  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    String prop = evt.getPropertyName();
//    if (ObjectModel.PROPERTY_DESCRIPTION_CHANGED==prop)
//      refreshVisuals();
  }
  
  protected void refreshVisuals()
  {
//    ((IDiagramElementFigure)figure).setText(getTransitionModel().getDescription());
  }
  
}
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
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.viewers.TextCellEditor;
import de.tif.jacob.designer.editor.diagram.activity.commands.ConnectionCreateCommand;
import de.tif.jacob.designer.editor.diagram.activity.commands.ConnectionReconnectCommand;
import de.tif.jacob.designer.editor.diagram.activity.directedit.LabelCellEditorLocator;
import de.tif.jacob.designer.editor.diagram.activity.editpart.anchor.LeftRightAnchor;
import de.tif.jacob.designer.editor.diagram.activity.editpart.anchor.TopBottomAnchor;
import de.tif.jacob.designer.editor.diagram.activity.editpolicies.DiagramElementDirectEditPolicy;
import de.tif.jacob.designer.editor.diagram.activity.editpolicies.ShapeComponentEditPolicy;
import de.tif.jacob.designer.editor.diagram.activity.figures.ActivityFigure;
import de.tif.jacob.designer.editor.diagram.activity.figures.BranchMergeFigure;
import de.tif.jacob.designer.editor.diagram.activity.figures.EndFigure;
import de.tif.jacob.designer.editor.diagram.activity.figures.ForkJoinFigure;
import de.tif.jacob.designer.editor.diagram.activity.figures.IDiagramElementFigure;
import de.tif.jacob.designer.editor.diagram.activity.figures.StartFigure;
import de.tif.jacob.designer.editor.jacobform.directedit.ExtendedDirectEditManager;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.diagram.activity.ActivityModel;
import de.tif.jacob.designer.model.diagram.activity.BranchMergeModel;
import de.tif.jacob.designer.model.diagram.activity.DiagramElementModel;
import de.tif.jacob.designer.model.diagram.activity.EndModel;
import de.tif.jacob.designer.model.diagram.activity.ForkJoinModel;
import de.tif.jacob.designer.model.diagram.activity.StartModel;
import de.tif.jacob.designer.model.diagram.activity.TransitionModel;


class DiagramElementEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart
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
    this.getSourceConnectionAnchor((ConnectionEditPart)null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
   */
  protected void createEditPolicies()
  {
    // allow removal of the associated model element
    installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentEditPolicy());

    if (getModel() instanceof ActivityModel)
      installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new DiagramElementDirectEditPolicy());

  	// allow the creation of connections and
    // and the reconnection of connections between Shape instances
    installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy()
    {
      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
       */
      protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
      {
        ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
        DiagramElementModel target = (DiagramElementModel) getHost().getModel();
        if(!(target instanceof StartModel))
        {
          cmd.setTarget(target);
          return cmd;
        }
        return null;
      }

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
       */
      protected Command getConnectionCreateCommand(CreateConnectionRequest request)
      {
        DiagramElementModel source = (DiagramElementModel) getHost().getModel();

        // von dem Ende darf keine Transition weg führen
        //
        if((source instanceof EndModel))
          return null;
        
        // Die Activität darf nicht bereits in einer anderen Transition als startpunkt vorhanden
        // sein
        if((source.getSourceConnections().size()>0) && (source instanceof ActivityModel))
          return null;
        
        if((source.getSourceConnections().size()>0) && (source instanceof StartModel))
          return null;
        
        ConnectionCreateCommand cmd = new ConnectionCreateCommand(source.getJacobModel(), source);
        request.setStartCommand(cmd);

        return cmd;
      }

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
       */
      protected Command getReconnectSourceCommand(ReconnectRequest request)
      {
        TransitionModel conn = (TransitionModel) request.getConnectionEditPart().getModel();
        DiagramElementModel newSource = (DiagramElementModel) getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        if(!(newSource instanceof EndModel))
        {
          cmd.setNewSource(newSource);
          return cmd;
        }
        return null;
      }

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
       */
      protected Command getReconnectTargetCommand(ReconnectRequest request)
      {
        TransitionModel conn = (TransitionModel) request.getConnectionEditPart().getModel();
        DiagramElementModel newTarget = (DiagramElementModel) getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        if(!(newTarget instanceof StartModel))
        {
          cmd.setNewTarget(newTarget);
          return cmd;
        }
        return null;
      }
    });
  }

	public void performRequest(Request request)
	{
	  
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && getModel() instanceof ActivityModel)
		{
		  IDiagramElementFigure figure = (IDiagramElementFigure)getFigure();
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
    IFigure f = createFigureForModel();
    return f;
  }

  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  private IFigure createFigureForModel()
  {
    if (getModel() instanceof ActivityModel)
      return new ActivityFigure(((ActivityModel)getModel()).getName());
    if (getModel() instanceof BranchMergeModel)
      return new BranchMergeFigure();
    if (getModel() instanceof ForkJoinModel)
      return new ForkJoinFigure();
    if (getModel() instanceof StartModel)
      return new StartFigure();
    if (getModel() instanceof EndModel)
      return new EndFigure();
    else
      throw new IllegalArgumentException("Unable to create figure for model type ["+getModel().getClass().getName()+"]");
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

  private DiagramElementModel getDiagramElementModel()
  {
    return (DiagramElementModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
   */
  protected List getModelSourceConnections()
  {
    return getDiagramElementModel().getSourceConnections();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
   */
  protected List getModelTargetConnections()
  {
    return getDiagramElementModel().getTargetConnections();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
  {
    if(getModel() instanceof StartModel)
      return new EllipseAnchor(getFigure());
    if(getModel() instanceof EndModel)
      return new EllipseAnchor(getFigure());
    if(getModel() instanceof BranchMergeModel)
      return new LeftRightAnchor( getFigure());
    if(getModel() instanceof ActivityModel)
      return new TopBottomAnchor( getFigure());
    
    return new ChopboxAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getSourceConnectionAnchor(Request request)
  {
    return new ChopboxAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
  {
    if(getModel() instanceof StartModel)
      return new EllipseAnchor(getFigure());
    if(getModel() instanceof EndModel)
      return new EllipseAnchor(getFigure());
    if(getModel() instanceof BranchMergeModel)
      return new TopBottomAnchor( getFigure());

    return new ChopboxAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getTargetConnectionAnchor(Request request)
  {
    return new ChopboxAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    String prop = evt.getPropertyName();
    if (ObjectModel.PROPERTY_CONSTRAINT_CHANGED==prop)
      refreshVisuals();
    if (ObjectModel.PROPERTY_NAME_CHANGED==prop)
      refreshVisuals();
    else if (DiagramElementModel.SOURCE_CONNECTIONS_PROP.equals(prop))
      refreshSourceConnections();
    else if (DiagramElementModel.TARGET_CONNECTIONS_PROP.equals(prop))
      refreshTargetConnections();
  }

  protected void refreshVisuals()
  {
    // transfer the size and location from the model instance to the
    // corresponding figure
    Rectangle bounds = new Rectangle(getDiagramElementModel().getLocation(), getDiagramElementModel().getSize());
    figure.setBounds(bounds);
    
    ((IDiagramElementFigure)figure).setText(getDiagramElementModel().getName());
    
    // notify parent container of changed position & location
    // if this line is removed, the XYLayoutManager used by the parent container
    // (the Figure of the ShapesDiagramEditPart), will not know the bounds of
    // this figure
    // and will not draw it correctly.
    ((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, bounds);
  }
}
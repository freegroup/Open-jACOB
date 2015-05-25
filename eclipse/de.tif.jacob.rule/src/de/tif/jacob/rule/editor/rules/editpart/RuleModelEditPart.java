package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import de.tif.jacob.rule.editor.rules.commands.AbstractConnectionCreateCommand;
import de.tif.jacob.rule.editor.rules.commands.ConnectionCreateCommand;
import de.tif.jacob.rule.editor.rules.editpart.anchor.LeftMiddleAnchor;
import de.tif.jacob.rule.editor.rules.editpart.anchor.RightMiddleAnchor;
import de.tif.jacob.rule.editor.rules.editpolicies.ShapeComponentEditPolicy;
import de.tif.jacob.rule.editor.rules.model.AbstractMultiTargetModel;
import de.tif.jacob.rule.editor.rules.model.AbstractSourceModel;
import de.tif.jacob.rule.editor.rules.model.AbstractTargetModel;
import de.tif.jacob.rule.editor.rules.model.ObjectModel;
import de.tif.jacob.rule.editor.rules.model.RuleModel;


public abstract class RuleModelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener,  NodeEditPart
{
  public RuleModelEditPart()
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
      ((RuleModel) getModel()).addPropertyChangeListener(this);
    }
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
      	AbstractConnectionCreateCommand cmd = (AbstractConnectionCreateCommand) request.getStartCommand();
        Object target = getHost().getModel();
        if(target instanceof AbstractMultiTargetModel)
        {
          cmd.setTarget((AbstractMultiTargetModel)target);
          return cmd;
        }
        else if(target instanceof AbstractTargetModel)
        {
          AbstractTargetModel bo = (AbstractTargetModel)target;
          // Ein BusinessObjectModel darf nur einmal das Target sein
          //
          if(bo.getRulesetModel().getTargetTransitions(bo).size()>0)
            return null;
          
          cmd.setTarget(bo);
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
        RuleModel source = (RuleModel ) getHost().getModel();

        // von dem Ende darf keine Transition weg f�hren
        //
        if((source instanceof AbstractSourceModel))
        {
          AbstractSourceModel bo =(AbstractSourceModel)source;
          // ein AbstractSourceModel hat nur einen Nachfolger
          if(bo.getSuccessor()!=null)
            return null;
          
          ConnectionCreateCommand cmd = new ConnectionCreateCommand(bo.getRulesetModel(),bo);
          request.setStartCommand(cmd);
          
          return cmd;
        }
        
        return null;
      }

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
       */
      protected Command getReconnectSourceCommand(ReconnectRequest request)
      {
        return null;
      }

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
       */
      protected Command getReconnectTargetCommand(ReconnectRequest request)
      {
        return null;
      }
    });
  }


	/*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  protected IFigure createFigure()
  {
    return createFigureForModel();
  }

  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  public abstract IFigure createFigureForModel();

  /**
   * Upon deactivation, detach from the model element as a property change
   * listener.
   */
  public void deactivate()
  {
    if (isActive())
    {
      super.deactivate();
      ((RuleModel) getModel()).removePropertyChangeListener(this);
    }
  }

  public RuleModel getRuleModel()
  {
    return (RuleModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
   */
  protected List getModelSourceConnections()
  {
    if(getRuleModel() instanceof AbstractSourceModel)
      return getRuleModel().getRulesetModel().getSourceTransitions((AbstractSourceModel)getRuleModel());
    return Collections.EMPTY_LIST;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
   */
  
  protected List getModelTargetConnections()
  {
    if(getRuleModel() instanceof AbstractTargetModel)
      return getRuleModel().getRulesetModel().getTargetTransitions((AbstractTargetModel)getRuleModel());
    return Collections.EMPTY_LIST;
  }

  public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
  {
    return new LeftMiddleAnchor(getFigure());
  }

  public ConnectionAnchor getTargetConnectionAnchor(Request request)
  {
    return new LeftMiddleAnchor(getFigure());
  }

  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
  {
    return new RightMiddleAnchor(getFigure());
  }

  public ConnectionAnchor getSourceConnectionAnchor(Request request)
  {
    return new RightMiddleAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    String prop = evt.getPropertyName();
    if (ObjectModel.PROPERTY_POSITION_CHANGED==prop)
      refreshVisuals();
    else if (ObjectModel.PROPERTY_MODEL_CHANGED==prop)
      refreshVisuals();
    else if (ObjectModel.SOURCE_CONNECTIONS_PROP==prop)
      refreshSourceConnections();
    else if (ObjectModel.TARGET_CONNECTIONS_PROP==prop)
      refreshTargetConnections();
  }

  protected void refreshVisuals()
  {
  	Dimension dim = figure.getPreferredSize();
    figure.setBounds(new Rectangle( getRuleModel().getPosition(), dim));
  }
}
package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.editor.rules.commands.AbstractConnectionCreateCommand;
import de.tif.jacob.rule.editor.rules.commands.ConnectionCreateCommand;
import de.tif.jacob.rule.editor.rules.editpart.anchor.RightMiddleAnchor;
import de.tif.jacob.rule.editor.rules.editpolicies.ShapeComponentEditPolicy;
import de.tif.jacob.rule.editor.rules.figures.decision.EnumValueFigure;
import de.tif.jacob.rule.editor.rules.model.BusinessObjectModel;
import de.tif.jacob.rule.editor.rules.model.DecisionExitValueModel;
import de.tif.jacob.rule.editor.rules.model.ObjectModel;
import de.tif.jacob.rule.editor.rules.model.StartModel;


public class ConditionalExitModelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart
{
  public ConditionalExitModelEditPart()
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
      getDecisionExitValueModel().addPropertyChangeListener(this);
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
        if(target instanceof StartModel)
        {
        	return null;
        }
        if(target instanceof BusinessObjectModel)
        {
          BusinessObjectModel bo = (BusinessObjectModel)target;
          // Ein BusinessObjectModel darf nur einmal das Target sein
          //
          if(bo.getRulesetModel().getTargetTransitions(bo).size()>0)
            return null;
          
          cmd.setTarget((BusinessObjectModel)target);
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
      	Object source = getHost().getModel();

        // von dem Ende darf keine Transition weg f�hren
        //
        if((source instanceof DecisionExitValueModel))
        {
        	DecisionExitValueModel bool =(DecisionExitValueModel)source;
          // ein BusinessObjectModel hat nur einen Nachfolger
          if(bool.getSuccessor()!=null)
            return null;
          
          ConnectionCreateCommand cmd = new ConnectionCreateCommand(bool.getParent().getRulesetModel(),bool);
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
        /*
        TransitionModel conn = (TransitionModel) request.getConnectionEditPart().getModel();
        DiagramElementModel newSource = (DiagramElementModel) getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        if(!(newSource instanceof EndModel))
        {
          cmd.setNewSource(newSource);
          return cmd;
        }
        */
        return null;
      }

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
       */
      protected Command getReconnectTargetCommand(ReconnectRequest request)
      {
        /*
        TransitionModel conn = (TransitionModel) request.getConnectionEditPart().getModel();
        DiagramElementModel newTarget = (DiagramElementModel) getHost().getModel();
        ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
        if(!(newTarget instanceof StartModel))
        {
          cmd.setNewTarget(newTarget);
          return cmd;
        }
        */
        return null;
      }
    });
  }

	public void performRequest(Request request)
	{
	  if(request.getType()== RequestConstants.REQ_OPEN)
    {
//      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
//      new SelectBusinessMethodDialog(display.getActiveShell(),(BusinessObjectModel)getModel()).open();
    }
	  else
	  {
		  super.performRequest(request);
	  }
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
  public IFigure createFigureForModel()
  {
  	return new EnumValueFigure(getDecisionExitValueModel().getValue());
    //return new RectangleFigure();
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
      getDecisionExitValueModel().removePropertyChangeListener(this);
    }
  }

  
  private DecisionExitValueModel getDecisionExitValueModel()
  {
    return (DecisionExitValueModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
   */
  
  protected List getModelSourceConnections()
  {
  	List cons = new ArrayList();
  	if(getDecisionExitValueModel().getTransistionModel()!=null)
  		cons.add(getDecisionExitValueModel().getTransistionModel());
    return cons;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
  {
    return new RightMiddleAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getSourceConnectionAnchor(Request request)
  {
    return new RightMiddleAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
  {
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
    if (ObjectModel.PROPERTY_POSITION_CHANGED==prop)
      refreshVisuals();
    else if (ObjectModel.SOURCE_CONNECTIONS_PROP==prop)
      refreshSourceConnections();
  }

  protected void refreshVisuals()
  {
    DecisionModelEditPart parent = (DecisionModelEditPart)getParent();
    
    Point topLeft = parent.getFigure().getBounds().getTopLeft();
    
    int offset = Constants.RULE_GRID_HEIGHT;
    int w = getFigure().getPreferredSize().width;
    int h = getFigure().getPreferredSize().height;
    
    int index = parent.getChildren().indexOf(this);
    int xTrans = parent.getFigure().getSize().width- w;
    int yTrans = 50*index+(offset*(index+1))- (h/2);
    getFigure().setLocation(topLeft.getTranslated(xTrans,yTrans));
  }
}
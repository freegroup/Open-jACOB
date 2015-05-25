package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.editor.rules.dialogs.decision._boolean.SelectDecisionMethodDialog;
import de.tif.jacob.rule.editor.rules.editpart.anchor.OffsetAnchor;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;
import de.tif.jacob.rule.editor.rules.model.ObjectModel;


public abstract class DecisionModelEditPart extends RuleModelEditPart
{
  public DecisionModelEditPart()
  {
  }
  

	public void performRequest(Request request)
	{
	  if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      new SelectDecisionMethodDialog(display.getActiveShell(),(DecisionModel)getModel()).open();
    }
	  else
	  {
		  super.performRequest(request);
	  }
	}

  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
  {
    return new OffsetAnchor(getFigure(),0,Constants.RULE_GRID_HEIGHT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getTargetConnectionAnchor(Request request)
  {
    return new OffsetAnchor(getFigure(),0,Constants.RULE_GRID_HEIGHT);
  }
  


  public DecisionModel getDecisionModel()
  {
    return (DecisionModel) getModel();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
   */
  protected List getModelChildren()
  {
    return getDecisionModel().getChildren();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    String prop =evt.getPropertyName();
    if (ObjectModel.PROPERTY_ELEMENT_ADDED==prop)
      refreshChildren();
    else if (ObjectModel.PROPERTY_ELEMENT_REMOVED==prop)
      refreshChildren();
    else
    	super.propertyChange(evt);
  }
}
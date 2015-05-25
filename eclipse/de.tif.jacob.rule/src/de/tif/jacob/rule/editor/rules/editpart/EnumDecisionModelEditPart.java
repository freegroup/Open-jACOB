package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import de.tif.jacob.rule.Constants;
import de.tif.jacob.rule.editor.rules.dialogs.decision._enum.SelectEnumFieldDialog;
import de.tif.jacob.rule.editor.rules.figures.EnumDecisionFigure;
import de.tif.jacob.rule.editor.rules.model.EnumDecisionModel;
import de.tif.jacob.rule.editor.rules.model.ObjectModel;


public class EnumDecisionModelEditPart extends DecisionModelEditPart
{
  
  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
	
  public IFigure createFigureForModel()
  {
      return new EnumDecisionFigure();
  }
  
  public void performRequest(Request request)
  {
    if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      new SelectEnumFieldDialog(display.getActiveShell(),(EnumDecisionModel)getModel()).open();
    }
    else
    {
      super.performRequest(request);
    }
  }
  
	protected void refreshVisuals()
  {
    EnumDecisionModel model = (EnumDecisionModel)getModel();
    Rectangle rect = model.getBounds();
    
    int count = model.getChildren().size();
    rect.height = Math.max(Constants.RULE_ELEMENT_HEIGHT,Constants.RULE_ELEMENT_HEIGHT*count+(Constants.RULE_GRID_HEIGHT*(count-1)) );
    Iterator iter = getChildren().iterator();
    while(iter.hasNext())
    {
      AbstractGraphicalEditPart part= (AbstractGraphicalEditPart)iter.next();
      part.refresh();
      rect.width = Math.max(rect.width,part.getFigure().getPreferredSize().width);
    }
    figure.setBounds(rect);
    iter = getChildren().iterator();
    while(iter.hasNext())
    {
      AbstractGraphicalEditPart part= (AbstractGraphicalEditPart)iter.next();
      part.refresh();
    }
  }
  
	
  public void propertyChange(PropertyChangeEvent evt)
  {
    String prop =evt.getPropertyName();
    
    if (ObjectModel.PROPERTY_ELEMENT_ADDED==prop)
      refreshChildren();
    else if (ObjectModel.PROPERTY_ELEMENT_REMOVED==prop)
    	refreshChildren();
    else if (ObjectModel.PROPERTY_MODEL_CHANGED==prop)
    {
    	refreshChildren();
    	refreshVisuals();
    }
    else
    	super.propertyChange(evt);
  }
  
}
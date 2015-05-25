package de.tif.jacob.rule.editor.rules.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import de.tif.jacob.rule.editor.rules.editpolicies.ShapeComponentEditPolicy;
import de.tif.jacob.rule.editor.rules.figures.CommentFigure;
import de.tif.jacob.rule.editor.rules.model.CommentModel;
import de.tif.jacob.rule.editor.rules.model.DecisionModel;


public class CommentEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener
{
  public CommentEditPart()
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
       getDecisionModel().addPropertyChangeListener(this);
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
  }

	/*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
   */
  protected IFigure createFigure()
  {
    return new CommentFigure();
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
      getDecisionModel().removePropertyChangeListener(this);
    }
  }

  private DecisionModel getDecisionModel()
  {
    return ((CommentModel) getModel()).getDecisionModel();
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
     refreshVisuals();
  }

  protected void refreshVisuals()
  {
    Label figure = (Label)getFigure();
    figure.setText(getDecisionModel().getDisplayLabel());

    int w=figure.getPreferredSize().width;
    int h=figure.getPreferredSize().height;
    
    Point position = getDecisionModel().getPosition();
    position = position.getTranslated(-w/2,-h);
    position.translate(getDecisionModel().getBounds().width/2,0);
    figure.setBounds(new Rectangle(position,new Dimension(w,h)));
  }
}
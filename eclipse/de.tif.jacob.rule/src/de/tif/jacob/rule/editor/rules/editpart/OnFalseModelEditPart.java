package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;

import de.tif.jacob.rule.editor.rules.editpart.anchor.BottomCenterAnchor;
import de.tif.jacob.rule.editor.rules.figures.OnFalseFigure;


public class OnFalseModelEditPart extends ConditionalExitModelEditPart
{
  public OnFalseModelEditPart()
  {
  }
  
  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  public IFigure createFigureForModel()
  {
    return new OnFalseFigure();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
  {
    return new BottomCenterAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getSourceConnectionAnchor(Request request)
  {
    return new BottomCenterAnchor(getFigure());
  }
  
  protected void refreshVisuals()
  {
  	BooleanDecisionModelEditPart parent = (BooleanDecisionModelEditPart)getParent();
    IFigure parentFig = parent.getFigure();
    
    int x = parentFig.getBounds().getCenter().x- figure.getSize().width/2;
    int y = parentFig.getBounds().getBottom().y- figure.getSize().height-1;
    
    figure.setLocation(new Point(x,y));
  }
}
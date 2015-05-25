package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;

import de.tif.jacob.rule.editor.rules.editpart.anchor.RightMiddleAnchor;
import de.tif.jacob.rule.editor.rules.figures.OnTrueFigure;


public class OnTrueModelEditPart extends ConditionalExitModelEditPart
{
  public OnTrueModelEditPart()
  {
  }
  
  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  public IFigure createFigureForModel()
  {
  
    return new OnTrueFigure();
  }

  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
  {
    return new RightMiddleAnchor(getFigure());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getSourceConnectionAnchor(Request request)
  {
    return new RightMiddleAnchor(getFigure());
  }
  
  protected void refreshVisuals()
  {
    BooleanDecisionModelEditPart parent = (BooleanDecisionModelEditPart)getParent();
    IFigure parentFig = parent.getFigure();
    
    int x = parentFig.getBounds().getRight().x- figure.getSize().width-1;
    int y = parentFig.getBounds().getCenter().y- figure.getSize().height/2;
    
    figure.setLocation(new Point(x,y));
  }
  
}
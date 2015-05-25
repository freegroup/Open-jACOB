package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;

import de.tif.jacob.rule.editor.rules.editpart.anchor.RightMiddleAnchor;
import de.tif.jacob.rule.editor.rules.figures.StartFigure;


public class StartEditPart extends RuleModelEditPart implements NodeEditPart
{
  public StartEditPart()
  {
  }
  
  /**
   * Return a IFigure depending on the instance of the current model element.
   * This allows this EditPart to be used for both sublasses of Shape.
   */
  public IFigure createFigureForModel()
  {
      return new StartFigure();
  }

  public void performRequest(Request request)
  {
  }
  
  protected void createEditPolicies()
  {
    super.createEditPolicies();
//    installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE , new DiagramElementDirectEditPolicy());
    installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
   */
  public ConnectionAnchor getSourceConnectionAnchor(Request request)
  {
    return new RightMiddleAnchor(((StartFigure)getFigure()).getCircle());
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
   */
  public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
  {
    return new RightMiddleAnchor(((StartFigure)getFigure()).getCircle());
  }

}
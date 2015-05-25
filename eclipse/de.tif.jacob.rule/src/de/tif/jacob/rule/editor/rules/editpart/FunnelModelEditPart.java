/*
 * Created on 07.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import de.tif.jacob.rule.editor.rules.figures.*;

public class FunnelModelEditPart extends RuleModelEditPart
{
  /**
   * @param figureClass
   */
  public FunnelModelEditPart()
  {
  }
  
  public IFigure createFigureForModel()
  {
    return new FunnelFigure();
  }
  
  public void performRequest(Request request)
  {
    if(request.getType()== RequestConstants.REQ_OPEN)
    {
    }
    else
    {
      super.performRequest(request);
    }
  }
}

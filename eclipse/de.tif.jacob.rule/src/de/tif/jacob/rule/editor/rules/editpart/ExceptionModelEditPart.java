/*
 * Created on 07.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import de.tif.jacob.rule.editor.rules.dialogs.exception.ExceptionParameterDialog;
import de.tif.jacob.rule.editor.rules.figures.ExceptionFigure;
import de.tif.jacob.rule.editor.rules.model.ExceptionModel;

public class ExceptionModelEditPart extends RuleModelEditPart
{
  /**
   * @param figureClass
   */
  public ExceptionModelEditPart()
  {
  }
  
  public IFigure createFigureForModel()
  {
    return new ExceptionFigure();
  }
  
  public void performRequest(Request request)
  {
    if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      
      new ExceptionParameterDialog(display.getActiveShell(),(ExceptionModel)getModel()).open();
    }
    else
    {
      super.performRequest(request);
    }
  }
}

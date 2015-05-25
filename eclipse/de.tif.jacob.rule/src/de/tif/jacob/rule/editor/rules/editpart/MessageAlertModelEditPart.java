/*
 * Created on 07.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import de.tif.jacob.rule.editor.rules.dialogs.messageAlert.AlertParameterDialog;
import de.tif.jacob.rule.editor.rules.figures.MessageAlertFigure;
import de.tif.jacob.rule.editor.rules.model.MessageAlertModel;

public class MessageAlertModelEditPart extends RuleModelEditPart
{
  /**
   * @param figureClass
   */
  public MessageAlertModelEditPart()
  {
  }
  
  public IFigure createFigureForModel()
  {
    return new MessageAlertFigure();
  }
  
  public void performRequest(Request request)
  {
    if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      
      new AlertParameterDialog(display.getActiveShell(),(MessageAlertModel)getModel()).open();
    }
    else
    {
      super.performRequest(request);
    }
  }
}

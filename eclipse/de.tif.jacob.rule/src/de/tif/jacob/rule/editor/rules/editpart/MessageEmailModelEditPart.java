/*
 * Created on 07.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import de.tif.jacob.rule.editor.rules.dialogs.commonBo.SelectBusinessMethodDialog;
import de.tif.jacob.rule.editor.rules.dialogs.messageAlert.AlertParameterDialog;
import de.tif.jacob.rule.editor.rules.dialogs.messageEmail.EmailParameterDialog;
import de.tif.jacob.rule.editor.rules.figures.MessageAlertFigure;
import de.tif.jacob.rule.editor.rules.figures.MessageEmailFigure;
import de.tif.jacob.rule.editor.rules.model.BusinessObjectModel;
import de.tif.jacob.rule.editor.rules.model.MessageAlertModel;
import de.tif.jacob.rule.editor.rules.model.MessageEMailModel;

public class MessageEmailModelEditPart extends RuleModelEditPart
{
  /**
   * @param figureClass
   */
  public MessageEmailModelEditPart()
  {
  }
  
  
  public IFigure createFigureForModel()
  {
    return new MessageEmailFigure();
  }


  public void performRequest(Request request)
  {
    if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      
      new EmailParameterDialog(display.getActiveShell(),(MessageEMailModel)getModel()).open();
    }
    else
    {
      super.performRequest(request);
    }
  }
}

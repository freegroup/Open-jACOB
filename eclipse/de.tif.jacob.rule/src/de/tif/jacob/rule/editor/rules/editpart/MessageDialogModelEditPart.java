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
import de.tif.jacob.rule.editor.rules.dialogs.userInformation.DialogParameterDialog;
import de.tif.jacob.rule.editor.rules.figures.MessageAlertFigure;
import de.tif.jacob.rule.editor.rules.figures.MessageDialogFigure;
import de.tif.jacob.rule.editor.rules.figures.MessageEmailFigure;
import de.tif.jacob.rule.editor.rules.model.BusinessObjectModel;
import de.tif.jacob.rule.editor.rules.model.MessageAlertModel;
import de.tif.jacob.rule.editor.rules.model.MessageDialogModel;

public class MessageDialogModelEditPart extends RuleModelEditPart
{
  /**
   * @param figureClass
   */
  public MessageDialogModelEditPart()
  {
  }
  
  public IFigure createFigureForModel()
  {
    return new MessageDialogFigure();
  }
  
  public void performRequest(Request request)
  {
    if(request.getType()== RequestConstants.REQ_OPEN)
    {
      org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display.getDefault();   
      
      new DialogParameterDialog(display.getActiveShell(),(MessageDialogModel)getModel()).open();
    }
    else
    {
      super.performRequest(request);
    }
  }
}

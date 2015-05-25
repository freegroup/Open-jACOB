/*
 * Created on Jul 4, 2004
 *
 */
package jacob.event.screen.f_administration.call_escalation;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Es muss überprüft werden ob bei manueller Eingabe (recipien=='folgende Adresse')
 * die Adresse auch wirklich eingeben wurde.<br>
 *
 */
public class ActionUpdate extends IActionButtonEventHandler
{
  /* 
   * 
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button)  throws Exception
  {
    // 
    if(context.getGroup().getDataStatus()==IGuiElement.UPDATE)
    {
		  String recipient =context.getGroup().getInputFieldValue("actionRecipient");
		  if(recipient.equals("folgende Adresse") && context.getGroup().getInputFieldValue("callactionNotificationaddr").length()==0)
		  {
		    IMessageDialog dialog =context.createMessageDialog("Das 'Adress' Feld muß ausgefüllt sein.");
		    dialog.show();
		    return false;
		  }
    }
    return true;
  }

  /* 
   * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onSuccess(IClientContext context, IGuiElement button)
  {
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context,  IGuiElement.GroupState status,  IGuiElement emitter)  throws Exception
  {
  }
}

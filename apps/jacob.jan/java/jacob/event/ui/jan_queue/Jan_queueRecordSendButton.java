/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 13:16:58 CEST 2006
 */
package jacob.event.ui.jan_queue;

import jacob.common.task.SendProtocol;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Jan_queueRecordSendButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Jan_queueRecordSendButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Jan_queueRecordSendButton.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    if (SendProtocol.send(context, context.getSelectedRecord()))
    {
      context.createMessageDialog("Message has been successfully sent!").show();
      context.getDataTable().clear();
    }
    else
    {
      context.createMessageDialog("Sending message failed!").show();
      context.getDataTable().reloadSelectedRecord();
    }
	}
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}


/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 07 21:49:31 CET 2006
 */
package jacob.event.ui.email;

import jacob.common.AbstractSendIMAP;
import jacob.model.Configuration;
import jacob.model.Email;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;


/**
 * The event handler for the ReplyButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ReplyButton extends AbstractSendIMAP 
{
	static public final transient String RCS_ID = "$Id: ReplyButton.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		String email = context.getUser().getEMail();
		IDataTableRecord record = context.getSelectedRecord();
		
		String subject = record.getSaveStringValue(Email.subject);
		String body    = record.getSaveStringValue(Email.body);
		String from    = record.getSaveStringValue(Email.from);
		
		String[] bodyLines = body.split("\n|\r");
		body="";
		for (int i = 0; i < bodyLines.length; i=i+2) // +2 => Beim split wird auch das \n als eigene Zeile mitgenommen
		{
			body=body+"> "+bodyLines[i]+"\n";
		}
		showDialog(context,email,from,"","Re: "+subject,body);
	}

	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception 
	{
		super.onGroupStatusChanged(context, state, element);
		((IButton)element).setIcon(Icon.email_open);
	}
	
}


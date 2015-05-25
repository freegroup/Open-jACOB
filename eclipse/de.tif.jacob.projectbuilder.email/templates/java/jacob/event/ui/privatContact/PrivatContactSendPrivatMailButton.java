/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 04 21:14:52 CEST 2006
 */
package jacob.event.ui.privatContact;

import jacob.common.AbstractSendIMAP;
import jacob.common.AppLogger;
import jacob.model.Configuration;
import jacob.model.PrivatContact;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;


/**
 * The event handler for the PrivatContactSendPrivatMailButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class PrivatContactSendPrivatMailButton extends AbstractSendIMAP 
{
	static public final transient String RCS_ID = "$Id: PrivatContactSendPrivatMailButton.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		String email = context.getUser().getEMail();
		String to    = context.getSelectedRecord().getSaveStringValue(PrivatContact.privat_email);
    String signature = (String)context.getUser().getProperty(Configuration.signature);
		showDialog(context,email,to,"","",signature);
	}

	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception 
	{
		super.onGroupStatusChanged(context, state, element);
		((IButton)element).setIcon(Icon.email_edit);
		((IButton)element).setLabel("");
		element.setEnable(state!= IGuiElement.SEARCH);
	}
}


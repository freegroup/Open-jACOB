/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 03 15:57:41 CET 2006
 */
package jacob.event.ui.configuration;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.SendFactory;
import jacob.model.Configuration;

import org.apache.commons.logging.Log;


/**
 * The event handler for the ConfigurationRecordSelectedButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ConfigurationRecordSelectedButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ConfigurationRecordSelectedButton.java,v 1.1 2007/11/25 22:11:22 freegroup Exp $";
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
		IAskDialog dialog= context.createAskDialog("To eMail:", new IAskDialogCallback() {
		
			public void onOk(IClientContext context, String value) throws Exception 
			{
				IDataTableRecord currentRecord = context.getSelectedRecord();

			  String smtpHost    = currentRecord.getSaveStringValue(Configuration.smtp_server);
			  String smtpUser    = currentRecord.getSaveStringValue(Configuration.smtp_user);
			  String smtpFrom    = currentRecord.getSaveStringValue(Configuration.from_email);
			  String smtpPasswd  = currentRecord.getSaveStringValue(Configuration.smtp_password);
			  String msg         = currentRecord.getSaveStringValue(Configuration.notify_about_deletemark);
			  String subject     = currentRecord.getSaveStringValue(Configuration.notify_about_deletesubject);
			  
			  SendFactory.send(smtpHost, smtpUser, smtpPasswd, smtpFrom, new String[]{value}, new String[]{}, subject, msg);
			}
		
			public void onCancel(IClientContext context) throws Exception 
			{
			}
		
		});
		
		dialog.show();
	}
}


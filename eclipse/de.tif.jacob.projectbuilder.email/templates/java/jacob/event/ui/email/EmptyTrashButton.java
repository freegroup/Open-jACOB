/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 04 21:25:19 CEST 2006
 */
package jacob.event.ui.email;

import javax.mail.Flags;
import javax.mail.Message;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Configuration;
import jacob.model.Email;
import jacob.util.imap.FolderUtil;

import org.apache.commons.logging.Log;

import com.sun.mail.imap.IMAPFolder;


/**
 * The event handler for the EmptyTrashButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class EmptyTrashButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: EmptyTrashButton.java,v 1.1 2007/11/25 22:12:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		String trashFolderName =  (String)context.getUser().getProperty(Configuration.folder_trash);
		String server    = (String)context.getUser().getProperty(Configuration.imap_server);
		String user  = (String)context.getUser().getProperty(Configuration.user);
		String password  = (String)context.getUser().getProperty(Configuration.password);
		IMAPFolder folder = FolderUtil.ensureFolderAndOpen(server, user, password, trashFolderName);


    Message[] msgs = folder.getMessages();

    if (msgs.length != 0) 
    {
			folder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
    }
    folder.close(true);

    
		IDataTable emailTable = context.getDataTable(Email.NAME);
		emailTable.qbeClear();
		emailTable.qbeSetValue(Email.folder, trashFolderName);
		
		IDataTransaction trans = context.getDataAccessor().newTransaction();

		try
		{
			emailTable.searchAndDelete(trans);
			trans.commit();
		}
		finally
		{
			trans.close();
		}
	}

	
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		String trashFolderName =  (String)context.getUser().getProperty(Configuration.folder_trash);
		button.setVisible(trashFolderName!=null && trashFolderName.length()>0);
		((IButton)button).setIcon(Icon.lightning);
	}
}

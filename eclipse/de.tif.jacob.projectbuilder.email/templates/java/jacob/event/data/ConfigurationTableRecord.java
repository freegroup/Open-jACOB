/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Feb 27 16:00:42 CET 2006
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.security.IUser;
import jacob.common.AppLogger;
import jacob.model.Configuration;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class ConfigurationTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: ConfigurationTableRecord.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

		// enter your code here
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
		Context context = Context.getCurrent();
		IUser user = context.getUser();
		if(!user.isSystem())
		{
			user.setProperty(Configuration.folder_out,tableRecord.getStringValue(Configuration.folder_out));
			user.setProperty(Configuration.folder_spam,tableRecord.getStringValue(Configuration.folder_spam));
			user.setProperty(Configuration.folder_trash,tableRecord.getStringValue(Configuration.folder_trash));
			user.setProperty(Configuration.save_incoming_emailaddress,tableRecord.getIntegerValue(Configuration.save_incoming_emailaddress));
			user.setProperty(Configuration.save_outgoing_emailaddress,tableRecord.getIntegerValue(Configuration.save_outgoing_emailaddress));
			user.setProperty(Configuration.imap_server,tableRecord.getStringValue(Configuration.imap_server));
			user.setProperty(Configuration.password,tableRecord.getStringValue(Configuration.password));
			user.setProperty(Configuration.smtp_server,tableRecord.getStringValue(Configuration.smtp_server));
			user.setProperty(Configuration.user,tableRecord.getStringValue(Configuration.user));
      user.setProperty(Configuration.signature,tableRecord.getStringValue(Configuration.signature));
		}
	}
}

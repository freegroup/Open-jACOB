/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Dec 12 11:49:25 CET 2006
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.common.Constants;
import jacob.common.RuleCache;
import jacob.model.Account;
import jacob.model.Room;

import java.io.ByteArrayInputStream;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.ruleengine.RuleEngine;

/**
 *
 * @author andherz
 */
public class AccountTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: AccountTableRecord.java,v 1.1 2007/02/02 22:26:46 freegroup Exp $";
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
		byte[] rule = RuleCache.get(Context.getCurrent(), Account.NAME, "afterNewAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		byte[] rule = RuleCache.get(Context.getCurrent(), Account.NAME, "afterDeleteAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
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
		byte[] rule = RuleCache.get(Context.getCurrent(), Account.NAME, "beforeCommitAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);

		// Der user muss Mitglied in einem Raum sein
		//
		Object key = tableRecord.getValue(Account.room_key);
		if(key ==null)
		{
			IDataTable roomTable = tableRecord.getAccessor().newAccessor().getTable(Room.NAME);
			roomTable.qbeSetKeyValue(Room.name, Constants.DEFAULT_ROOM_NAME);
			roomTable.search();
			tableRecord.setLinkedRecord(transaction, roomTable.getSelectedRecord());
		}
		
		// Falls die Mandator Id nicht vorhanden ist, dann wird diese ordentlich 
		// gesetzt
		//
		key = tableRecord.getValue(Account.mandator_id);
		if(key ==null)
		{
			IDataTableRecord room = tableRecord.getLinkedRecord(Room.NAME);
			tableRecord.setStringValue(transaction, Account.mandator_id, "/digisim/"+room.getSaveStringValue(Room.name)+"/"+tableRecord.getSaveStringValue(Account.loginname));
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
		byte[] rule = RuleCache.get(Context.getCurrent(), Account.NAME, "afterCommitAction");
		if(rule!=null)
			RuleEngine.execute(new ByteArrayInputStream(rule),tableRecord);
	}
}

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Oct 19 04:03:01 CEST 2005
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.reminder.IReminder;
import jacob.reminder.ReminderFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 *
 * @author andreas
 */
public class ContractTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: ContractTableRecord.java,v 1.4 2005/10/19 06:55:01 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    // Set agent, i.e. the current user
    //
    IDataTable agent = tableRecord.getAccessor().getTable("contractAgent");
    agent.qbeClear();
    agent.qbeSetKeyValue("pkey", transaction.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
    {
      tableRecord.setLinkedRecord(transaction, agent.getRecord(0));
    }
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
	public void beforeCommitAction(IDataTableRecord contractRecord, IDataTransaction transaction) throws Exception
	{
		if(contractRecord.isDeleted())
			return;

    // create project number
    //
    if (contractRecord.isNew())
    {
      if (contractRecord.hasLinkedRecord("product"))
      {
        IDataTableRecord product = contractRecord.getLinkedRecord("product");
        Date actualdate = new Date();
        DateFormat format = new SimpleDateFormat("'-'yyyyMMdd'-'");
        String projectnumber = product.getSaveStringValue("contractprefix") + format.format(actualdate) + contractRecord.getValue("pkey");
        contractRecord.setStringValue(transaction, "contractnumber", projectnumber);
      }
    }
    
    // set modified date and by
    //
    if (contractRecord.isUpdated() && contractRecord.hasChangedValues())
    {
      contractRecord.setValue(transaction, "datemodified", "now");
      contractRecord.setStringValueWithTruncation(transaction, "modifiedby", transaction.getUser().getFullName());
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord contractRecord) throws Exception
	{
    // (de)activate reminder
    //
    if (contractRecord.isDeleted())
    {
      if (!contractRecord.hasNullValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(contractRecord);
        myReminder.delete(contractRecord);
      }
    }
    else
    {
      if (contractRecord.hasChangedValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(contractRecord);

        if (contractRecord.hasNullValue("reminder"))
        {
          myReminder.delete(contractRecord);
        }
        else
        {
          myReminder.setWhen(contractRecord.getDateValue("reminder"));
          myReminder.setMethod(IReminder.OWNER);
          myReminder.setRecipient(contractRecord.getLinkedRecord("contractOwner"));

          myReminder.setMsg(ApplicationMessage.getLocalized("ContractReminder.Text", contractRecord.getValue("contractnumber")));
          myReminder.schedule();
        }
      }
    }
	}
}

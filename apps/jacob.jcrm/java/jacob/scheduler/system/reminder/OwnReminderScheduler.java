/*
 * Created on 26.09.2005
 *
 */
package jacob.scheduler.system.reminder;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.TaskContextSystem;

/**
 * @author andreas
 *
 */
public class OwnReminderScheduler extends AbstractScheduleReminder
{
  static public final transient String RCS_ID = "$Id: OwnReminderScheduler.java,v 1.1 2005/10/12 15:30:07 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  public static final int INDIVIDUAL = 0;
  public static final int ALERT = 1;
  public static final int EMAIL = 2;
  public static final int FAX = 3;
  public static final int SMS = 4;

  public OwnReminderScheduler()
  {
  }

  /**
   * Wenn Tabelle Activity, dann bookmark sonst System Alert
   * @param adressee
   * @param reminderentryRecord
   */
  public void sendAlert(String adressee, IDataTableRecord reminderentryRecord) throws Exception
  {
    IDataTransaction trans = reminderentryRecord.getAccessor().newTransaction();
    try
    {
      IDataTable table = reminderentryRecord.getAccessor().getTable("reminderalert");
      IDataTableRecord bookmark = table.newRecord(trans);

      bookmark.setValue(trans, "addressee", adressee);
      bookmark.setValue(trans, "tablename", reminderentryRecord.getValue("tablename"));
      bookmark.setValue(trans, "tablekey", reminderentryRecord.getValue("tablekey"));
      bookmark.setValue(trans, "dateposted", reminderentryRecord.getValue("when"));
      bookmark.setStringValueWithTruncation(trans, "message", reminderentryRecord.getSaveStringValue("message"));
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  public void notifyOwner(IDataTableRecord reminderentryRecord) throws Exception
  {
    IDataTable employeeTable = reminderentryRecord.getAccessor().getTable("employee");
    employeeTable.clear();
    employeeTable.qbeClear();
    employeeTable.qbeSetKeyValue("pkey", reminderentryRecord.getValue("ownerkey"));
    employeeTable.search();
    if (employeeTable.recordCount() != 1)
      logger.warn("Owner with key = " + reminderentryRecord.getValue("ownerkey") + " not found");

    IDataTableRecord employee = employeeTable.getRecord(0);
    String channel = employee.getSaveStringValue("notifymethod");
    if ("Email".equals(channel))
    {
      sendEmail(employee.getSaveStringValue("email"), reminderentryRecord.getSaveStringValue("message"));
    }
    if ("Fax".equals(channel))
    {
      sendFAX(employee.getSaveStringValue("fax"), reminderentryRecord.getSaveStringValue("message"));
    }
    if ("Alert".equals(channel))
    {
      sendAlert(employee.getSaveStringValue("loginname"), reminderentryRecord);
    }
    if ("Pager".equals(channel))
    {
      sendAlert(employee.getSaveStringValue("pager"), reminderentryRecord.getSaveStringValue("message"));
    }
  }

  public void notifyWorkgroup(IDataTableRecord reminderentryRecord) throws Exception
  {
    IDataAccessor accessor = reminderentryRecord.getAccessor().newAccessor();
    IDataTable groupTable = accessor.getTable("workgroup");
    groupTable.clear();
    groupTable.qbeClear();
    groupTable.qbeSetKeyValue("pkey", reminderentryRecord.getValue("workgroupkey"));
    groupTable.search();
    if (groupTable.recordCount() != 1)
      logger.warn("Workgroup with key = " + reminderentryRecord.getValue("workgroupkey") + " not found");

    IDataTableRecord workgroup = groupTable.getRecord(0);
    String channel = workgroup.getSaveStringValue("notifymethod");
    if ("Email".equals(channel))
    {
      sendEmail(workgroup.getSaveStringValue("notificationaddr"), reminderentryRecord.getSaveStringValue("message"));
    }
    if ("Fax".equals(channel))
    {
      sendFAX(workgroup.getSaveStringValue("notificationaddr"), reminderentryRecord.getSaveStringValue("message"));
    }
    if ("Alert".equals(channel))
    {
      sendAlert(workgroup.getSaveStringValue("notificationaddr"), reminderentryRecord);
    }
    if ("Pager".equals(channel))
    {
      sendAlert(workgroup.getSaveStringValue("notificationaddr"), reminderentryRecord.getSaveStringValue("message"));
    }
    if ("Owners".equals(channel))
    {
      IDataTable groupmemberTable = accessor.getTable("groupmember");
      groupmemberTable.qbeClear();
      groupmemberTable.clear();
      groupmemberTable.qbeSetValue("notifymethod", "!NULL");

      groupmemberTable.search();
      if (groupmemberTable.recordCount() < 1)
        logger.warn("No groupmembers to notify found for workgroup with key = " + reminderentryRecord.getValue("workgroupkey"));
      for (int i = 0; i < groupmemberTable.recordCount(); i++)
      {
        IDataTableRecord groupmember = groupmemberTable.getRecord(i);
        IDataTableRecord employee = groupmember.getLinkedRecord("employee");
        channel = groupmember.getSaveStringValue("notifymethod");
        if ("Email".equals(channel))
        {
          sendEmail(employee.getSaveStringValue("email"), reminderentryRecord.getSaveStringValue("message"));
        }
        if ("Fax".equals(channel))
        {
          sendFAX(employee.getSaveStringValue("fax"), reminderentryRecord.getSaveStringValue("message"));
        }
        if ("Alert".equals(channel))
        {
          sendAlert(employee.getSaveStringValue("loginname"), reminderentryRecord);
        }
        if ("Pager".equals(channel))
        {
          sendAlert(employee.getSaveStringValue("pager"), reminderentryRecord.getSaveStringValue("message"));
        }
      }
    }
  }

  private void processRecord(IDataTableRecord reminderentryRecord) throws Exception
  {
    IDataTransaction trans = reminderentryRecord.getTable().startNewTransaction();
    try
    {
      // lock record in advance
      trans.lock(reminderentryRecord);

      // process record, i.e. do appropriate notifications
      //
      switch (reminderentryRecord.getintValue("action"))
      {
        case INDIVIDUAL:
          if (reminderentryRecord.getValue("ownerkey") != null)
          {
            notifyOwner(reminderentryRecord);
          }
          else
          {
            if (reminderentryRecord.getValue("workgroupkey") != null)
            {
              notifyWorkgroup(reminderentryRecord);
            }
            else
            {
              throw new Exception("No Owner or Workgroup defined");
            }
          }
          break;

        case FAX:
          sendFAX(reminderentryRecord.getSaveStringValue("addressee"), reminderentryRecord.getSaveStringValue("message"));
          break;

        case EMAIL:
          sendEmail(reminderentryRecord.getSaveStringValue("addressee"), reminderentryRecord.getSaveStringValue("message"));
          break;

        case ALERT:
          sendAlert(reminderentryRecord.getSaveStringValue("addressee"), reminderentryRecord);
          break;

        case SMS:
          sendSMS(reminderentryRecord.getSaveStringValue("addressee"), reminderentryRecord.getSaveStringValue("message"));
          break;
      }

      // and delete the record
      //
      reminderentryRecord.delete(trans);
      trans.commit();
    }
    catch (RecordLockedException e)
    {
      // ignore -> try again next time
    }
    finally
    {
      trans.close();
    }
  }

  public void run(TaskContextSystem context) throws Exception
  {
    IDataTable eventTable = context.getDataTable("reminderentry");
    eventTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
    eventTable.clear();
    eventTable.qbeClear();
    eventTable.qbeSetKeyValue("when", "<=now"); // Zeitstempel wann eskalieren
    eventTable.search();
    for (int i = 0; i < eventTable.count(); i++)
    {
      processRecord(eventTable.getRecord(i));
    }
  }
}

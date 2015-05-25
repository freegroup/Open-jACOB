/*
 * Created on 24.05.2005 by mike
 * 
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
 * @author mike
 *
 */
public class QuintusReminderScheduler extends AbstractScheduleReminder
{
  static public final transient String RCS_ID = "$Id: QuintusReminderScheduler.java,v 1.4 2005/10/12 15:29:57 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();
  public static final int INDIVIDUAL = 0;
  public static final int ALERT = 1;
  public static final int EMAIL = 2;
  public static final int FAX = 3;
  public static final int SMS = 4;

  public QuintusReminderScheduler()
  {
  }

  /**
   * Wenn Tabelle Activity, dann bookmark sonst System Alert
   * @param adressee
   * @param record
   */
  public void sendAlert(String adressee, IDataTableRecord record) throws Exception
  {
    IDataTransaction trans = record.getAccessor().newTransaction();
    try
    {
      IDataTable table = record.getAccessor().getTable("qw_alert");
      IDataTableRecord bookmark = table.newRecord(trans);

      bookmark.setValue(trans, "addressee", adressee);
      bookmark.setValue(trans, "sender", "systemuser");
      bookmark.setValue(trans, "tablename", record.getValue("tablename"));
      bookmark.setValue(trans, "tablekey", record.getValue("tablekey"));
      bookmark.setStringValueWithTruncation(trans, "message", record.getSaveStringValue("message"));
      bookmark.setValue(trans, "alerttype", "Reminder");
      bookmark.setValue(trans, "dateposted", "now");
      bookmark.setValue(trans, "severity", "1");
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  public void notifyOwner(IDataTableRecord record) throws Exception
  {
    IDataTable employeeTable = record.getAccessor().getTable("employee");
    employeeTable.clear();
    employeeTable.qbeClear();
    employeeTable.qbeSetKeyValue("pkey", record.getValue("ownerkey"));
    employeeTable.search();
    if (employeeTable.recordCount() != 1)
      logger.warn("Owner with key = " + record.getValue("ownerkey") + " not found");

    IDataTableRecord employee = employeeTable.getRecord(0);
    String channel = employee.getSaveStringValue("notifymethod");
    if ("Email".equals(channel))
    {
      sendEmail(employee.getSaveStringValue("email"), record.getSaveStringValue("message"));
    }
    if ("Fax".equals(channel))
    {
      sendFAX(employee.getSaveStringValue("fax"), record.getSaveStringValue("message"));
    }
    if ("Alert".equals(channel))
    {
      sendAlert(employee.getSaveStringValue("loginname"), record);
    }
    if ("Pager".equals(channel))
    {
      sendAlert(employee.getSaveStringValue("pager"), record.getSaveStringValue("message"));
    }
  }

  public void notifyWorkgroup(IDataTableRecord record) throws Exception
  {
    IDataAccessor accessor = record.getAccessor().newAccessor();
    IDataTable groupTable = accessor.getTable("workgroup");
    groupTable.clear();
    groupTable.qbeClear();
    groupTable.qbeSetKeyValue("pkey", record.getValue("workgroupkey"));
    groupTable.search();
    if (groupTable.recordCount() != 1)
      logger.warn("Workgroup with key = " + record.getValue("workgroupkey") + " not found");

    IDataTableRecord workgroup = groupTable.getRecord(0);
    String channel = workgroup.getSaveStringValue("notifymethod");
    if ("Email".equals(channel))
    {
      sendEmail(workgroup.getSaveStringValue("notificationaddr"), record.getSaveStringValue("message"));
    }
    if ("Fax".equals(channel))
    {
      sendFAX(workgroup.getSaveStringValue("notificationaddr"), record.getSaveStringValue("message"));
    }
    if ("Alert".equals(channel))
    {
      sendAlert(workgroup.getSaveStringValue("notificationaddr"), record);
    }
    if ("Pager".equals(channel))
    {
      sendAlert(workgroup.getSaveStringValue("notificationaddr"), record.getSaveStringValue("message"));
    }
    if ("Owners".equals(channel))
    {
      IDataTable groupmemberTable = accessor.getTable("groupmember");
      groupmemberTable.qbeClear();
      groupmemberTable.clear();
      groupmemberTable.qbeSetValue("notifymethod", "!NULL");

      groupmemberTable.search();
      if (groupmemberTable.recordCount() < 1)
        logger.warn("No groupmembers to notify found for workgroup with key = " + record.getValue("workgroupkey"));
      for (int i = 0; i < groupmemberTable.recordCount(); i++)
      {
        IDataTableRecord groupmember = groupmemberTable.getRecord(i);
        IDataTableRecord employee = groupmember.getLinkedRecord("employee");
        channel = groupmember.getSaveStringValue("notifymethod");
        if ("Email".equals(channel))
        {
          sendEmail(employee.getSaveStringValue("email"), record.getSaveStringValue("message"));
        }
        if ("Fax".equals(channel))
        {
          sendFAX(employee.getSaveStringValue("fax"), record.getSaveStringValue("message"));
        }
        if ("Alert".equals(channel))
        {
          sendAlert(employee.getSaveStringValue("loginname"), record);
        }
        if ("Pager".equals(channel))
        {
          sendAlert(employee.getSaveStringValue("pager"), record.getSaveStringValue("message"));
        }
      }
    }
  }

  public void processRecord(IDataTableRecord record) throws Exception
  {
    switch (record.getintValue("action"))
    {
      case INDIVIDUAL:
        if (record.getValue("ownerkey") != null)
        {
          notifyOwner(record);
        }
        else
        {
          if (record.getValue("workgroupkey") != null)
          {
            notifyWorkgroup(record);
          }
          else
          {
            throw new Exception("No Owner od Workgroup defined");
          }
        }
        break;

      case FAX:
        sendFAX(record.getSaveStringValue("addressee"), record.getSaveStringValue("message"));
        break;
        
      case EMAIL:
        sendEmail(record.getSaveStringValue("addressee"), record.getSaveStringValue("message"));
        break;
        
      case ALERT:
        sendAlert(record.getSaveStringValue("addressee"), record);
        break;
        
      case SMS:
        sendSMS(record.getSaveStringValue("addressee"), record.getSaveStringValue("message"));
        break;
    }
    
    IDataTransaction trans = record.getTable().startNewTransaction();
    try
    {
      record.delete(trans);
      trans.commit();
    }
    catch (RecordLockedException e)
    {
      // nichts tun Event wird gerade geändert und nochmals gefeuert
    }
    finally
    {
      trans.close();
    }
  }

  public void run(TaskContextSystem context) throws Exception
  {
    IDataTable eventTable = context.getDataTable("qw_events");
    eventTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
    eventTable.clear();
    eventTable.qbeClear();
    eventTable.qbeSetKeyValue("escalationkey", "0"); // nur reminder
    eventTable.qbeSetKeyValue("when", "<=now"); // Zeitstempel wann eskalieren
    eventTable.search();
    for (int i = 0; i < eventTable.count(); i++)
    {
      processRecord(eventTable.getRecord(i));
    }
  }
}

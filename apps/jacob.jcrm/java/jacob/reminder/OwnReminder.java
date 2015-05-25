/*
 * Created on 22.09.2005
 *
 */
package jacob.reminder;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * @author andreas
 *
 */
public class OwnReminder extends IReminder
{
  static public final transient String RCS_ID = "$Id: OwnReminder.java,v 1.1 2005/10/12 15:29:01 sonntag Exp $";

  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  public OwnReminder(IDataTableRecord contextRecord)
  {
    setContextRecord(contextRecord);
  }

  /**
   * gibt einen eventuell vorhandenen EventRecord zurück
   * 
   * @param contextRecord
   * @return
   * @throws Exception
   */
  private IDataTableRecord getEventRecord(IDataTableRecord contextRecord) throws Exception
  {
    IDataTableRecord eventRecord = null;
    IDataTable eventTable = contextRecord.getAccessor().getTable("reminderentry");
    eventTable.clear();
    eventTable.qbeClear();
    // Constraint für Reminder:
    eventTable.qbeSetKeyValue("tablename", contextRecord.getTableAlias().getTableDefinition().getName());
    eventTable.qbeSetKeyValue("tablekey", contextRecord.getPrimaryKeyValue().toString());
    eventTable.search();
    if (eventTable.recordCount() == 1)
      eventRecord = eventTable.getRecord(0);

    return eventRecord;
  }

  /*
   * erzeugt einen Datensatz in qw_events
   * 
   * @see jacob.reminder.IReminder#schedule(de.tif.jacob.core.data.IDataTableRecord,
   *      int, java.lang.String, java.util.Date, java.lang.String)
   */
  public void schedule() throws Exception
  {
    if (recipient == null)
      throw new Exception("There is no recipient for this reminder defined.");

    IDataTransaction trans = contextRecord.getAccessor().newTransaction();

    try
    {
      IDataTableRecord eventRecord = getEventRecord(contextRecord);
      if (eventRecord == null)
      {
        eventRecord = contextRecord.getAccessor().getTable("reminderentry").newRecord(trans);
      }
      eventRecord.setDateValue(trans, "when", getWhen()); // wann soll
      // gefeuert
      // werden
      eventRecord.setValue(trans, "datemodified", "now"); // Zeitstempel
      eventRecord.setStringValueWithTruncation(trans, "message", getMsg()); // die
      // Nachricht
      eventRecord.setValue(trans, "tablename", contextRecord.getTableAlias().getTableDefinition().getName());
      eventRecord.setValue(trans, "tablekey", contextRecord.getPrimaryKeyValue().toString());

      switch (getMethod())
      {
        case IReminder.OWNER:
          eventRecord.setValue(trans, "action", "0");
          eventRecord.setValue(trans, "ownerkey", recipient.getPrimaryKeyValue().toString());
          break;
          
        case IReminder.WORKGROUP:
          eventRecord.setValue(trans, "action", "0");
          eventRecord.setValue(trans, "workgroupkey", recipient.getPrimaryKeyValue().toString());
          break;
          
        case IReminder.FAX:
          eventRecord.setIntValue(trans, "action", IReminder.FAX);
          eventRecord.setValue(trans, "addressee", recipient.getValue("fax"));
          break;
          
        case IReminder.EMAIL:
          eventRecord.setIntValue(trans, "action", IReminder.EMAIL);
          eventRecord.setValue(trans, "addressee", recipient.getValue("email"));
          break;
          
        case IReminder.ALERT:
          eventRecord.setIntValue(trans, "action", IReminder.ALERT);
          eventRecord.setValue(trans, "addressee", recipient.getValue("loginname"));
          break;
          
        case IReminder.SMS:
          eventRecord.setIntValue(trans, "action", IReminder.SMS);
          eventRecord.setValue(trans, "addressee", recipient.getValue("pager"));
          break;

        default:
          throw new Exception("There is no reminder method defined");
      }
      trans.commit();
    }

    finally
    {
      trans.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see jacob.reminder.IReminder#delete(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void delete(IDataTableRecord contextRecord) throws Exception
  {
    IDataTable eventTable = contextRecord.getAccessor().getTable("reminderentry");
    eventTable.qbeClear();
    eventTable.qbeSetKeyValue("tablename", contextRecord.getTableAlias().getTableDefinition().getName());
    eventTable.qbeSetKeyValue("tablekey", contextRecord.getPrimaryKeyValue().toString());
    IDataTransaction trans = eventTable.startNewTransaction();
    try
    {
      eventTable.fastDelete(trans);
    }
    finally
    {
      trans.close();
    }
  }
}

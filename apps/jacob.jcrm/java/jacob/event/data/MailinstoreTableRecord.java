/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import java.util.Date;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * 
 * @author andreas
 */
public class MailinstoreTableRecord extends DataTableRecordEventHandler
{

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // delete status record as well
    IDataTable statusTable = tableRecord.getAccessor().getTable("mailinstatus");
    statusTable.qbeClear();
    statusTable.qbeSetKeyValue("pkey", tableRecord.getValue("pkey"));
    statusTable.fastDelete(transaction);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
      return;

    IDataTable statusTable = tableRecord.getAccessor().getTable("mailinstatus");
    if (tableRecord.isNew())
    {
      IDataTableRecord statusRecord = statusTable.getSelectedRecord();
      if (statusTable.getSelectedRecord() == null)
      {
        statusRecord = statusTable.newRecord(transaction);
        statusRecord.setValue(transaction, "pkey", tableRecord.getValue("pkey"));
      }

      int mins = tableRecord.getintValue("interval");
      if (mins > 0)
        statusRecord.setValue(transaction, "nextaccess", "now+" + mins + "min");
      else
        statusRecord.setValue(transaction, "nextaccess", "now");
    }
    else if (tableRecord.hasChangedValue("interval"))
    {
      IDataTableRecord statusRecord = statusTable.getRecord(tableRecord.getPrimaryKeyValue());
      Date lastaccess = statusRecord.getDateValue("lastaccess");
      if (lastaccess == null)
        lastaccess = new Date();

      int mins = tableRecord.getintValue("interval");
      if (mins < 0)
        mins = 0;

      statusRecord.setDateValue(transaction, "nextaccess", lastaccess.getTime() + 60 * 1000L * mins);
    }
  }
}

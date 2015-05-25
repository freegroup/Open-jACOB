/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.common.AppProfile;
import jacob.common.DataUtils;
import jacob.reminder.IReminder;
import jacob.reminder.ReminderFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 * 
 * @author andreas
 */
public class SalesprojectTableRecord extends DataTableRecordEventHandler
{
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  private int calculateFocus(IDataTableRecord tableRecord) throws Exception
  {
    IDataTableRecord appprofile = AppProfile.getRecord(Context.getCurrent());
    Set focusType = new HashSet();
    Set focusChance = new HashSet();
    focusType.add(appprofile.getStringValue("so_type1"));
    focusChance.add(appprofile.getStringValue("so_chance1"));
    String chance = tableRecord.getStringValue("chance");
    String projecttype = tableRecord.getStringValue("type");
    BigDecimal estValue = tableRecord.getSaveDecimalValue("prod_value_est").add(tableRecord.getSaveDecimalValue("srv_value_est"));

    if (estValue.compareTo(appprofile.getSaveDecimalValue("so_value1")) > 0 && focusType.contains(projecttype) && focusChance.contains(chance))
      return 1;

    focusType.add(appprofile.getStringValue("so_type2"));
    focusChance.add(appprofile.getStringValue("so_chance2"));
    if (estValue.compareTo(appprofile.getSaveDecimalValue("so_value2")) > 0 && focusType.contains(projecttype) && focusChance.contains(chance))
      return 2;

    focusType.add(appprofile.getStringValue("so_type3"));
    focusChance.add(appprofile.getStringValue("so_chance3"));
    if (estValue.compareTo(appprofile.getSaveDecimalValue("so_value3")) > 0 && focusType.contains(projecttype) && focusChance.contains(chance))
      return 3;

    return 4;
  }

  public void afterCommitAction(IDataTableRecord projectRecord) throws Exception
  {
    // (de)activate reminder
    //
    if (projectRecord.isDeleted())
    {
      if (!projectRecord.hasNullValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(projectRecord);
        myReminder.delete(projectRecord);
      }
    }
    else
    {
      if (projectRecord.hasChangedValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(projectRecord);

        if (projectRecord.hasNullValue("reminder"))
        {
          myReminder.delete(projectRecord);
        }
        else
        {
          myReminder.setWhen(projectRecord.getDateValue("reminder"));
          myReminder.setMethod(IReminder.OWNER);
          if (projectRecord.hasLinkedRecord("salesrep"))
          {
            myReminder.setRecipient(projectRecord.getLinkedRecord("salesrep"));
          }
          else
          {
            myReminder.setRecipient(DataUtils.getUserRecord());
          }

          myReminder.setMsg(ApplicationMessage.getLocalized("ProjectReminder.Text", projectRecord.getValue("projectnumber")));
          myReminder.schedule();
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord projectRecord, IDataTransaction transaction) throws Exception
  {
    // Set agent, i.e. the current user
    //
    IDataTable agent = projectRecord.getAccessor().getTable("salesprojectAgent");
    agent.qbeClear();
    agent.qbeSetKeyValue("pkey", transaction.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
    {
      projectRecord.setLinkedRecord(transaction, agent.getRecord(0));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord projectRecord, IDataTransaction transaction) throws Exception
  {
    if (projectRecord.isDeleted())
      return;

    // calculate focus
    //
    projectRecord.setIntValue(transaction, "focus", calculateFocus(projectRecord));

    // create project number
    //
    if (projectRecord.isNew())
    {
      if (projectRecord.hasLinkedRecord("product"))
      {
        IDataTableRecord product = projectRecord.getLinkedRecord("product");
        Date actualdate = new Date();
        DateFormat format = new SimpleDateFormat("'-'yyyyMMdd'-'");
        String projectnumber = product.getSaveStringValue("contractprefix") + format.format(actualdate) + projectRecord.getValue("pkey");
        projectRecord.setStringValue(transaction, "projectnumber", projectnumber);
      }
    }
  }
}

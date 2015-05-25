/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException; 
import jacob.model.Activity;
import jacob.reminder.IReminder;
import jacob.reminder.ReminderFactory;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 *
 * @author andreas
 */
public class ActivityTableRecord extends DataTableRecordEventHandler
{

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    // (de)activate reminder
    //
    if (tableRecord.isDeleted())
    {
      if (!tableRecord.hasNullValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(tableRecord);
        myReminder.delete(tableRecord);
      }
    }
    else
    {
      if (tableRecord.hasChangedValue("reminder"))
      {
        IReminder myReminder = ReminderFactory.getReminder(tableRecord);

        if (tableRecord.hasNullValue("reminder"))
        {
          myReminder.delete(tableRecord);
        }
        else
        {
          myReminder.setWhen(tableRecord.getDateValue("reminder"));
          if (tableRecord.hasLinkedRecord("activityOwner"))
          {
            myReminder.setMethod(IReminder.OWNER);
            myReminder.setRecipient(tableRecord.getLinkedRecord("activityOwner"));
          }
          else
          {
            if (tableRecord.hasLinkedRecord("activityWorkgroup"))
            {
              myReminder.setMethod(IReminder.WORKGROUP);
              myReminder.setRecipient(tableRecord.getLinkedRecord("activityWorkgroup"));
            }
            else
            {
              myReminder.setMethod(IReminder.OWNER);
              myReminder.setRecipient(tableRecord.getLinkedRecord("activityAgent"));
            }
          }
          myReminder.setMsg(ApplicationMessage.getLocalized("ActivityReminder.Text", tableRecord.getStringValue("pkey"), tableRecord.getStringValue("description")));
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
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    tableRecord.setValue(transaction, "plan_start", "now");

    // Set agent, i.e. the current user
    //
    IDataTable agent = tableRecord.getAccessor().getTable("activityAgent");
    agent.qbeClear();
    agent.qbeSetKeyValue("pkey", transaction.getUser().getKey());
    agent.search();
    if (agent.recordCount() == 1)
    {
      tableRecord.setLinkedRecord(transaction, agent.getRecord(0));
    }
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

    if (!tableRecord.hasLinkedRecord("activitytype"))
    {
      // return here -> record would not commit anyhow since type is required
      return;
    }
    
    IDataTableRecord activity_type = tableRecord.getLinkedRecord("activitytype");
    
    // check whether contact is required
    //
    if (!tableRecord.hasLinkedRecord("activityContact"))
    {
        if (activity_type.getintValue("contact_req") == 1)
        {
          throw new BusinessException(new ApplicationMessage("ActivityTableRecord.1")); //$NON-NLS-1$
        }
    }
    
    // check whether project is required
    //
    if (!tableRecord.hasLinkedRecord("salesproject"))
    {
        if (activity_type.getintValue("project_req") == 1)
        {
          throw new BusinessException(new ApplicationMessage("ActivityTableRecord.0")); //$NON-NLS-1$
        }
    }
    
    if (tableRecord.hasChangedValue("workgroup_key"))
    {
      if (tableRecord.hasNullValue("workgroup_key"))
      {
        tableRecord.setValue(transaction, "dateassigned", null);
      }
      else
      {
        tableRecord.setValue(transaction, "dateassigned", "now");
      }
    }
    
    if (tableRecord.hasChangedValue("owner_key"))
    {
      if (tableRecord.hasNullValue("owner_key"))
      {
        tableRecord.setValue(transaction, "dateowned", null);
      }
      else
      {
        tableRecord.setValue(transaction, "dateowned", "now");
      }
    }
    
    // status has changed?
    if (tableRecord.hasChangedValue(Activity.status))
    {
      String status = tableRecord.getStringValue(Activity.status);
      
      if (Activity.status_ENUM._Not_Started.equals(status))
      {
        // reset start/done timestamps
        tableRecord.setValue(transaction, Activity.actual_start, null);
        tableRecord.setValue(transaction, Activity.actual_done, null);
      }
      else if (Activity.status_ENUM._Rejected.equals(status))
      {
        // do nothing here
      }
      else 
      {
        // status change to completed, in progress or deferred 
        
        
        if (tableRecord.hasNullValue(Activity.actual_start))
        {
          tableRecord.setValue(transaction, Activity.actual_start, "now");
        }
        
        if (Activity.status_ENUM._Completed.equals(status))
        {
          if (tableRecord.hasNullValue(Activity.actual_done))
          {
            tableRecord.setValue(transaction, Activity.actual_done, "now");
          }
        }
        else
        {
          tableRecord.setValue(transaction, Activity.actual_done, null);
        }
      }
    }
    
    if (tableRecord.hasLinkedRecord("incident"))
    {
      IDataTableRecord incident = tableRecord.getLinkedRecord("incident");
      String incidentStatus = incident.getSaveStringValue("status");
      if (!"Routed".equals(incidentStatus)&& !"Closed".equals(incidentStatus))
      {      
        incident.setValue(transaction, "status", "Routed");
      }
      
      
      if ("Completed".equals(tableRecord.getValue("status"))&& tableRecord.hasChangedValue("status"))
      {
        
        incident.setValue(transaction, "status", "Closed");
      }
    }
  }
}

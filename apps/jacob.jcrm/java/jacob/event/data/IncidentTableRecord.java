/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.model.Incident;
import jacob.model.IncidentDocument;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.security.IUser;

/**
 *
 * @author andreas
 */
public class IncidentTableRecord extends DataTableRecordEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord incident, IDataTransaction trans) throws Exception
	{
    deleteIncidentDocument(incident, trans);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord incident, IDataTransaction transaction) throws Exception
  {
    // Set agent, i.e. the current user
	  // Note: Do not set the agent, if incidents are created by tasks.
    //
    IUser user = transaction.getUser();
    if (!user.isSystem())
    {
      IDataTable agent = incident.getAccessor().getTable("incidentAgent");
      agent.qbeClear();
      agent.qbeSetKeyValue("pkey", user.getKey());
      agent.search();
      if (agent.recordCount() == 1)
      {
        incident.setLinkedRecord(transaction, agent.getRecord(0));
      }
    }
  }
  public void deleteIncidentDocument(IDataTableRecord incident, IDataTransaction trans) throws Exception
  {
    IDataTable document = incident.getAccessor().getTable(IncidentDocument.NAME);
    document.clear();
    document.qbeClear();
    document.qbeSetKeyValue(IncidentDocument.incident_key,incident.getValue(Incident.pkey));
    document.searchAndDelete(trans);
  }
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord incident, IDataTransaction trans) throws Exception
	{
    if (incident.isDeleted())
    {
      return;
    }
    
    if (incident.hasChangedValue("workgroup_key"))
    {
      if (incident.hasNullValue("workgroup_key"))
      {
        incident.setValue(trans, "status", "New");
      }
      else
      {
        incident.setValue(trans, "dateassigned", "now");
        incident.setValue(trans, "status", "Assigned");
      }
    }

    // set status change timestamps
    //
    if (incident.hasChangedValue("status"))
    {
      if ("Closed".equals(incident.getValue("status")))
      {
        incident.setValue(trans, "dateclosed", "now");
      }
      else
      {
        incident.setValue(trans, "dateclosed", null);
        if ("New".equals(incident.getValue("status")))
        {
          incident.setValue(trans, "dateassigned", null);
          incident.setValue(trans, "daterouted", null);
        }
        else if ("Routed".equals(incident.getValue("status")))
        {
          incident.setValue(trans, "daterouted", "now");
        }
      }
    }
	}
}

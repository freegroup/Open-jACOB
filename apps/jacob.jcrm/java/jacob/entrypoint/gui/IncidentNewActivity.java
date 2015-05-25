package jacob.entrypoint.gui;

import jacob.model.Activity;
import jacob.model.ActivityActivityDocument;
import jacob.model.ActivityDocument;
import jacob.model.Incident;

import java.util.Properties;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * A GUI Entry Point is one way to open the system with a dedicated form.
 * 
 * You can access this entry point within an WebBrowser with the URL:
 * http://localhost:8080/jacob/enter?entry=IncidentNewActivity&app=jcrm&user=USERNAME&pwd=PASSWORD&param1=abc
 * 
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and
 * password of the application. 2. Replace localhost:8080 with the real
 * servername and port. 3. You can add any additional parameters to the url. The
 * jACOB application servers will provide them for you via the
 * properties.getProperty("...") method.
 * 
 * @author {user}
 * 
 */
public class IncidentNewActivity extends IGuiEntryPoint
{

  /*
   * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
   */
  public void enter(IClientContext context, Properties props) throws Exception
  {
    String incidentKey = props.getProperty("incidentKey");
    // search incident
    IDataTable incidentTable = context.getDataTable("incident");
    incidentTable.qbeSetKeyValue("pkey", incidentKey);
    if (incidentTable.search() != 1)
      return;
    IDataTableRecord incident = incidentTable.getRecord(0);
    // set contact record
    IDataTable contact = context.getDataTable("activityContact");
    contact.qbeSetKeyValue("pkey", incident.getValue("contact_key"));
    contact.search();

    IDataTable activityTable = context.getDataTable("activity");
    IDataTransaction transaction = activityTable.startNewTransaction();

    // create an sales activity
    //

    IDataTableRecord activityRecord = activityTable.newRecord(transaction);

    activityRecord.setStringValueWithTruncation(transaction, "description", incident.getStringValue("description"));
    activityRecord.setValue(transaction, "importance", "Normal");
    activityRecord.setValue(transaction, "status", "In Progress");
    activityRecord.setValue(transaction, "plan_start", "now");
    activityRecord.setValue(transaction, "plan_done", "now+7d");
    if (contact.recordCount()==1)
    {
      activityRecord.setLinkedRecord(transaction,contact.getRecord(0));
     
    }
    
  //activityRecord.setValue(transaction, "contact_key", incident.getValue("contact_key"));
    activityRecord.setValue(transaction, "workgroup_key", incident.getValue("workgroup_key"));
    activityRecord.setValue(transaction, "agent_key", incident.getValue("agent_key"));
    activityRecord.setValue(transaction, "incident_key", incident.getValue("pkey"));
    activityRecord.setValue(transaction, "notes", incident.getValue("notes"));
    // link possible document records
    IDataTable activityDocument = context.getDataTable(ActivityActivityDocument.NAME);
    activityDocument.qbeSetKeyValue(ActivityDocument.incident_key,incident.getValue(Incident.pkey));
    activityDocument.search();
    for (int i = 0; i < activityDocument.recordCount(); i++)
    {
      activityDocument.getRecord(i).setValue(transaction,ActivityDocument.activity_key,activityRecord.getValue(Activity.pkey));
    }
    context.getDataAccessor().propagateRecord(activityRecord,"r_sales",Filldirection.BOTH);

  }

  /**
   * Return the domain for the GUI entry point. A Domain is the name of a group
   * in your outlookbar/composed application
   */
  public String getDomain()
  {
    return "f_sales";
  }

  /**
   * Return the name of a form within the returned domain.
   */
  public String getForm()
  {
    return "activity";
  }

  /**
   * @return false if the GUI entry point has no left side navigation
   *         (outlookbar)
   */
  public boolean hasNavigation()
  {
    return false;
  }

  /**
   * @return false if the GUI entry point has no search browser
   */
  public boolean hasSearchBrowser()
  {
    return false;
  }

  /**
   * @return false if the GUI entry point has no toolbar a the top
   */
  public boolean hasToolbar()
  {
    return false;
  }
}

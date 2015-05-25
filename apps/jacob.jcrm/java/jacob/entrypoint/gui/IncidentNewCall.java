package jacob.entrypoint.gui;

import jacob.model.Call;
import jacob.model.CallContact;
import jacob.model.CallDocument;
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
public class IncidentNewCall extends IGuiEntryPoint
{

  /*
   * @see de.tif.jacob.screen.entrypoint.IEntryPoint#execute()
   */
  public void enter(IClientContext context, Properties props) throws Exception
  {
    String incidentKey = props.getProperty("incidentKey");
    // search incident
    IDataTable incidentTable = context.getDataTable(Incident.NAME);
    incidentTable.qbeSetKeyValue(Incident.pkey, incidentKey);
    if (incidentTable.search() != 1)
      return;
    IDataTableRecord incident = incidentTable.getRecord(0);
    // set contact record
    IDataTable contact = context.getDataTable(CallContact.NAME);
    contact.qbeSetKeyValue(CallContact.pkey, incident.getValue(Incident.contact_key));
    contact.search();

    IDataTable callTable = context.getDataTable(Call.NAME);
    IDataTransaction transaction = callTable.startNewTransaction();

    // create an sales activity
    //

    IDataTableRecord callRecord = callTable.newRecord(transaction);
    jacob.common.Call.setDefault(context,callRecord,transaction);
    callRecord.setStringValueWithTruncation(transaction, Call.problem, incident.getStringValue(Incident.description));
    if (contact.recordCount()==1)
    {
      callRecord.setLinkedRecord(transaction,contact.getRecord(0));
      context.getDataAccessor().propagateRecord(contact.getRecord(0),"r_callcontact",Filldirection.BACKWARD);
    }

    callRecord.setValue(transaction, Call.incident_key, incident.getValue(Incident.pkey));
    callRecord.setValue(transaction, Call.problemtext, incident.getValue(Incident.notes));
    
    // link possible document records
    IDataTable incidentDocument = context.getDataTable(CallDocument.NAME);
    incidentDocument.qbeSetKeyValue(CallDocument.incident_key,incident.getValue(Incident.pkey));
    incidentDocument.search();
    for (int i = 0; i < incidentDocument.recordCount(); i++)
    {
      incidentDocument.getRecord(i).setValue(transaction,CallDocument.call_key,callRecord.getValue(Call.pkey));
    }
    context.getDataAccessor().propagateRecord(callRecord,"r_call",Filldirection.BOTH);
  }

  /**
   * Return the domain for the GUI entry point. A Domain is the name of a group
   * in your outlookbar/composed application
   */
  public String getDomain()
  {
    return "f_callManagement";
  }

  /**
   * Return the name of a form within the returned domain.
   */
  public String getForm()
  {
    return "callManage";
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

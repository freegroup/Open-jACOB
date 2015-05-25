/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.entrypoint.gui.ShowIncident;
import jacob.model.Categoryowner;
import jacob.model.Customer;
import jacob.model.Employee;
import jacob.model.IncidentEntry;
import jacob.model.IncidentEntryCategory;
import jacob.model.Incident_attachement;
import jacob.model.Organization;

import java.net.URL;
import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.xml.Serializer;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.messaging.Message;
import de.tif.jacob.transformer.Transformer;
import de.tif.jacob.util.xml.XMLWriter;

/**
 * 
 * @author andherz
 */
public class IncidentEntryTableRecord extends DataTableRecordEventHandler
{

  /**
   * 
   * @param incident
   * @param emailaddress
   * @param userLogin
   * @param xslFile
   * @throws Exception
   */
  private static void sendIncidentEmail(IDataTableRecord incidentRecord, String xslFile) throws Exception
  {
    IDataTableRecord categoryOwnerRecord = incidentRecord.getLinkedRecord(IncidentEntryCategory.NAME).getLinkedRecord(Categoryowner.NAME);
    String emailaddress = categoryOwnerRecord.getStringValue(Categoryowner.email);
    String userLogin = categoryOwnerRecord.getStringValue(Categoryowner.loginname);
    
    if (emailaddress != null)
    {
      IDataAccessor accessor = incidentRecord.getAccessor();
      IApplicationDefinition appDef = accessor.getApplication();

      // Transform the XML document to HTML
      //
      URL xsl = ClassProvider.getInstance(appDef, "jacob.resources.ResourceProvider").getClass().getResource(xslFile);
      String pkey = incidentRecord.getStringValue("pkey");
      String appId = appDef.getName();

      // create the url for the entry point
      //
      String url = "";
      Context context = Context.getCurrent();
      if (context instanceof SessionContext)
      {
        Properties props = new Properties();
        props.put("pkey", pkey);
        props.put("user", userLogin);
        url = XMLWriter.valueToXML(EntryPointUrl.getUrl((SessionContext) context, new ShowIncident(), props, false));
      }

      // Transformation of the request -> XML
      //
      StringBuffer sb = new StringBuffer(2048);
      sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
      sb.append("<msg><entrypoint><url>");
      sb.append(url);
      sb.append("</url><name>");
      sb.append(appId);
      sb.append("</name></entrypoint>");
      Serializer.appendXml(sb, incidentRecord, appDef.getDefaultRelationSet(), Filldirection.BACKWARD);
      sb.append("</msg>");
      String doc = new String(Transformer.render(sb.toString(), xsl, "text/html"), "ISO-8859-1");

      IDataTableRecord customerRecord = incidentRecord.getLinkedRecord(Customer.NAME);
      
      Message msg = Message.createHtmlEMailMessage(emailaddress, doc);
      msg.setSender(customerRecord.getStringValue(Customer.fullname));
      msg.setSenderAddr(customerRecord.getStringValue(Customer.email));
      
      // add incident attachments as email attachments
      IDataTable attachmentTable = accessor.getTable(Incident_attachement.NAME);
      attachmentTable.qbeClear();
      attachmentTable.qbeSetKeyValue(Incident_attachement.incident_key, pkey);
      attachmentTable.search();
      for (int i=0; i<attachmentTable.recordCount();i++)
      {
        IDataTableRecord attachmentRecord = attachmentTable.getRecord(i);
        DataDocumentValue document = attachmentRecord.getDocumentValue(Incident_attachement.document);
        msg.addAttachment(document);
      }
      
      msg.send();
    }
  }

  public void afterCommitAction(IDataTableRecord incidentEntryRecord) throws Exception
  {
    if (incidentEntryRecord.isNew())
    {
      if (Context.getCurrent().getUser().getMandatorId() == null)
        return; // keine Nachricht wenn Erfasser ein Bearbeiter ist

      sendIncidentEmail(incidentEntryRecord, "./stylesheet/NewIncidentMail.xsl");
    }
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    tableRecord.setValue(transaction, IncidentEntry.creator_key, transaction.getUser().getKey());
    
    // fill in the organization
    //
    Context context = Context.getCurrent();
    String userKey = context.getUser().getKey();

    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable employeeTable = acc.getTable(Employee.NAME);
    employeeTable.qbeClear();
    employeeTable.qbeSetKeyValue(Employee.pkey, userKey);
    employeeTable.search();
    IDataTableRecord organization = employeeTable.getSelectedRecord().getLinkedRecord(Organization.NAME);
    if (organization != null)
    {
      tableRecord.setLinkedRecord(transaction, organization);
    }
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // Be in mind: It is not possible to modify the 'tableRecord' if we want
    // delete them
    //
    if (tableRecord.isDeleted())
      return;

    // enter your code here
  }
}

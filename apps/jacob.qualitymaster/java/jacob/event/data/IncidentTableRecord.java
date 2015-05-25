/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 10 15:22:22 CEST 2009
 */
package jacob.event.data;

import jacob.entrypoint.gui.ShowIncidentCustomer;
import jacob.model.Categoryowner;
import jacob.model.Customer;
import jacob.model.Incident;

import java.net.URL;
import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataAccessor;
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
 * @author andreas
 */
public final class IncidentTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: IncidentTableRecord.java,v 1.5 2010-03-24 12:42:59 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  private static void sendIncidentEmailToCustomer(IDataTableRecord incidentRecord, String xslFile) throws Exception
  {
    Context context = Context.getCurrent();
    IDataTableRecord customerRecord = incidentRecord.getLinkedRecord(Customer.NAME);

    // keine Nachricht wenn Customer der Bearbeiter ist
    if (context.getUser().getKey().equals(customerRecord.getStringValue(Customer.pkey)))
      return;

    String emailaddress = customerRecord.getStringValue(Categoryowner.email);
    String userLogin = customerRecord.getStringValue(Categoryowner.loginname);

    if (emailaddress != null)
    {
      IDataAccessor accessor = incidentRecord.getAccessor();
      IApplicationDefinition appDef = accessor.getApplication();

      // Transform the XML document to HTML
      //
      URL xsl = ClassProvider.getInstance(appDef, "jacob.resources.ResourceProvider").getClass().getResource(xslFile);
      String pkey = incidentRecord.getStringValue(Incident.pkey);
      String appId = appDef.getName();

      // create the url for the entry point
      //
      String url = "";
      if (context instanceof SessionContext)
      {
        Properties props = new Properties();
        props.put("pkey", pkey);
        props.put("user", userLogin);
        url = XMLWriter.valueToXML(EntryPointUrl.getUrl((SessionContext) context, new ShowIncidentCustomer(), props, false));
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

      Message msg = Message.createHtmlEMailMessage(emailaddress, doc);
      msg.setSender(context.getUser().getFullName());
      msg.setSenderAddr(context.getUser().getEMail());
      msg.send();
    }
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if(!transaction.getUser().isSystem())
      tableRecord.setValue(transaction, Incident.creator_key, transaction.getUser().getKey());
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
      return;

  }

  public void afterCommitAction(IDataTableRecord incidentRecord) throws Exception
  {
    if (incidentRecord.isUpdated())
    {
      if (incidentRecord.hasChangedValue(Incident.state))
        sendIncidentEmailToCustomer(incidentRecord, "./stylesheet/StatusChangeIncidentMail.xsl");
      else if (incidentRecord.hasChangedValue(Incident.subject) || //
          incidentRecord.hasChangedValue(Incident.priority) || //
          incidentRecord.hasChangedValue(Incident.category_key) || //
          incidentRecord.hasChangedValue(Incident.milestone_key) || //
          incidentRecord.hasChangedValue(Incident.description) || //
          incidentRecord.hasChangedValue(Incident.cust_solution_info))
        sendIncidentEmailToCustomer(incidentRecord, "./stylesheet/ContentChangeIncidentMail.xsl");
    }
  }
}

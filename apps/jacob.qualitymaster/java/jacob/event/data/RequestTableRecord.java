/*
 * Created on Mar 31, 2004
 *
 */
package jacob.event.data;

import jacob.entrypoint.gui.ShowRequest;
import jacob.model.Creater;
import jacob.model.Employee;
import jacob.model.Owner;
import jacob.model.Request;
import jacob.model.Tester;

import java.net.URL;
import java.util.Properties;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.SessionContext;
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
 * @author Andreas Herz
 * 
 */
public class RequestTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: RequestTableRecord.java,v 1.32 2009-07-21 14:36:38 sonntag Exp $";

  static public final transient String RCS_REV = "$Revision: 1.32 $";

  /*
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.DataTableRecord,
   *      de.tif.jacob.core.data.DataTransaction) @author Andreas Herz
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.DataTableRecord,
   *      de.tif.jacob.core.data.DataTransaction) @author Andreas Herz
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    Context context = Context.getCurrent();
    if (context.getUser().getMandatorId() != null)
    {
      tableRecord.setValue(transaction, "organization_key", context.getUser().getMandatorId());
    }
  }

  /*
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.DataTableRecord,
   *      de.tif.jacob.core.data.DataTransaction) @author Andreas Herz
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
//    String status = tableRecord.getSaveStringValue(Request.state);

    // Der Tester und der Owner eines Requests dÃ¼rfen nicht die gleiche
    // Person sein.
    //
//    if (ObjectUtil.equalsIgnoreNull(tableRecord.getValue("owner_key"), tableRecord.getValue("tester_key")))
//      throw new UserException(new ApplicationMessage("RequestTableRecord.OwnerTesterSame"));
  }

  /*
   * Send a mail if the owner of an item has been changed
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.DataTableRecord)
   *      @author Andreas Herz
   */
  public void afterCommitAction(IDataTableRecord request) throws Exception
  {
    // Falls der Owner sich ändert, wird eine eMail versendet.
    //
    if (request.hasChangedValue(Request.owner_key))
      checkUserAssignmentAndSend(request, Owner.NAME, "./stylesheet/OwnerMail.xsl");

    // Falls der Tester sich ändert, wird eine eMail versendet.
    //
    if (request.hasChangedValue(Request.tester_key))
      checkUserAssignmentAndSend(request, Tester.NAME, "./stylesheet/TesterMail.xsl");

    if (request.hasChangedValue(Request.state))
    {
      // Falls der Request geschlossen wird, wird eine eMail an den Reporter versendet.
      //
      if (Request.state_ENUM._Done.equals(request.getStringValue(Request.state)))
        checkUserAssignmentAndSend(request, Creater.NAME, "./stylesheet/DoneMail.xsl");

      if (Request.state_ENUM._Obsolete.equals(request.getStringValue(Request.state)))
        checkUserAssignmentAndSend(request, Creater.NAME, "./stylesheet/ObsoleteMail.xsl");

      if (Request.state_ENUM._Duplicate.equals(request.getStringValue(Request.state)))
        checkUserAssignmentAndSend(request, Creater.NAME, "./stylesheet/DuplicateMail.xsl");

      if (Request.state_ENUM._Declined.equals(request.getStringValue(Request.state)))
        checkUserAssignmentAndSend(request, Creater.NAME, "./stylesheet/DeclinedMail.xsl");

      // Falls der Status auf [QA] gewechselt hat, muss dem Tester Bescheid
      // gesagt werden.
      //
      if (Request.state_ENUM._QA.equals(request.getStringValue(Request.state)))
      {
        checkUserAssignmentAndSend(request, Tester.NAME, "./stylesheet/TesterToDoMail.xsl");
      }
    }
  }

  /**
   * 
   * @param request
   * @param property
   * @param linkedTable
   * @param emailField
   * @param loginField
   * @param xslFile
   * @throws Exception
   */
  private static void checkUserAssignmentAndSend(IDataTableRecord request, String linkedTable, String xslFile) throws Exception
  {
    IDataTableRecord userRecord = request.getLinkedRecord(linkedTable);
    if (userRecord != null)
    {
      Context context = Context.getCurrent();

      // keine Nachricht wenn der Empfänger der Bearbeiter ist
      if (context.getUser().getKey().equals(userRecord.getStringValue(Employee.pkey)))
        return;

      String email = userRecord.getStringValue(Employee.email);
      String user = userRecord.getStringValue(Employee.loginname);
      if (email != null)
      {
        IApplicationDefinition appDef = request.getAccessor().getApplication();

        // Transform the XML document to HTML
        //
        URL xsl = ClassProvider.getInstance(appDef, "jacob.resources.ResourceProvider").getClass().getResource(xslFile);
        String pkey = request.getStringValue("pkey");
        String appId = appDef.getName();

        // create the url for the entry point
        //
        String url = "";
        if (context instanceof SessionContext)
        {
          Properties props = new Properties();
          props.put("pkey", pkey);
          props.put("user",user);
          
          url = XMLWriter.valueToXML(EntryPointUrl.getUrl((SessionContext) context, new ShowRequest(), props, false));
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
        Serializer.appendXml(sb, request, appDef.getDefaultRelationSet(), Filldirection.BACKWARD);
        sb.append("</msg>");
        String doc = new String(Transformer.render(sb.toString(), xsl, "text/html"), "ISO-8859-1");
        // System.out.println(sb.toString());
        // send the document as HTML-eMail
        //
        Message.sendHtmlEMail(email, doc);
      }
    }
  }
}

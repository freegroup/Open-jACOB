/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 11 11:07:12 CET 2010
 */
package jacob.entrypoint.cmd;
import jacob.common.AppLogger;
import jacob.model.Configuration;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
/**
 * This is a entry point for the 'docfinder' application. A CMD entry point is
 * another way to open the jACOB Application for external systems.
 * <p>
 * This type of entry point is useful, if your client is unable to
 * implement/call a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost
 * :8080/jacob/cmdenter?entry=SetupEMailAccount&app=docfinder&user
 * =USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and
 * password of the application.
 * <li>
 * 2. Replace localhost:8080 with the real server name and port.
 * <li>
 * 3. You can add any additional parameters to the url. The jACOB application
 * server will provide them for you via the
 * <code>properties.getProperty("...")</code> method.
 * <li>
 * 
 * @author andherz
 */
public class SetupEMailAccount implements ICmdEntryPoint
{
  static public final transient String RCS_ID = "$Id: SetupEMailAccount.java,v 1.1 2010/01/12 09:48:31 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /*
   * The main method for the entry point.
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws IOException
  {
    IDataAccessor acessor = context.getDataAccessor();
    // Es muss immer ein Configuration Record existieren
    //
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      String smtp_server = properties.getProperty("smtp.server");
      String smtp_user = properties.getProperty("smtp.user");
      String smtp_password = properties.getProperty("smtp.password");
      String smtp_from = properties.getProperty("smtp.from");
      String pop3_server = properties.getProperty("pop3.server");
      String pop3_user = properties.getProperty("pop3.user");
      String pop3_password = properties.getProperty("pop3.password");
      
      IDataTable table = context.getDataTable(Configuration.NAME);
      
      table.qbeClear();
      IDataTableRecord record =null;
      if (table.search() == 0)
      {
        record = table.newRecord(trans);
      }
      else
      {
        record = table.getRecord(0);
      }
      record.setValue(trans, Configuration.smtp_server, smtp_server);
      record.setValue(trans, Configuration.smtp_user, smtp_user);
      record.setValue(trans, Configuration.smtp_password, smtp_password);
      record.setValue(trans, Configuration.from_email, smtp_from);
      record.setValue(trans, Configuration.pop3_server, pop3_server);
      record.setValue(trans, Configuration.pop3_user, pop3_user);
      record.setValue(trans, Configuration.pop3_password, pop3_password);
      record.setValue(trans, Configuration.notify_about_delete, Boolean.TRUE);
      record.setValue(trans, Configuration.notify_about_deletemark, "delete message");
      record.setValue(trans, Configuration.notify_about_deletesubject, "DocFinder delete request");
      trans.commit();
    }
    catch (Exception e)
    {
      throw new IOException("InternalError:" + e.toString());
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * Returns the mime type for this entry point.
   * 
   * The Web client need this information for the proper display of the returned
   * content.
   */
  public String getMimeType(CmdEntryPointContext context, Properties properties)
  {
    return "text/plain";
  }
}

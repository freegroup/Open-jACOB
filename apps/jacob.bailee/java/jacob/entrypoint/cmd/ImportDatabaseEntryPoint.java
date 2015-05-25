/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Feb 01 10:06:23 CET 2010
 */
package jacob.entrypoint.cmd;
import jacob.common.AppLogger;
import jacob.model.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.file.Directory;
/**
 * This is a entry point for the 'bailee' application. A CMD entry point is
 * another way to open the jACOB Application for external systems.
 * <p>
 * This type of entry point is useful, if your client is unable to
 * implement/call a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=ImportDatabaseEntryPoint&app=bailee&user=USERNAME&pwd=PASSWORD&param1=abc
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
public class ImportDatabaseEntryPoint implements ICmdEntryPoint
{
  static public final transient String RCS_ID = "$Id: ImportDatabaseEntryPoint.java,v 1.1 2010/02/08 16:23:38 freegroup Exp $";
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
    IDataAccessor accessor = context.getDataAccessor();
    try
    {
      String directory = properties.getProperty("directory");
      ArrayList<File> files = Directory.getAll(new File(directory), false);
      for (File file : files)
      {
        if (file.getName().endsWith("description"))
        {
          IDataTransaction trans = accessor.newTransaction();
          try
          {
            String documentFileName = StringUtil.replace(file.getAbsolutePath(),"description","document");
            System.out.println(documentFileName);
            byte[] content=FileUtils.readFileToByteArray(new File(documentFileName));
            Properties props = new Properties();
            FileInputStream stream = new FileInputStream(file);
            props.load(stream);
            stream.close();
            IDataTableRecord imageRecord = Document.newRecord(context, trans);
            imageRecord.setStringValue(trans, Document.owner_email, props.getProperty("sender"));
            imageRecord.setStringValue(trans, Document.tag, props.getProperty("tag"));
            imageRecord.setDocumentValue(trans, Document.file, DataDocumentValue.create(props.getProperty("file-name"),content));
            trans.commit();
          }
          catch (Exception exc)
          {
            System.out.println(exc.getMessage());
          }
          finally
          {
            trans.close();
          }
        }
      }
    }
    catch (Exception e)
    {
      throw new IOException("InternalError:" + e.toString());
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

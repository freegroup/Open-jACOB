/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 23 22:52:11 CEST 2007
 */
package jacob.entrypoint.cmd;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import jacob.common.AppLogger;
import jacob.model.Feedback;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;


/**
 * This is a entry point for the 'digisim' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * <p>
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=CreateFeedback&app=digisim&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 * 
 * @author andherz
 */
public class CreateFeedback implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: CreateFeedback.java,v 1.1 2007/08/26 14:08:28 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/*
	 * The main method for the entry point.
	 * 
	 */
	public void enter(CmdEntryPointContext context, Properties properties) throws IOException
	{
		IDataAccessor accessor = context.getDataAccessor();
    IDataTransaction trans = accessor.newTransaction();
		try
		{
      String subject = properties.getProperty("subject");
      String message = properties.getProperty("message");

      IDataTableRecord record = accessor.getTable(Feedback.NAME).newRecord(trans);
      record.setValue(trans, Feedback.subject, subject);
      record.setValue(trans, Feedback.message, message);
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
	 * The Web client need this information for the proper display of the returned content.
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "text/plain";
	}
}

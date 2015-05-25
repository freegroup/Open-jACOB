/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 11 11:16:55 CET 2010
 */
package jacob.entrypoint.cmd;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import jacob.common.AppLogger;
import jacob.model.Whitelist;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;


/**
 * This is a entry point for the 'docfinder' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * <p>
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=AddToWhitelist&app=docfinder&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 * 
 * @author andherz
 */
public class AddToWhitelist implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: AddToWhitelist.java,v 1.2 2010/01/29 16:02:03 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
			String email = properties.getProperty("email");
      IDataTableRecord emailRecord = Whitelist.newRecord(accessor, trans);
      emailRecord.setValue(trans, Whitelist.email,email);
      trans.commit();
		}
		catch (Exception e)
		{
			// ignore. Eventeull ist die eMail bereits vorhanden
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

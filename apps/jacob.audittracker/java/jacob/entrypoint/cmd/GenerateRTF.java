/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Mar 13 08:40:49 CET 2007
 */
package jacob.entrypoint.cmd;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import jacob.common.AppLogger;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;


/**
 * This is a entry point for the 'audittracker' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * <p>
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=GenerateRTF&app=audittracker&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 * 
 * @author andherz
 */
public class GenerateRTF implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: GenerateRTF.java,v 1.1 2007/04/11 21:06:12 herz Exp $";
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
		try
		{
			String pkey = properties.getProperty("pkey");

			context.getStream().write(("This is the returned content with param1="+pkey).getBytes());
		}
		catch (Exception e)
		{
			throw new IOException("InternalError:" + e.toString());
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

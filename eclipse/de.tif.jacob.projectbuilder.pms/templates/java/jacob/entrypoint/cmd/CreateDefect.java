/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 24 17:50:49 CEST 2006
 */
package jacob.entrypoint.cmd;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import jacob.common.AppLogger;
import jacob.model.Defect;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.util.StringUtil;


/**
 * This is a entry point for the 'ProcessManagementSystem' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * <p>
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=CreateDefect&app={applicationName}&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 * 
 * @author andherz
 */
public class CreateDefect implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: CreateDefect.java,v 1.1 2007/11/25 22:17:56 freegroup Exp $";
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
		IDataTransaction transaction = accessor.newTransaction();
		try
		{
			String pkey = properties.getProperty("pkey");
			String subject = properties.getProperty("subject");
			String description = properties.getProperty("description");
			if(pkey==null || StringUtil.toSaveString(subject).length()==0 || StringUtil.toSaveString(description).length()==0)
			{
				context.getStream().write(("<html><body>Es murde <b>keine</b> Meldung angelegt. <i>Betreff</i> und <i>Beschreibung</i> waren nicht ausgef&uuml;llt...<a href=\"#\" onclick=\"window.close()\">schliessen</a></body></html>").getBytes());
				return;
			}

			IDataTable defectTable = accessor.getTable(Defect.NAME);
			IDataTableRecord defect =defectTable.newRecord(transaction);
			defect.setValue(transaction, Defect.subject, subject);
			defect.setValue(transaction, Defect.description, description);
			defect.setValue(transaction, Defect.process_key, pkey);
			transaction.commit();
			context.getStream().write(("<html><body>Neue Meldung wurde angelegt...<a href=\"#\" onclick=\"window.close()\">schliessen</a></body></html>").getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IOException("InternalError:" + e.toString());
		}
		finally
		{
			transaction.close();
		}
	}

	/**
	 * Returns the mime type for this entry point.
	 * 
	 * The Web client need this information for the proper display of the returned content.
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "text/html";
	}
}

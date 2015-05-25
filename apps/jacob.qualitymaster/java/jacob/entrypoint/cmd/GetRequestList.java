/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Dec 19 21:20:10 CET 2005
 */
package jacob.entrypoint.cmd;

import jacob.common.AppLogger;
import jacob.export.outline.castor.CastorDefect;
import jacob.export.outline.castor.CastorResponse;
import jacob.model.Request;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;


/**
 * This is a entry point for the 'qualitymaster' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * <p>
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=GetRequestList&app=qualitymaster&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 * 
 * @author andherz
 */
public class GetRequestList implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: GetRequestList.java,v 1.2 2006/02/24 02:16:16 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
		try
		{
			CastorResponse response = new CastorResponse();
			IDataTable requestTable = context.getDataTable(Request.NAME);

			requestTable.qbeSetValue(Request.owner_key, context.getUser().getKey());
			requestTable.qbeSetValue(Request.state, Request.state_ENUM._In_progress);
			requestTable.qbeSetValue(Request.state, Request.state_ENUM._New);
			requestTable.qbeSetValue(Request.state, Request.state_ENUM._Proved);
			requestTable.search();

			for(int i =0;i<requestTable.recordCount();i++)
			{
				IDataTableRecord record = requestTable.getRecord(i);
				CastorDefect defect = new CastorDefect();
				defect.setCreateDate(record.getSaveStringValue(Request.datereported));
				defect.setPkey(record.getSaveStringValue(Request.pkey));
				defect.setPriority(record.getSaveStringValue(Request.priority));
				defect.setState(record.getSaveStringValue(Request.state));
				defect.setSubject(record.getSaveStringValue(Request.subject));
				response.addCastorDefect(defect);
			}
			
			OutputStreamWriter writer = new OutputStreamWriter(context.getStream(),"ISO-8859-1");
			response.marshal(writer);
			writer.flush();
			writer.close();
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

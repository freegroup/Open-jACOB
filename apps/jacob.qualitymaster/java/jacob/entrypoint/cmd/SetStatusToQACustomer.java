package jacob.entrypoint.cmd;



import jacob.common.AppLogger;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;

/**
 * This is a entry point for the 'qualitymaster' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * 
 * This type of entry point is usefull if your client are unable to implement/call
 * a SOAP request.
 * 
 * You can return any type of dokument (XML, plain text, gif images,.....)
 * 
 * You can access this entry point with an WebBrowser with the url:
 * http://localhost:8100/jacob/cmdenter?entry=SetStatusToQACustomer&app=qualitymaster&user=q&pwd=q&pkey=17
 *  
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real username and password of the application.
 *       2. Replace localhost:8080 with the real servername and port.
 *       3. You can add any additional parameters to the url. The jACOB application servers will provide them
 *          for you via the properties.getProperty("...") method.
 */
public class SetStatusToQACustomer implements ICmdEntryPoint
{
	static Log logger = AppLogger.getLogger();

	/*
	 * The main method for the entry point
	 * 
	 */
	public void enter(CmdEntryPointContext context, Properties properties) throws IOException
	{
		IDataAccessor accessor = context.getDataAccessor();
    IDataTransaction trans = accessor.newTransaction();
		try
		{
			String pkey = properties.getProperty("pkey");
			IDataTable requestTable = accessor.getTable("request");
			requestTable.qbeClear();
      requestTable.qbeSetKeyValue("pkey",pkey);
      requestTable.qbeSetValue("requeststatus","QA");
      requestTable.search();
      if (requestTable.recordCount()==1)
      {
          requestTable.getRecord(0).setValue(trans,"requeststatus","QA Customer");
          trans.commit();
          context.getStream().write(("<html><head><META HTTP-EQUIV=Refresh CONTENT=\"0; URL=index.jsp\"></head></html>").getBytes());  
      }
      else
      {
          context.getStream().write(("<html><body>Status of Request "+pkey+" is not 'QA'.</body></html>").getBytes());
      }
            
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
	 * The Web client need this information for the propper display of the returned content.
	 */
	public String getMimeType(CmdEntryPointContext context, Properties properties)
	{
		return "text/html";
	}
}

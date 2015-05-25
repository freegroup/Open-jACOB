/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 19:41:26 CEST 2010
 */
package jacob.entrypoint.cmd;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import jacob.common.AppLogger;
import jacob.common.AttachmentUtil;
import jacob.model.Attachment;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.entrypoint.CmdEntryPointContext;
import de.tif.jacob.entrypoint.ICmdEntryPoint;
import de.tif.jacob.messaging.Message;


/**
 * This is a entry point for the 'wdb' application.
 * A CMD entry point is another way to open the jACOB Application for external
 * systems.
 * <p>
 * This type of entry point is useful, if your client is unable to implement/call
 * a SOAP request.
 * <p>
 * You can return any type of document (XML, plain text, gif images,.....)
 * <p>
 * You can access this entry point with an web browser with the url:
 * http://localhost:8080/jacob/cmdenter?entry=ImageProvider&app=wdb&user=USERNAME&pwd=PASSWORD&param1=abc
 * <p>
 * Note: 1. Replace USERNAME/PASSWORD in the url with the real user name and password of the application.<li>
 *       2. Replace localhost:8080 with the real server name and port.<li>
 *       3. You can add any additional parameters to the url. The jACOB application server will provide them
 *          for you via the <code>properties.getProperty("...")</code> method.<li>
 * 
 * @author andherz
 */
public class ImageProvider implements ICmdEntryPoint
{
	static public final transient String RCS_ID = "$Id: ImageProvider.java,v 1.2 2010-10-21 10:28:49 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";
  String DEFAULT_MIMETYPE = "application/octet-stream";

  /*
   * The main method for the entry point.
   * 
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws IOException
  {
    try
    {
      String pkey = properties.getProperty("id");
      IDataTableRecord docRecord = AttachmentUtil.findByPkey(context, pkey);
      String mimeType = DEFAULT_MIMETYPE;
      DataDocumentValue doc = docRecord.getDocumentValue(Attachment.document);
      mimeType = Message.getMimeType(doc.getName());
      if(mimeType==null)
        mimeType = DEFAULT_MIMETYPE;
      
      context.setContentType(mimeType);
      context.setContentName(doc.getName());
      
      context.getStream().write(doc.getContent());
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
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
    return DEFAULT_MIMETYPE;
  }
}

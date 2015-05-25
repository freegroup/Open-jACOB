/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.IDocumentFieldEventHandler;


/**
 *
 * @author {author}
 */
public class {class} extends IDocumentFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: IDocumentFieldEventHandler.java,v 1.1 2007/10/17 10:23:05 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  /**
   * Notfication if the user has upload a new document.
   * 
   * @param context
   *          The current client context
   * @param documentField
   *          The document field itself
   */
  public void afterUpload(IClientContext context, IDocument documentField, String docName, byte[] doc) throws Exception
  {
  }


  /**
   * Notification if the user has delete an existing document (clean).
   * 
   * @param context
   *          The current client context
   * @param documentField
   *          The document field itself
   */
  public void afterDelete(IClientContext context, IDocument documentField) throws Exception
  {
  }
}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.IMutableFormEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author {author}
 */
 public class {class} extends IMutableFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: IMutableFormEventHandler.java,v 1.2 2009/06/05 10:33:41 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();


  /**
   * Will be called, if there is a state change from visible=>hidden.
   * 
   * This happens, if the user switches the Domain or Form and the end state of this form
   * is "hidden".
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onHide(IClientContext context, IMutableForm form) throws Exception 
  {
    // insert your code here
  }
  
  /**
   * Will be called, if there is a state change from hidden=>visible.
   * 
   * This happens, if the user switches to this Form.
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onShow(IClientContext context, IMutableForm form) throws Exception 
  {
    // insert your code here
  }
}

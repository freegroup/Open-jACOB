/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 09 22:28:16 CET 2006
 */
package jacob.event.ui;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.IFormEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class Identity extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Identity.java,v 1.1 2007/11/25 22:12:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();


  /**
   * Will be called if the will be change the state from visible=>hidden.
   * 
   * This happends if the user switch the Domain or Form and the end state of this form
   * is "hidden".
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onHide(IClientContext context, IForm form) throws Exception 
  {
		System.out.println("hide:"+getClass().getName());
  }
  
  /**
   * Will be called if the will be change the state from hidden=>visible.
   * 
   * This happends if the user switch to this Form.
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onShow(IClientContext context, IForm form) throws Exception 
  {
		System.out.println("Show:"+getClass().getName());
  }
}
/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jan 15 17:25:20 CET 2009
 */
package jacob.event.ui;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.event.IFormEventHandler;


/**
 *
 * @author andherz
 */
 public class CALLHANDLING extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: CALLHANDLING.java,v 1.2 2009/04/02 13:40:13 A.Boeken Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
    form.hideTabStrip(true);
    // insert your code here
  }
}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Apr 10 14:32:20 CEST 2007
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
 public class Template extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Template.java,v 1.1 2007/04/11 21:06:12 herz Exp $";
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
    // insert your code here
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
    context.getApplication().setSearchBrowserVisible(false);
  }
}

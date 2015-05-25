/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 21 10:47:36 CEST 2010
 */
package jacob.event.ui;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IToolbar;
import de.tif.jacob.screen.event.IFormEventHandler;


/**
 *
 * @author andherz
 */
 public class Dbg_recyclebin extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Dbg_recyclebin.java,v 1.1 2010-09-21 11:28:20 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();


  /**
   * Will be called, if there is a state change from visible=>hidden.
   * 
   * This happens, if the user switches the domain or form and the end state of this form
   * is "hidden".
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onHide(IClientContext context, IForm form) throws Exception 
  {
    IToolbar buttonToolbar = context.getApplication().getToolbar();
    IToolbar searchToolbar = (IToolbar)context.getProperty("toolbar");
 
    context.getApplication().setToolbar(searchToolbar);
    context.setPropertyForWindow("toolbar",buttonToolbar);
    
  }
  
  /**
   * Will be called, if there is a state change from hidden=>visible.
   * 
   * This happens, if the user switches to this Form.
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onShow(IClientContext context, IForm form) throws Exception 
  {
    IToolbar buttonToolbar = context.getApplication().getToolbar();
    IToolbar searchToolbar = (IToolbar)context.getProperty("toolbar");
 
    context.getApplication().setToolbar(buttonToolbar);
    context.setPropertyForWindow("toolbar",searchToolbar);
  }
}

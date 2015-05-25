/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Mar 10 22:47:35 CET 2006
 */
package jacob.event.ui;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.IFormEventHandler;

import jacob.common.AppLogger;
import jacob.model.Email;

import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class CommonContact extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: CommonContact.java,v 1.1 2007/11/25 22:12:33 freegroup Exp $";
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
		Boolean initialSearch = (Boolean)context.getUser().getProperty(Application.INITIAL_COMMON);
		if(initialSearch==Boolean.TRUE)
		{
			context.getDataAccessor().qbeClearAll();
			IDataBrowser browser = context.getDataBrowser("commonContactBrowser");
			browser.search(IRelationSet.LOCAL_NAME);
			form.getCurrentBrowser().setData(context,browser);
			context.getUser().setProperty(Application.INITIAL_COMMON,Boolean.FALSE);
		}
  }
}

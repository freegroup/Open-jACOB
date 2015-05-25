/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 08 09:05:43 CET 2007
 */
package jacob.event.ui;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IFormEventHandler;
import de.tif.jacob.screen.impl.html.Form;

/**
 *
 * @author andherz
 */
 public class Ppt extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Ppt.java,v 1.2 2007/10/02 10:53:06 herz Exp $";
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
    IDataAccessor accessor = ((Form)form).getDataAccessor();
  	Boolean initialSearch = (Boolean)context.getUser().getProperty(Application.INITIAL_PPT);
  	if(initialSearch==Boolean.TRUE || initialSearch==null)
  	{
      accessor.qbeClearAll();
  		IDataBrowser browser = accessor.getBrowser("pptBrowser");
  		
  		browser.search("default",Filldirection.BOTH);
  		
  		// Von der "ppt" gruppe den browser holen und die neuen Daten darin einfüllen
  		//
  		IGroup group = (IGroup)context.getForm().findByName("pptGroup"); 
  		group.getBrowser().setData(context,browser);
  
      if(browser.recordCount()>=1)
      {
        browser.setSelectedRecordIndex(0);
        browser.propagateSelections();
      }
      
  		context.getUser().setProperty(Application.INITIAL_PPT,Boolean.FALSE);
  	}
  }
}

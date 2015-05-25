/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Feb 27 17:31:01 CET 2006
 */
package jacob.event.ui;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.*;

/**
 *
 * @author andherz
 */
public class Contact extends IDomainEventHandler
{
	static public final transient String RCS_ID = "$Id: Contact.java,v 1.1 2007/11/25 22:12:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  
  /**
   * This event method will be called, if the user switches to another form or
   * domain.
   * 
   * @param context
   *          The current client context
   * @param group The hidden group
   */
  public void onShow(IClientContext context, IDomain domain) throws Exception
  {
  	context.getApplication().setSearchBrowserVisible(true);
  }
}

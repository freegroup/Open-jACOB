/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 07 18:38:30 CET 2006
 */
package jacob.event.ui;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.*;

/**
 *
 * @author andherz
 */
public class Configuration extends IDomainEventHandler
{
	static public final transient String RCS_ID = "$Id: Configuration.java,v 1.1 2007/11/25 22:12:33 freegroup Exp $";
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
		IDataTable table = context.getDataTable(jacob.model.Configuration.NAME);
  	if(table.recordCount()!=1);
  	{
  		table.qbeClear();
  		table.search();
  	}
  	context.getApplication().setSearchBrowserVisible(false);
  }


	public void onHide(IClientContext arg0, IDomain arg1) throws Exception 
	{
	}
}

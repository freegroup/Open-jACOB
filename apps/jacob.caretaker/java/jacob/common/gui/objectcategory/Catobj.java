/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jan 24 08:42:47 CET 2008
 */
package jacob.common.gui.objectcategory;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Category;
import jacob.model.Object;

import org.apache.commons.logging.Log;


/**
 * The event handler for the Catobj record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Catobj extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Catobj.java,v 1.1 2008/01/25 10:14:35 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{

    IDataTableRecord currentRecord = context.getSelectedRecord();
    IDataBrowser browser = context.getDataBrowser("objectBrowser");
    IDataAccessor acc = context.getDataAccessor();
    IDataTransaction transaction = acc.newTransaction();
    try
    {
      for (int i = 0; i < browser.recordCount(); i++)
      {
        IDataTableRecord objrec = browser.getRecord(i).getTableRecord();
        objrec.setValue(transaction, Object.objectcategory_key, currentRecord.getValue(Category.pkey));
      }
      transaction.commit();
      alert(browser.recordCount() + " Objekte zugewiesen zu Gewerk --> " + currentRecord.getSaveStringValue(Category.longname));

    }
    finally
    {
      transaction.close();
    }

  

	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}


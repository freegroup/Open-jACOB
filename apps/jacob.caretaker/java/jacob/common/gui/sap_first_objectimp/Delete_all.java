/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 16 14:43:08 CET 2007
 */
package jacob.common.gui.sap_first_objectimp;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Sap_first_objectimp;
import jacob.model.Sap_objimport;

import org.apache.commons.logging.Log;


/**
 * The event handler for the Delete_all generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Delete_all extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Delete_all.java,v 1.1 2007/12/18 14:17:20 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();
  
  static class MyCallback implements IOkCancelDialogCallback
  {
    public void onOk(IClientContext context) throws Exception
    {
      IDataAccessor acc = context.getDataAccessor().newAccessor();
      IDataTransaction transaction = acc.newTransaction();
      IDataTable urimport = acc.getTable(Sap_first_objectimp.NAME);
      try 
      {
        urimport.fastDelete(transaction);
        transaction.commit();
      } 
      finally
      {
        transaction.close();
        }
      context.clearDomain();
    }

    public void onCancel(IClientContext context) throws Exception
    {

    }
  }

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    
    IOkCancelDialog dialog = context.createOkCancelDialog("Alle Synchronisationsanweisungen werden gelöscht!\nLöschen?", new MyCallback());
    dialog.show();
    

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

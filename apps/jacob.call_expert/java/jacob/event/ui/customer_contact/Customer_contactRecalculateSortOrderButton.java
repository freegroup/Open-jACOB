/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Feb 04 16:57:15 CET 2009
 */
package jacob.event.ui.customer_contact;

import jacob.common.AppLogger;
import jacob.model.Customer_contact;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Customer_contactRecalculateSortOrderButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author andherz
 */
public class Customer_contactRecalculateSortOrderButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Customer_contactRecalculateSortOrderButton.java,v 1.3 2009/11/23 11:33:44 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

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
    public void onClick(IClientContext context, IGuiElement emitter) throws Exception
    {
        IDataAccessor acc = context.getDataAccessor().newAccessor();

        IDataTransaction transaction = acc.newTransaction();
        try
        {
            IDataTable customerContactTable = acc.getTable(Customer_contact.NAME);
            //      customerContactTable.qbeSetValue(Customer_contact.sort_id, "null");
            customerContactTable.setUnlimitedRecords();
            customerContactTable.search();
            for(int i=0;i<customerContactTable.recordCount();i++)
            {
                IDataTableRecord contactRecord = customerContactTable.getRecord(i);
                contactRecord.setValue(transaction, Customer_contact.sort_id, contactRecord.getValue(Customer_contact.pkey));
            }
            transaction.commit();
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
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
    {
        // You can enable/disable the button in relation to your conditions.
        //
        //emitter.setEnable(true/false);
    }
}

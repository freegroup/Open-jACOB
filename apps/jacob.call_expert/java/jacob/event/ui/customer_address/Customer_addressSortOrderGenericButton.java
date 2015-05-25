/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Feb 05 14:52:16 CET 2009
 */
package jacob.event.ui.customer_address;

import jacob.common.AppLogger;
import jacob.model.Customer_address;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Customer_addressSortOrderGenericButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class Customer_addressSortOrderGenericButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Customer_addressSortOrderGenericButton.java,v 1.2 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

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
            IDataTable customerAdrTable = acc.getTable(Customer_address.NAME);
            //customerAdrTable.qbeSetValue(Customer_address.sort_id, "null");
            customerAdrTable.setUnlimitedRecords();
            customerAdrTable.search();
            for(int i=0;i<customerAdrTable.recordCount();i++)
            {
                IDataTableRecord adrRecord = customerAdrTable.getRecord(i);
                adrRecord.setValue(transaction, Customer_address.sort_id, adrRecord.getValue(Customer_address.pkey));
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
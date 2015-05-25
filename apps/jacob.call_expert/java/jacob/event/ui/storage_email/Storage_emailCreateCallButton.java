/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu May 14 16:12:06 CEST 2009
 */
package jacob.event.ui.storage_email;

import jacob.browser.CustomerBrowser;
import jacob.browser.RequestBrowser;
import jacob.common.AppLogger;
import jacob.common.FormManager;
import jacob.common.GroupManagerRequestHeader;
import jacob.model.Contact_summary;
import jacob.model.Customer;
import jacob.model.Customer_contact;
import jacob.model.Event;
import jacob.model.Event_history;
import jacob.model.Storage_email;
import jacob.relationset.CustomerCallRelationset;
import jacob.relationset.RequestRelationset;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Storage_emailCreateCallButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author andherz
 */
public class Storage_emailCreateCallButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Storage_emailCreateCallButton.java,v 1.9 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.9 $";

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
        IDataTableRecord storage_emailRecord = context.getSelectedRecord();
        IDataTableRecord aicRecord = context.getDataTable(Contact_summary.NAME).getSelectedRecord();

        String from = storage_emailRecord.getSaveStringValue(Storage_email.from);

        // Switch the form and DataAccessor to CallHandling
        //
        FormManager.showRequest(context,null);

        // Prepare a new Event-Record for further request update/new operation
        // Reason: we must link the new event record with the inbound email record at this place.
        //
        IDataTable eventTable = context.getDataTable(Event.NAME);
        IDataTableRecord eventRecord = eventTable.getSelectedRecord();

        if(eventRecord!=null && eventRecord.isNew())
        {
            // so nothing
        }
        else
        {
            // create a new EVENT and assign the inbound email with the event reocrd
            //
            IDataTransaction trans = context.getDataAccessor().newTransaction();
            eventRecord = Event.newRecord(context,trans);
            eventRecord.setValue(trans, Event.storage_email_key, storage_emailRecord.getValue(Storage_email.pkey));
            eventRecord.setValue(trans, Event.type, Event.type_ENUM._InboundEmail);
            eventRecord.setValue(trans, Event.direction, Event.direction_ENUM._in);
            eventRecord.setValue(trans, Event.media_type, Event.media_type_ENUM._email);

            IGuiElement searchButton = context.getForm().findByName("customerSearchButton");
            context.getForm().setInputFieldValue("customerFilterText",from);

            //IDataTable customer = context.getDataTable(Customer.NAME);
            IDataAccessor acc = context.getDataAccessor();
            IDataBrowser customer = acc.getBrowser(CustomerBrowser.NAME);
            IDataTable customerContact = acc.getTable(Customer_contact.NAME);

            // propagate the AIC Contact to the CALLHANDLING form
            //
            if(aicRecord!=null)
                acc.propagateRecord(aicRecord,IRelationSet.LOCAL_NAME ,Filldirection.NONE);


            customerContact.qbeClear();
            customerContact.qbeSetKeyValue(Customer_contact.contact, from);
            customer.search(CustomerCallRelationset.NAME);
            if (customer.recordCount()==1)
            {
                acc.propagateRecord(customer.getRecord(0).getTableRecord(), CustomerCallRelationset.NAME, Filldirection.BOTH);
                IDataBrowser requestBrowser = acc.getBrowser(RequestBrowser.NAME);
                IDataTable event = acc.getTable(Event_history.NAME);
                event.qbeSetKeyValue(Event.customer_key,customer.getRecord(0).getTableRecord().getValue(Customer.pkey));
                requestBrowser.search(RequestRelationset.NAME);
                if (requestBrowser.recordCount()==1)
                {
                    acc.propagateRecord(requestBrowser.getRecord(0).getTableRecord(), Filldirection.BOTH);
                }
                if (requestBrowser.recordCount()>1)
                {
                    context.showTransparentMessage(requestBrowser.recordCount() + " Requests exist for " + customer.getRecord(0).getTableRecord().getSaveStringValue(Customer.fullname));
                    FormManager.getCallhandling(context).setCurrentBrowser(GroupManagerRequestHeader.get(context).getBrowser());
                }
            }
            if (customer.recordCount()==0)
            {
                context.showTipDialog(searchButton, "Press 'Search' or add additional search criteria");
            }
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

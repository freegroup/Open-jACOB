/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 04 14:48:57 CET 2008
 */
package jacob.event.ui.customer;

import jacob.browser.CustomerBrowser;
import jacob.common.AppLogger;
import jacob.common.GroupManagerCustomerHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Customer;
import jacob.model.Customer_contact;
import jacob.model.Event;
import jacob.model.Request;
import jacob.relationset.CustomerEventRelationset;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;

/**
 * The event handler for the CustomerSearchButton search button.<br>
 *
 * @author achim
 */
public class CustomerSearchButton extends ISearchActionEventHandler
{
    static public final transient String RCS_ID = "$Id: CustomerSearchButton.java,v 1.7 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.7 $";

    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * This event handler will be called, if the corresponding button has been
     * pressed. You can prevent the execution of the SEARCH action if you return
     * <code>false</code>.<br>
     *
     * @param context
     *          The current context of the application
     * @param button
     *          The action button (the emitter of the event)
     * @return Return <code>false</code>, if you want to avoid the execution of
     *         the action else return <code>true</code>.
     */
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        String value = context.getGroup().getInputFieldValue("customerFilterText");
        if (GroupManagerCustomerHeader.isSelected(context))
        {
            // A Record is backfilled in the Group and the corresponding TabPane.
            // => 1. Clear the group and the TabPane.
            // 2. Restore the search criteria from the unbound text field
            //
            System.out.println("***search1"+context.getDataTable(Event.NAME).getSelectedRecord());
            context.clearGroup();
            System.out.println("***search2"+context.getDataTable(Event.NAME).getSelectedRecord());
            TabManagerRequest.clearTab(context, context.getGroup().getGroupTableAlias());
            System.out.println("***search3"+context.getDataTable(Event.NAME).getSelectedRecord());
            context.getGroup().setInputFieldValue("customerFilterText", value);
        }

        if (value != null && value.length() > 0)
        {
            context.getDataTable().qbeSetValue(Customer.fullname, "|" + value);
            if (StringUtils.isNumeric(value))
            {
                context.getDataTable().qbeSetValue(Customer.pkey, "|"+value);
            }

            //TODO: Change to addresses
            //context.getDataTable().qbeSetValue(Customer.city, "|" + value);
            context.getDataTable(Customer_contact.NAME).qbeSetValue(Customer_contact.contact, "|" + value);
        }
        System.out.println("***search"+context.getDataTable(Event.NAME).getSelectedRecord());
        return true;
    }

    /**
     * This event method will be called, if the SEARCH action has been
     * successfully executed.<br>
     *
     * @param context
     *          The current context of the application
     * @param button
     *          The action button (the emitter of the event)
     */
    public void onSuccess(IClientContext context, IGuiElement button)
    {
        try
        {
            IBrowser guiBrowser = context.getGUIBrowser();
            // do any stupid search
            //
            IDataAccessor acc = context.getDataAccessor().newAccessor();
            IDataBrowser additionalBrowser = acc.getBrowser(CustomerBrowser.NAME);
            IDataTable event = context.getDataTable(Event.NAME);

            if (null != context.getDataTable(Request.NAME).getSelectedRecord())
            {
                event.qbeSetKeyValue(Event.request_key, context.getDataTable(Request.NAME).getSelectedRecord().getPrimaryKeyValue());

                // additionalBrowser.search("customerEventRelationset");
                additionalBrowser.searchWhere(CustomerEventRelationset.NAME, "event.request_key="
                    + context.getDataTable(Request.NAME).getSelectedRecord().getPrimaryKeyValue());

                // reverse the loop so the same order will be maintained, since
                // we add to the top
                for (int i = additionalBrowser.recordCount() - 1; i >= 0; i--)
                {
                    IDataBrowserRecord record = additionalBrowser.getRecord(i);
                    // remove first to ensure it will be added again at the top
                    removeRecord(guiBrowser, record);
                    guiBrowser.add(context, record.getTableRecord(), false);
                }
                for (int i = 0; i < additionalBrowser.recordCount(); i++)
                {
                    guiBrowser.setEmphasize(context, i, true);
                }
                context.getGUIBrowser().setSelectedRecordIndex(context, -1);
            }
            // Ensure that the content area are visible (if possible)
            if(guiBrowser.getSelectedRecord(context)!=null)
                TabManagerRequest.setActive(context, Customer.NAME);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void removeRecord(IBrowser browser, IDataBrowserRecord record)
    {
        // ideal solution: browser.getData().removeRecord(record)
        // that does not work though since the record is not directly from the
        // same browser
        IDataBrowser dataBrowser = browser.getData();
        int count = dataBrowser.recordCount();
        for (int i = 0; i < count; i++)
        {
            IDataBrowserRecord browserRecord = dataBrowser.getRecord(i);
            // direct equals does not work, so compare the primary key values
            if (browserRecord.getPrimaryKeyValue().equals(record.getPrimaryKeyValue()))
            {
                dataBrowser.removeRecord(browserRecord);
                return;
            }
        }
    }

    /**
     * It is not possible to search if the goup is in the new or update mode.
     *
     * @param context
     *          The current client context
     * @param status
     *          The new group state. The group is the parent of the corresponding
     *          event button.
     * @param button
     *          The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement button) throws Exception
    {
        button.setEnable(!(state == IGroup.NEW || state == IGroup.UPDATE));
    }
}

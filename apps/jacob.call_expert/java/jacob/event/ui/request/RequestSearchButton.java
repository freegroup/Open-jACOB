/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Nov 05 15:47:03 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.AppLogger;
import jacob.common.GroupManagerRequestHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Customer_history;
import jacob.model.Event_history;
import jacob.model.Request;
import jacob.model.Serial;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;

/**
 * The event handler for the RequestSearchButton search button.<br>
 *
 * @author achim
 */
public class RequestSearchButton extends ISearchActionEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestSearchButton.java,v 1.6 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.6 $";

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
        String value = context.getGroup().getInputFieldValue("requestFilterText");
        if (GroupManagerRequestHeader.isSelected(context))
        {
            // A Record is backfilled in the Group and the corresponding TabPane.
            // => 1. Clear the group and the TabPane.
            // 2. Restore the search criteria from the unbound text field
            //
            context.clearGroup();
            TabManagerRequest.clearTab(context, context.getGroup().getGroupTableAlias());
            context.getGroup().setInputFieldValue("requestFilterText", value);
        }

        if (StringUtils.isNotEmpty(value))
        {
            if (null != context.getDataTable(Customer_history.NAME).getSelectedRecord())
            {
                context.getDataTable(Event_history.NAME).qbeSetValue(Event_history.customer_key,"|"+
                    context.getDataTable(Customer_history.NAME).getSelectedRecord().getSaveStringValue(Customer_history.pkey));
            }
            context.getDataTable().qbeSetValue(Request.description, "|" + value);
            context.getDataTable().qbeSetValue(Request.pkey_txt, "|" + value);
            context.getDataTable(Serial.NAME).qbeSetValue(Serial.serialno, "|" + value);

            // Constrain Search to Event-Customer

            // Doesn't work at the moment. This depends on the INNER JOIN / OUTER JOIN
            // stuff of the QBE engine.
            // Solution required?!
            //
            // context.getDataTable(Vendor.NAME).qbeSetValue(Vendor.name, "|"+value);
            // context.getDataTable(Model.NAME).qbeSetValue(Model.name, "|"+value);
            // context.getDataTable(Product.NAME).qbeSetValue(Product.name,
            // "|"+value);
        }
        else
        {
            if (null != context.getDataTable(Customer_history.NAME).getSelectedRecord())
            {
                context.getDataTable(Event_history.NAME).qbeSetValue(Event_history.customer_key,
                    context.getDataTable(Customer_history.NAME).getSelectedRecord().getSaveStringValue(Customer_history.pkey));
            }

        }
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
            // Ensure that the content area are visible (if possible)
            if(context.getSelectedRecord()!=null)
                TabManagerRequest.setActive(context, Request.NAME);
        }
        catch (Exception e)
        {
            ExceptionHandler.handle(context,e);
        }
    }

    /**
     * The status of the parent group (TableAlias) has been changed.<br>
     * <br>
     * This is a good place to enable/disable the button on relation to the group
     * state or the selected record.<br>
     * <br>
     * Possible values for the different states are defined in IGuiElement<br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
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
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        button.setEnable(status.equals(IGroup.SEARCH) || status.equals(IGroup.SELECTED));
    }
}

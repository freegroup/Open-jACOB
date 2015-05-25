/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Jan 20 14:21:37 CET 2009
 */
package jacob.event.ui.event;

import jacob.common.AppLogger;
import jacob.common.tabcontainer.TabManagerHistoryDomainEvent;
import jacob.model.Customer;
import jacob.model.Event_company;
import jacob.model.Request;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;

/**
 * Central Class with handles the visibility of the the different tabs in the
 * content area.
 *
 * @author achim
 */
public class EventContainer extends ITabContainerEventHandler
{
    static public final transient String RCS_ID  = "$Id: EventContainer.java,v 1.5 2009/11/23 11:33:44 R.Spoor Exp $";

    static public final transient String RCS_REV = "$Revision: 1.5 $";

    /**
     * Use this logger to write messages and NOT the
     * <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log   logger  = AppLogger.getLogger();

    /**
     * Will be fired when the Event-Records has been changed.
     *
     */
    public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
    {
        if (state == IGroup.SELECTED)
        {
            IDataTableRecord companyRecord = context.getDataTable(Event_company.NAME).getSelectedRecord();
            IDataTableRecord customerRecord = context.getDataTable(Customer.NAME).getSelectedRecord();
            IDataTableRecord requestRecord = context.getDataTable(Request.NAME).getSelectedRecord();

            TabManagerHistoryDomainEvent.setVisible(context, Event_company.NAME, companyRecord != null);
            TabManagerHistoryDomainEvent.setVisible(context, Customer.NAME, customerRecord != null);
            TabManagerHistoryDomainEvent.setVisible(context, Request.NAME, requestRecord != null);
        }
    }
}

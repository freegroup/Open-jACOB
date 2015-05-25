/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Nov 06 13:48:44 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.AppLogger;
import jacob.common.tabcontainer.TabManagerSerial;
import jacob.model.Serial;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author achim
 */
public class RequestSerialContainer extends ITabContainerEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestSerialContainer.java,v 1.2 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /*
     * This event method will be called, if the status of the corresponding group
     * has been changed. Derived event handlers could overwrite this method, e.g.
     * to enable/disable GUI elements in relation to the group state. <br>
     * Possible group state values are defined in
     * {@link IGuiElement}:<br>
     * <ul>
     *     <li>{@link IGuiElement#UPDATE}</li>
     *     <li>{@link IGuiElement#NEW}</li>
     *     <li>{@link IGuiElement#SEARCH}</li>
     *     <li>{@link IGuiElement#SELECTED}</li>
     * </ul>
     *
     * @param context
     *          The current client context
     * @param state
     *          The new group state
     * @param element
     *          The corresponding GUI element to this event handler
     */
    public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
    {
        element.hideTabStrip(true);
        if (state == IGuiElement.SELECTED)
        {
            // either selected a request, or cancelled the current request
            // cancel the serial transaction if present, then show the display tab
            IDataTableRecord serialRecord = context.getDataTable(Serial.NAME).getSelectedRecord();
            if (serialRecord != null)
            {
                IDataTransaction transaction = serialRecord.getCurrentTransaction();
                if (transaction != null)
                {
                    transaction.close();
                }
            }
            TabManagerSerial.showDisplayTab(context);
        }
    }
}

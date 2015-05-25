/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 18 16:22:24 CET 2008
 */
package jacob.event.ui.company;

import jacob.common.AppLogger;
import jacob.common.GroupManagerCustomerHeader;
import jacob.common.GroupManagerRequestHeader;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author achim
 */
public class CompanyHeaderGroup extends IGroupEventHandler
{
    static public final transient String RCS_ID = "$Id: CompanyHeaderGroup.java,v 1.4 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * The status of the parent group (TableAlias) has been changed.<br>
     * <br>
     * Possible values for the different states are defined in IGuiElement<br>
     * <ul>
     *     <li>IGuiElement.UPDATE</li>
     *     <li>IGuiElement.NEW</li>
     *     <li>IGuiElement.SEARCH</li>
     *     <li>IGuiElement.SELECTED</li>
     * </ul>
     *
     * @param context The current client context
     * @param status  The new status of the group.
     * @param emitter The corresponding GUI element of this event handler
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
    {
        GroupManagerCustomerHeader.enableChildren(context, status==IGroup.SEARCH ||status == IGroup.SELECTED);
        GroupManagerRequestHeader.enableChildren(context, status==IGroup.SEARCH ||status == IGroup.SELECTED);
    }

    @Override
    public Class getSearchBrowserEventHandlerClass()
    {
        return CompanyBrowser.class;
    }


}

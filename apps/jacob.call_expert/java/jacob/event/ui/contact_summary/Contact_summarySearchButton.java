/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Aug 12 12:06:23 CEST 2009
 */
package jacob.event.ui.contact_summary;

import jacob.common.AppLogger;
import jacob.model.Contact_summary;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;


/**
 * The event handler for the Contact_summarySearchButton search button.<br>
 *
 * @author achim
 */
public class Contact_summarySearchButton extends ISearchActionEventHandler
{
    static public final transient String RCS_ID = "$Id: Contact_summarySearchButton.java,v 1.2 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
     */
    static private final transient Log logger = AppLogger.getLogger();

    /**
     * This event handler will be called, if the corresponding button has been pressed.
     * You can prevent the execution of the SEARCH action if you return <code>false</code>.<br>
     *
     * @param context The current context of the application
     * @param button  The action button (the emitter of the event)
     * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
     */
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        int x = (Integer)context.getProperty("ContactDelay");
        String delay = "now - " +x + "h .. now";
        System.out.println(delay);
        context.getDataTable().qbeSetValue(Contact_summary.createtime, delay);
        return true;
    }

    /**
     * This event method will be called, if the SEARCH action has been successfully executed.<br>
     *
     * @param context The current context of the application
     * @param button  The action button (the emitter of the event)
     */
    public void onSuccess(IClientContext context, IGuiElement button)
    {
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

/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Jan 15 17:44:23 CET 2009
 */
package jacob.event.ui.serial;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IHotkeyEventHandler;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.screen.event.KeyEvent;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 *
 * @author andherz
 */
public class SerialSerailno extends ITextFieldEventHandler implements IHotkeyEventHandler
{
    static public final transient String RCS_ID = "$Id: SerialSerailno.java,v 1.3 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

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
     * @param status The new group status
     * @param emitter The corresponding GUI element of this event handler
     */
    public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
    {
        // insert your code here e.g.
        //
        // text.setEditable(state.equals(IGuiElement.NEW));
    }

    /**
     * Eventhandler for hot keys like ENTER.
     * You must implement the interface "HotkeyEventHandler" if you want receive this events.
     */
    public void keyPressed(IClientContext context, KeyEvent e) throws Exception
    {
        SerialCreateImage.createSerial(context);
    }

    public int getKeyMask(IClientContext context)
    {
        return KeyEvent.VK_ENTER;
    }


}

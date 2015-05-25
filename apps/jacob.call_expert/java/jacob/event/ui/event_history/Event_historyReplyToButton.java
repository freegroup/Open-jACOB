/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Apr 21 12:46:59 CEST 2009
 */
package jacob.event.ui.event_history;

import jacob.common.AppLogger;
import jacob.common.dblayer.ContactTypeManager;
import jacob.common.media.IMediaChannelHandler;
import jacob.model.Customer_contact_type;
import jacob.model.Event_history;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Event_historyReplyToButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author andherz
 */
public class Event_historyReplyToButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Event_historyReplyToButton.java,v 1.4 2009/11/23 11:33:46 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

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
        IDataTableRecord eventHistoryRecord = context.getDataTable(Event_history.NAME).getSelectedRecord();
        if(eventHistoryRecord!=null)
        {
            String mediaType = eventHistoryRecord.getStringValue(Event_history.media_type);
            if(mediaType!=null)
            {
                IDataTableRecord contactType = ContactTypeManager.findByType(context, mediaType);

                String mediaHandler = contactType.getStringValue(Customer_contact_type.java_outbound_handler_class);
                if(mediaHandler!=null)
                {
                    Object obj = Class.forName(mediaHandler).newInstance();
                    if(obj instanceof IMediaChannelHandler)
                    {
                        IMediaChannelHandler handler =(IMediaChannelHandler)obj;
                        handler.replyTo(context, eventHistoryRecord);
                    }
                }
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
        emitter.setVisible(false);
        IDataTableRecord eventHistoryRecord = context.getDataTable(emitter.getGroup().getGroupTableAlias()).getSelectedRecord();
        if(eventHistoryRecord!=null)
        {
            String mediaType = eventHistoryRecord.getStringValue(Event_history.media_type);
            String direction = eventHistoryRecord.getStringValue(Event_history.direction);
            emitter.setVisible(mediaType!=null && Event_history.direction_ENUM._in.equals(direction));
        }
    }
}

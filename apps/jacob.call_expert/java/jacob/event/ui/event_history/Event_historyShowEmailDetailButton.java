/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jun 24 12:13:37 CEST 2009
 */
package jacob.event.ui.event_history;

import jacob.common.FormManager;
import jacob.model.Event_history;
import jacob.model.Storage_email;
import jacob.relationset.RequestRelationset;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Event_historyShowEmailDetailButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author achim
 */
public class Event_historyShowEmailDetailButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Event_historyShowEmailDetailButton.java,v 1.2 2009/11/23 11:33:46 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";


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
        IDataTableRecord eventRecord = context.getDataTable(Event_history.NAME).getSelectedRecord();
        FormManager.showEmailDetail(context);

        // transfer the record to the detail form
        //
        context.getDataAccessor().clear();
        context.getDataAccessor().propagateRecord(eventRecord, RequestRelationset.NAME, Filldirection.BOTH);
    }


    @Override
    public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
    {
        IDataTableRecord emailRecord = context.getDataTable(Storage_email.NAME).getSelectedRecord();
        element.setVisible(emailRecord!=null);
    }


}

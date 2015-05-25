/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jul 01 13:08:56 CEST 2009
 */
package jacob.event.ui.request;

import jacob.common.FormManager;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;

/**
 *
 * @author andherz
 */
public class RequestFollowUpListbox extends ITableListBoxEventHandler
{
    static public final transient String RCS_ID = "$Id: RequestFollowUpListbox.java,v 1.2 2009/11/23 11:33:45 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

    /**
     * Called, if the user clicks on one entry n the TableListBox
     *
     * @param context The current work context of the jACOB application.
     * @param emitter The emitter of the event.
     * @param selectedRecord The entry which the user has been selected
     */
    public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
    {
        FormManager.showFollowUp(context, selectedRecord);
    }

    public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
    {
        // your code here
    }
}

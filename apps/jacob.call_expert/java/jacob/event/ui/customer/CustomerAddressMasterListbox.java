/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Feb 04 16:43:19 CET 2009
 */
package jacob.event.ui.customer;

import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CustomerAddress;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;

/**
 *
 * @author achim
 */
public class CustomerAddressMasterListbox extends ITableListBoxEventHandler
{
    static public final transient String RCS_ID = "$Id: CustomerAddressMasterListbox.java,v 1.4 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /**
     * Called, if the user clicks on one entry n the TableListBox
     *
     * @param context The current work context of the jACOB application.
     * @param emitter The emitter of the event.
     * @param selectedRecord The entry which the user has been selected
     */
    public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
    {
        MasterDetailManager.onSelect(MasterDetailManager_CustomerAddress.INSTANCE, context,selectedRecord);
    }

    public boolean beforeAction(IClientContext context, ITableListBox emitter, IDataBrowserRecord record) throws Exception
    {
        return MasterDetailManager.beforeSelect(MasterDetailManager_CustomerAddress.INSTANCE, context,record);
    }


    public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
    {
        MasterDetailManager.onGroupStatusChanged(MasterDetailManager_CustomerAddress.INSTANCE, context,state);
    }
}

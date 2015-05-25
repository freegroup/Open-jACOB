/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 10 13:50:18 CET 2009
 */
package jacob.event.ui.company;

import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
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
public class CompanyContactMasterListbox extends ITableListBoxEventHandler
{
    static public final transient String RCS_ID = "$Id: CompanyContactMasterListbox.java,v 1.5 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.5 $";

    /**
     * Called, if the user clicks on one entry n the TableListBox
     *
     * @param context The current work context of the jACOB application.
     * @param emitter The emitter of the event.
     * @param selectedRecord The entry which the user has been selected
     */
    public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
    {
        MasterDetailManager.onSelect(MasterDetailManager_CompanyContact.INSTANCE, context,selectedRecord);
        System.out.println("Listboxrecs");
        for (int i = 0; i < emitter.getData().recordCount(); i++)
        {
            System.out.println(emitter.getData().getRecord(i));
        }
    }

    public boolean beforeAction(IClientContext context, ITableListBox emitter, IDataBrowserRecord record) throws Exception
    {
        //return MasterDetailManager.beforeSelect(MasterDetailManager_CompanyContact.INSTANCE, context,record);
        return true;
    }


    public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
    {
        MasterDetailManager.onGroupStatusChanged(MasterDetailManager_CompanyContact.INSTANCE, context,state);
    }
}

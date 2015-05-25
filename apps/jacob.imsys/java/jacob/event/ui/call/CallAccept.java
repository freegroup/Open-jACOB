package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Jun 20 15:04:39 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.Call;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CallAccept-Button. <br>
 * The onAction will be calle if the user clicks on this button <br>
 * Insert your custom code in the onAction-method. <br>
 * 
 * @author mike
 * 
 */
public class CallAccept extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: CallAccept.java,v 1.2 2005/06/29 14:19:24 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.2 $";

    // use this logger to write messages and NOT the System.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    private static final Set validStatus = new HashSet();
    static
    {
        validStatus.add("Neu");
        validStatus.add("Durchgestellt");
        validStatus.add("Fehlgeroutet");
        validStatus.add("AK zugewiesen");
    }

    private class OkCancelDialogCallback implements IOkCancelDialogCallback
    {
        class GridCallback implements IGridTableDialogCallback
          {
            /* (non-Javadoc)
             * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext, int, java.util.Properties)
             */
            /* 
             * @see de.tif.jacob.screen.dialogs.IGridTableDialogCallback#onSelect(de.tif.jacob.screen.IClientContext, int, java.util.Properties)
             */
            public void onSelect(IClientContext context, int rowIndex, Properties columns) throws Exception
            {
                IDataTableRecord call = context.getSelectedRecord();
                IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
                try
                {
                    call.setValue(currentTransaction, "callstatus", "Angenommen");
                    DataUtils.linkTable(context, currentTransaction, call, "workgroupcall", "callworkgroup", "pkey", columns.getProperty("ID"));
                    currentTransaction.commit();
                }
                finally
                {
                    currentTransaction.close();
                }      
            }
          }
        public void onOk(IClientContext context) throws Exception
        {
            // wenn OK, dann den Owner suchen und Status setzen
            IDataAccessor newAccessor = context.getDataAccessor().newAccessor();
            IDataTable groupmember = newAccessor.getTable("groupmember");
            IDataTable owner = newAccessor.getTable("workgroup");
            owner.qbeSetValue("wrkgrptype", "OWNER");
            groupmember.qbeSetValue("accessallowed", "lesen/schreiben");
            groupmember.qbeSetKeyValue("employeegroup", context.getUser().getKey());
            groupmember.search("r_wkgrpemp");
            if (groupmember.recordCount() == 0)
            {
                throw new BusinessException("Sie sind nicht als Mitarbeiter Typ 'OWNER' registriert.\n Wenden Sie sich an einen Administrator.");
            }
            if (groupmember.recordCount() == 1)
            {
                IDataTableRecord call = context.getSelectedRecord();
                IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
                try
                {
                    call.setValue(currentTransaction, "callstatus", "Angenommen");
                    DataUtils.linkTable(context, currentTransaction, call, "workgroupcall", "callworkgroup", "pkey", groupmember.getRecord(0).getSaveStringValue("workgroupgroup"));
                    currentTransaction.commit();
                }
                finally
                {
                    currentTransaction.close();
                }
            }
            else
            {
                // Es gibt mehrere mögliche Owner
               IGuiElement button = context.getGroup().findByName("callRouting"); 
               IGridTableDialog dialog = context.createGridTableDialog(button, new GridCallback());

               // create the header for the selection grid dialog
               //
               String[] header = new String[]{"ID" , "Bearbeiter","Beschreibung"};
               dialog.setHeader(header);
        
               String[][] daten=new String[groupmember.recordCount()][3];
               for (int j = 0; j < groupmember.recordCount(); j++)
           {
                 IDataTableRecord ownerRec = groupmember.getRecord(j).getLinkedRecord("workgroup" );
                 daten[j]= new String[]{ ownerRec.getSaveStringValue("pkey"),ownerRec.getSaveStringValue("name"),ownerRec.getSaveStringValue("description")};
           }
                 dialog.setData(daten);
                 dialog.show(300,100);
            }

        }

        public void onCancel(IClientContext context) throws Exception
        {
        }

        {
        }

    }

    /**
     * The user has been click on the corresponding button.
     * 
     * @param context
     *            The current client context
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onAction(IClientContext context, IGuiElement button) throws Exception
    {
        IDataTableRecord callrecord = context.getSelectedRecord();
        if (!Call.accessallowed(callrecord))
        {
            IMessageDialog dialog = context.createMessageDialog("Sie haben keinen schreibenden Zugriff auf diese Meldung");
            dialog.show();
            return;
        }

        // fragen ob angenommen werden soll
        IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Wollen Sie die Meldung wirklich annehmen?", new OkCancelDialogCallback());
        okCancelDialog.show();

    }

    /**
     * The status of the parent group (TableAlias) has been changed. <br>
     * <br>
     * This is a good place to enable/disable the button on relation to the
     * group state or the selected record. <br>
     * <br>
     * Possible values for the state is defined in IGuiElement <br>
     * <ul>
     * <li>IGuiElement.UPDATE</li>
     * <li>IGuiElement.NEW</li>
     * <li>IGuiElement.SEARCH</li>
     * <li>IGuiElement.SELECTED</li>
     * </ul>
     * 
     * @param context
     *            The current client context
     * @param status
     *            The new group state. The group is the parent of the
     *            corresponding event button.
     * @param button
     *            The corresponding button to this event handler
     * @throws Exception
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
    {
        if (status == IGuiElement.SELECTED)
        {
            IDataTableRecord currentRecord = context.getSelectedRecord();
            String callstatus = currentRecord.getStringValue("callstatus");

            button.setEnable(validStatus.contains(callstatus));
        }
    }
}

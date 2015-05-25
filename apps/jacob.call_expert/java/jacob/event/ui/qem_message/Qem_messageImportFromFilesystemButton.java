/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Apr 22 14:49:50 CEST 2009
 */
package jacob.event.ui.qem_message;

import jacob.common.AppLogger;
import jacob.model.Qem_message;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.file.Directory;


/**
 * The event handler for the Qem_messageImportFromFilesystemButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author andherz
 */
public class Qem_messageImportFromFilesystemButton extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: Qem_messageImportFromFilesystemButton.java,v 1.2 2009/11/23 11:33:46 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

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
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTransaction trans = acc.newTransaction();
        try
        {
            IDataTable qemMessageTable = acc.getTable(Qem_message.NAME);
            qemMessageTable.fastDelete(trans);
            trans.commit();
        }
        finally
        {
            trans.close();
        }
        context.createAskDialog("Directory to import", new IAskDialogCallback() {

            public void onOk(IClientContext context, String value) throws Exception
            {
                IDataAccessor acc = context.getDataAccessor().newAccessor();
                List<File> files = Directory.getAll(new File(value), false);
                int counter =0;
                for (File file : files)
                {
                    IDataTransaction trans = acc.newTransaction();
                    try
                    {
                        if(!file.getName().endsWith(".msg"))
                            continue;
                        String baseName = FilenameUtils.getBaseName(file.getName());
                        IDataTableRecord msgRecord = Qem_message.newRecord(acc, trans);
                        msgRecord.setValue(trans, Qem_message.pkey, baseName);
                        msgRecord.setValue(trans, Qem_message.messagebody, FileUtils.readFileToString(file));
                        trans.commit();
                        System.out.println(counter++ +"/"+files.size()+" " +file.getName()+ " ("+baseName+")");
                    }
                    finally
                    {
                        trans.close();
                    }
                }

            }

            public void onCancel(IClientContext context) throws Exception
            {
            }
        }).show();
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
        // You can enable/disable the button in relation to your conditions.
        //
        //emitter.setEnable(true/false);
    }
}

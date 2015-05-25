package jacob.event.ui.employee;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Nov 14 15:13:50 CET 2005
 *
 */
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;



 /**
  * The Event handler for the EmployeeSetDefaultPassword-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class EmployeeSetDefaultPassword extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: EmployeeSetDefaultPassword.java,v 1.1 2005/11/17 17:35:15 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
     *      de.tif.jacob.screen.IGuiElement)
     */
    public void onAction(IClientContext context, IGuiElement menu) throws Exception
    {
        // fragen ob angenommen werden soll
        IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Wollen Sie das Passwort wirklich zurücksetzen?", new IOkCancelDialogCallback()
        {
            public void onOk(IClientContext context) throws Exception
            {
                // wenn OK, dann den Password zurücksetzen
                IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
                try
                {
                    context.getSelectedRecord().setValue(currentTransaction, "passwdhash", null);
                    currentTransaction.commit();
                }
                finally
                {
                    currentTransaction.close();
                }
            }

            public void onCancel(IClientContext context) throws Exception
            {
            }
        });

        okCancelDialog.show();

    }

    /*
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
     *      int, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement menuItem) throws Exception
    {
        // Nur administratoren dürfen das Passwort zurücksetzen
        menuItem.setEnable(status == IGuiElement.SELECTED && context.getUser().hasRole("cq_admin"));
    }
}

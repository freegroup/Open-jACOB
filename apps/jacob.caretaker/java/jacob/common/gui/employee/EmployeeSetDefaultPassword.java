/*
 * Created on 10.02.2005
 *
 *
 */
package jacob.common.gui.employee;

import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author mike
 * 
 * Setzt das Password des Employee Datensatzes auf null
 */
public class EmployeeSetDefaultPassword extends IButtonEventHandler
{
    static public final transient String RCS_ID = "$Id: EmployeeSetDefaultPassword.java,v 1.3 2005/03/03 15:38:15 sonntag Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

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

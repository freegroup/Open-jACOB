package jacob.event.ui.call;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Sep 19 13:10:52 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.ui.CustomerCallbackyPhone;

import org.apache.commons.logging.Log;

/**
 * The Event handler for the CallCallCustomer-Button.<br>
 * The onAction will be calle if the user clicks on this button<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author mike
 * 
 */
public class CallCallCustomer extends CustomerCallbackyPhone
{

    /*
     * (non-Javadoc)
     * 
     * @see jacob.common.ui.CustomerCallbackyPhone#getPhoneNumber(de.tif.jacob.screen.IClientContext)
     */
    public String getPhoneNumber(IClientContext context) throws Exception
    {

        return context.getSelectedRecord().getLinkedRecord("customer").getSaveStringValue("phone");

    }

    /*
     * (non-Javadoc)
     * 
     * @see jacob.common.ui.CustomerCallbackyPhone#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
     *      de.tif.jacob.screen.IGuiElement.GroupState,
     *      de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
    {
        if (status == IGuiElement.SELECTED)
        {
            IDataTableRecord call = context.getSelectedRecord();

            if (call.hasLinkedRecord("customer"))
            {
                button.setEnable(call.getLinkedRecord("customer").getSaveStringValue("phone").length() > 1);
            }
            else
            {
                button.setEnable(false);
            }
        }
    }

}
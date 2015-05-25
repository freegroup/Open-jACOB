package jacob.event.ui.caller;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Sep 19 13:29:00 CEST 2005
 *
 */
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
  * The Event handler for the CallCustomerPhone-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallCustomerPhone extends CustomerCallbackyPhone
{

    /* (non-Javadoc)
     * @see jacob.common.ui.CustomerCallbackyPhone#getPhoneNumber(de.tif.jacob.screen.IClientContext)
     */
    public String getPhoneNumber(IClientContext context) throws Exception
    {
         return context.getSelectedRecord().getSaveStringValue("phone");
        
    }
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
    {
        if (status == IGuiElement.SELECTED)
        {
            IDataTableRecord customer = context.getSelectedRecord();

                button.setEnable(customer.getSaveStringValue("phone").length() > 1);
  
        }
    }
}
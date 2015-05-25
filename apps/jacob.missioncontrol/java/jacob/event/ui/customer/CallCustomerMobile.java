package jacob.event.ui.customer;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Sep 23 14:14:23 CEST 2005
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
  * The Event handler for the CallCustomerMobile-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallCustomerMobile  extends CustomerCallbackyPhone
{

    /* (non-Javadoc)
     * @see jacob.common.ui.CustomerCallbackyPhone#getPhoneNumber(de.tif.jacob.screen.IClientContext)
     */
    public String getPhoneNumber(IClientContext context) throws Exception
    {
         return context.getSelectedRecord().getSaveStringValue("mobile");
        
    }
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
    {
        if (status == IGuiElement.SELECTED)
        {
            IDataTableRecord customer = context.getSelectedRecord();

                button.setEnable(customer.getSaveStringValue("mobile").length() > 1);
  
        }
    }
}
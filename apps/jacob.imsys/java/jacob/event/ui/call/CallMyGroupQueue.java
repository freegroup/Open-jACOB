package jacob.event.ui.call;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Fri Jun 03 15:53:51 CEST 2005
 *
 */
import jacob.common.Call;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * The Event handler for the CallMyGroupQueue-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CallMyGroupQueue extends IButtonEventHandler 
{
    static public final transient String RCS_ID = "$Id: CallMyGroupQueue.java,v 1.1 2005/06/03 15:18:53 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.1 $";
    public void onAction(IClientContext context, IGuiElement button)throws Exception
    {
            Call.findByUserAndState(context, context.getUser(), "Durchgestellt|AK zugewiesen|Fehlgeroutet|Angenommen", "r_queues");
    }
  
    /**
     * Enable disable the button when state of the group has been changed
     */
    public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
    {
    }
}
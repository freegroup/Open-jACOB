/*
 * Created on 14.09.2005 by mike
 * 
 *
 */
package jacob.event.screen.f_call_entryak_fc.callTemplate;

import java.util.Vector;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

public class CalltemplateButtonUpdateRecord extends IActionButtonEventHandler
{
    static private final Vector accessRoles = new Vector();
    
    static
    {
        //accessRoles.add("CQ_PM");
        accessRoles.add("CQ_ADMIN");
        //accessRoles.add("CQ_AGENT");
        //accessRoles.add("CQ_SUPERAK");
        //accessRoles.add("CQ_SDADMIN");
    }
    public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
    {
        return true;
    }

    public void onSuccess(IClientContext context, IGuiElement button) throws Exception
    {

    }

    /* (non-Javadoc)
     * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
     */
    public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
    {
        
        if (!context.getUser().hasOneRoleOf(accessRoles))
        {
            emitter.setEnable(false);
        }
    }

}

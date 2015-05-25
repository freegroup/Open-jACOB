/*
 * Created on Jun 30, 2004
 *
 */
package jacob.event.screen.f_custinfo.customercall;

import java.util.Properties;

import de.tif.jacob.entrypoint.EntryPointUrl;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 *
 */
public class CustomerintGotoCustinfo extends IButtonEventHandler
{

  /* 
   * @see de.tif.etr.screen.event.IButtonEventHandler#onAction(de.tif.etr.screen.IClientContext, de.tif.etr.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement emitter) throws Exception
  {
    Properties props=new Properties();
    EntryPointUrl url=new EntryPointUrl(EntryPointUrl.ENTRYPOINT_GUI,"ShowMyCalls",props);
    url.show(context);
  }

  /* 
   * @see de.tif.etr.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.etr.screen.IClientContext, int, de.tif.etr.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context,   IGuiElement.GroupState status,  IGuiElement emitter)   throws Exception
  {
  }

}

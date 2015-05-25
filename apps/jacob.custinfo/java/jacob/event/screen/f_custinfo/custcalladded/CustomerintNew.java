/*
 * Created on May 11, 2004
 *
 */
package jacob.event.screen.f_custinfo.custcalladded;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 *
 */
public class CustomerintNew extends IButtonEventHandler
{

  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // Reset the generic field. A generic Field is not triggerd from the engine.
    //
    IForm                 nextForm = (IForm)context.getDomain().findByName("customercall");
    ISingleDataGuiElement element  =(ISingleDataGuiElement)nextForm.findByName("customerintCalltext");
    element.setValue("");
    
    context.setCurrentForm("customercall");
  }

  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(  IClientContext context,  IGuiElement.GroupState status,  IGuiElement element)   throws Exception
  {
  }

}

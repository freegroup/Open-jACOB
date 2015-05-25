package jacob.common.recyclebin;

import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

public class DeleteRecordButton extends IActionButtonEventHandler
{

  /**
   * This event handler will be called, if the corresponding button has been pressed.
   * You can prevent the execution of the DELETE action if you return <code>false</code>.<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
  {
    int index = context.getDataBrowser().getSelectedRecordIndex();
    context.setPropertyForWindow("DELETE_INDEX", index);
    return true;
  }

  /**
   * This event method will be called, if the DELETE action has been successfully executed.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button) 
  {
    try
    {
      
      if(context.getDataBrowser().recordCount()==0)
        return;
      
      int index = (Integer)context.getProperty("DELETE_INDEX");
      
      if(index>0)
        context.getGUIBrowser().setSelectedRecordIndex(context, index-1);
      else
        context.getGUIBrowser().setSelectedRecordIndex(context, 0);
      
    }
    catch(Exception exc)
    {
      ExceptionHandler.handle(context,exc);
    }
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
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setLabel("Endgültig Löschen");
  }
}

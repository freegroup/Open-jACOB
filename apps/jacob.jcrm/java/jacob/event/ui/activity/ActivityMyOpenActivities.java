package jacob.event.ui.activity;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 06 14:47:15 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the ActivityMyOpenActivities-Button.<br>
 * The onAction will be called, if the user clicks on this button.<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author andreas
 *
 */
public class ActivityMyOpenActivities extends IButtonEventHandler
{
  /**
   * Search all activities in status Not Started|In Progress|Deferred an loggedin User is the owner
   *
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    context.clearDomain();

    IDataTable table = context.getDataTable();
    IDataBrowser browser = context.getDataBrowser();

    table.qbeSetValue("status", "Not Started|In Progress|Deferred");
    table.qbeSetKeyValue("owner_key", context.getUser().getKey());

    // do the search itself
    // 
    browser.search("r_sales");

    // display the result set
    //
    context.getGUIBrowser().setData(context, browser);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

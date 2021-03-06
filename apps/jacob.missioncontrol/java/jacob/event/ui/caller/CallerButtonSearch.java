package jacob.event.ui.caller;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Tue Aug 30 12:47:37 CEST 2005
 *
 */
import jacob.common.AppLogger;
import jacob.common.CDSync;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * This is an event handler for a update button.
 * 
 * @author andreas
 *  
 */
public class CallerButtonSearch extends ISearchActionEventHandler
{
  static public final transient String RCS_ID = "$Id: CallerButtonSearch.java,v 1.2 2005/09/15 20:01:44 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * This event handler will be called if the corresponding button has been
   * pressed. You can prevent the execution of the SEARCH action if you return
   * [false]. <br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @return Return 'false' if you want to avoid the execution of the action
   *         else return [true]
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    String firstname = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerFirstname")).getValue());
    String lastname = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerLastname")).getValue());
    String phone = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerPhone")).getValue());
    String mobile = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerMobile")).getValue());
    String plant = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerPlant")).getValue());
    String plant2 = "";
    String email = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerEmail")).getValue());
    String cuid = StringUtil.toSaveString(((ISingleDataGuiElement) context.getGroup().findByName("callerUid")).getValue());
    String department = "";
    IDataTableRecord orgRec = context.getDataAccessor().getTable("caller_org").getSelectedRecord();

    CDSync.sync(context, firstname, lastname, department, phone, mobile, plant, plant2, email, cuid, orgRec);
    
    return true;
  }

  /**
   * This event method will be called if the search action has been successfully
   * done. <br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button)
  {
  }

  /**
   * The event handle if the status of the group has been changed. This is a
   * good place to enable/disable the button on relation to the group state.
   * <br>
   * <br>
   * Possible values for the state is defined in IGuiElement <br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresbonding button to this event handler
   *  
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
  {
    // you can enable/disable the update button
    //
    //button.setEnable(true/false);
  }
}

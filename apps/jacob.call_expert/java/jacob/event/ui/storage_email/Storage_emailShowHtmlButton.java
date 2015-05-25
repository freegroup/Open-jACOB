/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jun 02 17:08:00 CEST 2009
 */
package jacob.event.ui.storage_email;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

/**
 * The event handler for the Storage_emailShowHtmlButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author achim
 */
public class Storage_emailShowHtmlButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Storage_emailShowHtmlButton.java,v 1.1 2009/06/02 15:55:04 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    if (StringUtils.isNotEmpty(context.getGroup().findByName("storage_emailStyledText").getLabel()))
    {
      ITabContainer emailContainer = (ITabContainer) context.getGroup().findByName("storage_emailContainer");
      emailContainer.setActivePane(context, 1);
    }

  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setVisible(false);
    if (status.equals(IGroup.SELECTED))
    {
      button.setVisible(StringUtils.isNotEmpty(context.getGroup().findByName("storage_emailStyledText").getLabel()));
    }

  }
}

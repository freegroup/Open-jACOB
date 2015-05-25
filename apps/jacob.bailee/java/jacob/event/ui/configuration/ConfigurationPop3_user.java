/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jan 11 11:34:43 CET 2010
 */
package jacob.event.ui.configuration;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author andherz
 */
 public class ConfigurationPop3_user extends ITextFieldEventHandler // implements IAutosuggestProvider, IHotkeyEventHandler
 {
	static public final transient String RCS_ID = "$Id: ConfigurationPop3_user.java,v 1.1 2010/01/12 09:48:31 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
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
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
    if(context.isDemoMode() && context.getSelectedRecord()!=null)
      text.setValue(":-)");
	}
  
  /**
   * Eventhandler for hot keys like ENTER.
   * You must implement the interface "HotkeyEventHandler" if you want receive this events.
   * 
  public void keyPressed(IClientContext context, KeyEvent e)
  {
    System.out.println("pressed");
  }
  
  public int getKeyMask(IClientContext context)
  {
    return KeyEvent.VK_ENTER;
  }
  */
  
}

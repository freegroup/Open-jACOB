/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Nov 13 18:44:40 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author achim
 */
 public class CustomerText extends ITextFieldEventHandler // implements IAutosuggestProvider, HotkeyEventHandler
 {
	static public final transient String RCS_ID = "$Id: CustomerText.java,v 1.1 2009/02/17 15:12:12 A.Boeken Exp $";
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
		// insert your code here e.g.
    //
    // text.setEditable(state.equals(IGuiElement.NEW));
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

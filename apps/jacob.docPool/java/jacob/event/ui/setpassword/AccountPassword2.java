/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 14:49:01 CEST 2010
 */
package jacob.event.ui.setpassword;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IPassword;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class AccountPassword2 extends IPasswordFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AccountPassword2.java,v 1.2 2010-07-16 14:26:15 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
	 * @param passwordField The corresponding GUI element of this event handler
	 * 
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IPassword passwordField) throws Exception
	{
		passwordField.setEditable(true);
	}
}

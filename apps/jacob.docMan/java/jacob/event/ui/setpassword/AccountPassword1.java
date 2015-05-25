/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 14:48:26 CEST 2010
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
 public class AccountPassword1 extends IPasswordFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AccountPassword1.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
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
	 * @param passwordField The corresponding GUI element of this event handler
	 * 
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IPassword passwordField) throws Exception
	{
		passwordField.setEditable(true);
	}
}

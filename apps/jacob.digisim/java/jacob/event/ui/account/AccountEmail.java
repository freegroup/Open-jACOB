/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 02 17:05:23 CET 2007
 */
package jacob.event.ui.account;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class AccountEmail extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AccountEmail.java,v 1.1 2007/02/02 22:26:47 freegroup Exp $";
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
	
  public void onGroupStatusChanged(IClientContext context, GroupState status, IText emitter) throws Exception
	{
		// Ist in der Datenbank nciht als required markiert, da Admin und Guest keine eMailAddresse
		// Haben. Wird jedoch ein neuer User angelegt, dann wird dies vorausgesetzt
		//
		if(status == IGuiElement.UPDATE || status == IGuiElement.NEW)
			emitter.setRequired(true);
	}


}

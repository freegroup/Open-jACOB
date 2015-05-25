/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed May 10 22:55:20 CEST 2006
 */
package jacob.event.ui.user_application;

import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author mike
 */
 public class User_applicationDisplayname extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: User_applicationDisplayname.java,v 1.1 2006-05-12 10:04:52 mike Exp $";
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
	 * @param emitter The corresponding GUI element of this event handler
	 * 
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		if (emitter.getDataStatus() == IGuiElement.NEW ||emitter.getDataStatus() == IGuiElement.UPDATE )
    {
      emitter.setBackgroundColor(Color.RED);
    }
    else
    {
      emitter.setBackgroundColor(null);
    }
	}
}

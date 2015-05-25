/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Jan 20 14:29:53 CET 2009
 */
package jacob.event.ui.project;

import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author R.Spoor
 */
 public class ProjectFilterAvailableAdminGroupsText extends ITextFieldEventHandler implements IHotkeyEventHandler
 {
	static public final transient String RCS_ID = "$Id: ProjectFilterAvailableAdminGroupsText.java,v 1.2 2009/02/18 15:28:55 R.Spoor Exp $";
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
	 * @param state   The new group status
	 * @param text    The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
        text.setEnable(state == IGuiElement.UPDATE || state == IGuiElement.NEW);
        if (!text.isEnabled())
        {
            text.clear(context);
        }
	}

    public int getKeyMask(IClientContext context)
    {
        return KeyEvent.VK_ENTER;
    }

    public void keyPressed(IClientContext context, KeyEvent event)
           throws Exception
    {
        if (event.getKeyCode() == KeyEvent.VK_ENTER)
        {
            ProjectFilterAvailableAdminGroupsButton.filterAvailableGroups(context);
        }
    }
}

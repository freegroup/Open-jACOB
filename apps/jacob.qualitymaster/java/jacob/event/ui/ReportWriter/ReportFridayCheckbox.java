package jacob.event.ui.ReportWriter;

import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class ReportFridayCheckbox extends ICheckBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: ReportFridayCheckbox.java,v 1.1 2009-12-24 10:02:21 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler method will be called, if the user sets a mark at a
	 * checkbox.
	 * 
	 * @param checkBox The checkbox itself
	 * @param context The current context of the application
	 *
	 */
	public void onCheck(IClientContext context, ICheckBox checkBox) throws Exception
	{
	}
  
	/**
	 * This event handler method will be called, if the user unchecks a
	 * checkbox.
	 * 
	 * @param checkBox The checkbox itself
	 * @param context The current context of the application
	 */
	public void onUncheck(IClientContext context, ICheckBox checkBox) throws Exception
	{
	}
  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    element.setEnable(state == IGroup.UPDATE);
  }
	
}

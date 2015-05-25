package jacob.event.ui.request_enabler;

import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.GuiEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;
import de.tif.jacob.screen.impl.GuiElement;

/**
 * @author andherz
 *
 */
public class Request_enablerAny_flag3 extends ICheckBoxEventHandler
{
  static public  final transient String RCS_ID = "$Id: Request_enablerAny_flag3.java,v 1.1 2007/11/25 22:19:38 freegroup Exp $";
  static public  final transient String RCS_REV = "$Revision: 1.1 $";
  
  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	public void onGroupStatusChanged(IClientContext context, GroupState state,	IGuiElement emitter) throws Exception 
	{
		if(state== GuiElement.UPDATE || state== GuiElement.NEW)
			emitter.setEnable(context.getSelectedRecord().getSaveStringValue("state").equals("progress"));
	}

	/**
   * This event handler method will be called if he user set a mark at a
   * HTML-CheckBox.
   * 
   * @param checkBox The CheckBox itself
   * @param context The current context of the application
   *
   */
  public void onCheck(IClientContext context, ICheckBox checkBox)throws Exception
  {
  }
  
  /**
   * This event handler method will be called if the user uncheck an
   * HTML CheckBox.
   * 
   * @param checkBox The CheckBox itself
   * @param context The current context of the application
   * 
   */
  public void onUncheck(IClientContext context, ICheckBox checkBox)throws Exception
  {
  }
}

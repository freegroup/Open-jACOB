package jacob.event.ui.ReportWriter;

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
 public class ReportSearchNameText extends ITextFieldEventHandler // implements IAutosuggestProvider, IHotkeyEventHandler
 {
	static public final transient String RCS_ID = "$Id: ReportSearchNameText.java,v 1.1 2009-12-24 10:02:22 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
    text.setEditable(true);
	}

/*
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

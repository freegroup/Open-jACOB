/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on {date}
 */
package {package};

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IListBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IListBoxEventHandler;

/**
 *
 * @author {author}
 */
public class {class} extends IListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: IListBoxEventHandler.java,v 1.1 2007/05/18 16:13:26 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state 
	 * of the related table record.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param emitter The emitter of the event.
	 */
	public void onSelect(IClientContext context, IListBox emitter) throws Exception
	{
	}

	/**
	 * The event handler if the group status has been changed.<br>
	 * This is a good place to enable/disable some combo box entries in relation to the state of the
	 * selected record.<br>
	 * <br>
	 * Note: You can only enable/disable <b>valid</b> enum values of the corresponding table field.<br>
	 * <br>
	 * <code>
	 *	 IListBox listBox =(IListBox)emitter;
	 *	 // remove all list box entries
	 *	 listBox.enableOptions(false);
	 *   if(..some condition...)
	 *   {  
	 *     listBox.enableOptions("Duplicate",true);
	 *     listBox.enableOptions("Declined",true);
	 *   }
	 *   else if(...another condition...)
	 *   {  
	 *     listBox.enableOptions("Proved",true);
	 *     listBox.enableOptions("In progress",true);
	 *     listBox.enableOptions("QA",true);
	 *   }
	 *   else // enable all options
	 *   	listBox.enableOptions(true);
	 * 
	 * </code>
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param status  The new state of the group.
	 * @param listBox The emitter of the event.
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IListBox listBox) throws Exception
	{
    // your code here
	}
}

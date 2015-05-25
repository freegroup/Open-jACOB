/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 16:41:34 CEST 2010
 */
package jacob.event.ui.recyclebin;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class RecyclebinContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: RecyclebinContainer.java,v 1.2 2010-07-16 14:26:14 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

   /*
    * This event method will be called, if the status of the corresponding group
    * has been changed. Derived event handlers could overwrite this method, e.g.
    * to enable/disable GUI elements in relation to the group state. <br>
    * Possible group state values are defined in
    * {@link IGuiElement}:<br>
    * <ul>
    *     <li>{@link IGuiElement#UPDATE}</li>
    *     <li>{@link IGuiElement#NEW}</li>
    *     <li>{@link IGuiElement#SEARCH}</li>
    *     <li>{@link IGuiElement#SELECTED}</li>
    * </ul>
    * 
    * @param context
    *          The current client context
    * @param state
    *          The new group state
    * @param element
    *          The corresponding GUI element to this event handler
    */
    public void onGroupStatusChanged(IClientContext context, GroupState state, ITabContainer element) throws Exception
    {
      element.hideTabStrip(true);
      if(state==IGroup.SELECTED)
      {
        if(context.getSelectedRecord().getStringValue(Recyclebin.type).equals(Recyclebin.type_ENUM._folder))
          element.setActivePane(context,1);
        else
          element.setActivePane(context,2);
      }
      else
      {
        element.setActivePane(context,0);
      }
    }
}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 05 12:28:57 CEST 2010
 */
package jacob.event.ui.menutree;

import java.util.List;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.common.MenutreeUtil;
import jacob.common.NewsUtil;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class MenutreeContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: MenutreeContainer.java,v 1.1 2010-08-06 16:00:27 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
      
      element.setActivePane(context,0);
      
      if(state == IGroup.SELECTED)
        element.setActivePane(context,1);
      else if(state == IGroup.UPDATE)
        element.setActivePane(context,2);
      else if(state == IGroup.NEW)
        element.setActivePane(context,3);

      // Wichtig, damit beim Update nur das sichtbare Pane seine Daten in die 
      // Datenschicht "pustet".
      List<IPane> panes = element.getPanes();
      for (IPane pane : panes)
      {
         pane.setEnable(false); 
      }
      element.getActivePane().setEnable(true);
    }
}

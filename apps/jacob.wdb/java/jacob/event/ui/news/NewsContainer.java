/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 29 17:07:02 CEST 2010
 */
package jacob.event.ui.news;

import java.util.List;

import jacob.common.AppLogger;
import jacob.common.NewsUtil;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IPane;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class NewsContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: NewsContainer.java,v 1.2 2010-08-05 08:21:48 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
      
      if(state == IGroup.NEW)
        element.setActivePane(context,4);
      else if(NewsUtil.count(context)==0)
        element.setActivePane(context,0);
      else if(state == IGroup.SEARCH)
        element.setActivePane(context,1);
      else if(state == IGroup.SELECTED)
        element.setActivePane(context,2);
      else if(state == IGroup.UPDATE)
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

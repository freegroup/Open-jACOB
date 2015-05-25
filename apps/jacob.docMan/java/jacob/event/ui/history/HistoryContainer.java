/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 13 12:37:24 CEST 2010
 */
package jacob.event.ui.history;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.History;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class HistoryContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: HistoryContainer.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


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
      
      IDataTableRecord record = context.getSelectedRecord();
      if(record!=null)
      {
        String alias = record.getSaveStringValue(History.related_alias);
        if(alias.equals(Folder.NAME))
          element.setActivePane(context, 1);
        else if(alias.equals(Document.NAME))
          element.setActivePane(context, 2);
      }
    }
}

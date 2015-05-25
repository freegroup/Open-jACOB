/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 02 15:17:18 CEST 2010
 */
package jacob.event.ui.bo.documents;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.model.Document;
import jacob.model.Folder;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITabContainerEventHandler;


/**
 *
 * @author andherz
 */
public class BoContainer extends ITabContainerEventHandler
{
	static public final transient String RCS_ID = "$Id: BoContainer.java,v 1.2 2010-07-16 14:26:15 herz Exp $";
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
      IDataTableRecord folderRecord = context.getDataTable(Folder.NAME).getSelectedRecord();
      IDataTableRecord documentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
      element.hideTabStrip(true);
      element.setActivePane(context, 0);
      if(folderRecord!=null)
        element.setActivePane(context, 1);
      else if(documentRecord!=null)
        element.setActivePane(context,2);
    }
}

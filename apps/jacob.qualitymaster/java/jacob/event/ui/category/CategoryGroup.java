/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 17 11:43:11 CET 2006
 */
package jacob.event.ui.category;

import jacob.model.Category;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;


/**
 *
 * @author andreas
 */
 public class CategoryGroup extends IGroupListenerEventHandler 
 {
	static public final transient String RCS_ID = "$Id: CategoryGroup.java,v 1.1 2006/02/24 02:16:16 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
   * @param status  The new status of the group.
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
    IGroup group = context.getGroup();
    IGuiElement categoryOrgcategoryIFBrowser =group.findByName("categoryOrgcategoryIFBrowser");
    
    // TODO: Hack, remove if deployed on Engine with fixed problem
    if (categoryOrgcategoryIFBrowser == null)
      categoryOrgcategoryIFBrowser =group.findByName("orgcategoryIFBrowser");
    
    if (group.getDataStatus() == IGuiElement.NEW || group.getDataStatus() == IGuiElement.UPDATE)
    {
      // disable IF browser if global flag is selected 
      categoryOrgcategoryIFBrowser.setEnable(context.getSelectedRecord().getintValue(Category.global)==0);
    }
    else
    {
      categoryOrgcategoryIFBrowser.setEnable(group.getDataStatus() == IGuiElement.SEARCH);
    }
	}
}

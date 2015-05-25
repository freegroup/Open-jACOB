/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jan 20 14:37:16 CET 2009
 */
package jacob.event.ui.project;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Projectpowerusergroup;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.ListBoxUtil;


/**
 * The event handler for the ProjectFilterSelectedPowerUserGroupsButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * 
 * @author R.Spoor
 */
public class ProjectFilterSelectedPowerUserGroupsButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProjectFilterSelectedPowerUserGroupsButton.java,v 1.1 2009/02/17 15:23:29 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

    static void filterSelectedGroups(IClientContext context) throws Exception
    {
        IGroup group = context.getGroup();
        String filter = group.getInputFieldValue("projectFilterSelectedPowerUserGroupsText");
        IMutableListBox selected = (IMutableListBox)group.findByName("projectSelectedPowerUserGroupsListbox");
        Map<String,IDataTableRecord> contents = ListBoxUtil.getContents(context, selected);
        if (contents == null)
        {
            return;
        }
        selected.setOptions(new String[0]);
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        IDataTable table = accessor.getTable(Projectpowerusergroup.NAME);
        table.qbeClear();
        table.qbeSetValue(Projectpowerusergroup.name, filter);
        int count = table.search();
        Set<String> groups = new TreeSet<String>();
        for (int i = 0; i < count; i++)
        {
            IDataTableRecord record = table.getRecord(i);
            String groupname = record.getStringValue(Projectpowerusergroup.name);
            if (contents.containsKey(groupname))
            {
                groups.add(groupname);
            }
        }
        String[] entries = new String[groups.size()];
        selected.setOptions(groups.toArray(entries));
    }

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param emitter The corresponding button to this event handler
	 */
	@Override
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
        filterSelectedGroups(context);
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param emitter The corresponding button to this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
        emitter.setEnable(status == IGuiElement.UPDATE || status == IGuiElement.NEW);
	}
}

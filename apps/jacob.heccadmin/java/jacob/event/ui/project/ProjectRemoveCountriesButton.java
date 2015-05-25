/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Nov 27 12:05:45 CET 2008
 */
package jacob.event.ui.project;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Project;
import jacob.model.Projectcountry;
import jacob.model.Projectcountry_MN;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.ListBoxUtil;


/**
 * The event handler for the ProjectRemoveCountriesButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * 
 * @author R.Spoor
 */
public class ProjectRemoveCountriesButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProjectRemoveCountriesButton.java,v 1.1 2009/02/17 15:23:32 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
        IGroup group = context.getGroup();
        IDataTableRecord currentRecord = context.getSelectedRecord();
        if (currentRecord == null)
        {
        	return;
        }
        IMutableListBox selected = (IMutableListBox)group.findByName("projectSelectedCountriesListbox");
        IMutableListBox available = (IMutableListBox)group.findByName("projectAvailableCountriesListbox");
        ListBoxUtil.removeSelection(
            context, selected, available, currentRecord, Project.pkey,
            Projectcountry.pkey, Projectcountry_MN.NAME,
            Projectcountry_MN.project_key, Projectcountry_MN.country_key
        );
        IMutableComboBox selectedCombo = (IMutableComboBox)group.findByName("projectFilterSelectedCountriesCombobox");
        IMutableComboBox availableCombo = (IMutableComboBox)group.findByName("projectFilterAvailableCountriesCombobox");
        ProjectFilterSelectedCountriesCombobox.filterSelectedCountries(context, selectedCombo);
        ProjectFilterAvailableCountriesCombobox.filterAvailableCountries(context, availableCombo);
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

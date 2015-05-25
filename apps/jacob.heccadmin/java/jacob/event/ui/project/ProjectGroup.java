/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Nov 27 11:58:55 CET 2008
 */
package jacob.event.ui.project;

import java.util.Map;
import java.util.TreeMap;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;
import de.tif.jacob.security.IUser;
import jacob.common.AppLogger;
import jacob.model.Countrygroup;
import jacob.model.Project;
import jacob.model.Projectadmingroup;
import jacob.model.Projectadmingroup_MN;
import jacob.model.Projecthdadmin;
import jacob.model.Projecthdadmin_MN;
import jacob.model.Projectcountry;
import jacob.model.Projectcountry_MN;
import jacob.model.Projectlanguage;
import jacob.model.Projectlanguage_MN;
import jacob.model.Projectpowerusergroup;
import jacob.model.Projectpowerusergroup_MN;
import jacob.model.Projectusergroup;
import jacob.model.Projectusergroup_MN;
import org.apache.commons.logging.Log;
import com.hecc.jacob.ldap.Role;
import com.hecc.jacob.util.ComboBoxUtil;
import com.hecc.jacob.util.ListBoxUtil;


/**
 *
 * @author R.Spoor
 */
 public class ProjectGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: ProjectGroup.java,v 1.1 2009/02/17 15:23:29 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
	 * @param group   The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
		IMutableListBox selectedCountries = (IMutableListBox)group.findByName("projectSelectedCountriesListbox");
		IMutableListBox availableCountries = (IMutableListBox)group.findByName("projectAvailableCountriesListbox");

		IMutableListBox selectedLanguages = (IMutableListBox)group.findByName("projectSelectedLanguagesListbox");
		IMutableListBox availableLanguages = (IMutableListBox)group.findByName("projectAvailableLanguagesListbox");

        IMutableListBox selectedAdminGroups = (IMutableListBox)group.findByName("projectSelectedAdminGroupsListbox");
        IMutableListBox availableAdminGroups = (IMutableListBox)group.findByName("projectAvailableAdminGroupsListbox");
        IMutableListBox selectedPowerUserGroups = (IMutableListBox)group.findByName("projectAvailablePowerUserGroupsListbox");
        IMutableListBox availablePowerUserGroups = (IMutableListBox)group.findByName("projectAvailablePowerUserGroupsListbox");
        IMutableListBox selectedUserGroups = (IMutableListBox)group.findByName("projectSelectedUserGroupsListbox");
        IMutableListBox availableUserGroups = (IMutableListBox)group.findByName("projectAvailableUserGroupsListbox");

        IMutableListBox selectedHDAdmins = (IMutableListBox)group.findByName("projectSelectedHDAdminsListbox");
        IMutableListBox availableHDUsers = (IMutableListBox)group.findByName("projectAvailableHDUsersListbox");

		String[] empty = {};
		// always clear and rebuild if necessary
		selectedCountries.setOptions(empty);
		availableCountries.setOptions(empty);
		
		selectedLanguages.setOptions(empty);
		availableLanguages.setOptions(empty);
		
		selectedAdminGroups.setOptions(empty);
		availableAdminGroups.setOptions(empty);
        selectedPowerUserGroups.setOptions(empty);
        availablePowerUserGroups.setOptions(empty);
        selectedUserGroups.setOptions(empty);
        availableUserGroups.setOptions(empty);
        
		selectedHDAdmins.setOptions(empty);
		availableHDUsers.setOptions(empty);
		if (status == IGuiElement.NEW || status == IGuiElement.UPDATE || status == IGuiElement.SELECTED)
		{
			IDataRecord record = context.getSelectedRecord();
			Long projectKey = record.getLongValue(Project.pkey);
			IDataAccessor accessor = context.getDataAccessor().newAccessor();
			String linktable = (status == IGuiElement.NEW ? null : Projectcountry_MN.NAME);
			ListBoxUtil.fillLists(
			    context, selectedCountries, availableCountries, accessor, projectKey,
			    Projectcountry.NAME, Projectcountry.pkey, Projectcountry.country,
			    linktable, Projectcountry_MN.project_key, Projectcountry_MN.country_key, null
			);
			linktable = (status == IGuiElement.NEW ? null : Projectlanguage_MN.NAME);
			ListBoxUtil.fillLists(
				context, selectedLanguages, availableLanguages, accessor, projectKey,
				Projectlanguage.NAME, Projectlanguage.pkey, Projectlanguage.language,
				linktable, Projectlanguage_MN.project_key, Projectlanguage_MN.language_key, null
			);
			linktable = (status == IGuiElement.NEW ? null : Projectadmingroup_MN.NAME);
			ListBoxUtil.fillLists(
			    context, selectedAdminGroups, availableAdminGroups, accessor, projectKey,
			    Projectadmingroup.NAME, Projectadmingroup.groupname, Projectadmingroup.name,
			    linktable, Projectadmingroup_MN.project_key, Projectadmingroup_MN.groupname, null
			);
			linktable = (status == IGuiElement.NEW ? null : Projectpowerusergroup_MN.NAME);
			ListBoxUtil.fillLists(
			    context, selectedPowerUserGroups, availablePowerUserGroups, accessor, projectKey,
			    Projectpowerusergroup.NAME, Projectpowerusergroup.groupname, Projectpowerusergroup.name,
			    linktable, Projectpowerusergroup_MN.project_key, Projectpowerusergroup_MN.groupname, null
			);
			linktable = (status == IGuiElement.NEW ? null : Projectusergroup_MN.NAME);
			ListBoxUtil.fillLists(
			    context, selectedUserGroups, availableUserGroups, accessor, projectKey,
			    Projectusergroup.NAME, Projectusergroup.groupname, Projectusergroup.name,
			    linktable, Projectusergroup_MN.project_key, Projectusergroup_MN.groupname, null
			);
			linktable = (status == IGuiElement.NEW ? null : Projecthdadmin_MN.NAME);
			ListBoxUtil.fillLists(
			    context, selectedHDAdmins, availableHDUsers, accessor, projectKey,
			    Projecthdadmin.NAME, Projecthdadmin.loginname, Projecthdadmin.name,
			    linktable, Projecthdadmin_MN.project_key, Projecthdadmin_MN.adminname, null
			);
		}

		IMutableComboBox selectedCombo = (IMutableComboBox)group.findByName("projectFilterSelectedCountriesCombobox");
        IMutableComboBox availableCombo = (IMutableComboBox)group.findByName("projectFilterAvailableCountriesCombobox");
        Map<String,IDataTableRecord> groups = null;
		if (status == IGuiElement.NEW || status == IGuiElement.UPDATE)
		{
		    groups = new TreeMap<String,IDataTableRecord>();
		    IDataAccessor accessor = context.getDataAccessor().newAccessor();
		    IDataTable table = accessor.getTable(Countrygroup.NAME);
		    table.qbeClear();
		    int count = table.search();
		    groups.put("", null);
		    for (int i = 0; i < count; i++)
		    {
		        IDataTableRecord record = table.getRecord(i);
		        String value = record.getStringValue(Countrygroup.countrygroup);
		        groups.put(value, record);
		    }
		}
		ComboBoxUtil.setContents(context, selectedCombo, groups);
        ComboBoxUtil.setContents(context, availableCombo, groups);
	}

	/**
	 * Will be called if the will be change the state from visible=>hidden.
	 * 
	 * This happends if the user switch the Domain or Form which contains this group.
	 * 
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onHide(IClientContext context, IGroup group) throws Exception 
	{
		// insert your code here
	}

    /**
	 * Will be called if the will be change the state from hidden=>visible.
	 * 
	 * This happends if the user switch to a Form which contains this group.
	 * 
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onShow(IClientContext context, IGroup group) throws Exception 
	{
	    IUser user = context.getUser();
	    if (!user.hasRole(Role.ADMIN.getName()))
	    {
	        IGuiElement element = group.findByName("projectHistory");
	        element.setVisible(false);

	        ITabContainer tabContainer = (ITabContainer)group.findByName("projectContainer");
	        ITabPane tabPane = tabContainer.getPane("projectUserGroups");
	        tabPane.setVisible(false);
	        tabPane = tabContainer.getPane("projectHelpdesk");
	        tabPane.setVisible(false);
	    }
	}
}

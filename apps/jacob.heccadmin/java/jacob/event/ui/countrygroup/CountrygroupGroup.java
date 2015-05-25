/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Nov 27 12:21:20 CET 2008
 */
package jacob.event.ui.countrygroup;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;
import jacob.common.AppLogger;
import jacob.model.Countrygroup;
import jacob.model.Countrygroupcountry;
import jacob.model.Countrygroupcountry_MN;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.ListBoxUtil;


/**
 *
 * @author R.Spoor
 */
 public class CountrygroupGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: CountrygroupGroup.java,v 1.1 2009/02/17 15:23:43 R.Spoor Exp $";
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
		boolean enabled = status == IGuiElement.UPDATE || status == IGuiElement.NEW;

		IMutableListBox selectedCountries = (IMutableListBox)group.findByName("countrygroupSelectedCountriesListbox");
		IMutableListBox availableCountries = (IMutableListBox)group.findByName("countrygroupAvailableCountriesListbox");
		group.findByName("countrygroupAddCountriesButton").setEnable(enabled);
		group.findByName("countrygroupRemoveCountriesButton").setEnable(enabled);

		// always clear and rebuild if necessary
		selectedCountries.setOptions(new String[0]);
		availableCountries.setOptions(new String[0]);
		if (status == IGuiElement.NEW || status == IGuiElement.UPDATE || status == IGuiElement.SELECTED)
		{
			IDataRecord record = context.getSelectedRecord();
			Long groupKey = record.getLongValue(Countrygroup.pkey);
			IDataAccessor accessor = context.getDataAccessor().newAccessor();
			String linktable = (status == IGuiElement.NEW ? null : Countrygroupcountry_MN.NAME);
			ListBoxUtil.fillLists(
			    context, selectedCountries, availableCountries, accessor, groupKey,
				Countrygroupcountry.NAME, Countrygroupcountry.pkey, Countrygroupcountry.country,
				linktable, Countrygroupcountry_MN.countrygroup_key, Countrygroupcountry_MN.country_key, null
			);
		}
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
		// insert your code here
	}
}

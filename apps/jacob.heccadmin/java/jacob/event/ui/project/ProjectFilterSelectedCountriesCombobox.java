/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Dec 10 14:56:59 CET 2008
 */
package jacob.event.ui.project;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import jacob.common.AppLogger;
import jacob.model.Country;
import jacob.model.Countrygroup;
import jacob.model.Countrygroupcountry;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.ListBoxUtil;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableComboBoxEventHandler;

/**
 *
 * @author R.Spoor
 */
public class ProjectFilterSelectedCountriesCombobox extends IMutableComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: ProjectFilterSelectedCountriesCombobox.java,v 1.1 2009/02/17 15:23:32 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();


    static void filterSelectedCountries(IClientContext context,
                                        IMutableComboBox comboBox)
           throws Exception
    {
        IGroup group = context.getGroup();
        String filter = comboBox.getValue();
        IMutableListBox selected = (IMutableListBox)group.findByName("projectSelectedCountriesListbox");
        Map<String,IDataTableRecord> contents = ListBoxUtil.getContents(context, selected);
        if (contents == null)
        {
            return;
        }
        selected.setOptions(new String[0]);
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        IDataTable table = null;
        int count = 0;
        if (filter != null && filter.length() > 0)
        {
            table = accessor.getTable(Countrygroupcountry.NAME);
            table.qbeClear();
            IDataTable groupTable = accessor.getTable(Countrygroup.NAME);
            groupTable.qbeClear();
            groupTable.qbeSetKeyValue(Countrygroup.countrygroup, filter);
            count = table.search("countrygroupRelationset");
        }
        else
        {
            table = accessor.getTable(Country.NAME);
            table.qbeClear();
            count = table.search();
        }
        Set<String> countries = new TreeSet<String>();
        for (int i = 0; i < count; i++)
        {
            IDataTableRecord record = table.getRecord(i);
            String country = record.getStringValue(Country.country);
            if (contents.containsKey(country))
            {
                countries.add(country);
            }
        }
        String[] entries = new String[countries.size()];
        selected.setOptions(countries.toArray(entries));
    }

	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state 
	 * of the related table record.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param comboBox  The emitter of the event.
	 */
	@Override
	public void onSelect(IClientContext context, IMutableComboBox comboBox) throws Exception
	{
	    filterSelectedCountries(context, comboBox);
	}

	/**
	 * The event handler if the group status has been changed.<br>
	 * This is a good place to enable/disable some list box entries in relation to the state of the
	 * selected record.<br>
	 * <br>
	 * Note: You can only enable/disable <b>valid</b> enum values of the corresponding table field.<br>
	 * <br>
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param state   The new state of the group.
	 * @param comboBox The emitter of the event.
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableComboBox comboBox) throws Exception
	{
        comboBox.setEnable(state == IGuiElement.UPDATE || state == IGuiElement.NEW);
        if (!comboBox.isEnabled())
        {
            comboBox.clear(context);
        }
	}
}

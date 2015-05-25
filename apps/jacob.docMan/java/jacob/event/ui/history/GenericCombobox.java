/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 13 13:44:11 CEST 2010
 */
package jacob.event.ui.history;

import jacob.model.History;

import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableComboBoxEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 *
 * @author andherz
 */
public class GenericCombobox extends IMutableComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: GenericCombobox.java,v 1.1 2010-09-17 08:42:25 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	private static final Map<String,String> qbe = new HashMap<String,String>();
	
	static
	{
    qbe.put("innerhalb der letzten Woche", "today-7d..today");
    qbe.put("innerhalb der letzten zwei Wochen", "today-14d..today");
    qbe.put("innerhalb des letzten Monats", "today-28d..today");
    qbe.put("innerhalb der letzten zwei Monate", "today-54d..today");
	}
	
	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state 
	 * of the related table record.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param comboBox  The emitter of the event.
	 */
	public void onSelect(IClientContext context, IMutableComboBox comboBox) throws Exception
	{
		String qbeQuery = qbe.get(comboBox.getValue());
    context.getGroup().setInputFieldValue("historyCombobox1", comboBox.getValue());
    context.getGroup().setInputFieldValue("historyCombobox2", comboBox.getValue());
    context.getGroup().setInputFieldValue("historyCombobox3", comboBox.getValue());

    IDataBrowser browser = context.getDataBrowser();

    context.getDataAccessor().clear();
    IDataTable historyTable = context.getDataTable();
    historyTable.qbeSetValue(History.create_date, qbeQuery);
    browser.search(IRelationSet.LOCAL_NAME);
    if(browser.recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
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
	 * @param status  The new state of the group.
	 * @param comboBox The emitter of the event.
	 */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IMutableComboBox comboBox) throws Exception
	{
    comboBox.setEnable(true);
    comboBox.setEditable(true);
    comboBox.setMultichoice(false);
    comboBox.setRequired(true);
    String lastValue = comboBox.getValue();
		comboBox.removeOptions();
    comboBox.addOption("innerhalb der letzten Woche");
    comboBox.addOption("innerhalb der letzten zwei Wochen");
    comboBox.addOption("innerhalb des letzten Monats");
    comboBox.addOption("innerhalb der letzten zwei Monate");
    
    if(StringUtil.emptyOrNull(lastValue))
      comboBox.setValue(comboBox.getOptions()[0]);
    else
      comboBox.setValue(lastValue);
	}
}

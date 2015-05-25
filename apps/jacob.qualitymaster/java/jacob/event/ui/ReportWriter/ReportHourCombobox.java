package jacob.event.ui.ReportWriter;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableComboBoxEventHandler;

/**
 *
 * @author andherz
 */
public class ReportHourCombobox extends IMutableComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: ReportHourCombobox.java,v 1.1 2009-12-24 10:02:21 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state 
	 * of the related table record.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param comboBox  The emitter of the event.
	 */
	public void onSelect(IClientContext context, IMutableComboBox comboBox) throws Exception
	{
		// your code here
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
    comboBox.removeOptions();
    comboBox.addOption("0");
    comboBox.addOption("1");
    comboBox.addOption("2");
    comboBox.addOption("3");
    comboBox.addOption("4");
    comboBox.addOption("5");
    comboBox.addOption("6");
    comboBox.addOption("7");
    comboBox.addOption("8");
    comboBox.addOption("9");
    comboBox.addOption("10");
    comboBox.addOption("11");
    comboBox.addOption("12");
    comboBox.addOption("13");
    comboBox.addOption("14");
    comboBox.addOption("15");
    comboBox.addOption("16");
    comboBox.addOption("17");
    comboBox.addOption("18");
    comboBox.addOption("19");
    comboBox.addOption("20");
    comboBox.addOption("21");
    comboBox.addOption("22");
    comboBox.addOption("23");
    comboBox.setEnable(state == IGroup.UPDATE);
	}
}

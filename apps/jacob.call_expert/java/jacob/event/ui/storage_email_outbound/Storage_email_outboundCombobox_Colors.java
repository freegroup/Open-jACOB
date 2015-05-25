/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jun 10 14:44:24 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;

import jacob.common.AppLogger;
import jacob.common.htmleditor.HTMLEditorHelper;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IMutableComboBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IMutableComboBoxEventHandler;

/**
 *
 * @author achim
 */
public class Storage_email_outboundCombobox_Colors extends IMutableComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: Storage_email_outboundCombobox_Colors.java,v 1.3 2009/07/01 11:48:24 A.Herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

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
	  String color = comboBox.getValue();
    HTMLEditorHelper.insertTag(context, "<font style=\"color:" + color + "\">", "</font>");
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
    String[] colors = new String[]{ "white", "silver", "gray", "black", "red", "blue", "green", "yellow", "orange"};
    comboBox.removeOptions();
    comboBox.setOptions(colors);
	}
}

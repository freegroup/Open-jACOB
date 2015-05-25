/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 03 12:53:49 CET 2006
 */
package jacob.event.ui.configuration;

import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;
import jacob.common.AppLogger;
import jacob.model.Configuration;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class ConfigurationNotify_about_delete extends ICheckBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: ConfigurationNotify_about_delete.java,v 1.1 2007/11/25 22:11:22 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler method will be called, if the user sets a mark at a
	 * checkbox.
	 * 
	 * @param checkBox The checkbox itself
	 * @param context The current context of the application
	 *
	 */
	public void onCheck(IClientContext context, ICheckBox checkBox) throws Exception
	{
		context.getGroup().findByName("configurationNotify_about_deletesubject").setEnable(true);
		context.getGroup().findByName("configurationNotify_about_deletemark").setEnable(true);
	}
  
	/**
	 * This event handler method will be called, if the user unchecks a
	 * checkbox.
	 * 
	 * @param checkBox The checkbox itself
	 * @param context The current context of the application
	 */
	public void onUncheck(IClientContext context, ICheckBox checkBox) throws Exception
	{
		context.getGroup().findByName("configurationNotify_about_deletesubject").setEnable(false);
		context.getGroup().findByName("configurationNotify_about_deletemark").setEnable(false);
	}

	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception 
	{
		ICheckBox checkBox =(ICheckBox)element;
		if(context.getSelectedRecord()!=null)
		{
			if(checkBox.isChecked())
			{
				context.getGroup().findByName("configurationNotify_about_deletesubject").setEnable(true);
				context.getGroup().findByName("configurationNotify_about_deletemark").setEnable(true);
			}
			else
			{
				context.getGroup().findByName("configurationNotify_about_deletesubject").setEnable(false);
				context.getGroup().findByName("configurationNotify_about_deletemark").setEnable(false);
			}
		}
	}
}

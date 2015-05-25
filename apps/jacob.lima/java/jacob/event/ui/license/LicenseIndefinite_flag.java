/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 02 17:02:01 CET 2006
 */
package jacob.event.ui.license;

import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;

/**
 *
 * @author andreas
 */
public class LicenseIndefinite_flag extends ICheckBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: LicenseIndefinite_flag.java,v 1.1 2006/03/07 19:20:35 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
    ISingleDataGuiElement expiration = (ISingleDataGuiElement) context.getGroup().findByName("licenseExpiration_date");
    expiration.setEnable(false);
    expiration.setRequired(false);
    expiration.setValue("");
    
    ISingleDataGuiElement licenseIs_demo = (ISingleDataGuiElement) context.getGroup().findByName("licenseIs_demo");
    licenseIs_demo.setEnable(false);
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
    ISingleDataGuiElement expiration = (ISingleDataGuiElement) context.getGroup().findByName("licenseExpiration_date");
    expiration.setEnable(true);
    expiration.setRequired(true);
    
    ISingleDataGuiElement licenseIs_demo = (ISingleDataGuiElement) context.getGroup().findByName("licenseIs_demo");
    licenseIs_demo.setEnable(true);
	}
}

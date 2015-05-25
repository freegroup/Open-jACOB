package jacob.event.ui.license;

import jacob.model.License;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;

/**
 * @author andherz
 * 
 */
public class LicenseIs_demo extends ICheckBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: LicenseIs_demo.java,v 1.2 2006/03/07 19:20:34 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * This event handler method will be called if he user set a mark at a
   * HTML-CheckBox.
   * 
   * @param checkBox
   *          The CheckBox itself
   * @param context
   *          The current context of the application
   * 
   */
  public void onCheck(IClientContext context, ICheckBox checkBox) throws Exception
  {
    // demo licenses have always 3 users
    ISingleDataGuiElement licenseUser_count = (ISingleDataGuiElement) context.getGroup().findByName("licenseUser_count");
    licenseUser_count.setEnable(false);
    licenseUser_count.setValue("3");
    
    // demo licenses must have an expiration date
    ISingleDataGuiElement licenseIndefinite_flag = (ISingleDataGuiElement) context.getGroup().findByName("licenseIndefinite_flag");
    licenseIndefinite_flag.setEnable(false);
  }

  /**
   * This event handler method will be called if the user uncheck an HTML
   * CheckBox.
   * 
   * @param checkBox
   *          The CheckBox itself
   * @param context
   *          The current context of the application
   * 
   */
  public void onUncheck(IClientContext context, ICheckBox checkBox) throws Exception
  {
    ISingleDataGuiElement licenseUser_count = (ISingleDataGuiElement) context.getGroup().findByName("licenseUser_count");
    licenseUser_count.setEnable(true);
    
    ISingleDataGuiElement licenseIndefinite_flag = (ISingleDataGuiElement) context.getGroup().findByName("licenseIndefinite_flag");
    licenseIndefinite_flag.setEnable(License.type_ENUM._enhanced.equals(context.getSelectedRecord().getValue(License.type)));
  }
}

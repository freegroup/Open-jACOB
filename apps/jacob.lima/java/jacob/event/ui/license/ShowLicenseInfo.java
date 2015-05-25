/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Mar 03 00:11:58 CET 2006
 */
package jacob.event.ui.license;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.license.Base64Coder;
import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the ShowLicenseInfo record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ShowLicenseInfo extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ShowLicenseInfo.java,v 1.1 2006/03/07 19:20:35 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord currentRecord = context.getSelectedRecord();
    String licenseKey = currentRecord.getStringValue(jacob.model.License.hash_key);
    showLicenseInfo(context, licenseKey);
  }

  public static void showLicenseInfo(IClientContext context, String licenseKey) throws Exception
  {
    // extract license info from key
    String tokens[] = licenseKey.split("#");
    if (tokens.length != 2)
      throw new LicenseException("LicenseKey is invalid");
    String licenseInfo = Base64Coder.decode(tokens[0]);

    License license = new License(licenseInfo, licenseKey);

    FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
        "20dlu,p,2dlu,grow,2dlu,p,2dlu,60dlu,20dlu"); // 5 rows
    CellConstraints c = new CellConstraints();

    IFormDialog dialog = context.createFormDialog("jACOB License Information", layout, null);
    dialog.addLabel("Information:", c.xy(1, 1));
    dialog.addTextArea("info", license.toString(), true, c.xy(1, 3));
    dialog.addLabel("Key:", c.xy(1, 5));
    dialog.addTextArea("key", licenseKey, true, true, c.xy(1, 7));

    dialog.show(400, 300);
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

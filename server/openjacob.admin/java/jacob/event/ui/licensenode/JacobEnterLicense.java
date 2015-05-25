/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package jacob.event.ui.licensenode;

import java.util.Map;

import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseFactory;
import de.tif.jacob.license.LicenseManager;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the JacobEnterLicense generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class JacobEnterLicense extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: JacobEnterLicense.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static class EnterKeyCallback implements IFormDialogCallback
  {
    private EnterKeyCallback()
    {
    }

    public void onSubmit(IClientContext context, String buttonId, Map values) throws Exception
    {
      if ("save".equals(buttonId))
      {
        String licenseKey = (String) values.get("key");
        if (licenseKey != null)
          licenseKey = licenseKey.trim();
        if (licenseKey.length() == 0)
        {
          context.createMessageDialog("This is not a valid license key").show();
        }
        else
        {
          LicenseManager licManager = LicenseFactory.getLicenseManager();
          
          // validate license key first
          License license = licManager.validateLicense(licenseKey);
          if (license.isExpired())
          {
            FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
                "20dlu,p,2dlu,grow,20dlu"); // 5 rows
            CellConstraints c = new CellConstraints();

            IFormDialog dialog = context.createFormDialog("jACOB License expired", layout, null);
            dialog.addLabel("Expired license:", c.xy(1, 1));
            dialog.addTextArea("info", license.toString(), true, c.xy(1, 3));

            dialog.setCancelButton("Close");
            dialog.show(400, 250);
            return;
          }

          licManager.setLicense(licenseKey);
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
        "20dlu,p,2dlu,grow,20dlu"); // 5 rows
    CellConstraints c = new CellConstraints();

    IFormDialog dialog = context.createFormDialog("Enter jACOB License Key", layout, new EnterKeyCallback());
    dialog.addLabel("Please enter new license key:", c.xy(1, 1));
    dialog.addTextArea("key", "", c.xy(1, 3));

    dialog.addSubmitButton("save", "Save");
    dialog.setCancelButton("Close");
    dialog.show(500, 250);
  }
}

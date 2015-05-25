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

import de.tif.jacob.license.License;
import de.tif.jacob.license.LicenseFactory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the JacobShowLicense generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class JacobShowLicense extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: JacobShowLicense.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // check the license key
    //
    try
    {
      License license = LicenseFactory.getLicenseManager().getLicense();

      FormLayout layout = new FormLayout("10dlu,grow,10dlu", // 3 columns
          "20dlu,p,2dlu,grow,20dlu"); // 5 rows
      CellConstraints c = new CellConstraints();

      IFormDialog dialog = context.createFormDialog("jACOB License Information", layout, null);
      dialog.addLabel("Information:", c.xy(1, 1));
      dialog.addTextArea("info", license.toString(), true, c.xy(1, 3));

      dialog.setCancelButton("Close");
      dialog.show(400, 250);
    }
    catch (Exception e)
    {
      context.createMessageDialog("No valid license existing!").show();
    }
  }
}

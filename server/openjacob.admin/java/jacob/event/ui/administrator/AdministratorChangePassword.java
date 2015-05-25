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

package jacob.event.ui.administrator;

import jacob.security.User;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the AdministratorChangePassword record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class AdministratorChangePassword extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: AdministratorChangePassword.java,v 1.1 2007/01/19 07:44:32 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Callback class for the AskDialog of a new password.
   */
  private static class AskCallback implements IAskDialogCallback
  {
    /* Do noting if the users cancel the AskDialog */
    public void onCancel(IClientContext context)
    {
    }

    /* Called if the user press [ok] in the AskDialog */
    public void onOk(IClientContext context, String password) throws Exception
    {
      IDataTableRecord adminRecord = context.getDataTable().getSelectedRecord();
      if (null != adminRecord)
      {
        IDataTransaction transaction = context.getDataAccessor().newTransaction();
        try
        {
          // create hashcode from password
          adminRecord.setValue(transaction, "passwdhash", User.createHashcode(password));
          transaction.commit();
        }
        finally
        {
          transaction.close();
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IAskDialog dialog = context.createAskDialog("Please enter a new password", new AskCallback());
    dialog.show();
  }
}

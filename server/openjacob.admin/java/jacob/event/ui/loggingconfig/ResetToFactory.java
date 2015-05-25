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

package jacob.event.ui.loggingconfig;

import jacob.model.Loggingconfig;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.logging.LoggingManager;


/**
 * The event handler for the ResetToFactory record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ResetToFactory extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ResetToFactory.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDialog dialog = context.createOkCancelDialog("Do you really want to reset the logging configuration to factory?", new IOkCancelDialogCallback()
    {
      public void onOk(IClientContext context) throws Exception
      {
        IDataTableRecord factoryConfig = context.getSelectedRecord();
        IDataTransaction trans = factoryConfig.getAccessor().newTransaction();
        try
        {
          factoryConfig.setValue(trans, Loggingconfig.properties, LoggingManager.getDefaultConfiguration());
          trans.commit();
        }
        finally
        {
          trans.close();
        }
      }

      public void onCancel(IClientContext context) throws Exception
      {
        // nothing to do here
      }
    });
    dialog.show();
  }
}


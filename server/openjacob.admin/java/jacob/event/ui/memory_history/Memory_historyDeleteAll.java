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

package jacob.event.ui.memory_history;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Memory_historyDeleteAll generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Memory_historyDeleteAll extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: Memory_historyDeleteAll.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static class DeleteAllCallback implements IOkCancelDialogCallback
  {
    public void onCancel(IClientContext context)
    {
    }
    
    public void onOk(IClientContext context) throws Exception
    {
      IDataAccessor accessor = context.getDataAccessor();
      IDataTransaction trans = accessor.newTransaction(); 
      try
      { 
        IDataTable table = context.getDataTable();
        table.qbeClear();
        table.fastDelete(trans);
        trans.commit();
        context.clearDomain();
      }
      finally 
      {
        trans.close();
      }
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IOkCancelDialog dialog = context.createOkCancelDialog("Do you really want to delete all records?", new DeleteAllCallback());
    dialog.show();
  }
}

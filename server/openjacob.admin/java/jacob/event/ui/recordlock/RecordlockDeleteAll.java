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

package jacob.event.ui.recordlock;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the RecordlockDeleteAll generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class RecordlockDeleteAll extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: RecordlockDeleteAll.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataBrowser data = context.getGUIBrowser().getData();
    if (data.recordCount()==0)
    {
      context.createMessageDialog(new CoreMessage("SEARCH_LOCK_BEFORE_DELETE")).show();
      return;
    }
    
    // and delete record 
    IDataTransaction transaction = context.getDataAccessor().newTransaction();
    try
    {
      for (int i=0;i<data.recordCount();i++)
      {
        data.getRecord(i).getTableRecord().delete(transaction);
      }
      transaction.commit();
    }
    finally
    {
     transaction.close(); 
    }
    
    // and remove all entries from browser
    data.clear();
  }
}

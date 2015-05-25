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
/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 02:52:39 CEST 2006
 */
package jacob.common.ui;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the RemoveBrowserButtonEventHandler generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public abstract class RemoveBrowserButtonEventHandler extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: RemoveBrowserButtonEventHandler.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private static class ConfirmCallback implements IOkCancelDialogCallback
  {
    private final IDataBrowser browser;

    private ConfirmCallback(IDataBrowser browser)
    {
      this.browser = browser;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        for (int i = 0; i < this.browser.recordCount(); i++)
        {
          IDataTableRecord record = this.browser.getRecord(i).getTableRecord();
          record.delete(trans);
        }
        trans.commit();
      }
      finally
      {
        trans.close();
      }
      this.browser.clear();
      context.getGroup().clear(context);
    }
  }
  
  protected abstract String getRecordEntityName(IClientContext context);

  public final void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataBrowser browser = context.getGroup().getBrowser().getData();
    if (browser.recordCount() > 0)
      context.createOkCancelDialog("Delete all " + browser.recordCount() + " " + getRecordEntityName(context) + "?", new ConfirmCallback(browser)).show();
  }
   
  public final void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED || status == IGuiElement.SEARCH);
  }
}

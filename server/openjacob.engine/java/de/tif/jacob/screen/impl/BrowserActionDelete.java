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

package de.tif.jacob.screen.impl;

import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataBrowserInternal;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionDelete extends BrowserAction
{
  /* 
   * Is a action of a row. Not in the headeer bar. 
   */
  public String getIcon()
  {
    return "10x10_delete.png";
  }


  public String getLabelId()
  {
    return "BUTTON_COMMON_DELETE";
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_DELETE";
  }
  
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context, final IBrowser b, final String value)  throws Exception
  {
    // Falls die Gruppe im Updatemodus ist wird KEINE R�ckfrage an den Benutzer gestellt.
    // Man kann in diesem Fall [Abbrechen] dr�cken wenn man etwas falsch gemacht hat.
    //
    if(context.getDataTable().getTableTransaction()!=null)
    {
      // delete a recod in the DataBrowser
      //
      int index=Integer.parseInt(value);
      IDataTransaction trans = context.getDataTable().getTableTransaction();
      b.getData().getRecord(index).getTableRecord().delete(trans);
      b.setSelectedRecordIndex(context,-1);
      //((InFormBrowser)browser).setEditRowIndex(-1);
      
      // IBIS: das Austragen aus dem DataBrowser sollte nicht von Hand passieren m�ssen
      //       Zeile sollte dann entfernt werden.
      ((IDataBrowserInternal)b.getData()).remove(index);
      ((GuiElement)b).resetCache();
    }
    // Ist die GUI nicht im UpdateMode, dann wird eine R�ckfrage an den Benutzer gestellt
    // und die Transaktion gleich mit commit() abgeschlossen.
    // R�ckfrage erfolgt hier, da man die Aktion durch [Abbrechen] nicht R�ckg�ngig machen kann.
    //
    else
    {
      IOkCancelDialog dialog = context.createOkCancelDialog(new CoreMessage("CONFIRM_DELETE_RECORD"),new IOkCancelDialogCallback()
      {
        public void onOk(IClientContext context) throws Exception
        {      
          IDataTransaction trans = context.getDataAccessor().newTransaction();
          try
          {
            // delete a recod in the DataBrowser
            //
            int index=Integer.parseInt(value);
            b.getData().getRecord(index).getTableRecord().delete(trans);
            b.setSelectedRecordIndex(context,-1);
            ((IDataBrowserInternal)b.getData()).remove(index);
            ((GuiElement)b).resetCache();
            trans.commit();
          }
          finally
          {
            trans.close();
          }
        }
        public void onCancel(IClientContext context) throws Exception  { }
      });
      dialog.show();
    }
  }
}

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

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionAdd extends BrowserAction
{
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getIcon()
   */
  public String getIcon()
  {
    return "20x20_add.png";
  }


  public String getLabelId()
  {
    return "BUTTON_COMMON_NEW";
  }

  /*
   * @see de.tif.jacob.screen.impl.html.BrowserAction#getTooltipMessageId()
   */
  public String getTooltipId()
  {
    return "BROWSER_ACTION_TOOLTIP_ADD";
  }

  /** 
   *      FromTable                   ToTable
   *      ---------                  -----------
   *     |         | 1          0-n |           |
   *     |         | -------------> |           |
   *     |         |  RelationToUse |           |
   *      ---------                  -----------
   *
   **/
  public void execute(IClientContext context, IBrowser browser, String value)  throws Exception
  {
    // Deselect the current record from the browser.
    //
    // Dies ist sehr wichtig. Es wird am Ende des Browser ein neuer Record eingefuegt.
    // Die Statusverwaltung kommt sonst durcheinander, wenn sich der Index nach dem Einfuegen 
    // veraendert.
    //
    ((HTTPInFormBrowser)browser).setSelectedRecordIndex(context,-1);

    // Get the parent (FromTable) record for the new record.
    //
    IDataTable groupTable = context.getDataTable( browser.getGroupTableAlias());
    IDataTableRecord fromDataRecord = groupTable.getSelectedRecord(); 
    
    // Create a record in the 'ToTable'
    //
    ITableAlias alias =browser.getData().getTableAlias();
    IDataTransaction trans = context.getDataTable().getTableTransaction();
    IDataTableRecord toDataRecord = context.getDataTable(alias.getName()).newRecord(trans);

    // make the 'ToTable' Record to the n-Relation element of the 'FromTable'
    //
    toDataRecord.setLinkedRecord(trans, fromDataRecord);

    // FREEGROUP: AS 2.9.2008: Bitte überprüfen (Hotfix für Informbrowser-Problem)
    int newIndex =((HTTPInFormBrowser)browser).getDataInternal().append(toDataRecord);
    ((HTTPInFormBrowser)browser).setSelectedRecordIndex(context, newIndex);
//    highlightRecord(context,newIndex);
  }
}


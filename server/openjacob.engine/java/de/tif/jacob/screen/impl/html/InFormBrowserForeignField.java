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

package de.tif.jacob.screen.impl.html;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.definition.guielements.ForeignInputFieldDefinition;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.BrowserAction;

/**
 *
 */
public class InFormBrowserForeignField extends ForeignField
{
  IDataTableRecord recordToDisplay=null;
  
  /**
   * The action if the user clicks on the select dialog of an ForeignFieldBrowser.
   * In einem InformBrowser wird nicht das Standardverhalten genommen. Es wird nicht der Record in
   * der Tabelle auf selektiert geschaltet sondern dem ForeignField als Record to Display angeboten.
   *
   */
  public class Click extends BrowserAction
  {
    public String getTooltipId(){return "BROWSER_ACTION_TOOLTIP_INFORMBROWSERCLICK"; }
    public String getLabelId(){return "BROWSER_ACTION_TOOLTIP_INFORMBROWSERCLICK"; }
    
    public void execute(IClientContext context, IBrowser browser, String value)  throws Exception
    {
      IDataTableRecord record =browser.getData().getRecord(Integer.parseInt(value)).getTableRecord();
      setValue(context, record);
    }
  }
  
  /**
   * @param foreign
   */
  public InFormBrowserForeignField(IApplication app, ForeignInputFieldDefinition foreign)
  {
    super(app, foreign);
    // overide the standard behavior of the the 'click' action.
    // Don't select the record in the DataBrowser/Table. Handover the foreignField the new
    // record to display.
    //
  }

  public void setValue(IClientContext context, IDataTableRecord record) throws Exception
  {
    this.recordToDisplay=record;
    super.setValue(context, record);
  }
  
  /**
   * Returns the record to display for this foreign field
   * 
   * @param context
   * @return
   * @throws Exception
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception
  {
    return recordToDisplay;
  }

  /**
   * Required for some UI-Plugins
   * @deprecated use getSelectedRecord instead
   */
  public IDataTableRecord getDisplayRecord(IClientContext context) throws Exception
  {
    return recordToDisplay;
  }

  /**
   * 
   */
  public void tryBackfill(IClientContext context, int maxDisplayRecords, boolean displaySelectionDialog, boolean countRecordsAsWell) throws Exception
  {
    // The user has been clicked on an foreign field. Prepare the dialog and render them
    // on the client screen
    //
    IDataAccessor accessor = new DataAccessor(getApplicationDefinition());// context.getDataAccessor();
    IDataBrowser  dataBrowser  = accessor.getBrowser(definition.getBrowserToUse());
    accessor.qbeClearAll();
    
    performSearch(context, accessor, dataBrowser, maxDisplayRecords, countRecordsAsWell);
    
    switch(dataBrowser.recordCount())
    {
    case 0:
      context.createMessageDialog(new CoreMessage("NO_FOREIGNFIELD_ENTRY_FOUND")).show();
      break;
    case 1:
      setValue(context, dataBrowser.getRecord(0).getTableRecord());
      break;
    default:
      // more than one found. Display the selection dialog.
      ForeignFieldBrowser guiBrowser =new ForeignFieldBrowser(getApplication(),definition.getBrowserToUse(),this,context, dataBrowser);
      guiBrowser.addHiddenAction(new Click());
      ((Application)context.getApplication()).setForeignFieldBrowser(guiBrowser);
    }    
  }

  
  /**
   * This event occours if the ForeignField displays an record and the user delete one character in
   * input field. 
   * The ForeignField switch now form SELECTED to SEARCH mode and removes the record to display.
   * 
   * @param context
   * @param value
   * @throws Exception
   */
  protected void removeBackfill(IClientContext context) throws Exception
  {
    setDataStatus(context, SEARCH);
    context.getForm().setFocus(this);
    // reset the foreign key elements to the record
    clearInternalKey(); 
    resetValue =getValue();
  }
  
  /**
   * Reset the toTable and fromTable field values
   * 
   *
   */
  protected void clearInternalKey()
  {
    recordToDisplay=null;
    super.clearInternalKey();
  }
}

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

import java.util.Iterator;

import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.report.IReport;
import de.tif.jacob.report.ReportManager;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;

/**
 * Will be fired if the user clicks in a row of the browser.
 * 
 * @author Andras Herz
 */
public class BrowserActionSaveReport extends BrowserAction
{

  public String getLabelId()
  {
    return "BUTTON_COMMON_SAVE";
  }

  public String getTooltipId()
  {
    return "BUTTON_COMMON_SAVE";
  }

 
  /* 
   * @see de.tif.jacob.screen.impl.html.BrowserAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.impl.html.Browser, java.lang.String)
   */
  public void execute(IClientContext context, IBrowser browser, String value)  throws Exception
  {
    HTTPReportBrowser reportBrowser = (HTTPReportBrowser)browser;
    // Checks whether at least one column is defined
    //
    if (reportBrowser.getColumns().size() < 1)
    {
      context.createMessageDialog(new CoreMessage(CoreMessage.NO_COLUMNS_IN_REPORT)).show();
      return;
    }
    
    ActionTypeSearch type = new ActionTypeSearch(reportBrowser.getRelationSet(), reportBrowser.getFilldirection(),false);

    // Suchebedingungen die eventuell per Script gesetzt worden sind vorher holen
    //
    IDataFieldConstraints scriptConstraints= reportBrowser.getStartConstraints();
    
    // Nur wenn die Gruppe im SEARCH Modus ist darf nochmals gesucht werden.
    // Ansonsten ist ein Record zurück gefüllt und es kommt später ein pkey
    // als Suchbedingung in den Report => Es wird nur noch GENAU EIN Record im Report gefunden.
    //
    if(context.getGroup().getDataStatus()==IGuiElement.SEARCH)
      ActionTypeHandler.INSTANCE.execute(type,context,null);

    final IReport report = ReportManager.transformToReport(context,scriptConstraints);
    report.setName(reportBrowser.getReportName());
    report.setOwner(context.getUser());
    
    // set the anchor table and the relationset from the last search (browser)
    //
    report.setAnchorTable(reportBrowser.getAnchorTable());
    report.setRelationset(reportBrowser.getRelationSet());
    report.setPrivate(reportBrowser.isPrivate());

    Iterator iter = reportBrowser.getBrowserTableFields().iterator();
    while(iter.hasNext())
    {
      IBrowserTableField field = (IBrowserTableField)iter.next();
      report.addColumn(field.getTableAlias().getName(), field.getTableField().getName(), field.getLabel(), field.getSortorder());
    }

    // Die letzte Suche des ReportBrowser löschen. Es wird sonst im Report mode Suchergebnisse angezeigt.
    //
    browser.getData().clear();
    
    
    final IReport oldReport = ReportManager.getReport(context.getApplicationDefinition(), context.getUser(), report.getName());
    if(oldReport!=null)
    {
      context.createOkCancelDialog(new CoreMessage("MSG_REPORT_OVERWRITE",report.getName()), new IOkCancelDialogCallback()
      {
        public void onOk(IClientContext context) throws Exception
        {
          report.setReportNotifyee(oldReport.getReportNotifyee());
			    report.save();
			    ((HTTPApplication)context.getApplication()).stopReportMode(context);
			    afterSaveAction(context, report);
        }

        public void onCancel(IClientContext context) throws Exception{}
      }).show();
    }
    else
    {
      report.save();
      ((HTTPApplication) context.getApplication()).stopReportMode(context);
      afterSaveAction(context, report);
    }
  }
  
  protected void afterSaveAction(IClientContext context, IReport report) throws Exception
  {
    context.createMessageDialog(new CoreMessage("MSG_REPORT_SAVE", report.getName())).show();
  }
}

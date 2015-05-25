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

package jacob.event.ui.scheduledreport;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ScheduledreportShowDefinition record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ScheduledreportShowDefinition extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ScheduledreportShowDefinition.java,v 1.1 2007/01/19 07:44:34 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
   *      de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord scheduledreportRecord = context.getDataTable().getSelectedRecord();
    if (null != scheduledreportRecord)
    {
      // backfill report definition
      IDataAccessor accessor = context.getDataAccessor();
      accessor.qbeClearAll();
      IDataTable reportTable = accessor.getTable("report");
      // Note: taskid equals "reportId:userId" !
      String taskId = scheduledreportRecord.getStringValue("taskid");
      String reportId = taskId.substring(0, taskId.indexOf(':'));
      reportTable.qbeSetKeyValue("id", reportId);
      IDataBrowser reportBrowser = accessor.getBrowser("reportBrowser");
      reportBrowser.search(accessor.getApplication().getDefaultRelationSet(), Filldirection.BACKWARD);
      if (reportBrowser.recordCount() > 0)
      {
        reportBrowser.setSelectedRecordIndex(0);
        reportBrowser.propagateSelections();
      }
    }
  }
}


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
package de.tif.jacob.core.model.impl;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.model.Jan_attachment;
import de.tif.jacob.core.model.Jan_queue;

/**
 *
 * @author andreas
 */
public class Jan_queueEventHandler extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: Jan_queueEventHandler.java,v 1.2 2009/10/29 20:31:56 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // delete attachments of message
    //
    IDataTable attachmentTable = tableRecord.getAccessor().getTable(Jan_attachment.NAME);
    attachmentTable.qbeClear();
    attachmentTable.qbeSetKeyValue(Jan_attachment.message_key, tableRecord.getValue(Jan_queue.pkey));
    attachmentTable.searchAndDelete(transaction);
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    if (tableRecord.isDeleted())
      return;
    
    if (tableRecord.hasChangedValue(Jan_queue.urlcomplete))
      tableRecord.setStringValueWithTruncation(transaction, Jan_queue.url, tableRecord.getSaveStringValue(Jan_queue.urlcomplete));
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}

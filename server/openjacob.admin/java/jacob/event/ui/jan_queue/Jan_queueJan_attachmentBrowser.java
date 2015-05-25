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

package jacob.event.ui.jan_queue;

import jacob.model.Jan_attachment;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andreas
 */
public class Jan_queueJan_attachmentBrowser extends de.tif.jacob.screen.event.IBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: Jan_queueJan_attachmentBrowser.java,v 1.1 2007/01/19 07:44:33 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Filters the cell data for the given browser. A browser can be an in-form
   * browser or a search browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          the browser itself
   * @param row
   *          the row which can be filtered
   * @param column
   *          the column
   * @param value
   *          the original value from the database
   * @return the new value for the browser or <code>null</code> to keep cell
   *         empty.
   */
	public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
	{
		return value;
	}
  
  /**
   * This hook method will be called, if the user selects a record in the
   * browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          The browser with the click event
   * @param selectedRecord
   *          the record which has been selected
   */
	public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
    context.createDocumentDialog(selectedRecord.getStringValue(Jan_attachment.mimetype), selectedRecord.getDocumentValue(Jan_attachment.document)).show();
	}
}

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

package de.tif.jacob.core.data;

import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.exception.RecordNotFoundException;


/**
 * Data browser records represent records created when populating instances of
 * {@link IDataBrowser}. The structure of data browser records is determined by
 * the definition, i.e. {@link IBrowserDefinition}, of the respective data
 * browser.
 * <p>
 * In constrast to instances of {@link IDataTableRecord} data browser records
 * could not be modified.
 * <p>
 * Data browser record instances could be obtained by means of
 * <li>{@link IDataBrowser#getRecord(int)}.
 * 
 * @author andreas
 */
public interface IDataBrowserRecord extends IDataRecord
{
//	public DataRecordReference getReference();

//	public ITableField getFieldDefinition(int fieldIndex);

	/**
	 * Returns the parent data browser.
	 * 
	 * @return the parent data browser
	 */
	public IDataBrowser getBrowser();

	/**
   * Returns the related data table record.
   * 
   * @return the related data table record.
   * @throws RecordNotFoundException
   *           if the data table record could not be retrieved.
   */
  public IDataTableRecord getTableRecord() throws RecordNotFoundException;
  
  /**
   * Refreshes this browser record by means of reloading it from data source.
   * <p>
   * 
   * Note:
   * {@link DataTableRecordEventHandler#afterSearchAction(de.tif.jacob.core.data.event.IDataBrowserModifiableRecord)}
   * will be called upon successful reload of the record.
   * 
   * @throws RecordNotFoundException
   *           if the record has already been deleted from data source
   * @since 2.8.1
   */
  public void reload() throws RecordNotFoundException;
}

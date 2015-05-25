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

package de.tif.jacob.core.data.internal;

import java.util.Locale;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataRecord;
import de.tif.jacob.core.data.impl.IDataBrowserSortStrategy;
import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.ISelection;

/**
 * @author andreas
 *
 */
public interface IDataBrowserInternal extends IDataBrowser
{
  public long getChangeCount();

  /**
   * Sets the flag which indicates that a record count should be performed on
   * the next search operation.
   * 
   * Note: This flag will be reset after the next search operation has been
   * performed.
   * 
   * @see #getRealRecordCount()
   */
  public void performRecordCount();

  /**
   * Returns the real number of records matching the search criterias of the
   * last search operation.
   * 
   * @return the real record number or <code>-1</code> if no record counting
   *         has been performed on executing of the last search operation.
   * 
   * @see #performRecordCount()
   */
  public long getRealRecordCount();
  
  /**
   * Internal method for checking, whether the specified record is already propagated.
   * @param index
   * @return
   */
  public boolean isAlreadyPropagated(int index);

  /**
   * Flag to indicate whether a search has been performed since the last clear.
   * 
   * @return <code>true</code> if search has been performed, otherwise <code>false</code>
   */
  public boolean isSearchPerformed();

  /**
   * Appends a table record to this data browser
   * 
   * Attention: Internal Use!!!
   * 
   * @param record
   *          the record to add
   * @return the index of the location where the record has been added
   */
  public int append(IDataTableRecord record);
  
  public int add(IDataTableRecord record);
  
  /**
   * Adds a table record to this data browser
   * 
   * Attention: Internal Use!!!
   * 
   * @param record
   *          the record to add
   * @return the index of the location where the record has been added
   */
  public int add(IDataTableRecord record, IRelationSet relationSet);
  
  public int addRecord(int indexOfInsert, DataRecord record);
  
  public void remove(int index);
  
  /**
   * Returns the current sort strategy.
   * <p>
   * For internal use only!
   * 
   * @return the current sort strategy
   */
  public IDataBrowserSortStrategy getGuiSortStrategy();
  
  /**
   * Creates the new sort strategy.
   * <p>
   * For internal use only!
   * 
   * @return the new sort strategy
   */
  public IDataBrowserSortStrategy newGuiSortStrategy();

  /**
   * Creates the new sort strategy.
   * <p>
   * For internal use only!
   * 
   * @return the new sort strategy
   */
  public IDataBrowserSortStrategy newGuiSortStrategy(Locale sortLocale);
  
  /**
   * This method returns the constraints of the last search performed on this
   * data browser. <br>Attention: Internally last search constraints will not
   * be reset, if clear() is called.
   * 
   * @return constraints of the last search or <code>null</code>, if no
   *         search has been performed so far
   */
  public IDataFieldConstraints getLastSearchConstraints();
  
  public void removeSelected();
  
  public IDataTableRecordInternal getRecordToUpdate(IDataTransaction trans, int index) throws RecordLockedException, RecordNotFoundException;

  /**
   * Internal method
   * 
   * @param groupAliasName
   * @throws InvalidExpressionException
   */
  public void searchIFB(String groupAliasName) throws InvalidExpressionException;
  
  public boolean isDisableSearchIFB();
  
  /**
   * Invokes the multiple update mode for the given selection of browser
   * records.
   * <p>
   * Note: In case of <code>isolated</code> is <code>false</code>, the
   * record returned could be already invalid, i.e. its associated transaction
   * (see {@link IDataMultiUpdateTableRecord#getAssociatedTransaction()}) could
   * already been closed, if an {@link RecordException} has occured!
   * 
   * @param selection
   *          selection of {@link IDataBrowserRecord}
   * @param isolated
   *          <code>true</code> for isolated transaction mode, i.e. each
   *          underlying record has its own transaction, otherwise
   *          <code>false</code>, i.e. updates of the underlying records will
   *          be committed all together or not at all.
   * @return the multiple update data table record which propagates all changes
   *         to the underlying records
   * @throws Exception
   *           on any severe error
   */
  public IDataMultiUpdateTableRecord startMultiUpdate(ISelection selection, boolean isolated) throws Exception;
}

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

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;

/**
 * Internal data table record interface
 * 
 * @author Andreas Sonntag
 */
public interface IDataTableRecordInternal extends IDataTableRecord, IDataRecordInternal
{
  public boolean setValue(IDataTransaction dataTransaction, ITableField field, Object value, Locale locale) throws InvalidExpressionException, RecordLockedException;

  public boolean setValue(IDataTransaction dataTransaction, String fieldName, Object value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException;

  /**
   * @since 2.9.3
   */
  public void appendLongTextValue(IDataTransaction transaction, String fieldName, String value, boolean alwaysWriteChangeHeaderIfActivated) throws NoSuchFieldException, RecordLockedException;

  /**
   * @since 2.9.3
   */
  public void prependLongTextValue(IDataTransaction transaction, String fieldName, String value, boolean alwaysWriteChangeHeaderIfActivated) throws NoSuchFieldException, RecordLockedException;
}

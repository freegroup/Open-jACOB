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

package de.tif.jacob.core.data.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataTableRecordInternal;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * @author Andreas Sonntag
 */
public abstract class AbstractDataTableRecord extends DataRecord implements IDataTableRecordInternal
{
  static public transient final String RCS_ID = "$Id: AbstractDataTableRecord.java,v 1.2 2010/03/25 15:22:31 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  static private final transient Log logger = LogFactory.getLog(AbstractDataTableRecord.class);
  
  protected final DataTable parent;
  
  protected AbstractDataTableRecord(DataTable parent)
  {
    this.parent = parent;
  }
  
  public final DataRecordSet getParent()
  {
    return this.parent;
  }
  
  public final IDataTable getTable()
  {
    return this.parent;
  }

  public final ITableField getFieldDefinition(String fieldName) throws NoSuchFieldException
  {
    return getFieldDefinition(this.parent.getFieldIndexByFieldName(fieldName));
  }
  
  public final Object getValue(ITableField field)
  {
    if (!this.parent.getTableAlias().getTableDefinition().equals(field.getTableDefinition()))
    {
      throw new RuntimeException(field + " is not a field of table " + this.parent.getTableAlias().getTableDefinition());
    }
    return getValue(field.getFieldIndex());
  }
  
  public final boolean isNormal()
  {
    return DataRecordMode.NORMAL == getMode();
  }

  public final boolean isNew()
  {
    return DataRecordMode.NEW == getMode();
  }

  public final boolean isNewOrUpdated()
  {
    DataRecordMode mode = getMode();
    return DataRecordMode.NEW == mode || DataRecordMode.UPDATE == mode;
  }

  public final boolean isUpdated()
  {
    return DataRecordMode.UPDATE == getMode();
  }

  public final boolean isDeleted()
  {
    return DataRecordMode.DELETE == getMode();
  }

  public final IDataKeyValue getKeyValue(String keyName)
  {
    if (null == keyName)
      throw new NullPointerException("keyName is null");

    return getKeyValue(getTableAlias().getTableDefinition().getKey(keyName));
  }
  
  public final boolean setDateValue(IDataTransaction dataTransaction, String fieldName, java.util.Date value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, value);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setDateValue(IDataTransaction dataTransaction, String fieldName, long timeMillis) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, new Date(timeMillis));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setDecimalValue(IDataTransaction dataTransaction, String fieldName, BigDecimal value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, value);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setDoubleValue(IDataTransaction dataTransaction, String fieldName, double value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, new Double(value));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setFloatValue(IDataTransaction dataTransaction, String fieldName, float value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, new Float(value));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setIntValue(IDataTransaction dataTransaction, String fieldName, int value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, new Integer(value));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setBooleanValue(IDataTransaction transaction, String fieldName, boolean value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    try
    {
      return setValue(transaction, fieldName, Boolean.valueOf(value));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setLongValue(IDataTransaction dataTransaction, String fieldName, long value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, new Long(value));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setTimeValue(IDataTransaction dataTransaction, String fieldName, Time value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, value);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setTimestampValue(IDataTransaction dataTransaction, String fieldName, Timestamp value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, value);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setTimestampValue(IDataTransaction dataTransaction, String fieldName, long timeMillis) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, new Timestamp(timeMillis));
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setBinaryValue(IDataTransaction dataTransaction, String fieldName, byte[] value) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, value);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final boolean setDocumentValue(IDataTransaction dataTransaction, String fieldName, DataDocumentValue documentValue) throws NoSuchFieldException, RecordLockedException
  {
    try
    {
      return setValue(dataTransaction, fieldName, documentValue);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException();
    }
  }

  public final IDataTableRecord getLinkedRecord(String tableAliasName) throws RecordNotFoundException
  {
    return getLinkedRecord(this.parent.getAccessor().getApplication().getTableAlias(tableAliasName));
  }

  public final IDataTableRecord getLinkedRecord(ITableAlias tableAlias) throws RecordNotFoundException
  {
    if (logger.isTraceEnabled())
      logger.trace("Getting linked record for alias " + tableAlias);

    IKey foreignKey = this.parent.getAccessorInternal().getForeignKey(this.getParent().getTableAlias(), tableAlias);
    IDataKeyValue foreignKeyValue = getKeyValue(foreignKey);
    if (null == foreignKeyValue)
    {
      return null;
    }
    
    // look first in the transaction "cache" for the linked record.
    // Reason: Record might be linked to an uncommitted record!
    AbstractDataTransaction currentTransaction = (AbstractDataTransaction) getCurrentTransaction();
    if (null != currentTransaction)
    {
      IDataTableRecord transactionRecord = currentTransaction.getRecord(tableAlias, foreignKeyValue);
      if (null != transactionRecord)
      {
        return transactionRecord;
      }
    }

    return getAccessor().getTable(tableAlias).getRecord(foreignKeyValue);
  }

  public final boolean hasLinkedRecord(String tableAliasName)
  {
    return hasLinkedRecord(this.parent.getAccessor().getApplication().getTableAlias(tableAliasName));
  }
  
  public final boolean hasLinkedRecord(ITableAlias tableAlias)
  {
    IKey foreignKey = this.parent.getAccessorInternal().getForeignKey(this.getParent().getTableAlias(), tableAlias);
    return null != getKeyValue(foreignKey);
  }
  
  public final void resetLinkedRecord(IDataTransaction transaction, String tableAliasName) throws RecordLockedException
  {
    resetLinkedRecord(transaction, this.parent.getAccessor().getApplication().getTableAlias(tableAliasName));
  }
  
  public final void resetLinkedRecord(IDataTransaction dataTransaction, ITableAlias fromTableAlias) throws RecordLockedException
  {
    if (logger.isTraceEnabled())
      logger.trace("Reseting linked record for alias " + fromTableAlias);

    IKey foreignKey = this.parent.getAccessorInternal().getForeignKey(this.getParent().getTableAlias(), fromTableAlias);
    for (int i=0; i < foreignKey.getTableFields().size(); i++)
    {
      ITableField foreignField = (ITableField) foreignKey.getTableFields().get(i);
      try
      {
        setValue(dataTransaction, foreignField, null);
      }
      catch (InvalidExpressionException ex)
      {
        // should never occur
        throw new RuntimeException("Unexpected state", ex);
      }
    }
    
    // this record is also the selected record?
    //
    if (this == getTable().getSelectedRecord())
    {
      // clear old propagations
      getAccessor().getTable(fromTableAlias).clear();
    }
  }

  public final void setLinkedRecord(IDataTransaction dataTransaction, IDataRecord fromRecord) throws RecordLockedException, RecordNotFoundException
  {
    ITableAlias fromTableAlias = fromRecord.getTableAlias();
    
    if (logger.isTraceEnabled())
      logger.trace("Setting linked record for alias " + fromTableAlias);

    // link records on data layer, i.e. set foreign key value(s)
    //
    IKey foreignKey = this.parent.getAccessorInternal().getForeignKey(this.getParent().getTableAlias(), fromTableAlias);
    IDataKeyValue primaryFromKeyValue = fromRecord.getPrimaryKeyValue();
    for (int i=0; i < foreignKey.getTableFields().size(); i++)
    {
      ITableField foreignField = (ITableField) foreignKey.getTableFields().get(i);
      try
      {
        setValue(dataTransaction, foreignField,primaryFromKeyValue.getFieldValue(i));
      }
      catch (InvalidExpressionException ex)
      {
       // should never occur
        throw new RuntimeException("Unexpected state", ex);
      }
    }
    
    // this record is also the selected record?
    //
    if (this == getTable().getSelectedRecord())
    {
      // "backfill" the fromRecord as well
      
      // fromRecord is already the "backfill" record?
      IDataTableRecord currentFromRecord = getAccessor().getTable(fromTableAlias).getSelectedRecord();
      if (currentFromRecord == null || !primaryFromKeyValue.equals(currentFromRecord.getPrimaryKeyValue()))
      {
        // avoid to search for uncommitted records
        //
        AbstractDataTransaction currentTransaction = (AbstractDataTransaction) getCurrentTransaction();
        if (null != currentTransaction)
        {
          IDataTableRecord transactionRecord = currentTransaction.getRecord(fromTableAlias, primaryFromKeyValue);
          if (null != transactionRecord && transactionRecord.isNew())
          {
            return;
          }
        }

        // "backfill"
        getAccessorInternal().searchTableRecord(fromTableAlias, primaryFromKeyValue, true);
      }
    }
  }

  public final boolean hasNullValue(String fieldName) throws NoSuchFieldException
  {
    return hasNullValue(this.parent.getFieldIndexByFieldName(fieldName));
  }
  
  public final Object getOldValue(String fieldName) throws NoSuchFieldException
  {
    return getOldValue(this.parent.getFieldIndexByFieldName(fieldName));
  }

  public final boolean getOldbooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Boolean value = getOldBooleanValue(fieldName);
    return value == null ? false : value.booleanValue();
  }

  public final int getOldintValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Integer value = getOldIntegerValue(fieldName);
    return value == null ? 0 : value.intValue();
  }
  
  public final long getOldlongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Long value = getOldLongValue(fieldName);
    return value == null ? 0 : value.longValue();
  }
  
  public final double getOlddoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Double value = getOldDoubleValue(fieldName);
    return value == null ? 0.0 : value.doubleValue();
  }
  
  public final float getOldfloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Float value = getOldFloatValue(fieldName);
    return value == null ? 0.0f : value.floatValue();
  }
  
  public final BigDecimal getOldSaveDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    BigDecimal result = getOldDecimalValue(fieldName);
    return result == null ? DECIMAL_0 : result;
  }
  
  public final String getOldStringValue(String fieldName) throws NoSuchFieldException
  {
    return getOldStringValue(this.parent.getFieldIndexByFieldName(fieldName));
  }

  public final String getOldSaveStringValue(String fieldName) throws NoSuchFieldException
  {
    String value = getOldStringValue(fieldName);
    return value == null ? "" : value;
  }
  
  public final String getOldStringValue(String fieldName, Locale locale) throws NoSuchFieldException
  {
    return getOldStringValue(this.parent.getFieldIndexByFieldName(fieldName), locale);
  }

  public final String getOldStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException
  {
    return getOldStringValue(this.parent.getFieldIndexByFieldName(fieldName), locale, style);
  }

  public final String getOldStringValue(int fieldIndex)
  {
    return getOldStringValue(fieldIndex, null);
  }
  
  public final String getOldStringValue(int fieldIndex, Locale locale)
  {
    return getOldStringValue(fieldIndex, locale, DEFAULT_STYLE);
  }
  
  public abstract String getOldStringValue(int fieldIndex, Locale locale, int style);
  
  public final boolean setStringValue(IDataTransaction dataTransaction, String fieldName, String value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException
  {
    return setValue(dataTransaction, fieldName, value);
  }

  public final boolean setStringValue(IDataTransaction transaction, String fieldName, String value, Locale locale) throws NoSuchFieldException,
      InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return setValue(transaction, fieldName, value, locale);
  }
  
  public final boolean setStringValueWithTruncation(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException
  {
    return setStringValueWithTruncation(transaction, fieldName, value, null);
  }

  public final boolean setValue(IDataTransaction dataTransaction, ITableField field, Object value) throws InvalidExpressionException, RecordLockedException
  {
    return setValue(dataTransaction, field, value, null);
  }
  
  public final boolean setValue(IDataTransaction dataTransaction, String fieldName, Object value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException
  {
    return setValue(dataTransaction, fieldName, value, null);
  }
  
  public final IDataTableRecord getTableRecord()
  {
    return this;
  }

  public final void appendLongTextValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, RecordLockedException
  {
    appendLongTextValue(transaction, fieldName, value, false);
  }

  public final void prependLongTextValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, RecordLockedException
  {
    prependLongTextValue(transaction, fieldName, value, false);
  }
}

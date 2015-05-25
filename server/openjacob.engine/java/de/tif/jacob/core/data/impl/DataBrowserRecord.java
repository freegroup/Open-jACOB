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
/*
 * Created on 30.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data.impl;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Locale;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.IDataBrowserModifiableRecord;
import de.tif.jacob.core.data.internal.IDataBrowserRecordInternal;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.BooleanFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampResolution;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.util.DatetimeUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataBrowserRecord extends DataRecord implements IDataBrowserRecordInternal, IDataBrowserModifiableRecord
{
  static public transient final String RCS_ID = "$Id: DataBrowserRecord.java,v 1.9 2010/08/11 22:32:58 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.9 $";

  private final class DefaultProvider implements IDataBrowserRecordValueProvider
  {
    private final Object[] values;
    private final int[] primaryKeyIndices;

    private DefaultProvider(int[] primaryKeyIndices, Object[] values)
    {
      this.values = values;
      this.primaryKeyIndices = primaryKeyIndices;
    }

    public IDataKeyValue getPrimaryKeyValue()
    {
      // determine primary key value
      return getKeyValue(this.primaryKeyIndices);
    }

    public IDataTableRecord getTableRecord() throws RecordNotFoundException
    {
      IDataKeyValue primaryKey = getPrimaryKeyValue();
      DataTable dataTable = DataBrowserRecord.this.parent.getRelatedDataTable();
      IDataTableRecord tableRecord = dataTable.getCachedRecord(primaryKey);
      if (null != tableRecord)
      {
        return tableRecord;
      }
      return dataTable.loadRecord(primaryKey);
    }

    public boolean isModifiedValue(int fieldIndex)
    {
      return this.values[fieldIndex] != null && this.values[fieldIndex] instanceof ModifiedValue;
    }

    public Object getValueInternal(int fieldIndex)
    {
      return this.values[fieldIndex];
    }

    public void setValueInternal(int fieldIndex, Object value)
    {
      this.values[fieldIndex] = value;
    }

    public DataTableRecord getWrappedTableRecord()
    {
      // no wrapped record
      return null;
    }
  }
  
  private static final class ModifiedValue
  {
    private final Object value;
    
    private ModifiedValue(Object value)
    {
      this.value = value;
    }
  }

  private final DataBrowser parent;

  // unique sequence number within a data browser
  private final int seqNbr;

  private IDataBrowserRecordValueProvider exchangeableProvider;

  protected DataBrowserRecord(DataBrowser parent, int[] primaryKeyIndices, Object[] values, int seqNbr)
  {
    this.parent = parent;
    this.seqNbr = seqNbr;
    this.exchangeableProvider = new DefaultProvider(primaryKeyIndices, values);
  }

  protected DataBrowserRecord(DataBrowser parent, int seqNbr, IDataBrowserRecordValueProvider provider)
  {
    this.parent = parent;
    this.seqNbr = seqNbr;
    this.exchangeableProvider = provider;
  }

  public DataRecordMode getMode()
  {
    return DataRecordMode.NORMAL;
  }

  public DataRecordSet getParent()
  {
    return this.parent;
  }

  public IDataBrowser getBrowser()
  {
    return this.parent;
  }

  public boolean isModifiedValue(int fieldIndex)
  {
    return this.exchangeableProvider.isModifiedValue(fieldIndex);
  }

  public Object getValueInternal(int fieldIndex)
  {
    Object dataValue = this.exchangeableProvider.getValueInternal(fieldIndex);
    if (null == dataValue)
      return null;

    if (dataValue instanceof ModifiedValue)
      return ((ModifiedValue) dataValue).value;
    return dataValue;
  }

  public Object getValue(int fieldIndex)
  {
    Object dataValue = this.exchangeableProvider.getValueInternal(fieldIndex);
    if (null == dataValue)
      return null;

    if (dataValue instanceof ModifiedValue)
    {
      dataValue = ((ModifiedValue) dataValue).value;
      if (dataValue instanceof java.util.Date)
      {
        // object is mutable -> clone
        return ((java.util.Date) dataValue).clone();
      }
      return dataValue;
    }

    ITableField field = getFieldDefinition(fieldIndex);
    if (null == field)
    {
      // field might be null because of calculated browser fields
      return dataValue;
    }
    return field.getType().convertDataValueToObject(dataValue);
  }
  
  public String getStringValue(int fieldIndex, Locale locale, int style)
  {
    Object value = this.exchangeableProvider.getValueInternal(fieldIndex);
    if (null == value)
      return null;

    if (value instanceof ModifiedValue)
    {
      value = ((ModifiedValue) value).value;
      if (value instanceof BigDecimal)
      {
        return I18N.toString((BigDecimal) value, locale, style);
      }
      if (value instanceof Timestamp)
      {
        return TimestampResolution.SEC_BASE.toString((Timestamp) value, locale);
      }
      if (value instanceof Time)
      {
        return DatetimeUtil.convertDateToString((Time) value, locale);
      }
      if (value instanceof java.util.Date)
      {
        return DatetimeUtil.convertDateToString((java.util.Date) value, locale);
      }
      if (value instanceof Boolean)
      {
        return BooleanFieldType.convertToString((Boolean) value, locale, style);
      }
      if (value instanceof Double)
      {
        return I18N.toString((Double) value, locale, style);
      }
      if (value instanceof Float)
      {
        return I18N.toString((Float) value, locale, style);
      }
      return value.toString();
    }

    ITableField field = getFieldDefinition(fieldIndex);
    if (null == field)
    {
      // field might be null because of calculated browser fields
      return value.toString();
    }
    return field.getType().convertDataValueToString(value, locale, style);
  }
  
  public void setValue(int fieldIndex, Object value)
  {
    this.exchangeableProvider.setValueInternal(fieldIndex, value == null ? null : new ModifiedValue(value));
  }

  public void setValue(String fieldName, Object value) throws NoSuchFieldException
  {
    setValue(getParent().getFieldIndexByFieldName(fieldName), value);
  }
  
  public IDataKeyValue getPrimaryKeyValue()
  {
    return this.exchangeableProvider.getPrimaryKeyValue();
  }

  public IDataTableRecord getTableRecord() throws RecordNotFoundException
  {
    return this.exchangeableProvider.getTableRecord();
  }

  // IBIS: Erst mal temporär
  public IDataTableRecord getTableRecordToUpdate(IDataTransaction trans) throws RecordNotFoundException, RecordLockedException
  {
    return this.parent.getRecordToUpdate(trans, this);
  }

  public int getSeqNbr()
  {
    return this.seqNbr;
  }

  /**
   * Internal method!
   * 
   * @return the provider
   */
  protected IDataBrowserRecordValueProvider getProvider()
  {
    return exchangeableProvider;
  }

  /**
   * Internal method!
   * 
   * @param provider the provider to set
   */
  protected void setProvider(IDataBrowserRecordValueProvider provider)
  {
    this.exchangeableProvider = provider;
  }

  public void reload() throws RecordNotFoundException
  {
    // Check primary key
    //
    IDataKeyValue primaryKeyValue = getPrimaryKeyValue();
    if (primaryKeyValue == null)
      throw new RuntimeException("Record can not be reloaded because no primary key existing");

    // Reload data from data source
    //
    IDataAccessor accessor = this.parent.getAccessor().newAccessor();
    ITableAlias alias = getTableAlias();
    IDataTable dataTable = accessor.getTable(alias);
    dataTable.qbeSetKeyValue(alias.getTableDefinition().getPrimaryKey(), primaryKeyValue);
    DataBrowser tempDataBrowser = new DataBrowser((DataAccessor) accessor, this.parent.getBrowserDefinition(), false);
    try
    {
      IRelationSet relationSet = this.parent.getRelationSet();
      tempDataBrowser.search(relationSet != null ? relationSet : accessor.getApplication().getLocalRelationSet());
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException(ex);
    }
    switch (tempDataBrowser.recordCount())
    {
      case 0:
        throw new RecordNotFoundException(alias.getTableDefinition(), primaryKeyValue);
      case 1:
        break;
      default:
        // should never occur
        throw new IllegalStateException();
    }
    DataBrowserRecord tempDataBrowserRecord = (DataBrowserRecord) tempDataBrowser.getRecord(0);
    this.exchangeableProvider = tempDataBrowserRecord.exchangeableProvider;

    //  
    this.parent.incrementChangeCounter();
  }
}

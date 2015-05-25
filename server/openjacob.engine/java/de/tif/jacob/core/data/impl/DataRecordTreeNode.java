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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * @author Andreas
 *
 * Man kann die Klasse allerdings nicht als vollwertigen IDataTableRecord
 * verwenden, da intern immer eine Classcast Exception auftritt.
 * Währe aber schön wenn die Records gleichwertig behandelt werden können.
 */
public class DataRecordTreeNode implements IDataTableRecord
{
  static public transient final String        RCS_ID = "$Id: DataRecordTreeNode.java,v 1.3 2010/01/20 03:18:14 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.3 $";
  
//  private DataRecordTreeNode parent;
  private List children = new ArrayList();
  private IDataTableRecord nodeRecord;
  
  
  /* required for Class.newInstance() - don't remove*/
  public DataRecordTreeNode()
  {
  }
  
  public final void setNodeRecord(IDataTableRecord record)
  {
    nodeRecord= record;
    onSetNodeRecord();
  }
  
  public IDataTableRecord getInternalRecord()
  {
    return nodeRecord;
  }
  
  /**
   * will be called if the record for this treeNode has been set during the propagate<br>
   * Add your custom code in the inherited class here.
   *
   */
  public void onSetNodeRecord()
  {
  }
  
  public List getChildren()
  {
    return Collections.unmodifiableList(children);
  }
  
  public void addChild(DataRecordTreeNode child) throws Exception
  {
    children.add(child);
//		child.parent=this;
  }
  
  // Damit sich eine Node gleich verhält wie ein IDataRecord
  // Vereinfacht die API
  public void appendLongTextValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    nodeRecord.appendLongTextValue(transaction, fieldName, value);
  }
  public void appendToHistory(IDataTransaction transaction, String note) throws RecordLockedException
  {
    nodeRecord.appendToHistory(transaction, note);
  }
  public void delete(IDataTransaction transaction) throws RecordLockedException
  {
    nodeRecord.delete(transaction);
  }
  public boolean equals(Object obj)
  {
    return nodeRecord.equals(obj);
  }
  public IDataAccessor getAccessor()
  {
    return nodeRecord.getAccessor();
  }
  public String getBinaryReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getBinaryReference(fieldName);
  }
  public byte[] getBytesValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getBytesValue(fieldIndex);
  }
  public byte[] getBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getBytesValue(fieldName);
  }
  public IDataTransaction getCurrentTransaction()
  {
    return nodeRecord.getCurrentTransaction();
  }
  public Date getDateValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getDateValue(fieldIndex);
  }
  public Date getDateValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getDateValue(fieldName);
  }
  public BigDecimal getDecimalValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getDecimalValue(fieldIndex);
  }
  public BigDecimal getDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getDecimalValue(fieldName);
  }
  public String getDocumentReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getDocumentReference(fieldName);
  }
  public DataDocumentValue getDocumentValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getDocumentValue(fieldIndex);
  }
  public DataDocumentValue getDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getDocumentValue(fieldName);
  }
  public double getdoubleValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getdoubleValue(fieldIndex);
  }
  public double getdoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getdoubleValue(fieldName);
  }
  public Double getDoubleValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getDoubleValue(fieldIndex);
  }
  public Double getDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getDoubleValue(fieldName);
  }
  public ITableField getFieldDefinition(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getFieldDefinition(fieldName);
  }
  public int getFieldNumber()
  {
    return nodeRecord.getFieldNumber();
  }
  public float getfloatValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getfloatValue(fieldIndex);
  }
  public float getfloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getfloatValue(fieldName);
  }
  public Float getFloatValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getFloatValue(fieldIndex);
  }
  public Float getFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getFloatValue(fieldName);
  }
  public IDataRecordId getId()
  {
    return nodeRecord.getId();
  }
  public Integer getIntegerValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getIntegerValue(fieldIndex);
  }
  public Integer getIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getIntegerValue(fieldName);
  }
  public int getintValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getintValue(fieldIndex);
  }
  public int getintValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getintValue(fieldName);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getbooleanValue(int)
   */
  public boolean getbooleanValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getbooleanValue(fieldIndex);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getBooleanValue(int)
   */
  public Boolean getBooleanValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getBooleanValue(fieldIndex);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getbooleanValue(java.lang.String)
   */
  public boolean getbooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getbooleanValue(fieldName);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getBooleanValue(java.lang.String)
   */
  public Boolean getBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getBooleanValue(fieldName);
  }

  public IDataKeyValue getKeyValue(IKey key)
  {
    return nodeRecord.getKeyValue(key);
  }
  public IDataKeyValue getKeyValue(String keyName)
  {
    return nodeRecord.getKeyValue(keyName);
  }
  public IDataTableRecord getLinkedRecord(ITableAlias tableAlias) throws RecordNotFoundException
  {
    return nodeRecord.getLinkedRecord(tableAlias);
  }
  public IDataTableRecord getLinkedRecord(String tableAliasName) throws RecordNotFoundException
  {
    return nodeRecord.getLinkedRecord(tableAliasName);
  }
  public String getLongTextReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getLongTextReference(fieldName);
  }
  public long getlongValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getlongValue(fieldIndex);
  }
  public long getlongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getlongValue(fieldName);
  }
  public Long getLongValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getLongValue(fieldIndex);
  }
  public Long getLongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getLongValue(fieldName);
  }
  public byte[] getOldBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldBytesValue(fieldName);
  }
  public Date getOldDateValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldDateValue(fieldName);
  }
  public BigDecimal getOldDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldDecimalValue(fieldName);
  }
  public DataDocumentValue getOldDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldDocumentValue(fieldName);
  }
  public double getOlddoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOlddoubleValue(fieldName);
  }
  public Double getOldDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldDoubleValue(fieldName);
  }
  public float getOldfloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldfloatValue(fieldName);
  }
  public Float getOldFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldFloatValue(fieldName);
  }
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldbooleanValue(java.lang.String)
   */
  public boolean getOldbooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldbooleanValue(fieldName);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldBooleanValue(java.lang.String)
   */
  public Boolean getOldBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldBooleanValue(fieldName);
  }

  public Integer getOldIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldIntegerValue(fieldName);
  }
  public int getOldintValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldintValue(fieldName);
  }
  public long getOldlongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldlongValue(fieldName);
  }
  public Long getOldLongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldLongValue(fieldName);
  }
  public BigDecimal getOldSaveDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldSaveDecimalValue(fieldName);
  }
  public String getOldSaveStringValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getOldSaveStringValue(fieldName);
  }
  public String getOldStringValue(int fieldIndex)
  {
    return nodeRecord.getOldStringValue(fieldIndex);
  }
  public String getOldStringValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getOldStringValue(fieldName);
  }
  public String getOldStringValue(String fieldName, Locale locale) throws NoSuchFieldException
  {
    return nodeRecord.getOldStringValue(fieldName, locale);
  }
  public String getOldStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException
  {
    return nodeRecord.getOldStringValue(fieldName, locale, style);
  }
  public Timestamp getOldTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldTimestampValue(fieldName);
  }
  public Time getOldTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getOldTimeValue(fieldName);
  }
  public Object getOldValue(int fieldIndex)
  {
    return nodeRecord.getOldValue(fieldIndex);
  }
  public Object getOldValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getOldValue(fieldName);
  }
  public IDataKeyValue getPrimaryKeyValue()
  {
    return nodeRecord.getPrimaryKeyValue();
  }
  public BigDecimal getSaveDecimalValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getSaveDecimalValue(fieldIndex);
  }
  public BigDecimal getSaveDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getSaveDecimalValue(fieldName);
  }
  public String getSaveStringValue(int fieldIndex) throws IndexOutOfBoundsException
  {
    return nodeRecord.getSaveStringValue(fieldIndex);
  }
  public String getSaveStringValue(int fieldIndex, Locale locale, int style) throws IndexOutOfBoundsException
  {
    return nodeRecord.getSaveStringValue(fieldIndex, locale, style);
  }
  public String getSaveStringValue(int fieldIndex, Locale locale) throws IndexOutOfBoundsException
  {
    return nodeRecord.getSaveStringValue(fieldIndex, locale);
  }
  public String getSaveStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException
  {
    return nodeRecord.getSaveStringValue(fieldName, locale, style);
  }
  public String getSaveStringValue(String fieldName, Locale locale) throws NoSuchFieldException
  {
    return nodeRecord.getSaveStringValue(fieldName, locale);
  }
  public String getSaveStringValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getSaveStringValue(fieldName);
  }
  public String getStringValue(int fieldIndex) throws IndexOutOfBoundsException
  {
    return nodeRecord.getStringValue(fieldIndex);
  }
  public String getStringValue(int fieldIndex, Locale locale) throws IndexOutOfBoundsException
  {
    return nodeRecord.getStringValue(fieldIndex, locale);
  }
  public String getStringValue(int fieldIndex, Locale locale, int style) throws IndexOutOfBoundsException
  {
    return nodeRecord.getStringValue(fieldIndex, locale, style);
  }
  public String getStringValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getStringValue(fieldName);
  }
  public String getStringValue(String fieldName, Locale locale) throws NoSuchFieldException
  {
    return nodeRecord.getStringValue(fieldName, locale);
  }
  public String getStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException
  {
    return nodeRecord.getStringValue(fieldName, locale, style);
  }
  public IDataTable getTable()
  {
    return nodeRecord.getTable();
  }
  public ITableAlias getTableAlias()
  {
    return nodeRecord.getTableAlias();
  }
  public Timestamp getTimestampValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getTimestampValue(fieldIndex);
  }
  public Timestamp getTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getTimestampValue(fieldName);
  }
  public Time getTimeValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return nodeRecord.getTimeValue(fieldIndex);
  }
  public Time getTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return nodeRecord.getTimeValue(fieldName);
  }
  public Object getValue(int fieldIndex) throws IndexOutOfBoundsException
  {
    return nodeRecord.getValue(fieldIndex);
  }
  public Object getValue(ITableField field)
  {
    return nodeRecord.getValue(field);
  }
  public Object getValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.getValue(fieldName);
  }
  public boolean hasChangedValue(int fieldIndex)
  {
    return nodeRecord.hasChangedValue(fieldIndex);
  }
  public boolean hasChangedValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.hasChangedValue(fieldName);
  }
  public boolean hasChangedValues()
  {
    return nodeRecord.hasChangedValues();
  }
  public int hashCode()
  {
    return nodeRecord.hashCode();
  }
  public boolean hasLinkedRecord(ITableAlias tableAlias)
  {
    return nodeRecord.hasLinkedRecord(tableAlias);
  }
  public boolean hasLinkedRecord(String tableAliasName)
  {
    return nodeRecord.hasLinkedRecord(tableAliasName);
  }
  public boolean hasNullValue(int fieldIndex) throws IndexOutOfBoundsException
  {
    return nodeRecord.hasNullValue(fieldIndex);
  }
  public boolean hasNullValue(String fieldName) throws NoSuchFieldException
  {
    return nodeRecord.hasNullValue(fieldName);
  }
  public boolean isDeleted()
  {
    return nodeRecord.isDeleted();
  }
  public boolean isPersistent()
  {
    return nodeRecord.isPersistent();
  }

  public boolean isNew()
  {
    return nodeRecord.isNew();
  }
  public boolean isNewOrUpdated()
  {
    return nodeRecord.isNewOrUpdated();
  }
  public boolean isNormal()
  {
    return nodeRecord.isNormal();
  }
  public boolean isUpdated()
  {
    return nodeRecord.isUpdated();
  }
  public void prependLongTextValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    nodeRecord.prependLongTextValue(transaction, fieldName, value);
  }
  public void resetLinkedRecord(IDataTransaction transaction, ITableAlias tableAlias) throws RecordLockedException
  {
    nodeRecord.resetLinkedRecord(transaction, tableAlias);
  }
  public void resetLinkedRecord(IDataTransaction transaction, String tableAliasName) throws RecordLockedException
  {
    nodeRecord.resetLinkedRecord(transaction, tableAliasName);
  }
  public boolean setBinaryValue(IDataTransaction transaction, String fieldName, byte[] value) throws NoSuchFieldException, RecordLockedException
  {
    return nodeRecord.setBinaryValue(transaction, fieldName, value);
  }
  public boolean setDateValue(IDataTransaction transaction, String fieldName, java.util.Date value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setDateValue(transaction, fieldName, value);
  }
  public boolean setDateValue(IDataTransaction transaction, String fieldName, long timeMillis) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setDateValue(transaction, fieldName, timeMillis);
  }
  public boolean setDecimalValue(IDataTransaction transaction, String fieldName, BigDecimal value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setDecimalValue(transaction, fieldName, value);
  }
  public boolean setDocumentValue(IDataTransaction transaction, String fieldName, DataDocumentValue documentValue) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setDocumentValue(transaction, fieldName, documentValue);
  }
  public boolean setDoubleValue(IDataTransaction transaction, String fieldName, double value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setDoubleValue(transaction, fieldName, value);
  }
  public boolean setFloatValue(IDataTransaction transaction, String fieldName, float value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setFloatValue(transaction, fieldName, value);
  }
  public boolean setIntValue(IDataTransaction transaction, String fieldName, int value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setIntValue(transaction, fieldName, value);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#setBooleanValue(de.tif.jacob.core.data.IDataTransaction, java.lang.String, boolean)
   */
  public boolean setBooleanValue(IDataTransaction transaction, String fieldName, boolean value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setBooleanValue(transaction, fieldName, value);
  }

  public void setLinkedRecord(IDataTransaction transaction, IDataRecord fromRecord) throws RecordLockedException, RecordNotFoundException
  {
    nodeRecord.setLinkedRecord(transaction, fromRecord);
  }
  public boolean setLongValue(IDataTransaction transaction, String fieldName, long value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setLongValue(transaction, fieldName, value);
  }
  public boolean setStringValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setStringValue(transaction, fieldName, value);
  }
  public boolean setStringValue(IDataTransaction transaction, String fieldName, String value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setStringValue(transaction, fieldName, value, locale);
  }
  public boolean setStringValueWithTruncation(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setStringValueWithTruncation(transaction, fieldName, value);
  }
  public boolean setStringValueWithTruncation(IDataTransaction transaction, String fieldName, String value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setStringValueWithTruncation(transaction, fieldName, value, locale);
  }
  public boolean setTimestampValue(IDataTransaction transaction, String fieldName, long timeMillis) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setTimestampValue(transaction, fieldName, timeMillis);
  }
  public boolean setTimestampValue(IDataTransaction transaction, String fieldName, Timestamp value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setTimestampValue(transaction, fieldName, value);
  }
  public boolean setTimeValue(IDataTransaction transaction, String fieldName, Time value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setTimeValue(transaction, fieldName, value);
  }
  public boolean setValue(IDataTransaction transaction, ITableField field, Object value) throws InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setValue(transaction, field, value);
  }
  public boolean setValue(IDataTransaction transaction, String fieldName, Object value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
    return nodeRecord.setValue(transaction, fieldName, value);
  }
  public String toString()
  {
    return nodeRecord.toString();
  }
}

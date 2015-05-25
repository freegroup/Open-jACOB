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

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.internal.IDataRecordInternal;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.transformer.ITransformerRow;

/**
 * Abstract base data record implementation.
 * 
 * @author Andreas Sonntag
 */
public abstract class DataRecord implements ITransformerRow, IDataRecordInternal
{
	static public transient final String RCS_ID = "$Id: DataRecord.java,v 1.9 2010/09/21 12:04:11 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.9 $";
	
	protected final static BigDecimal DECIMAL_0 = new BigDecimal("0.0");
	
	private Object stickyRetrievalObject;

  public final void addStickyRetrievalObject(Object stickyRetrievalObject)
  {
    this.stickyRetrievalObject = stickyRetrievalObject;
  }

  public final Object getStickyRetrievalObject()
  {
    return this.stickyRetrievalObject;
  }

  public IDataRecordId getId()
	{
		IDataKeyValue primaryKey = this.getPrimaryKeyValue();
		if (null == primaryKey)
		{
			// use hashCode as pseudo id, hoping it to be unique!
			return new DataRecordId(getParent().getTableAlias().getTableDefinition(), super.hashCode());
		}
		return new DataRecordId(getParent().getTableAlias().getTableDefinition(), primaryKey);
	}

  /**
   * Returns the mode of this record.
   * 
   * @return the record mode
   */
  public abstract DataRecordMode getMode();
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getAccessor()
   */
  public final IDataAccessor getAccessor()
  {
    return getParent().getAccessor(); 
  }

  public final DataAccessor getAccessorInternal()
  {
    return getParent().getAccessorInternal(); 
  }

  public abstract DataRecordSet getParent();
  
  public abstract IDataTableRecord getTableRecord() throws RecordNotFoundException;
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getTableAlias()
   */
  public final ITableAlias getTableAlias()
  {
  	return getParent().getTableAlias();
  }

	/**
	 * @return
	 */
	public final DataRecordReference getReference()
	{
		return new DataRecordReference(getParent().getTableAlias().getName(), getPrimaryKeyValue());
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getValueNumber()
	 */
	public int getFieldNumber()
	{
		return getParent().getRecordSize();
	}

  public final Object getValue(String fieldName) throws NoSuchFieldException
	{
		return getValue(getParent().getFieldIndexByFieldName(fieldName));
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#hasNullValue(int)
   */
  public boolean hasNullValue(int fieldIndex) throws IndexOutOfBoundsException
  {
    // IBIS: Make more efficient!!!
    return getValue(fieldIndex) == null;
  }
  
  public boolean hasNullValue(String fieldName) throws NoSuchFieldException
  {
    return hasNullValue(getParent().getFieldIndexByFieldName(fieldName));
  }
  
	public abstract Object getValueInternal(int fieldIndex);
	
	/**
   * Returns the definition of the table field given by index.
   * 
   * @param fieldIndex
   *          the field index
   * @return the table field definition requested
   */
	public ITableField getFieldDefinition(int fieldIndex)
	{
		return getParent().getFieldDefinition(fieldIndex);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getSaveStringValue(int)
	 */
	public final String getSaveStringValue(int fieldIndex)
	{
		String value = getStringValue(fieldIndex);
		return value == null ? "" : value;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getSaveStringValue(int, java.util.Locale, int)
   */
  public final String getSaveStringValue(int fieldIndex, Locale locale, int style) throws IndexOutOfBoundsException
  {
		String value = getStringValue(fieldIndex, locale, style);
		return value == null ? "" : value;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getSaveStringValue(int, java.util.Locale)
   */
  public final String getSaveStringValue(int fieldIndex, Locale locale) throws IndexOutOfBoundsException
  {
		String value = getStringValue(fieldIndex, locale);
		return value == null ? "" : value;
  }
  
	public final String getSaveStringValue(String fieldName) throws NoSuchFieldException
	{
		String value = getStringValue(fieldName);
		return value == null ? "" : value;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getSaveStringValue(java.lang.String, java.util.Locale, int)
   */
  public final String getSaveStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException
  {
		String value = getStringValue(fieldName, locale, style);
		return value == null ? "" : value;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getSaveStringValue(java.lang.String, java.util.Locale)
   */
  public final String getSaveStringValue(String fieldName, Locale locale) throws NoSuchFieldException
  {
		String value = getStringValue(fieldName, locale);
		return value == null ? "" : value;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getStringValue(int)
   */
  public final String getStringValue(int fieldIndex)
  {
    return getStringValue(fieldIndex, null, DEFAULT_STYLE);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getStringValue(int, java.util.Locale)
   */
  public final String getStringValue(int fieldIndex, Locale locale)
  {
    return getStringValue(fieldIndex, locale, DEFAULT_STYLE);
  }
  
	public final String getStringValue(String fieldName) throws NoSuchFieldException
	{
		return getStringValue(getParent().getFieldIndexByFieldName(fieldName));
	}

	public final String getStringValue(String fieldName, Locale locale) throws NoSuchFieldException
	{
		return getStringValue(getParent().getFieldIndexByFieldName(fieldName), locale);
	}

	public final String getStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException
	{
		return getStringValue(getParent().getFieldIndexByFieldName(fieldName), locale, style);
	}

	protected static Time convertToTime(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Time)
    {
      return (Time) value;
    }
    if (value instanceof Timestamp)
    {
      return new Time(((Timestamp) value).getTime());
    }
    if (value instanceof Long)
    {
      return new Time(((Long) value).longValue());
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getTimeValue(int)
   */
  public final Time getTimeValue(int fieldIndex) throws ClassCastException
	{
    return convertToTime(getValueInternal(fieldIndex));
	}

  public final Time getTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException
	{
		return getTimeValue(getParent().getFieldIndexByFieldName(fieldName));
	}

	protected static Date convertToDate(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Date)
    {
      return (Date) value;
    }
    if (value instanceof Timestamp)
    {
      return new Date(((Timestamp) value).getTime());
    }
    if (value instanceof Long)
    {
      return new Date(((Long) value).longValue());
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getDateValue(int)
	 */
	public final Date getDateValue(int fieldIndex) throws ClassCastException
	{
    return convertToDate(getValueInternal(fieldIndex));
	}

	public final Date getDateValue(String fieldName) throws NoSuchFieldException, ClassCastException
	{
		return getDateValue(getParent().getFieldIndexByFieldName(fieldName));
	}
	
	protected static BigDecimal convertToDecimal(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof BigDecimal)
    {
      return (BigDecimal) value;
    }
    if (value instanceof Integer)
    {
      return BigDecimal.valueOf(((Integer) value).longValue());
    }
    if (value instanceof Long)
    {
      return BigDecimal.valueOf(((Long) value).longValue());
    }
    if (value instanceof Float)
    {
      return new BigDecimal(((Float) value).floatValue());
    }
    if (value instanceof Double)
    {
      return new BigDecimal(((Double) value).doubleValue());
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getDecimalValue(int)
	 */
	public final BigDecimal getDecimalValue(int fieldIndex) throws ClassCastException
	{
    return convertToDecimal(getValueInternal(fieldIndex));
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getSaveDecimalValue(int)
   */
  public final BigDecimal getSaveDecimalValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    BigDecimal result = convertToDecimal(getValueInternal(fieldIndex));
    return result == null ? DECIMAL_0 : result;
  }
  
  public final BigDecimal getDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
	{
		return getDecimalValue(getParent().getFieldIndexByFieldName(fieldName));
	}

  public final BigDecimal getSaveDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
		return getSaveDecimalValue(getParent().getFieldIndexByFieldName(fieldName));
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getdoubleValue(int)
	 */
	public final double getdoubleValue(int fieldIndex) throws ClassCastException
	{
		Double value = getDoubleValue(fieldIndex);
		return value == null ? 0.0 : value.doubleValue();  
	}
	
	protected static Double convertToDouble(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Double)
    {
      return (Double) value;
    }
    if (value instanceof Float)
    {
      return new Double(((Float) value).doubleValue());
    }
		if (value instanceof Integer)
		{
			return new Double(((Integer) value).doubleValue());
		}
		if (value instanceof Long)
		{
			return new Double(((Long) value).doubleValue());
		}
		if (value instanceof BigDecimal)
		{
			return new Double(((BigDecimal) value).doubleValue());
		}
    throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getDoubleValue(int)
	 */
	public final Double getDoubleValue(int fieldIndex) throws ClassCastException
	{
    return convertToDouble(getValueInternal(fieldIndex));
  }

  public final double getdoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getdoubleValue(getParent().getFieldIndexByFieldName(fieldName));
  }

  public final Double getDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getDoubleValue(getParent().getFieldIndexByFieldName(fieldName));
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getfloatValue(int)
	 */
	public final float getfloatValue(int fieldIndex) throws ClassCastException
	{
		Float value = getFloatValue(fieldIndex);
		return value == null ? 0.0f : value.floatValue();
	}
	
	protected static Float convertToFloat(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Float)
		{
			return (Float) value;
		}
		if (value instanceof Double)
		{
			return new Float(((Double) value).floatValue());
		}
		if (value instanceof Integer)
		{
			return new Float(((Integer) value).floatValue());
		}
		if (value instanceof Long)
		{
			return new Float(((Long) value).floatValue());
		}
		if (value instanceof BigDecimal)
		{
			return new Float(((BigDecimal) value).floatValue());
		}
		throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getFloatValue(int)
	 */
	public final Float getFloatValue(int fieldIndex) throws ClassCastException
	{
		return convertToFloat(getValueInternal(fieldIndex));
	}

  public final float getfloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getfloatValue(getParent().getFieldIndexByFieldName(fieldName));
  }

  public final Float getFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getFloatValue(getParent().getFieldIndexByFieldName(fieldName));
  }
  
	/* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getbooleanValue(int)
   */
  public boolean getbooleanValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    Boolean value = getBooleanValue(fieldIndex);
    return value == null ? false : value.booleanValue();
  }

  protected static Boolean convertToBoolean(Object value) throws ClassCastException
  {
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Boolean)
    {
      return (Boolean) value;
    }
    if (value instanceof Integer)
    {
      return Boolean.valueOf(((Integer) value).intValue() != 0);
    }
    if (value instanceof Long)
    {
      return Boolean.valueOf(((Long) value).longValue() != 0);
    }
    throw new ClassCastException(value.getClass().toString());
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getBooleanValue(int)
   */
  public Boolean getBooleanValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException
  {
    return convertToBoolean(getValueInternal(fieldIndex));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getbooleanValue(java.lang.String)
   */
  public boolean getbooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return getbooleanValue(getParent().getFieldIndexByFieldName(fieldName));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataRecord#getBooleanValue(java.lang.String)
   */
  public Boolean getBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return getBooleanValue(getParent().getFieldIndexByFieldName(fieldName));
  }

  /* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getintValue(int)
	 */
	public final int getintValue(int fieldIndex) throws ClassCastException
	{
		Integer value = getIntegerValue(fieldIndex);
		return value == null ? 0 : value.intValue();
	}
	
	protected static Integer convertToInteger(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Integer)
    {
      return (Integer) value;
    }
    if (value instanceof Long)
    {
      return new Integer(((Long) value).intValue());
    }
    if (value instanceof BigDecimal)
    {
      return new Integer(((BigDecimal) value).intValue());
    }
    if (value instanceof Float)
    {
      return new Integer(((Float) value).intValue());
    }
    if (value instanceof Double)
    {
      return new Integer(((Double) value).intValue());
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getIntegerValue(int)
	 */
	public final Integer getIntegerValue(int fieldIndex) throws ClassCastException
	{
    return convertToInteger(getValueInternal(fieldIndex));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getintValue(java.lang.String)
   */
  public final int getintValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getintValue(getParent().getFieldIndexByFieldName(fieldName));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getIntegerValue(java.lang.String)
   */
  public final Integer getIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getIntegerValue(getParent().getFieldIndexByFieldName(fieldName));
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getlongValue(int)
	 */
	public final long getlongValue(int fieldIndex) throws ClassCastException
	{
		Long value = getLongValue(fieldIndex);
		return value == null ? 0 : value.longValue();
	}
	
	protected static Long convertToLong(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Long)
    {
      return (Long) value;
    }
    if (value instanceof Integer)
    {
      return new Long(((Integer) value).intValue());
    }
    if (value instanceof BigDecimal)
    {
      return new Long(((BigDecimal) value).longValue());
    }
    if (value instanceof Float)
    {
      return new Long(((Float) value).longValue());
    }
    if (value instanceof Double)
    {
      return new Long(((Double) value).longValue());
    }
    if (value instanceof java.util.Date)
    {
      return new Long(((java.util.Date) value).getTime());
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getLongValue(int)
	 */
	public final Long getLongValue(int fieldIndex) throws ClassCastException
	{
    return convertToLong(getValueInternal(fieldIndex));
  }

  public final long getlongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getlongValue(getParent().getFieldIndexByFieldName(fieldName));
  }

  public final Long getLongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
  	return getLongValue(getParent().getFieldIndexByFieldName(fieldName));
  }
  
	protected static Timestamp convertToTimestamp(Object value) throws ClassCastException
	{
    if (value == null)
    {  
      return null;
    }
    if (value instanceof Timestamp)
    {
      return (Timestamp) value;
    }
    if (value instanceof Date)
    {
      return new Timestamp(((Date) value).getTime());
    }
    if (value instanceof Long)
    {
      return new Timestamp(((Long) value).longValue());
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
	public final Timestamp getTimestampValue(int fieldIndex) throws ClassCastException
	{
		return convertToTimestamp(getValueInternal(fieldIndex));
	}

  public final Timestamp getTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException
	{
		return getTimestampValue(getParent().getFieldIndexByFieldName(fieldName));
	}
  
	protected static byte[] convertToBytes(Object value) throws ClassCastException
	{
	  if (value == null)
	  {
	    throw new ClassCastException("Invalid field type");
	  }
    if (value instanceof DataBinary)
    {
      if (((DataBinary) value).isNull())
        return null;
      return ((DataBinary) value).getValue();
    }
    if (value instanceof DataDocument)
    {
      if (((DataDocument) value).isNull())
        return null;
      return ((DataDocument) value).getContent();
    }
    throw new ClassCastException(value.getClass().toString());
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getBytesValue(int)
	 */
	public final byte[] getBytesValue(int fieldIndex) throws ClassCastException
	{
	  return convertToBytes(getValueInternal(fieldIndex));
	}

  public final byte[] getBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return getBytesValue(getParent().getFieldIndexByFieldName(fieldName));
  }

	public DataDocumentValue getDocumentValue(int fieldIndex) throws ClassCastException
	{
		return (DataDocumentValue) getValue(fieldIndex);
	}

  public final DataDocumentValue getDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return getDocumentValue(getParent().getFieldIndexByFieldName(fieldName));
  }
  
	protected IDataKeyValue getKeyValue(int[] keyIndices)
	{
		if (keyIndices.length == 1)
		{
			Object value = getValueInternal(keyIndices[0]);
			if (value == null)
			{
				return null;
			}
			return new IDataKeyValue(value);
		}
		else if (keyIndices.length > 1)
		{
			Object[] keyFieldValues = new Object[keyIndices.length];
			for (int i = 0; i < keyIndices.length; i++)
			{
				keyFieldValues[i] = getValueInternal(keyIndices[i]);
				if (keyFieldValues[i] == null)
				{
					return null;
				}
			}
			return new IDataKeyValue(keyFieldValues);
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName()).append("[");
		for (int i = 0; i < getFieldNumber(); i++)
		{
			if (i != 0)
			{
				buffer.append(",");
			}
			buffer.append(getValueInternal(i));
		}
		buffer.append("]");
		return buffer.toString();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.transformer.ITransformerRow#getCellNumber()
   */
  public int getCellNumber()
  {
    return this.getFieldNumber();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.transformer.ITransformerRow#getCellValue(int)
   */
  public Object getCellValue(int cell)
  {
    Object value = getValueInternal(cell);
    
    // pass thru only numbers and datetime values
    if (value instanceof Number)
      return value;
    if (value instanceof java.util.Date)
      return value;
    
    // convert all other values to string
    return getStringValue(cell);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.transformer.ITransformerRow#getCellStringValue(int, java.util.Locale)
   */
  public String getCellStringValue(int cell, Locale locale)
  {
    return getStringValue(cell, locale);
  }

}

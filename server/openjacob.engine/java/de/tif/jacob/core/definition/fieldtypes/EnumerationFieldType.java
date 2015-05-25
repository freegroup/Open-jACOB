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

package de.tif.jacob.core.definition.fieldtypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.IDataNewKeysCache;
import de.tif.jacob.core.data.impl.qbe.QBEExpression;
import de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint;
import de.tif.jacob.core.data.impl.sql.ISQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLExecutionContext;
import de.tif.jacob.core.data.impl.sql.SQLStatementBuilder;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.guielements.Caption;
import de.tif.jacob.core.definition.guielements.ComboBoxInputFieldDefinition;
import de.tif.jacob.core.definition.guielements.Dimension;
import de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorTableField;
import de.tif.jacob.core.definition.impl.jad.castor.EnumerationField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.i18n.I18N;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class EnumerationFieldType extends FieldType
{
  static public final transient String RCS_ID = "$Id: EnumerationFieldType.java,v 1.18 2010/08/11 22:32:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.18 $";

  // List<String>
  private List enumeratedValues;
  // List<String> or null
  private List labels;
  private String defaultValue;
  private int maxLength = 0;
  
  // Map<String->Integer>
  private Map enumStrToIntMap;

  // Map<String->String>
  private Map enumStrToEnumLabelMap;

	/**
	 * 
	 */
  public EnumerationFieldType(List enumeratedValues, List labels, String defaultValue)
  {
    init(enumeratedValues, labels, defaultValue);
  }
  
  public EnumerationFieldType(EnumerationField type)
  {
    List enumeratedValues = new ArrayList(type.getValueCount());
    List labels = new ArrayList(enumeratedValues.size());

    if(type.getLabelCount()==0)
    {
      for (int i=0; i < type.getValueCount(); i++ )
      {
        enumeratedValues.add(type.getValue(i));
        labels.add(type.getValue(i));
      }
    }
    else
    {
      for (int i=0; i < type.getValueCount(); i++ )
      {
        enumeratedValues.add(type.getValue(i));
        labels.add(type.getLabel(i));
      }
    }
    init(enumeratedValues,labels, type.getDefault());
  }
  
  private void init(List enumeratedValues, List labels, String defaultValue)
  {
    if (labels != null && enumeratedValues.size() != labels.size())
      throw new RuntimeException("count mismatch of i18n and value entries in enum field");
      
    this.enumeratedValues = enumeratedValues;
    this.labels = labels;
    this.defaultValue = "".equals(defaultValue) ? null : defaultValue;
    this.enumStrToIntMap = new HashMap(enumeratedValues.size());
    this.enumStrToEnumLabelMap = new HashMap(enumeratedValues.size());
    
    for (int i = 0; i < this.enumeratedValues.size(); i++)
    {
      String enumeratedValue = (String) this.enumeratedValues.get(i);
      if (enumeratedValue.length() > this.maxLength)
        this.maxLength = enumeratedValue.length();
      
      // ueberpruefen ob ein Wert doppelt in der Enum-Map vorkommt
      //
      if (null != this.enumStrToIntMap.put(enumeratedValue, new Integer(i)))
      {
        // Mike : Wenn der Fehler auftritt, dann weiss man nicht wo, deshalb
        // alle Values ausgeben
        String values = "|";
        for (int j = 0; j < this.enumeratedValues.size(); j++)
        {
          values = values + this.enumeratedValues.get(j) + "|";
        }
        throw new RuntimeException("Duplicate enumeration value in: " + values);
      }
      
      String enumeratedLabel;
      if (labels != null)
      {
        enumeratedLabel = (String) this.labels.get(i);
        if (enumeratedLabel == null || enumeratedLabel.length() == 0)
          enumeratedLabel = enumeratedValue;
      }
      else
      {
        enumeratedLabel = enumeratedValue;
      }
      this.enumStrToEnumLabelMap.put(enumeratedValue, enumeratedLabel);
    }
    // Pruefen ob der Default-Wert auch Bestandteil der Enums ist
    //
    if (this.defaultValue != null && !this.enumStrToIntMap.keySet().contains(this.defaultValue))
    {
      // Mike : Wenn der Fehler auftritt, dann weiss man nicht wo, deshalb alle
      // Values ausgeben
      String values = "|";
      for (int j = 0; j < this.enumeratedValues.size(); j++)
      {
        values = values + this.enumeratedValues.get(j) + "|";
      }
      throw new RuntimeException("Default value [" + this.defaultValue + "] is not an valid enumeration value in " + values);
    }
  }
  
  /**
   * @return Returns the defaultValue.
   */
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  public boolean containsEnumeratedValue(String value)
  {
    return mapEnumStrToInt(value) != null;
  }
  
  public int sortDataValueNotNull(Object dataValue1, Object dataValue2, Collator collator)
  {
    // sort by order of enums!
    Integer val1 = mapEnumStrToInt((String) dataValue1);
    Integer val2 = mapEnumStrToInt((String) dataValue2);
    
    if (val1 == null)
      return val2 == null ? 0 : -1;
    if (val2 == null)
      return 1;
    
    return val1.compareTo(val2);
  }
  
	public Integer mapEnumStrToInt(String str)
	{
    if (null == str)
      return null;
		return (Integer) this.enumStrToIntMap.get(str);
	}
  
  /**
   * @param datasource
   * @param sqlQuery
   * @param value
   * @throws InvalidExpressionException
   */
  public void appendSqlEnumValue(SQLDataSource dataSource, SQLStatementBuilder sqlQuery, String value) throws InvalidExpressionException
  {
	  // delegate request
    dataSource.getAdjustment().getEnumerationAdjustment().appendSqlEnumValue(this, dataSource, sqlQuery, value);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#createQBEExpression(de.tif.jacob.core.data.impl.qbe.QBEFieldConstraint)
   */
  public QBEExpression createQBEExpression(DataSource dataSource, QBEFieldConstraint constraint) throws InvalidExpressionException, IllegalArgumentException
  {
    Object value = constraint.getValue();
    if (value instanceof String)
    {
      return QBEExpression.parseEnum((String) value);
    }
    else
    {
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

  public String getEnumeratedValue(int index) throws IndexOutOfBoundsException
  {
    String enumStr = (String) this.enumeratedValues.get(index);
    if (enumStr == null)
    {
      throw new IndexOutOfBoundsException(Integer.toString(index));
    }
    return enumStr;
  }
  
//  public String getLabel(int index) throws IndexOutOfBoundsException
//  {
//    String keysStr = (String) this.labels.get(index);
//    if (keysStr == null)
//    {
//      throw new IndexOutOfBoundsException(Integer.toString(index));
//    }
//    return keysStr;
//  }

  public String getEnumeratedLabel(String enumeratedValue) throws IndexOutOfBoundsException
  {
    String label = (String) this.enumStrToEnumLabelMap.get(enumeratedValue);
    if (label == null)
    {
      // throw new RuntimeException("Value [" + enumeratedValue + "] is not an valid enumeration value in " + this.enumeratedValues);
      return enumeratedValue;
    }

    return label;
  }

  public int enumeratedValueCount()
  {
    return this.enumeratedValues.size();
  }
  
  /**
   * Returns the maximal length of this field type
   * 
   * @return maximal length or 0 for unlimited length
   */
  public int getMaxLength()
  {
    return maxLength;
  }

	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("EnumerationFieldType[");
		buffer.append("enumeratedValues = ").append(enumeratedValues);
		buffer.append("]");
		return buffer.toString();
	}
  
  public Object convertDataValueToObject(Object dataValue)
  {
    // object is not mutable -> just pass thru
		return dataValue;
  }
  
  public String convertDataValueToString(Object value, Locale locale, int style)
  {
    return convertToString((String) value, locale, style);
  }
  
  private String convertToString(String value, Locale locale, int style)
  {
    if (locale != null)
    {
      return I18N.localizeLabel(getEnumeratedLabel(value), Context.getCurrent(), locale);
    }

    // returns the value as label
    return value;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertObjectToDataValue(java.lang.Object)
	 */
	public Object convertObjectToDataValue(DataSource dataSource, Object object, Object oldValue, Locale locale) throws InvalidExpressionException, IllegalArgumentException
	{
    if (object == null)
    {
      return null;
    }
    if (object instanceof String)
    {
      if (!this.enumStrToIntMap.keySet().contains(object))
      {
      	throw new InvalidExpressionException((String) object, "Value is not an enumeration value");
      }
      return object;
    }
    throw new IllegalArgumentException(object.getClass().toString());
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#convertSqlValueToDataValue(de.tif.jacob.core.data.sql.SQLDataSource, java.sql.ResultSet, int)
	 */
	public Object convertSQLValueToDataValue(SQLDataSource datasource, ResultSet rs, int index) throws SQLException
	{
	  // delegate request
    return datasource.getAdjustment().getEnumerationAdjustment().convertSQLValueToDataValue(this, datasource, rs, index);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#getInitDataValue(de.tif.jacob.core.data.DataSource, de.tif.jacob.core.definition.ITableField)
	 */
	public Object getInitDataValue(DataSource dataSource, ITableDefinition table, ITableField field, IDataNewKeysCache newKeysCache)
	{
		return this.defaultValue;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValue(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValue(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
	  // delegate request
    context.getDataSource().getAdjustment().getEnumerationAdjustment().setSQLValue(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForInsert(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForInsert(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
	  // delegate request
    context.getDataSource().getAdjustment().getEnumerationAdjustment().setSQLValueForInsert(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#setSqlValueForUpdate(de.tif.jacob.core.data.sql.SQLExecutionContext, java.sql.PreparedStatement, int, java.lang.Object)
	 */
	public void setSQLValueForUpdate(SQLExecutionContext context, PreparedStatement statement, int index, Object dataValue) throws SQLException
	{
	  // delegate request
    context.getDataSource().getAdjustment().getEnumerationAdjustment().setSQLValueForUpdate(this, context, statement, index, dataValue);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#toJacob(de.tif.jacob.core.jad.castor.TableField)
	 */
  public void toJacob(CastorTableField tableField, ConvertToJacobOptions options)
	{
    EnumerationField type = new EnumerationField();
    type.setDefault(this.defaultValue);
    for (int i=0; i < this.enumeratedValues.size(); i++)
    {
      type.addValue((String)this.enumeratedValues.get(i));
    }
    if (this.labels != null)
    {
      for (int i = 0; i < this.labels.size(); i++)
      {
        type.addLabel((String) this.labels.get(i));
      }
    }
    tableField.getCastorTableFieldChoice().setEnumeration(type);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.FieldType#createDefaultInputField(java.lang.String, de.tif.jacob.core.definition.guielements.Dimension, boolean, int, de.tif.jacob.core.definition.guielements.Caption, de.tif.jacob.core.definition.ITableAlias, de.tif.jacob.core.definition.ITableField)
	 */
	public LocalInputFieldDefinition createDefaultInputField(String name, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField)
	{
		return new ComboBoxInputFieldDefinition(name, null, null, null, (String[])enumeratedValues.toArray(new String[0]), false, false, false,false, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, null);
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLType()
   */
  public int getSQLType(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getEnumerationAdjustment().getSQLType(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSize()
   */
  public int getSQLSize(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getEnumerationAdjustment().getSQLSize(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getSQLDecimalDigits(de.tif.jacob.core.data.impl.sql.SQLDataSource)
   */
  public int getSQLDecimalDigits(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getEnumerationAdjustment().getSQLDecimalDigits(this, dataSource);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.FieldType#getDBDefaultValue()
   */
  public String getSQLDefaultValue(ISQLDataSource dataSource)
  {
	  // delegate request
    return dataSource.getAdjustment().getEnumerationAdjustment().getSQLDefaultValue(this, dataSource);
  }
}

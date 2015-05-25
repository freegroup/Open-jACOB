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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Locale;

import de.tif.jacob.core.definition.ITableAlias;

/**
 * A data record represents the joint behaviour of instances of
 * {@link IDataBrowserRecord} and {@link IDataTableRecord}.
 * <p>
 * 
 * There are multiple getter methods for accessing record field values. The
 * following table shows how jACOB field types are matched to java types. If a
 * field value could not converted to the requested java type, the respective
 * getter method would throw a <code>ClassCastException</code>.
 * <p>
 * <table BORDER="1">
 * <tr>
 * <td>To \ From</td>
 * <td align="center" colspan=14><b>jACOB field type </b></td>
 * </tr>
 * <tr>
 * <td><b>Java type(s) </b></td>
 * <td>Text</td>
 * <td>Boolean</td>
 * <td>Integer</td>
 * <td>Long</td>
 * <td>Decimal</td>
 * <td>Float</td>
 * <td>Double</td>
 * <td>Time</td>
 * <td>Date</td>
 * <td>Timestamp</td>
 * <td>Enumeration</td>
 * <td>Longtext</td>
 * <td>Binary</td>
 * <td>Document</td>
 * </tr>
 * <tr>
 * <td>java.lang.String</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td align="center"><b>X</b></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.lang.Boolean</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>int, java.lang.Integer</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>long, java.lang.Long</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.math.BigDecimal</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>float, java.lang.Float</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">x</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>double, java.lang.Double</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">x</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.sql.Time</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * <td>&nbsp;</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.sql.Date</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.sql.Timestamp</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center"><b>X </b></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>byte[]</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * <td align="center">X</td>
 * </tr>
 * <tr>
 * <td>{@link DataDocumentValue}</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * </tr>
 * </table> <br>
 * A bold <b>X </b> indicates the default java type for a given jACOB field
 * type. The default java type is returned when calling the generic
 * {@link #getValue(int)}method.
 * <p>
 * A lower x indicates that the conversion is possible but at the cost of lost
 * information.
 * 
 * @author andreas
 */
public interface IDataRecord
{
  /**
   * Constant for raw style output.
   * 
   * @see #getStringValue(int, Locale, int)
   * @since 2.10
   */
  public static final int RAW_STYLE = 0;
  
  /**
   * Constant for short style output.
   * 
   * @see #getStringValue(int, Locale, int)
   */
  public static final int SHORT_STYLE = 1;
  
  /**
   * Constant for medium style output.
   * 
   * @see #getStringValue(int, Locale, int)
   */
  public static final int MEDIUM_STYLE = 2;
  
  /**
   * Constant for long style output.
   * 
   * @see #getStringValue(int, Locale, int)
   */
  public static final int LONG_STYLE = 3;
  
  /**
   * Constant for default style output.
   * 
   * @see #getStringValue(int, Locale, int)
   */
  public static final int DEFAULT_STYLE = MEDIUM_STYLE;
  
	/**
   * Returns the primary key value of this record.
   * 
   * @return the primary key value or <code>null</code> if no primary key
   *         exists for this type of record.
   */
	public IDataKeyValue getPrimaryKeyValue();

	/**
   * Returns the record id of this record, i.e. an id which unambiguously
   * identifies a record in any table.
   * 
   * @return the record id
   */
	public IDataRecordId getId();

  /**
   * Returns the related data accessor instance.
   * 
   * @return the data accessor
   */
  public IDataAccessor getAccessor();

  /**
   * Returns the table alias this record belongs to.
   * 
   * @return the table alias
   */
  public ITableAlias getTableAlias();

	/**
   * Returns the number of fields of this record.
   * 
   * @return the number of fields
   */
  public int getFieldNumber();

	/**
   * Returns the field value specified by its index.
   * <p>
   * The java type (class) of the field value returned is determined by the
   * field type. Please see {@link IDataRecord}!
   * 
   * @param fieldIndex
   *          the field index of the value
   * @return The field value or <code>null</code>
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   */
  public Object getValue(int fieldIndex) throws IndexOutOfBoundsException;
	
  /**
   * Returns the field value specified by its name.
   * <p>
   * The java type (class) of the field value returned is determined by the
   * field type. Please see {@link IDataRecord}!
   * 
	 * @param fieldName
	 *          the name of the field
   * @return The field value or <code>null</code>
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
	 */
  public Object getValue(String fieldName) throws NoSuchFieldException;

  /**
   * Checks whether the field value specified by its index is <code>null</code>,
   * i.e. the field value has not been set.
   * <p>
   * Note: This method is equivalent to
   * <code>null == {@link #getValue(int)}</code>, but could be much more
   * efficient in case of document, binary or long text values. This is because
   * these values need not necessarily being retrieved from data source.
   * 
   * @param fieldIndex
   *          the field index of the value
   * @return <code>true</code> if the field value is <code>null</code>,
   *         otherwise <code>false</code>
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   */
  public boolean hasNullValue(int fieldIndex) throws IndexOutOfBoundsException;
  
  /**
   * Checks whether the field value specified by its name is <code>null</code>,
   * i.e. the field value has not been set.
   * <p>
   * Note: This method is equivalent to
   * <code>null == {@link #getValue(String)}</code>, but could be much more
   * efficient in case of document, binary or long text values. This is because
   * these values need not necessarily being retrieved from data source.
   * 
   * @param fieldName
   *          the name of the field
   * @return <code>true</code> if the field value is <code>null</code>,
   *         otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @since 2.10           
   */
  public boolean hasNullValue(String fieldName) throws NoSuchFieldException;
  
  /**
	 * Similar behaviour than {@link #getStringValue(int)} except that an empty
	 * string is returned in case the value is <code>null</code>.
	 * 
	 * @param fieldIndex
	 *          the field index
	 * @return the field value as string
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
	 */
	public String getSaveStringValue(int fieldIndex) throws IndexOutOfBoundsException;

  /**
	 * Similar behaviour than {@link #getStringValue(int, Locale)} except that an empty
	 * string is returned in case the value is <code>null</code>.
	 * 
	 * @param fieldIndex
	 *          the field index
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
	 * @return the field value as string
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
	 */
	public String getSaveStringValue(int fieldIndex, Locale locale) throws IndexOutOfBoundsException;

  /**
	 * Similar behaviour than {@link #getStringValue(int, Locale, int)} except that an empty
	 * string is returned in case the value is <code>null</code>.
	 * 
	 * @param fieldIndex
	 *          the field index
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
   * @param style
   *          the style to use
	 * @return the field value as string
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @see #DEFAULT_STYLE
   * @see #SHORT_STYLE
   * @see #MEDIUM_STYLE
   * @see #LONG_STYLE
	 */
	public String getSaveStringValue(int fieldIndex, Locale locale, int style) throws IndexOutOfBoundsException;

  /**
	 * Similar behaviour than {@link #getStringValue(String)} except that an
	 * empty string is returned in case the value is <code>null</code>.
	 * 
	 * @param fieldName
	 *          the name of the field
	 * @return the field value as string
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
	 */
	public String getSaveStringValue(String fieldName) throws NoSuchFieldException;

  /**
	 * Similar behaviour than {@link #getStringValue(String, Locale)} except that an
	 * empty string is returned in case the value is <code>null</code>.
	 * 
	 * @param fieldName
	 *          the name of the field
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
	 * @return the field value as string
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
	 */
	public String getSaveStringValue(String fieldName, Locale locale) throws NoSuchFieldException;

  /**
	 * Similar behaviour than {@link #getStringValue(String, Locale, int)} except that an
	 * empty string is returned in case the value is <code>null</code>.
	 * 
	 * @param fieldName
	 *          the name of the field
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
   * @param style
   *          the style to use
	 * @return the field value as string
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @see #DEFAULT_STYLE
   * @see #SHORT_STYLE
   * @see #MEDIUM_STYLE
   * @see #LONG_STYLE
	 */
	public String getSaveStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException;

	/**
   * Returns the default string representation of the specified field value.
   * <p>
   * The following table shows the format of the defaut string presentation
   * for the different jACOB field types:
   * <p>
   * <table BORDER="1">
   * <tr>
   * <td><b>Field type </b></td>
   * <td align="center"><b>Format </b></td>
   * <td align="center"><b>Example </b></td>
   * </tr>
   * <tr>
   * <td>Text</td>
   * <td>No specific representation since text values are already strings</td>
   * <td>Hello world</td>
   * </tr>
   * <tr>
   * <td>Integer</td>
   * <td>Standard decimal format without thousands separators</td>
   * <td>101234</td>
   * </tr>
   * <tr>
   * <td>Long</td>
   * <td>Standard decimal format without thousands separators</td>
   * <td>-1234567890</td>
   * </tr>
   * <tr>
   * <td>Decimal</td>
   * <td>Standard decimal format without thousands separators <br>and a dot as
   * decimal separator</td>
   * <td>-123456.7890</td>
   * </tr>
   * <tr>
   * <td>Float</td>
   * <td>as Decimal</td>
   * <td>0.00123456</td>
   * </tr>
   * <tr>
   * <td>Decimal</td>
   * <td>as Decimal</td>
   * <td>-123000.25</td>
   * </tr>
   * <tr>
   * <td>Time</td>
   * <td>HH:mm without seconds</td>
   * <td>21:15</td>
   * </tr>
   * <tr>
   * <td>Date</td>
   * <td>yyyy-MM-dd</td>
   * <td>2004-09-21</td>
   * </tr>
   * <tr>
   * <td>Timestamp</td>
   * <td>Depending on the resolution as: <br>
   * yyyy-MM-dd HH:mm <br>
   * yyyy-MM-dd HH:mm:ss <br>
   * yyyy-MM-dd HH:mm:ss.SSS</td>
   * <td>2004-09-21 21:15:10.320</td>
   * </tr>
   * <tr>
   * <td>Enumeration</td>
   * <td>The enumeration value</td>
   * <td>Confirmed</td>
   * </tr>
   * <tr>
   * <td>Longtext</td>
   * <td>The complete long text</td>
   * <td>The quick brown fox jumps over the lazy dog ...</td>
   * </tr>
   * <tr>
   * <td>Binary</td>
   * <td>Throws a UnsupportedOperationException</td>
   * <td>&nbsp;</td>
   * </tr>
   * </tr>
   * <td>Document</td>
   * <td>The document name, if existing</td>
   * <td>word.doc</td>
   * <tr></table> <br>
   * <p>
   * Note: This method is equivalent to {@link #getStringValue(int, Locale)},
   * if the locale is set to <code>null</code>.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as string or <code>null</code> if the value does
   *         not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   */
  public String getStringValue(int fieldIndex) throws IndexOutOfBoundsException;

  /**
   * Returns the specified field value as a string by means of considering the
   * given locale.
   * <p>
   * For example: This method would return "04/15/2005 12:59:00 AM" for a
   * timestamp field, if <code>locale</code> is set to <code>Locale.US</code>.
   * <p>
   * Note: This method is equivalent to
   * {@link #getStringValue(int, Locale, int)}, if the style is set to
   * {@link #DEFAULT_STYLE}.
   * 
   * @param fieldIndex
   *          the field index
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
   * @return the field value as string or <code>null</code> if the value does
   *         not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   */
  public String getStringValue(int fieldIndex, Locale locale) throws IndexOutOfBoundsException;

  /**
   * Returns the specified field value as a string by means of considering the
   * given locale and style.
   * <p>
   * For example: Setting the locale to <code>Locale.GERMAN</code> for a
   * decimal field would return "12345,678" for style {@link #SHORT_STYLE} and
   * "12.345,678" for style {@link #LONG_STYLE}.
   * 
   * @param fieldIndex
   *          the field index
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
   * @param style
   *          the style to use
   * @return the field value as string or <code>null</code> if the value does
   *         not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @see #DEFAULT_STYLE
   * @see #SHORT_STYLE
   * @see #MEDIUM_STYLE
   * @see #LONG_STYLE
   */
  public String getStringValue(int fieldIndex, Locale locale, int style) throws IndexOutOfBoundsException;
  
	/**
   * Returns the default string representation of the specified field value.
   * <p>
   * For further information see {@link #getStringValue(int)}.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as string or <code>null</code> if the value does
   *         not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
	public String getStringValue(String fieldName) throws NoSuchFieldException;

  /**
   * Returns the specified field value as a string by means of considering the
   * given locale.
   * <p>
   * For example: This method would return "04/15/2005 12:59:00 AM" for a
   * timestamp field, if <code>locale</code> is set to <code>Locale.US</code>.
   * <p>
   * Note: This method is equivalent to
   * {@link #getStringValue(String, Locale, int)}, if the style is set to
   * {@link #DEFAULT_STYLE}.
   * 
   * @param fieldName
   *          the name of the field
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
   * @return the field value as string or <code>null</code> if the value does
   *         not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public String getStringValue(String fieldName, Locale locale) throws NoSuchFieldException;

  /**
   * Returns the specified field value as a string by means of considering the
   * given locale and style.
   * <p>
   * For example: Setting the locale to <code>Locale.GERMAN</code> for a
   * decimal field would return "12345,678" for style {@link #SHORT_STYLE} and
   * "12.345,678" for style {@link #LONG_STYLE}.
   * 
	 * @param fieldName
	 *          the name of the field
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          string presentation (see {@link #getStringValue(int)}).
   * @param style
   *          the style to use
   * @return the field value as string or <code>null</code> if the value does
   *         not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @see #DEFAULT_STYLE
   * @see #SHORT_STYLE
   * @see #MEDIUM_STYLE
   * @see #LONG_STYLE
   */
  public String getStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException;

  /**
   * Returns the specified field value in <code>Time</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Time</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Time getTimeValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Time</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Time</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Time getTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Date</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Date</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Date getDateValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Date</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Date</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
	public Date getDateValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>BigDecimal</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>BigDecimal</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public BigDecimal getDecimalValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>BigDecimal</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>BigDecimal</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public BigDecimal getDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Similar behaviour than {@link #getDecimalValue(int)} except that
   * <code>0.0</code> is returned in case the value is <code>null</code>.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>BigDecimal</code> format or
   *         <code>0.0</code> if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public BigDecimal getSaveDecimalValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Similar behaviour than {@link #getDecimalValue(String)} except that
   * <code>0.0</code> is returned in case the value is <code>null</code>.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>BigDecimal</code> format or
   *         <code>0.0</code> if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public BigDecimal getSaveDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a <code>double</code> value.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a <code>double</code> value or <code>0.0</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public double getdoubleValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Double</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Double</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Double getDoubleValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value as a <code>double</code> value.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a <code>double</code> value or <code>0.0</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public double getdoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Double</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Double</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Double getDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a <code>float</code> value.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a <code>float</code> value or <code>0.0</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public float getfloatValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Float</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Float</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Float getFloatValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value as a <code>float</code> value.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a <code>float</code> value or <code>0.0</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public float getfloatValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Float</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Float</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Float getFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a <code>boolean</code> value.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a <code>boolean</code> value or <code>false</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   * @since 2.6           
   */
  public boolean getbooleanValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value as a <code>boolean</code> value.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a <code>boolean</code> value or <code>false</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   * @since 2.6           
   */
  public boolean getbooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Boolean</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Boolean</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   * @since 2.6           
   */
  public Boolean getBooleanValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Boolean</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Boolean</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   * @since 2.6           
   */
  public Boolean getBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a <code>int</code> value.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a <code>int</code> value or <code>0</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public int getintValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value as a <code>int</code> value.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a <code>int</code> value or <code>0</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public int getintValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Integer</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Integer</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Integer getIntegerValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Integer</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Integer</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Integer getIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a <code>long</code> value.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a <code>long</code> value or <code>0</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public long getlongValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value in <code>Long</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Long</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Long getLongValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;

  /**
   * Returns the specified field value as a <code>long</code> value.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a <code>long</code> value or <code>0</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public long getlongValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Long</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Long</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Long getLongValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value in <code>Timestamp</code> format.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value in <code>Timestamp</code> format or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Timestamp getTimestampValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns the specified field value in <code>Timestamp</code> format.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value in <code>Timestamp</code> format or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Timestamp getTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a <code>byte[]</code> array.
   * <p>
   * Attention: This method does not return a copy. So take care when modifying
   * the result array.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a <code>byte[]</code> array or
   *         <code>null</code> if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java
   *           type. For valid conversions see {@link IDataRecord}.
   */
	public byte[] getBytesValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns the specified field value as a <code>byte[]</code> array.
   * <p>
   * Attention: This method does not return a copy. So take care when modifying
   * the result array.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a <code>byte[]</code> array or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
  public byte[] getBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified field value as a {@link DataDocumentValue} value.
   * 
   * @param fieldIndex
   *          the field index
   * @return the field value as a {@link DataDocumentValue} or <code>null</code>
   *         if the value does not exist.
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= fieldIndex < {@link #getFieldNumber()}</code> is
   *           not fulfilled
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
	public DataDocumentValue getDocumentValue(int fieldIndex) throws IndexOutOfBoundsException, ClassCastException;
	
  /**
   * Returns the specified field value as a {@link DataDocumentValue} value.
   * 
   * @param fieldName
   *          the name of the field
   * @return the field value as a {@link DataDocumentValue} or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
	public DataDocumentValue getDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException;
	
}

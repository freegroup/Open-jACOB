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

import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;

/**
 * Data table records represent records created when populating instances of
 * {@link IDataTable}. The structure of data table records is determined by the
 * table definition, i.e. {@link ITableDefinition}, of the table alias the
 * respective data table is based on.
 * <p>
 * In contrast to instances of {@link IDataBrowserRecord} data table records
 * could be modified.
 * <p>
 * There are multiple setter methods for modifying record field values. The
 * following tables shows which java types are accepted as input for setting the
 * value of a record field depending on its jACOB field type. If a field value
 * is not accepted as input, the respective setter method would throw a
 * <code>IllegalArgumentException</code>.
 * <p>
 * <table BORDER="1">
 * <tr>
 * <td>input \ for</td>
 * <td align="center" colspan=14><b>jACOB field type</b></td>
 * </tr>
 * <tr>
 * <td><b>Java type(s)</b></td>
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
 * <td align="center"><b>x</b></td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * <td align="center"><b>x </b></td>
 * <td align="center"><b>X</b></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>boolean, java.lang.Boolean</td>
 * <td align="center">x</td>
 * <td align="center"><b>X</b></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>int, java.lang.Integer</td>
 * <td align="center">x</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>long, java.lang.Long</td>
 * <td align="center">x</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center"><b>X</b></td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.math.BigDecimal</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>float, java.lang.Float</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center"><b>X </b></td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>double, java.lang.Double</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center"><b>X </b></td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.sql.Time</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.sql.Date</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.util.Date</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>java.sql.Timestamp</td>
 * <td align="center">x</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td align="center">X</td>
 * <td align="center"><b>X </b></td>
 * <td>&nbsp;</td>
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>byte[]</td>
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
 * <td align="center">X</td>
 * <td align="center"><b>X </b></td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>{@link DataDocumentValue}</td>
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
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td align="center"><b>X </b></td>
 * </tr>
 * <tr>
 * <td>java.lang.Object</td>
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
 * <td align="center">X</td>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * </tr>
 * </table> <br>
 * A bold <b>X </b> indicates the default java type for a given jACOB field
 * type. The default java type is returned when calling the generic
 * {@link #getValue(int)} or {@link #getOldValue(int)} methods.
 * <p>
 * A lower x indicates that the java type is accepted as input but a
 * {@link InvalidExpressionException} could occur. This is the case if for
 * example a String value is too long or could not be converted to a valid
 * timestamp value.
 * <p>
 * Data table record instances could be obtained by means of
 * <li>{@link IDataTable#getRecord(int)}
 * <li>{@link IDataTable#getSelectedRecord()}
 * <li>{@link IDataTable#newRecord(IDataTransaction)}
 * <li>{@link IDataTable#reloadSelectedRecord()}
 * <li>{@link IDataTableRecord#getLinkedRecord(ITableAlias)}
 * <li>{@link IDataTableRecord#getLinkedRecord(String)}
 * <li>{@link IDataAccessor#cloneRecord(IDataTransaction, IDataTableRecord)}
 * <li>
 * {@link IDataAccessor#cloneRecord(IDataTransaction, IDataTableRecord, ITableAlias)}
 * <li>
 * {@link IDataAccessor#cloneRecord(IDataTransaction, IDataTableRecord, String)}
 * 
 * @author andreas
 */
public interface IDataTableRecord extends IDataRecord
{
	/**
	 * Returns the parent data table.
	 * 
	 * @return the parent data table
	 */
	public IDataTable getTable();
	
  /**
   * Returns the field value specified by the given table field instance.
   * <p>
   * The java type (class) of the field value returned is determined by the
   * field type. Please see {@link IDataRecord}!
   * 
	 * @param field
	 *          the table field to get the value for
   * @return The field value or <code>null</code>
	 */
  public Object getValue(ITableField field);
  
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
   */
  public boolean hasNullValue(String fieldName) throws NoSuchFieldException;
	
  /**
   * Sets the value of a record field to the given String (expression). The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws InvalidExpressionException
   *           if the field value is an invalid expression which can not be
   *           resolved
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
	public boolean setStringValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException;


  /**
   * Sets the value of a record field to the given String (expression). If
   * necessary, the String will be parsed by means of considering the given
   * locale.
   * <p>
   * For example: When setting <code>value</code> to "04/15/2005 12:59:00 AM"
   * for a timestamp field, the execution of this method would be successful, if
   * the <code>locale</code> is set to <code>Locale.US</code>.
   * Nevertheless, a <code>InvalidExpressionException</code> would be thrown,
   * if the <code>locale</code> is set to <code>Locale.GERMANY</code>
   * <p>
   * The value is not saved to the data source until
   * {@link IDataTransaction#commit()}is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          locale of the respective application
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws InvalidExpressionException
   *           if the field value is an invalid expression which can not be
   *           resolved
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
	public boolean setStringValue(IDataTransaction transaction, String fieldName, String value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field to the given String (expression). The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * <p>
   * In contrast to {@link #setStringValue(IDataTransaction, String, String)}
   * the given String value will be truncated, if the maximum field length of a
   * text field is exceeded.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws InvalidExpressionException
   *           if the field value is an invalid expression which can not be
   *           resolved
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
	public boolean setStringValueWithTruncation(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException;

	/**
   * Sets the value of a record field to the given String (expression). The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * <p>
   * In contrast to {@link #setStringValue(IDataTransaction, String, String, Locale)}
   * the given String value will be truncated, if the maximum field length of a
   * text field is exceeded.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          locale of the respective application
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws InvalidExpressionException
   *           if the field value is an invalid expression which can not be
   *           resolved
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
	 */
	public boolean setStringValueWithTruncation(IDataTransaction transaction, String fieldName, String value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a date or timestamp record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setDateValue(IDataTransaction transaction, String fieldName, java.util.Date value) 
  	throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a date or timestamp record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param timeMillis
   *          milliseconds since January 1, 1970, 00:00:00 GMT not to exceed the
   *          milliseconds representation for the year 8099. A negative number
   *          indicates the number of milliseconds before January 1, 1970,
   *          00:00:00 GMT.
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
	public boolean setDateValue(IDataTransaction transaction, String fieldName, long timeMillis)
		throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a decimal record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setDecimalValue(IDataTransaction transaction, String fieldName, BigDecimal value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field to the given <code>double</code> value. The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setDoubleValue(IDataTransaction transaction, String fieldName, double value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field to the given <code>float</code> value. The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setFloatValue(IDataTransaction transaction, String fieldName, float value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field to the given <code>int</code> value. The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setIntValue(IDataTransaction transaction, String fieldName, int value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field to the given <code>boolean</code> value. The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   * @since 2.6           
   */
  public boolean setBooleanValue(IDataTransaction transaction, String fieldName, boolean value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field to the given <code>long</code> value. The
   * value is not saved to the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setLongValue(IDataTransaction transaction, String fieldName, long value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a time record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setTimeValue(IDataTransaction transaction, String fieldName, Time value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a timestamp record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setTimestampValue(IDataTransaction transaction, String fieldName, Timestamp value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a timestamp record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param timeMillis
   *          milliseconds since January 1, 1970, 00:00:00 GMT not to exceed the
   *          milliseconds representation for the year 8099. A negative number
   *          indicates the number of milliseconds before January 1, 1970,
   *          00:00:00 GMT.
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setTimestampValue(IDataTransaction transaction, String fieldName, long timeMillis) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Appends the given value to the field value of a long text record field. The value is not saved to the
   * data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to append
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the record field is not a long text record field
   */
  public void appendLongTextValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Prepends the given value to the field value of a long text record field. The value is not saved to the
   * data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to prepend
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the record field is not a long text record field
   */
	public void prependLongTextValue(IDataTransaction transaction, String fieldName, String value) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;
  
  /**
   * Sets the value of a binary record field. The value is not saved
   * to the data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setBinaryValue(IDataTransaction transaction, String fieldName, byte[] value) throws NoSuchFieldException, RecordLockedException;

  /**
   * Sets the value of a document record field. The value is not saved to the
   * data source until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param documentValue
   *          the document value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the record field is not a document record field
   */
  public boolean setDocumentValue(IDataTransaction transaction, String fieldName, DataDocumentValue documentValue) throws NoSuchFieldException, RecordLockedException, IllegalArgumentException;

  /**
   * Sets the value of a record field. The value is not saved to the data source
   * until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param field
	 *          the table field to set the value for
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws InvalidExpressionException
   *           if the field value is an invalid expression which can not be
   *           resolved
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setValue(IDataTransaction transaction, ITableField field, Object value) 
  	throws InvalidExpressionException, RecordLockedException, IllegalArgumentException;
  
  /**
   * Sets the value of a record field. The value is not saved to the data source
   * until {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fieldName
   *          the name of the field
   * @param value
   *          the field value to set or <code>null</code> to reset the field
   *          value
   * @return <code>true</code> if the field value has been effectively
   *         changed, otherwise <code>false</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws InvalidExpressionException
   *           if the field value is an invalid expression which can not be
   *           resolved
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws IllegalArgumentException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataTableRecord}.
   */
  public boolean setValue(IDataTransaction transaction, String fieldName, Object value) 
  	throws NoSuchFieldException, InvalidExpressionException, RecordLockedException, IllegalArgumentException;
  
  /**
   * Appends the given note to the modification history of this record. The note
   * is not saved to the data source until {@link IDataTransaction#commit()} is
   * called.
   * <p>
   * Attention: Multiple notes can be appended to the history for the same
   * transaction by means of calling this method multiple times. Hence, a call
   * of this method does not overwrite previous calls of this method.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param note
   *          the note to append to the modification history
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   */
  public void appendToHistory(IDataTransaction transaction, String note) throws RecordLockedException;
  
  public String getBinaryReference(String fieldName) throws NoSuchFieldException, ClassCastException;
  
  public String getLongTextReference(String fieldName) throws NoSuchFieldException, ClassCastException;
  
  public String getDocumentReference(String fieldName) throws NoSuchFieldException, ClassCastException;
  
	/**
	 * Returns the old field value, i.e. the value returned reflects the value
	 * within the persistent data store. If the record has not been manipulated
	 * within the current transaction or no current transaction exists, this
	 * method would return the same value than {@link #getValue(int)}.
	 * <p>
	 * For new records the old value is always <code>null</code>!
	 * <p>
	 * Note: This method is especially useful in hooks on data layer to invoke
	 * additional actions on value changes.
	 * 
	 * @param fieldIndex
	 *          the field index of the value
	 * @return The old field value or <code>null</code>
	 */
	public Object getOldValue(int fieldIndex);
	
  /**
	 * Returns the old field value, i.e. the value returned reflects the value
	 * within the persistent data store. If the record has not been manipulated
	 * within the current transaction or no current transaction exists, this
	 * method would return the same value than {@link #getValue(String)}.
	 * <p>
	 * For new records the old value is always <code>null</code>!
	 * <p>
	 * Note: This method is especially useful in hooks on data layer to invoke
	 * additional actions on value changes.
   * 
   * @param fieldName
   *          the field name of the value
	 * @return The old field value or <code>null</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public Object getOldValue(String fieldName) throws NoSuchFieldException;
  
  /**
   * This method has the same behaviour as {@link #getOldStringValue(int)} except
   * that the field value is specified by name.
   * 
   * @param fieldName
   *          the field name of the value
   * @return The old field value as string or <code>null</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public String getOldStringValue(String fieldName) throws NoSuchFieldException;

  /**
   * This method has the same behaviour as {@link #getOldStringValue(int)}
   * except that the field value is specified by name and the given locale is
   * consider. For further information concerning locale handling see
   * {@link #getStringValue(String, Locale)}.
   * 
   * @param fieldName
   *          the name of the field
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          locale of the respective application
   * @return the old field value as string or <code>null</code> if the value does
   *         not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public String getOldStringValue(String fieldName, Locale locale) throws NoSuchFieldException;
  
  /**
   * This method has the same behaviour as {@link #getOldStringValue(int)}
   * except that the field value is specified by name and the given locale is
   * consider. For further information concerning locale handling see
   * {@link #getStringValue(String, Locale)}.
   * 
	 * @param fieldName
	 *          the name of the field
   * @param locale
   *          the locale to consider or <code>null</code> to use default
   *          locale of the respective application
   * @param style
   *          the style to use
   * @return the old field value as string or <code>null</code> if the value does
   *         not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   */
  public String getOldStringValue(String fieldName, Locale locale, int style) throws NoSuchFieldException;
  
  /**
	 * Similar behaviour than {@link #getOldStringValue(String)} except that an
	 * empty string is returned in case the value is <code>null</code>.
   * <p>
   * For new records the value returned is always an empty string!
   * 
	 * @param fieldName
	 *          the name of the field
	 * @return the old field value as string
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
	 */
	public String getOldSaveStringValue(String fieldName) throws NoSuchFieldException;

  /**
   * This method has the same behaviour as {@link #getOldValue(int)} except
   * that the field value is returned as default string representation.
   * 
	 * @param fieldIndex
	 *          the field index of the value
   * @return The old field value as string or <code>null</code>
   */
  public String getOldStringValue(int fieldIndex);
  
  /**
   * Returns the specified old field value as a <code>boolean</code> value, i.e.
   * the value returned reflects the value within the persistent data store. If
   * the record has not been manipulated within the current transaction or no
   * current transaction exists, this method would return the same value than
   * {@link #getbooleanValue(String)}.
   * <p>
   * For new records the old value is always <code>false</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a <code>boolean</code> value or <code>false</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java
   *           type. For valid conversions see {@link IDataRecord}.
   * @since 2.6           
   */
  public boolean getOldbooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Boolean</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getBooleanValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Boolean</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   * @since 2.6           
   */
  public Boolean getOldBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value as a <code>int</code> value, i.e.
   * the value returned reflects the value within the persistent data store. If
   * the record has not been manipulated within the current transaction or no
   * current transaction exists, this method would return the same value than
   * {@link #getintValue(String)}.
   * <p>
   * For new records the old value is always <code>0</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a <code>int</code> value or <code>0</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java
   *           type. For valid conversions see {@link IDataRecord}.
   */
  public int getOldintValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Integer</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getIntegerValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Integer</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Integer getOldIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value as a <code>long</code> value, i.e.
   * the value returned reflects the value within the persistent data store. If
   * the record has not been manipulated within the current transaction or no
   * current transaction exists, this method would return the same value than
   * {@link #getlongValue(String)}.
   * <p>
   * For new records the old value is always <code>0</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a <code>long</code> value or <code>0</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java
   *           type. For valid conversions see {@link IDataRecord}.
   */
  public long getOldlongValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Long</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getLongValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Long</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Long getOldLongValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value as a <code>float</code> value, i.e.
   * the value returned reflects the value within the persistent data store. If
   * the record has not been manipulated within the current transaction or no
   * current transaction exists, this method would return the same value than
   * {@link #getfloatValue(String)}.
   * <p>
   * For new records the old value is always <code>0.0</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a <code>float</code> value or <code>0.0</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java
   *           type. For valid conversions see {@link IDataRecord}.
   */
  public float getOldfloatValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Float</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getFloatValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Float</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Float getOldFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value as a <code>double</code> value, i.e.
   * the value returned reflects the value within the persistent data store. If
   * the record has not been manipulated within the current transaction or no
   * current transaction exists, this method would return the same value than
   * {@link #getdoubleValue(String)}.
   * <p>
   * For new records the old value is always <code>0.0</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a <code>double</code> value or <code>0.0</code>
   *         if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java
   *           type. For valid conversions see {@link IDataRecord}.
   */
  public double getOlddoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Double</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getDoubleValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Double</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Double getOldDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>BigDecimal</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getDecimalValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>BigDecimal</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public BigDecimal getOldDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Similar behaviour than {@link #getOldDecimalValue(String)} except that
   * <code>0.0</code> is returned in case the value is <code>null</code>.
   * <p>
   * For new records the value returned is always <code>0.0</code>!
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>BigDecimal</code> format or
   *         <code>0.0</code> if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public BigDecimal getOldSaveDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Time</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getTimeValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Time</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Time getOldTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Date</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getDateValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Date</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Date getOldDateValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value in <code>Timestamp</code> format,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getTimestampValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value in <code>Timestamp</code> format or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public Timestamp getOldTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value as a <code>byte[]</code> array,
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getBytesValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a <code>byte[]</code> array or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested format.
   *           For valid conversions see {@link IDataRecord}.
   */
  public byte[] getOldBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException;

  /**
   * Returns the specified old field value as a {@link DataDocumentValue},
   * i.e. the value returned reflects the value within the persistent data
   * store. If the record has not been manipulated within the current
   * transaction or no current transaction exists, this method would return the
   * same value than {@link #getDocumentValue(String)}.
   * <p>
   * For new records the old value is always <code>null</code>!
   * <p>
   * Note: This method is especially useful in hooks on data layer to invoke
   * additional actions on value changes.
   * 
   * @param fieldName
   *          the name of the field
   * @return the old field value as a {@link DataDocumentValue} or <code>null</code>
   *         if the value does not exist.
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
   * @throws ClassCastException
   *           if the field value can not be converted to the requested java type.
   *           For valid conversions see {@link IDataRecord}.
   */
	public DataDocumentValue getOldDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException;
	
	/**
	 * Checks whether any field value has been <b>effectively</b>
	 * changed within the current transaction. If the record has not been
	 * manipulated within the current transaction or no current transaction
	 * exists, <code>false</code> is returned in any case. <br>
	 * <li>New record: An effective change exists, if a field value unequals
	 * <code>null</code>.
	 * <li>Updated record: An effective change exists, if the field value
	 * unequals the value stored in the corresponding datasource. I.e. if a value
	 * is first changed from A to B and then back from B to A within the same
	 * transaction, no effective change exists!
	 * <p>
	 * Note: If a field value has been changed from <code>null</code> to a
	 * "regular" value, <code>true</code> is returned.
	 * 
	 * @return <code>true</code> if any field value has been changed, otherwise
	 *         <code>false</code>
	 */
	public boolean hasChangedValues();
	
	/**
	 * Checks whether the specified field value has been <b>effectively</b>
	 * changed within the current transaction. If the record has not been
	 * manipulated within the current transaction or no current transaction
	 * exists, <code>false</code> is returned in any case. <br>
	 * <li>New record: An effective change exists, if the field value unequals
	 * <code>null</code>.
	 * <li>Updated record: An effective change exists, if the field value
	 * unequals the value stored in the corresponding datasource. I.e. if a value
	 * is first changed from A to B and then back from B to A within the same
	 * transaction, no effective change exists!
	 * <p>
	 * Note: If the field value has been changed from <code>null</code> to a
	 * "regular" value, <code>true</code> is returned.
	 * <p>
	 * Note: This method is especially useful in hooks on data layer to invoke
	 * additional actions on value changes.
	 * 
	 * @param fieldIndex
	 *          the field index of the value
	 * @return <code>true</code> if the field value has been changed, otherwise
	 *         <code>false</code>
	 */
	public boolean hasChangedValue(int fieldIndex);

	/**
	 * This method has the same behaviour has {@link #hasChangedValue(int)}except
	 * that the field value is specified by name.
	 * 
	 * @param fieldName
	 *          the field name of the value
	 * @return <code>true</code> if the field value has been changed, otherwise
	 *         <code>false</code>
	 * @throws NoSuchFieldException
	 *           if no such field name exists for this record
	 */
	public boolean hasChangedValue(String fieldName) throws NoSuchFieldException;
	
	/**
	 * Checks whether the record is in normal mode, i.e. is neither new nor
	 * updated nor deleted.
	 * 
	 * @return <code>true</code> if the record is in normal mode otherwise
	 *         <code>false</code>
	 */
	public boolean isNormal();
	
  /**
   * Checks whether the record is a new record, i.e. is an uncommitted record
   * or if called from
   * {@link DataTableRecordEventHandler#afterCommitAction(IDataTableRecord)}
   * has just been committed.
   * 
   * @return <code>true</code> if the record is new otherwise <code>false</code>
   */
  public boolean isNew();

  /**
   * Checks whether the record is a new or currently updated record.
   * 
   * @return <code>true</code> if the record is new or updated otherwise
   *         <code>false</code>
   * @see #isNew()
   * @see #isUpdated()
   * @since 2.7.2
   */
  public boolean isNewOrUpdated();

  /**
	 * Checks whether the record is currently updated or if called from
	 * {@link DataTableRecordEventHandler#afterCommitAction(IDataTableRecord)}
	 * has just been updated.
	 * 
	 * @return <code>true</code> if the record is updated otherwise <code>false</code>
	 */
	public boolean isUpdated();
	
  /**
   * Checks whether the record is going to be deleted (within the current
   * transaction) or already has been deleted.
   * 
   * @return <code>true</code> if the record is deleted otherwise <code>false</code>
   */
  public boolean isDeleted();
  
  /**
   * Checks whether the record is persistent.
   * <p>
   * Note: A new record created by means of
   * {@link IDataTable#newRecord(IDataTransaction)} is not persistent until the
   * given transaction has been successfully committed. Hence, this method
   * already returns <code>true</code> when invoked within
   * {@link DataTableRecordEventHandler#afterCommitAction(IDataTableRecord)}. A
   * record deleted by means of {@link #delete(IDataTransaction)} is still
   * persistent until the given transaction has been successfully committed.
   * 
   * @return <code>true</code> if the record is persistent otherwise
   *         <code>false</code>
   * @since 2.9
   */
  public boolean isPersistent();
  
  /**
   * Returns the key value of the specified key.
   * 
   * @param key
   *          the key to get the value from
   * @return the key value or <code>null</code>, if the key is empty
   */
  public IDataKeyValue getKeyValue(IKey key);
  
  /**
	 * Returns the key value of the specified key.
	 * 
	 * @param keyName
	 *          the name of the key to get the value from
	 * @return the key value or <code>null</code>, if the key is empty
	 */
	public IDataKeyValue getKeyValue(String keyName);
	
	/**
   * Returns the record of the given table alias that is linked to this record.
   * <p>
   * Attention: There must be a 1:N relation between the given table alias and
   * the table alias of this record, i.e. the given table alias must be the
   * alias on the 1-side of the relation.
   * <p>
   * Note: Do not use <code>getLinkedRecord("myalias")==null</code> to check
   * whether a record exists, since this might be (if the record is not already
   * cached) much more expensive than calling
   * {@link #hasLinkedRecord(String)}!
   * 
   * @param tableAliasName
   *          the name of the table alias of the linked record
   * @return the linked record or <code>null</code>, if this record is not
   *         linked to a record of the given table alias
   * @throws RecordNotFoundException
   *           if the linked record could not be retrieved from the datasource,
   *           i.e. has already been deleted
   */
	public IDataTableRecord getLinkedRecord(String tableAliasName) throws RecordNotFoundException;
	
  /**
   * Returns the record of the given table alias that is linked to this record.
   * <p>
   * Attention: There must be a 1:N relation between the given table alias and
   * the table alias of this record, i.e. the given table alias must be the
   * alias on the 1-side of the relation.
   * <p>
   * Note: Do not use <code>getLinkedRecord(myalias)==null</code> to check
   * whether a record exists, since this might be (if the record is not already
   * cached) much more expensive than calling
   * {@link #hasLinkedRecord(ITableAlias)}!
   * 
   * @param tableAlias
   *          the table alias of the linked record
   * @return the linked record or <code>null</code>, if this record is not
   *         linked to a record of the given table alias
   * @throws RecordNotFoundException
   *           if the linked record could not be retrieved from the datasource,
   *           i.e. has already been deleted
   */
  public IDataTableRecord getLinkedRecord(ITableAlias tableAlias) throws RecordNotFoundException;
  
  /**
   * Checks whether this record is linked to a record of the given table alias.
   * <p>
   * Attention: There must be a 1:N relation between the given table alias and
   * the table alias of this record, i.e. the given table alias must be the
   * alias on the 1-side of the relation.
   * 
   * @param tableAliasName
   *          the name of the table alias of the linked record
   * @return <code>true</code> if a linked record exists, otherwise <code>false</code>
   */
  public boolean hasLinkedRecord(String tableAliasName);
  
  /**
   * Checks whether this record is linked to a record of the given table alias.
   * <p>
   * Attention: There must be a 1:N relation between the given table alias and
   * the table alias of this record, i.e. the given table alias must be the
   * alias on the 1-side of the relation.
   * 
   * @param tableAlias
   *          the table alias of the linked record
   * @return <code>true</code> if a linked record exists, otherwise <code>false</code>
   */
  public boolean hasLinkedRecord(ITableAlias tableAlias);
  
  /**
   * Unlinks this record from a record of the given table alias, if existing.
   * <p>
   * If this record is the selected table record, i.e.
   * <code>this == this.getTable().getSelectedRecord()</code>,
   * the respective data table specified by <code>tableAliasName</code> will be cleared.
   * <p>
   * Note that this modification is not executed on the respective data source
   * until {@link IDataTransaction#commit()} is called.
   * <p>
   * Attention: There must be a 1:N relation between the given table alias and
   * the table alias of this record, i.e. the given table alias must be the
   * alias on the 1-side of the relation.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param tableAliasName
   *          the name of the table alias of the linked record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @see #setLinkedRecord(IDataTransaction, IDataRecord)          
   */
  public void resetLinkedRecord(IDataTransaction transaction, String tableAliasName) throws RecordLockedException;
  
  /**
   * Unlinks this record from a record of the given table alias, if existing.
   * <p>
   * If this record is the selected table record, i.e.
   * <code>this == this.getTable().getSelectedRecord()</code>,
   * the respective data table specified by <code>tableAlias</code> will be cleared.
   * <p>
   * Note that this modification is not executed on the respective data source
   * until {@link IDataTransaction#commit()} is called.
   * <p>
   * Attention: There must be a 1:N relation between the given table alias and
   * the table alias of this record, i.e. the given table alias must be the
   * alias on the 1-side of the relation.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param tableAlias
   *          the table alias of the linked record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @see #setLinkedRecord(IDataTransaction, IDataRecord)           
   */
  public void resetLinkedRecord(IDataTransaction transaction, ITableAlias tableAlias) throws RecordLockedException;

  /**
   * Links this record with the given record.
   * <p>
   * If this record is the selected table record, i.e.
   * <code>this == this.getTable().getSelectedRecord()</code>,
   * <code>fromRecord</code> will be backfilled and become the selected record
   * of the respective data table, i.e.
   * <code>fromRecord.getPrimaryKeyValue().equals(fromTable.getSelectedRecord().getPrimaryKeyValue())</code>.
   * <p>
   * Note that this modification is not executed on the respective data source
   * until {@link IDataTransaction#commit()}is called.
   * <p>
   * Attention: There must be a 1:N relation between the alias of the given
   * record and the table alias of this record, i.e. the alias of the given
   * record must be the alias on the 1-side of the relation.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @param fromRecord
   *          the record to link with this record
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   * @throws RecordNotFoundException
   *           if the <code>fromRecord</code> could not be backfilled from
   *           data source.
   * @see #resetLinkedRecord(IDataTransaction, ITableAlias)
   * @see #resetLinkedRecord(IDataTransaction, String)
   */
  public void setLinkedRecord(IDataTransaction transaction, IDataRecord fromRecord) throws RecordLockedException, RecordNotFoundException;
  
	/**
   * Deletes the record from the underlying data source.
   * <p>
   * Note that the record is not deleted from the data source until
   * {@link IDataTransaction#commit()} is called.
   * 
   * @param transaction
   *          the transaction to register this modification
   * @throws RecordLockedException
   *           if the record is currently locked by another user
   */
  public void delete(IDataTransaction transaction) throws RecordLockedException;

	/**
   * Returns the definition of the table field given by name.
   * 
   * @param fieldName
   *          the field name
   * @return the table field definition requested
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public ITableField getFieldDefinition(String fieldName) throws NoSuchFieldException;
  
	/**
   * Return the current active transaction, which has been used to modify this
   * record.
   * 
   * @return Returns the current active transaction or <code>null</code>, if
   *         no current active transaction exists, e.g. the record has already
   *         been commited.
   */
	public IDataTransaction getCurrentTransaction();
}

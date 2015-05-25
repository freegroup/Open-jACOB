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
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.misc.InvalidFieldExpressionException;
import de.tif.jacob.core.data.impl.misc.Nullable;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAPseudoDeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TARecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataTableRecord extends AbstractDataTableRecord
{
	static public transient final String RCS_ID = "$Id: DataTableRecord.java,v 1.18 2010/12/09 12:29:20 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.18 $";

	static private final transient Log logger = LogFactory.getLog(DataTableRecord.class);
	
	// IBIS: try to avoid to remember data source
	private final DataSource dataSource;

	private final Object[] values;
	private final int[] primaryKeyIndices;
	private DataTransaction currentTransaction;
	private boolean deleted = false;
	private boolean persistent;

	/**
	 * Constructor for instantiating a data record fetched from database
	 * 
	 * @param parent
	 * @param dataSource
	 * @param primaryKeyIndices
	 * @param values
	 */
	protected DataTableRecord(DataTable parent, DataSource dataSource, int[] primaryKeyIndices, Object[] values)
	{
    super(parent);
		this.dataSource = dataSource;
		this.values = values;
		this.primaryKeyIndices = primaryKeyIndices;
		this.currentTransaction = null;
    this.persistent = true;
	}

	/**
	 * Constructor for instantiating a new data record
	 * 
	 * @param parent
	 * @param dataSource
	 */
	protected DataTableRecord(DataTable parent, DataSource dataSource)
	{
	  super(parent);
		ITableDefinition tableDefinition = parent.getTableAlias().getTableDefinition();
		List tableFields = tableDefinition.getTableFields();
		this.dataSource = dataSource;
		this.values = new Object[tableFields.size()];
		this.primaryKeyIndices = getKeyIndices(tableDefinition.getPrimaryKey());
		this.persistent = false;
	}
  
  /**
   * Internal method to initialize new records.
   * 
   * @param dataTransaction
   * @return the transaction new record action
   */
  protected TARecordAction initializeOnNew(DataTransaction dataTransaction)
  {
    ITableDefinition tableDefinition = this.parent.getTableAlias().getTableDefinition();
    List tableFields = tableDefinition.getTableFields();
    TARecordAction action = new TAInsertRecordAction(this);      
    try
    {
      // initializes currentTransaction as well
      checkTransaction(dataTransaction);

      // initialize values
      for (int i = 0; i < tableFields.size(); i++)
      {
        ITableField field = (ITableField) tableFields.get(i);
        Object value = field.getType().getInitDataValue(dataSource, tableDefinition, field, dataTransaction);
        if (null != value)
        {
          if (!(value instanceof Nullable && ((Nullable) value).isNull()))
          {
            action.addValue(i, value, null, dataTransaction.isSavepointEnabled());
          }
        }
        this.values[i] = value;
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Error in initialization", ex);
    }
    
    // register action
    dataTransaction.addAction(action);
    
    return action;
  }
  
  /**
   * This method is needed to copy a record to another data accessor or table alias, i.e.
   * another data table.
   * 
   * @param parent
   *          the new data table
   * @return the cloned record
   */
  protected DataTableRecord clone(DataTable parent)
  {
    if (!parent.getTableAlias().getTableDefinition().equals(this.parent.getTableAlias().getTableDefinition()))
      throw new IllegalStateException("table definitions are not the same");

    DataTableRecord clone = new DataTableRecord(parent, this.dataSource, this.primaryKeyIndices, (Object[]) this.values.clone());
    clone.deleted = this.deleted;
    clone.persistent = this.persistent;
    // Hack: Das ist nicht ganz richtig, da die Transaction auch Referenzen auf DataTableRecord hat und
    // somit Verwirrung entstehen kann.
    clone.currentTransaction = this.currentTransaction;
    return clone;
  }
	
  /**
   * This method is used to adjust temporary auto key values to the correct type
   * of data values, e.g. {@link DataAutoKeyIntegerValue} or
   * {@link DataAutoKeyLongValue}to <code>Long</code> for long fields.
   * 
   * @param action
   *          the record action
   */
  protected void actualizeAfterCommit(TARecordAction action)
  {
    for (int i = 0; i < getFieldNumber(); i++)
    {
      if (action.hasOldValue(i))
      {
        if (this.values[i] instanceof IDataAutoKeyValue)
        {
          Object valueToConvert = ((IDataAutoKeyValue) this.values[i]).getGeneratedValue();
          try
          {
            this.values[i] = getFieldDefinition(i).getType().convertObjectToDataValue(this.dataSource, valueToConvert, action.getOldValue(i), null);
          }
          catch (InvalidExpressionException ex)
          {
            // should never occur
            throw new RuntimeException(ex);
          }
        }
        else if (this.values[i] instanceof DataIncOrDecValue)
        {
          this.values[i] = ((DataIncOrDecValue) this.values[i]).getIncrementedValue();
        }
      }
    }
  }

	protected void markAsDeleted()
	{
		this.deleted = true;
	}

  public boolean isPersistent()
  {
    return persistent;
  }

  /**
   * Internal method!
   * 
   * @param persistent
   */
  public void setPersistent(boolean persistent)
  {
    this.persistent = persistent;
  }

  protected DataTableRecordEventHandler getEventHandler() throws Exception
	{
		return DataTableRecordEventHandler.get(this.parent);
	}

  public Object getValueInternal(int fieldIndex)
	{
		return this.values[fieldIndex];
	}

  public Object getValue(int fieldIndex)
  {
    Object dataValue = getValueInternal(fieldIndex);
    if (null == dataValue)
      return null;
    return getFieldDefinition(fieldIndex).getType().convertDataValueToObject(dataValue);
  }
  
  public String getStringValue(int fieldIndex, Locale locale, int style)
  {
    Object value = getValueInternal(fieldIndex);
    if (null == value)
      return null;
    return getFieldDefinition(fieldIndex).getType().convertDataValueToString(value, locale, style);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataRecord#getPrimaryKeyValue()
	 */
	public IDataKeyValue getPrimaryKeyValue()
	{
		// determine primary key value
		return getKeyValue(this.primaryKeyIndices);
	}

	private int[] getKeyIndices(IKey key)
	{
		// IBIS: remove hack

		if (null == key)
			return new int[0];

		// TODO: make more efficient
		List fields = key.getTableFields();
		int[] keyIndices = new int[fields.size()];

		for (int i = 0; i < fields.size(); i++)
		{
			keyIndices[i] = ((ITableField) fields.get(i)).getFieldIndex();
		}
		return keyIndices;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTableRecord#delete(de.tif.jacob.core.data.IDataTransaction)
	 */
	public void delete(IDataTransaction transaction) throws RecordLockedException
	{
		// perform transaction check first
	  DataTransaction dataTransaction = (DataTransaction) transaction;
		checkTransaction(dataTransaction);

    TARecordAction oldAction = dataTransaction.getRecordAction(this);
    TARecordAction action;
		if (null != oldAction)
		{
      // IBIS: avoid checking different action cases
      if (oldAction instanceof TAInsertRecordAction)
      {
        // IBIS: Offenes Problem, welches auftreten kann: Wie werden Änderungen im New-Hook wieder rückgängiggemacht?
        
        action = new TAPseudoDeleteRecordAction(this);
        dataTransaction.removeAction(oldAction);      
      }
      else if (oldAction instanceof TAUpdateRecordAction)
      {
        action = new TADeleteRecordAction(this);
        // Do not remove update action because we want to perform update in
        // advance of delete on an SQL datasource (e.g. to remove
        // self-references))
      }
      else if (oldAction instanceof TADeleteRecordAction)
      {
        // nothing more to do, i.e. record already deleted
        return;
      }
      else
      {
        throw new IllegalStateException();
      }
		}
    else
    {
      // delete should fail in case that record is modified by someone else
      dataTransaction.lock(this);
      
      action = new TADeleteRecordAction(this);
    }

		// set temporary deleted flag that within calling afterDeleteAction() the
    // method isDeleted() returns true.
		// Note: The flag has to be set back because the transaction might not commit!
    boolean saveDeleted = this.deleted;
    this.deleted = true;
    try
    {
      // invoke user specific hook before delete action is added to transaction,
      // i.e. to ensure that possible clear up actions are executed in advance.
      // Otherwise these could fail because of database constraints.
      //
      try
      {
        getEventHandler().afterDeleteAction(this, dataTransaction);
      }
      catch (UserException ex)
      {
        throw new UserRuntimeException(ex);
      }
      catch (UserRuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        throw new RuntimeException("Error in user hook", ex);
      }
    }
    finally
    {
      this.deleted = saveDeleted;
    }

		// add delete action to transaction
		dataTransaction.addAction(action);

		// Remark: Record will be removed from parent data table then calling commit!
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTableRecord#hasChangedValues()
	 */
	public boolean hasChangedValues()
  {
    if (this.currentTransaction == null)
      return false;

    TARecordAction action = this.currentTransaction.getRecordAction(this);
    if (null == action)
    {
      return false;
    }
    for (int fieldIndex = 0; fieldIndex < values.length; fieldIndex++)
    {
      if (action.hasOldValue(fieldIndex))
      {
        return true;
      }
    }
    return false;
  }
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTableRecord#hasChangedValue(int)
	 */
	public boolean hasChangedValue(int fieldIndex)
	{
		if (this.currentTransaction == null)
			return false;

		TARecordAction action = this.currentTransaction.getRecordAction(this);
		if (null == action)
		{
			return false;
		}
		return action.hasOldValue(fieldIndex);
	}
	
	public boolean hasChangedKeyValue(IKey key)
	{
  	if (null == key)
  		throw new NullPointerException("key is null");

  	if (!getTableAlias().getTableDefinition().hasKey(key))
  		throw new RuntimeException(key + " is not a key of "+ getTableAlias());
  	
	  List keyFields = key.getTableFields();
	  for (int i=0; i < keyFields.size();i++)
	  {
	    ITableField keyField = (ITableField) keyFields.get(i);
	    if (this.hasChangedValue(keyField.getFieldIndex()))
	      return true;
	  }
	  return false;
	}
	
	/**
	 * Returns the old id of this record in case the primary key
	 * has been modified within the current transaction.
	 * 
   * @return the old record id
	 */
	public IDataRecordId getOldId()
	{
		IDataKeyValue oldPrimaryKey = this.getOldPrimaryKeyValue();
		if (null != oldPrimaryKey)
		{
			return new DataRecordId(getParent().getTableAlias().getTableDefinition(), oldPrimaryKey);
		}
		return getId();
	}

	/**
	 * Returns the old primary key value of this record in case the primary key
	 * has been modified within the current transaction.
	 * 
	 * @return the old primary key or <code>null</code> if no changes have been
	 *         performed to any primary key field.
	 */
	public IDataKeyValue getOldPrimaryKeyValue()
	{
		if (this.primaryKeyIndices.length == 1)
		{
			if (this.hasChangedValue(this.primaryKeyIndices[0]))
			{
				Object value = getOldValue(this.primaryKeyIndices[0]);
				if (value == null)
				{
					return null;
				}
				return new IDataKeyValue(value);
			}
		}
		else if (this.primaryKeyIndices.length > 1)
		{
			boolean keyFieldChange = false;
			Object[] keyFieldValues = new Object[this.primaryKeyIndices.length];
			for (int i = 0; i < this.primaryKeyIndices.length; i++)
			{
				keyFieldChange = keyFieldChange || hasChangedValue(this.primaryKeyIndices[i]);
				
				keyFieldValues[i] = getOldValue(this.primaryKeyIndices[i]);
				if (keyFieldValues[i] == null)
				{
					return null;
				}
			}
			
			// only return key if any change to primary key has been performed
			if (keyFieldChange)
				return new IDataKeyValue(keyFieldValues);
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTableRecord#hasChangedValue(java.lang.String)
	 */
	public boolean hasChangedValue(String fieldName) throws NoSuchFieldException
	{
		return hasChangedValue(this.parent.getFieldIndexByFieldName(fieldName));
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTableRecord#getOldValue(int)
	 */
	public Object getOldValue(int fieldIndex)
	{
	  Object dataValue = getOldValueInternal(fieldIndex);
	  if (null == dataValue)
	    return null;
    return getFieldDefinition(fieldIndex).getType().convertDataValueToObject(dataValue);
	}
	
	protected Object getOldValueInternal(int fieldIndex)
	{
		if (this.currentTransaction != null)
		{
			TARecordAction action = this.currentTransaction.getRecordAction(this);
			if (null != action && action.hasOldValue(fieldIndex))
			{
				return action.getOldValue(fieldIndex);
			}
		}
		return getValueInternal(fieldIndex);
	}

	public DataRecordMode getMode()
	{
		if (this.currentTransaction == null)
			return DataRecordMode.NORMAL;
		
		if (this.deleted)
			return DataRecordMode.DELETE;

		TARecordAction action = this.currentTransaction.getRecordAction(this);
		if (action == null)
		{
			// assume update mode, if the record has an unclosed transaction
			// Note: This is needed for IDataTable.updateSelectedRecord() and IDataBrowser.getRecordToUpdate()
			// to mark records as updated
			return this.currentTransaction.isClosed() ? DataRecordMode.NORMAL : DataRecordMode.UPDATE;
		}
		return action.getRecordMode();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getKeyValue(de.tif.jacob.core.definition.IKey)
   */
  public IDataKeyValue getKeyValue(IKey key)
  {
  	if (null == key)
  		throw new NullPointerException("key is null");

  	if (!getTableAlias().getTableDefinition().hasKey(key))
  		throw new RuntimeException(key + " is not a key of "+ getTableAlias());
  	
  	// TODO: make more efficient
  	return getKeyValue(getKeyIndices(key));
  }

  public Boolean getOldBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToBoolean(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }

  public Integer getOldIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToInteger(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  public Long getOldLongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToLong(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  public Double getOldDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToDouble(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  public Float getOldFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToFloat(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  public Date getOldDateValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToDate(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldDecimalValue(java.lang.String)
   */
  public BigDecimal getOldDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToDecimal(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldTimestampValue(java.lang.String)
   */
  public Timestamp getOldTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToTimestamp(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldTimeValue(java.lang.String)
   */
  public Time getOldTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToTime(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldBytesValue(java.lang.String)
   */
  public byte[] getOldBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return convertToBytes(getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName)));
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getOldDocumentValue(java.lang.String)
   */
  public DataDocumentValue getOldDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return (DataDocumentValue) getOldValue(this.parent.getFieldIndexByFieldName(fieldName));
  }
  
  public String getOldStringValue(int fieldIndex, Locale locale, int style)
  {
    Object dataValue = getOldValueInternal(fieldIndex);
    if (null == dataValue)
      return null;

 		return getFieldDefinition(fieldIndex).getType().convertDataValueToString(dataValue, locale, style);
  }
  
  public boolean setStringValueWithTruncation(IDataTransaction transaction, String fieldName, String value, Locale locale) throws NoSuchFieldException,
      InvalidExpressionException, RecordLockedException, IllegalArgumentException
  {
	  // check whether the given value has to be truncated
	  if (value != null)
    {
      ITableField tableField = this.parent.getTableAlias().getTableDefinition().getTableField(fieldName);
      if (tableField.getType() instanceof TextFieldType)
      {
        int maxLength = ((TextFieldType) tableField.getType()).getMaxLength();
        if (maxLength != 0 && maxLength < value.length())
        {
          // truncated value
          value = value.substring(0, maxLength - 1);
        }
      }
    }
	  
	  return setValue(transaction, fieldName, value, locale);
  }
  
  public DataLongText getLongTextValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    return (DataLongText) getValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getLongTextReference(java.lang.String)
   */
  public String getLongTextReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    DataLongText longText = (DataLongText) getValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    return longText.toReference(this, fieldName);
  }
  
  private String appendChangeHeader(IDataTransaction transaction, String fieldName, String value, boolean appendNotPrepend) throws NoSuchFieldException
  {
    // The following block checks whether automatic change header generation is
    // activated for this long text field. If this is the case, a header will be
    // created which contains user and change time information.
    //
    ITableDefinition tableDef = this.parent.getTableAlias().getTableDefinition();
    ITableField tableField = tableDef.getTableField(fieldName);

    // write change header if ..
    boolean changeHeader = false;
    if (tableDef.getHistoryField() == tableField)
    {
      // this is the history field (regardless isChangeHeaderEnabled() setting)
      changeHeader = true;
    }
    else if (tableField.getType() instanceof LongTextFieldType)
    {
      // or change header is explicitly set
      changeHeader = ((LongTextFieldType) tableField.getType()).isChangeHeaderEnabled();
    }

    if (changeHeader)
    {
      try
      {
        String longTextWithHeader = getEventHandler().getLongTextWithHeader(this, transaction, fieldName, value, appendNotPrepend);
        if (longTextWithHeader == null)
          longTextWithHeader = DataTableRecordEventHandler.getLongTextWithDefaultHeader(this, transaction, fieldName, value, appendNotPrepend);
        return longTextWithHeader;
      }
      catch (RuntimeException ex)
      {
        throw ex;
      }
      catch (UserException ex)
      {
        throw new UserRuntimeException(ex);
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    }
    return value;
  }

  public void appendLongTextValue(IDataTransaction transaction, String fieldName, String value, boolean alwaysWriteChangeHeaderIfActivated) throws NoSuchFieldException, RecordLockedException
  {
    if (null == value)
      throw new NullPointerException();

    if (value.length() > 0)
    {
      // The following block checks whether the long text field is changed the
      // first time within this transaction.
      if (alwaysWriteChangeHeaderIfActivated || !hasChangedValue(fieldName))
        value = appendChangeHeader(transaction, fieldName, value, true);

      try
      {
        setValue(transaction, fieldName, getLongTextValue(fieldName).append(value));
      }
      catch (InvalidExpressionException ex)
      {
        // should never occur
        throw new RuntimeException();
      }
    }
  }

	public void prependLongTextValue(IDataTransaction transaction, String fieldName, String value, boolean alwaysWriteChangeHeaderIfActivated) throws NoSuchFieldException, RecordLockedException
	{
		if (null == value)
			throw new NullPointerException();
    
		if (value.length() > 0)
		{
      // The following block checks whether the long text field is changed the
      // first time within this transaction.
      if (alwaysWriteChangeHeaderIfActivated || !hasChangedValue(fieldName))
        value = appendChangeHeader(transaction, fieldName, value, false);

			try
			{
				setValue(transaction, fieldName, getLongTextValue(fieldName).prepend(value));
			}
			catch (InvalidExpressionException ex)
			{
				// should never occur
				throw new RuntimeException();
			}
		}
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#getBinaryReference(java.lang.String)
   */
  public String getBinaryReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    DataBinary dataBinary = (DataBinary) getValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    return dataBinary.toReference(this, fieldName);
  }

  public String getDocumentReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    DataDocument dataDocument = (DataDocument) getValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    return dataDocument.toReference(this, fieldName);
  }

  public boolean setValue(IDataTransaction dataTransaction, ITableField field, Object value, Locale locale) throws InvalidExpressionException, RecordLockedException
  {
    if (!this.parent.getTableAlias().getTableDefinition().equals(field.getTableDefinition()))
		{
			throw new RuntimeException(field + " is not a field of table " + this.parent.getTableAlias().getTableDefinition());
		}
    return setValue(dataTransaction, field.getFieldIndex(), value, locale);
  }
  
  public boolean setValue(IDataTransaction dataTransaction, String fieldName, Object value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException
  {
    return setValue(dataTransaction, this.parent.getFieldIndexByFieldName(fieldName), value, locale);
  }
  
  public void incrementValue(IDataTransaction dataTransaction, String fieldName, double incrementBy) throws NoSuchFieldException, RecordLockedException
  {
    if (incrementBy != 0.0d)
    {
      incOrDecValue(dataTransaction, fieldName, new Double(Math.abs(incrementBy)), incrementBy < 0);
    }
  }
  
  public void incrementValue(IDataTransaction dataTransaction, String fieldName, float incrementBy) throws NoSuchFieldException, RecordLockedException
  {
    if (incrementBy != 0.0f)
    {
      incOrDecValue(dataTransaction, fieldName, new Float(Math.abs(incrementBy)), incrementBy < 0);
    }
  }
  
  public void incrementValue(IDataTransaction dataTransaction, String fieldName, long incrementBy) throws NoSuchFieldException, RecordLockedException
  {
    if (incrementBy != 0L)
    {
      incOrDecValue(dataTransaction, fieldName, new Long(Math.abs(incrementBy)), incrementBy < 0);
    }
  }
  
  public void incrementValue(IDataTransaction dataTransaction, String fieldName, int incrementBy) throws NoSuchFieldException, RecordLockedException
  {
    if (incrementBy != 0)
    {
      incOrDecValue(dataTransaction, fieldName, new Integer(Math.abs(incrementBy)), incrementBy < 0);
    }
  }
  
  public void incrementValue(IDataTransaction dataTransaction, String fieldName, BigDecimal incrementBy) throws NoSuchFieldException, RecordLockedException
  {
    if (incrementBy == null)
      throw new NullPointerException("incrementBy is null");

    int cmp = incrementBy.compareTo(DECIMAL_0);
    if (cmp != 0)
    {
      incOrDecValue(dataTransaction, fieldName, incrementBy.abs(), cmp < 0);
    }
  }
  
  private void incOrDecValue(IDataTransaction transaction, String fieldName, Number incOrDecBy, boolean decrement) throws NoSuchFieldException, RecordLockedException
  {
		// perform transaction check first
	  DataTransaction dataTransaction = (DataTransaction) transaction;
		checkTransaction(dataTransaction);

		// only allowed for updating the record
    if (isNew())
      throw new IllegalStateException();
    
		// just for checking conformity of incrementBy
    // (will throw IllegalArgumentException if not conform)
		//
    int fieldIndex = this.parent.getFieldIndexByFieldName(fieldName);
		Object oldValue = this.values[fieldIndex];
		Object newValue = new DataIncOrDecValue(incOrDecBy, decrement);
		try
    {
		  newValue = getFieldDefinition(fieldIndex).getType().convertObjectToDataValue(this.dataSource, newValue, oldValue, null);
    }
    catch (InvalidExpressionException ex)
    {
      // should never occur
      throw new RuntimeException(ex);
    }
    
		if (logger.isDebugEnabled())
			logger.debug("value of field " + getFieldDefinition(fieldIndex).getName() + (decrement ? " decremented" : " incremented") + " by " + incOrDecBy);

		// register modification in transaction
		TARecordAction action = getRecordAction(dataTransaction, false);
		if (action instanceof TAInsertRecordAction)
		  throw new RuntimeException("Incrementing value not allowed for new records");
		
		action.addValue(fieldIndex, newValue, oldValue, dataTransaction.isSavepointEnabled());

		// and set new value
		this.values[fieldIndex] = newValue;
  }
  
  private TARecordAction getRecordAction(DataTransaction dataTransaction, boolean doLock) throws RecordLockedException
  {
    TARecordAction action = dataTransaction.getRecordAction(this);
    if (null == action)
    {
      // no action existing so far -> record should be updated
      
      // lock record?
      if (doLock)
        dataTransaction.lock(this);
      
      action = new TAUpdateRecordAction(this);
      dataTransaction.addAction(action);
    }
    else if (action instanceof TAUpdateRecordAction)
    {
      // for the cooperative update mode, record might not been locked so far
      if (doLock)
        dataTransaction.lock(this);
    }
    return action;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.IDataTableRecord#appendToHistory(de.tif.jacob.core.data.IDataTransaction, java.lang.String)
   */
  public void appendToHistory(IDataTransaction transaction, String note) throws RecordLockedException
  {
    // perform transaction check first
	  DataTransaction dataTransaction = (DataTransaction) transaction;
    checkTransaction(dataTransaction);

    if (logger.isDebugEnabled())
      logger.debug("history note: " + note);

    // register modification in transaction
    TARecordAction action = getRecordAction(dataTransaction, true);
    action.appendToHistory(note);
  }
  
	/**
	 * @param transaction
	 * @param fieldIndex
	 * @param newValue
	 * @param locale
	 * @return
	 * @throws InvalidExpressionException
	 * @throws RecordLockedException
	 */
	private boolean setValue(IDataTransaction transaction, int fieldIndex, Object newValue, Locale locale) throws InvalidExpressionException, RecordLockedException
	{
		// perform transaction check first
	  DataTransaction dataTransaction = (DataTransaction) transaction;
		checkTransaction(dataTransaction);

		// check whether any changes have to be performed
		//
		Object oldValue = this.values[fieldIndex];
		try
    {
      newValue = getFieldDefinition(fieldIndex).getType().convertObjectToDataValue(this.dataSource, newValue, oldValue, locale);
    }
    catch (InvalidExpressionException ex)
    {
      // exchange exception to add field info
      throw new InvalidFieldExpressionException(getFieldDefinition(fieldIndex), ex);
    }
    // IBIS: Nullable-Quatsch entfernen
    if (null == oldValue || ((oldValue instanceof Nullable) && ((Nullable)oldValue).isNull()))
    {
      if (null == newValue || ((newValue instanceof Nullable) && ((Nullable)newValue).isNull()))
      {
        // no change performed
        return false;
      }
      
      // will be handled in action.addValue()
      // oldValue = null;
    }
    else
    {
      if (oldValue.equals(newValue))
      {
        // no change performed
        return false;
      }
    }
    
		if (logger.isDebugEnabled())
			logger.debug("value of field " + getFieldDefinition(fieldIndex).getName() + " changed: " + oldValue + " -> " + newValue);

		// register modification in transaction
		TARecordAction action = getRecordAction(dataTransaction, true);
		action.addValue(fieldIndex, newValue, oldValue, dataTransaction.isSavepointEnabled());

		// and set new value
		this.values[fieldIndex] = newValue;

		return true;
	}
  
  /**
   * Internal method for rolling back changes after savepoint.
   * 
   * @param savepoint
   *          the savepoint
   */
  public void rollback(IDataSavepoint savepoint)
  {
    for (int fieldIndex = 0; fieldIndex < this.values.length; fieldIndex++)
    {
      if (savepoint.hasRollbackValue(fieldIndex))
        this.values[fieldIndex] = savepoint.getRollbackValue(fieldIndex);
    }
  }

	private void checkTransaction(DataTransaction transaction) throws RecordLockedException
	{
		if (transaction == null)
			throw new NullPointerException("You must hand over a transaction.");

		if (!transaction.isValid())
		 {
		  throw new RuntimeException("Record cannot be modified by an already committed transaction");
		}
		
		if (this.currentTransaction != transaction)
		{
			if (this.currentTransaction != null && this.currentTransaction.isValid())
			{
			  // Throw RecordLockedException instead of RuntimeException
			  // Note: There should not be any difference between record locked by another user or by the
			  // same user within a different session (transaction) except for transactions belonging to the 
			  // same root transaction.
        if (this.currentTransaction.isAlreadyLocked(this) && this.currentTransaction.getRootTransaction() != transaction.getRootTransaction())
			    throw new RecordLockedException(this.getId(), this.currentTransaction.getUser().getLoginId());
			  
				throw new RuntimeException("Record cannot be modified by multiple active transactions at once!");
			}

			this.currentTransaction = transaction;
		}
	}
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.IDataTableRecord#getCurrentTransaction()
	 */
	public IDataTransaction getCurrentTransaction()
	{
		// transaction already committed?
		if (null != this.currentTransaction && !this.currentTransaction.isValid())
		{
			// avoid returning inactive transactions 
			return null;
		}
		return currentTransaction;
	}

	/**
	 * @param currentTransaction The currentTransaction to set.
	 */
	protected void setCurrentTransaction(DataTransaction currentTransaction)
	{
	  try
	  {
	    checkTransaction(currentTransaction);
	  }
	  catch (RecordLockedException ex)
	  {
	    // should never occur
	    throw new RuntimeException(ex);
	  }
	}

}

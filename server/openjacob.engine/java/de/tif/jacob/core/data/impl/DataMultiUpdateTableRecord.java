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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataMultiUpdateTableRecord;
import de.tif.jacob.core.data.IDataMultiUpdateTransaction;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataTransactionInternal;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.core.exception.RecordException;
import de.tif.jacob.core.exception.RecordExceptionCollection;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.security.IUser;
import de.tif.jacob.util.ObjectUtil;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class DataMultiUpdateTableRecord extends AbstractDataTableRecord implements IDataMultiUpdateTableRecord
{
	static public transient final String RCS_ID = "$Id: DataMultiUpdateTableRecord.java,v 1.10 2010/10/27 20:11:50 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.10 $";

	static private final transient Log logger = LogFactory.getLog(DataMultiUpdateTableRecord.class);
  
  private static final String UNKNOWN = new String("unknown");
  
	private final DataTableRecord[] tableRecords;
	private final MultiUpdateTransaction associatedTransaction;
  
  private abstract class MultiUpdateTransaction extends AbstractDataTransaction implements IDataMultiUpdateTransaction
  {
    private final Map recordInvocationExceptionMap = new HashMap();
    private final Map recordPrimaryKeyInvocationExceptionMap = new HashMap();
    private final Map recordCompletionExceptionMap = new HashMap();
    private final Map recordPrimaryKeyCompletionExceptionMap = new HashMap();
    
    protected abstract DataTableRecord[] getRecordsToUpdate(DataBrowser browser, List browserRecords);
    
    public final IDataTransaction newEmbeddedTransaction(EmbeddedTransactionMode mode)
    {
      throw new UnsupportedOperationException();
    }

    public final void omitLocking(IDataRecord record)
    {
      throw new UnsupportedOperationException();
    }

    public final void lock(IDataRecord record) throws RecordLockedException
    {
      throw new UnsupportedOperationException();
    }

    public final void setHintForNewRecordNumber(ITableAlias alias, int newRecordNumber)
    {
      // ignore
    }

    public final void setHintForNewRecordNumber(String alias, int newRecordNumber)
    {
      // ignore
    }
    
    protected void registerInvocationException(DataBrowserRecord browserRecord, Exception ex)
    {
      this.recordInvocationExceptionMap.put(browserRecord, ex);
      IDataKeyValue primaryKey = browserRecord.getPrimaryKeyValue();
      if (primaryKey != null)
        this.recordPrimaryKeyInvocationExceptionMap.put(primaryKey, ex);
    }

    protected void registerCompletionException(DataBrowserRecord browserRecord, Exception ex)
    {
      this.recordCompletionExceptionMap.put(browserRecord, ex);
      IDataKeyValue primaryKey = browserRecord.getPrimaryKeyValue();
      if (primaryKey != null)
        this.recordPrimaryKeyCompletionExceptionMap.put(primaryKey, ex);
    }

    public final Exception getCompletionError(IDataRecord record) throws IllegalStateException
    {
      if (isValid())
        throw new IllegalStateException();

      Exception ex = (Exception) this.recordCompletionExceptionMap.get(record);
      if (ex != null)
        return ex;
      IDataKeyValue primaryKey = record.getPrimaryKeyValue();
      if (primaryKey != null)
        return (Exception) this.recordPrimaryKeyCompletionExceptionMap.get(primaryKey);
      return null;
    }

    public final Exception getInvocationError(IDataRecord record)
    {
      Exception ex = (Exception) this.recordInvocationExceptionMap.get(record);
      if (ex != null)
        return ex;
      IDataKeyValue primaryKey = record.getPrimaryKeyValue();
      if (primaryKey != null)
        return (Exception) this.recordPrimaryKeyInvocationExceptionMap.get(primaryKey);
      return null;
    }

    public final boolean hasCompletionErrors() throws IllegalStateException
    {
      if (isValid())
        throw new IllegalStateException();

      return this.recordCompletionExceptionMap.size() > 0;
    }

    public final int getCompletionErrorCount() throws IllegalStateException
    {
      if (isValid())
        throw new IllegalStateException();
      
      return this.recordCompletionExceptionMap.size();
    }

    public final boolean hasInvocationErrors()
    {
      return this.recordInvocationExceptionMap.size() > 0;
    }
    
    public final IDataTableRecord getRecord(ITableDefinition tableDefinition, IDataKeyValue primaryKey)
    {
      // no record cache
      return null;
    }

    protected final IDataTableRecord getRecord(ITableAlias tableAlias, IDataKeyValue primaryKey)
    {
      // no record cache
      return null;
    }
  }

  /**
   * Isolierter Fall, d.h. jeder Record in einer eigenen Transaktion.
   * 
   * @author Andreas Sonntag
   */
  private final class IsolatedMultiUpdateTransaction extends MultiUpdateTransaction
  {
    private final IUser user;
    private final Map properties = new HashMap();
    private final IDataTransactionInternal[] embeddedTransactions;
    private final DataBrowserRecord[] embeddedTransactionBrowserRecords;
    private boolean closed = false;
    private boolean commitActive = false;
    
    private IsolatedMultiUpdateTransaction(int count)
    {
      this.user = Context.getCurrent().getUser();
      this.embeddedTransactions = new IDataTransactionInternal[count];
      this.embeddedTransactionBrowserRecords = new DataBrowserRecord[count];
    }

    protected DataTableRecord[] getRecordsToUpdate(DataBrowser browser, List browserRecords)
    {
      DataTableRecord[] tableRecords = new DataTableRecord[browserRecords.size()];
      for (int i = 0; i < browserRecords.size(); i++)
      {
        DataBrowserRecord browserRecord = (DataBrowserRecord) browserRecords.get(i);
        
        // one transaction for each record
        IDataTransactionInternal transaction = (IDataTransactionInternal) browser.getAccessor().newTransaction();
        try
        {
          tableRecords[i] = (DataTableRecord) browser.getRecordToUpdate(transaction, browserRecord);
        }
        catch (RecordException ex)
        {
          // ignore
          transaction.close();
          registerInvocationException(browserRecord, ex);
          continue;
        }
        this.embeddedTransactions[i] = transaction;
        this.embeddedTransactionBrowserRecords[i] = browserRecord;
      }
      return tableRecords;
    }
    
    public boolean isIsolated()
    {
      return true;
    }

    public final Object getProperty(Object key)
    {
      Object value = this.properties.get(key);

      // get property value from context, if not set for this transaction
      //
      if (value == null)
        value = Context.getCurrent().getProperty(key);

      return value;
    }

    public final void setProperty(Object key, Object value)
    {
      if (!isValid())
        throw new IllegalStateException();

      this.properties.put(key, value);
    }

    public final Timestamp getTimestamp()
    {
      throw new UnsupportedOperationException();
    }

    private void checkOpen()
    {
      if (this.closed)
        throw new RuntimeException("Transaction has already been committed or closed");
    }

    public void close()
    {
      // Check if already closed
      if (this.closed == false)
      {
        for (int i = 0; i < this.embeddedTransactions.length; i++)
        {
          IDataTransaction transaction = this.embeddedTransactions[i];
          if (transaction != null)
            transaction.close();
        }

        unregister();
        this.closed = true;
      }
    }

    public void commit() throws UserException, Exception
    {
      checkOpen();

      if (this.commitActive)
        throw new IllegalStateException("Transaction is already being committed");

      this.commitActive = true;
      try
      {
        if (logger.isTraceEnabled())
          logger.trace("Transaction " + this + ":committing ..");

        for (int i = 0; i < this.embeddedTransactions.length; i++)
        {
          IDataTransaction transaction = this.embeddedTransactions[i];
          if (transaction != null && transaction.isValid())
          {
            try
            {
              transaction.commit();
            }
            catch (UserException ex)
            {
              registerCompletionException(this.embeddedTransactionBrowserRecords[i], ex);
            }
            catch (UserRuntimeException ex)
            {
              registerCompletionException(this.embeddedTransactionBrowserRecords[i], ex);
            }
            transaction.close();
          }
        }
      }
      finally
      {
        this.commitActive = false;
      }

      if (logger.isTraceEnabled())
        logger.trace("Transaction " + this + ": commit finished.");
    }

    public IUser getUser()
    {
      return this.user;
    }

    public boolean isValid()
    {
      return this.closed == false;
    }

    public void rollbackToSavepoint()
    {
      checkOpen();

      for (int i = 0; i < this.embeddedTransactions.length; i++)
      {
        IDataTransactionInternal transaction = this.embeddedTransactions[i];
        if (transaction != null && transaction.isValid())
        {
          transaction.rollbackToSavepoint();
        }
      }
    }

    public void setSavepoint()
    {
      checkOpen();

      for (int i = 0; i < this.embeddedTransactions.length; i++)
      {
        IDataTransactionInternal transaction = this.embeddedTransactions[i];
        if (transaction != null && transaction.isValid())
        {
          transaction.setSavepoint();
        }
      }
    }
  }

  /**
   * "Alles oder nichts" Transaktion
   * 
   * @author Andreas Sonntag
   */
  private final class ComprehensiveMultiUpdateTransaction extends MultiUpdateTransaction
  {
    private final IDataTransactionInternal embeddedTransaction;
    
    private ComprehensiveMultiUpdateTransaction()
    {
      this.embeddedTransaction = (IDataTransactionInternal) DataMultiUpdateTableRecord.this.parent.getAccessor().newTransaction();
    }
    
    protected DataTableRecord[] getRecordsToUpdate(DataBrowser browser, List browserRecords)
    {
      DataTableRecord[] tableRecords = new DataTableRecord[browserRecords.size()];
      boolean error = false;
      for (int i = 0; i < browserRecords.size(); i++)
      {
        DataBrowserRecord browserRecord = (DataBrowserRecord) browserRecords.get(i);
        try
        {
          tableRecords[i] = (DataTableRecord) browser.getRecordToUpdate(this.embeddedTransaction, browserRecord);
        }
        catch (RecordException ex)
        {
          error = true;
          registerInvocationException(browserRecord, ex);
          registerCompletionException(browserRecord, ex);
        }
      }
      // If an RecordException has occurred -> close transaction immediately 
      if (error)
        close();
      return tableRecords;
    }

    public boolean isIsolated()
    {
      return false;
    }

    public void close()
    {
      unregister();
      this.embeddedTransaction.close();
    }

    public void commit() throws UserException, Exception
    {
      this.embeddedTransaction.commit();
    }

    public boolean isValid()
    {
      return this.embeddedTransaction.isValid();
    }

    public Object getProperty(Object key)
    {
      return this.embeddedTransaction.getProperty(key);
    }

    public Timestamp getTimestamp()
    {
      return this.embeddedTransaction.getTimestamp();
    }

    public IUser getUser()
    {
      return this.embeddedTransaction.getUser();
    }

    public void setProperty(Object key, Object value)
    {
      this.embeddedTransaction.setProperty(key, value);
    }

    public void rollbackToSavepoint()
    {
      this.embeddedTransaction.rollbackToSavepoint();
    }

    public void setSavepoint()
    {
      this.embeddedTransaction.setSavepoint();
    }
  }

	/**
	 * Constructor for instantiating a data record fetched from database
	 * 
+	 * @param browser
	 * @param browserRecords
	 * @param isolated
	 * @throws RecordExceptionCollection
	 * @throws Exception
	 */
	protected DataMultiUpdateTableRecord(DataBrowser browser, List browserRecords, boolean isolated) throws RecordExceptionCollection, Exception
  {
    super((DataTable) browser.getAccessor().getTable(browser.getTableAlias()));
    
    if (isolated)
      this.associatedTransaction = new IsolatedMultiUpdateTransaction(browserRecords.size());
    else
      this.associatedTransaction = new ComprehensiveMultiUpdateTransaction();

    try
    {
      this.tableRecords = this.associatedTransaction.getRecordsToUpdate(browser, browserRecords);
    }
    catch (Exception ex)
    {
      // free resources
      this.associatedTransaction.close();
      throw ex;
    }
  }

  public List getUnderlyingRecords()
  {
    List records  = new ArrayList(this.tableRecords.length);
    for (int i=0; i<this.tableRecords.length;i++)
    {
      if (this.tableRecords[i]!=null)
        records.add(this.tableRecords[i]);
    }
    return records;
  }

  public Object getValueInternal(int fieldIndex)
  {
    Object dataValue = getDiverseValueInternal(fieldIndex);
    if (DIVERSE == dataValue)
      return null;
    return dataValue;
  }

  private Object getDiverseValueInternal(int fieldIndex)
  {
    Object result = UNKNOWN;
    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        if (result == UNKNOWN)
        {
          result = tableRecord.getValueInternal(fieldIndex);
        }
        else
        {
          if (!ObjectUtil.equals(result, tableRecord.getValueInternal(fieldIndex)))
            return DIVERSE;
        }
      }
    }
    return result == UNKNOWN ? null : result;
  }

  public Object getValue(int fieldIndex)
  {
    Object dataValue = getDiverseValueInternal(fieldIndex);
    if (null == dataValue)
      return null;
    if (DIVERSE == dataValue)
      return DIVERSE;
    return getFieldDefinition(fieldIndex).getType().convertDataValueToObject(dataValue);
  }
  
  public String getStringValue(int fieldIndex, Locale locale, int style)
  {
    Object value = getDiverseValueInternal(fieldIndex);
    if (null == value)
      return null;
    if (DIVERSE == value)
      return DIVERSE;
    return getFieldDefinition(fieldIndex).getType().convertDataValueToString(value, locale, style);
  }

	public IDataKeyValue getPrimaryKeyValue()
  {
    IKey primaryKey = parent.getTableAlias().getTableDefinition().getPrimaryKey();
    if (primaryKey == null)
      return null;
    // always returns null if more than one underlying record exists
    return getKeyValue(primaryKey);
  }

	public void delete(IDataTransaction transaction) throws RecordLockedException
	{
    throw new UnsupportedOperationException();
	}

	public boolean hasChangedValues()
  {
    if (!this.associatedTransaction.isValid())
      return false;

    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null && tableRecord.hasChangedValues())
        return true;
    }
    return false;
  }
	
	public boolean hasChangedValue(int fieldIndex)
  {
    if (!this.associatedTransaction.isValid())
      return false;

    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null && tableRecord.hasChangedValue(fieldIndex))
        return true;
    }
    return false;
  }
	
	public boolean hasChangedValue(String fieldName) throws NoSuchFieldException
	{
		return hasChangedValue(this.parent.getFieldIndexByFieldName(fieldName));
	}

  private Object getOldValueInternal(int fieldIndex)
  {
    Object result = UNKNOWN;
    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        if (result == UNKNOWN)
        {
          result = tableRecord.getOldValueInternal(fieldIndex);
        }
        else
        {
          if (!ObjectUtil.equals(result, tableRecord.getOldValueInternal(fieldIndex)))
            return DIVERSE;
        }
      }
    }
    return result == UNKNOWN ? null : result;
  }

	public Object getOldValue(int fieldIndex)
	{
    Object dataValue = getOldValueInternal(fieldIndex);
    if (null == dataValue)
      return null;
    if (DIVERSE == dataValue)
      return DIVERSE;
    return getFieldDefinition(fieldIndex).getType().convertDataValueToObject(dataValue);
	}
	
	public DataRecordMode getMode()
  {
    return this.associatedTransaction.isValid() ? DataRecordMode.UPDATE : DataRecordMode.NORMAL;
  }

  public IDataKeyValue getKeyValue(IKey key)
  {
    if (null == key)
      throw new NullPointerException("key is null");

    if (!getTableAlias().getTableDefinition().hasKey(key))
      throw new RuntimeException(key + " is not a key of " + getTableAlias());

    Object result = UNKNOWN;
    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        if (result == UNKNOWN)
        {
          result = tableRecord.getKeyValue(key);
        }
        else
        {
          if (!ObjectUtil.equals(result, tableRecord.getKeyValue(key)))
            // return null for diverse keys
            return null;
        }
      }
    }
    return result == UNKNOWN ? null : (IDataKeyValue) result;
  }

  public Boolean getOldBooleanValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToBoolean(value);
  }

  public Integer getOldIntegerValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToInteger(value);
  }
  
  public Long getOldLongValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToLong(value);
  }
  
  public Double getOldDoubleValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToDouble(value);
  }
  
  public Float getOldFloatValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToFloat(value);
  }
  
  public Date getOldDateValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToDate(value);
  }
  
  public BigDecimal getOldDecimalValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToDecimal(value);
  }
  
  public Timestamp getOldTimestampValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToTimestamp(value);
  }
  
  public Time getOldTimeValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToTime(value);
  }
  
  public byte[] getOldBytesValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return DataRecord.convertToBytes(value);
  }
  
  public DataDocumentValue getOldDocumentValue(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    Object value = getOldValue(this.parent.getFieldIndexByFieldName(fieldName));
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return (DataDocumentValue) value;
  }
  
  public String getOldStringValue(int fieldIndex, Locale locale, int style)
  {
    Object dataValue = getOldValueInternal(fieldIndex);
    if (dataValue == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    if (null == dataValue)
      return null;

 		return getFieldDefinition(fieldIndex).getType().convertDataValueToString(dataValue, locale, style);
  }
  
  public DataDocumentValue getDocumentValue(int fieldIndex) throws ClassCastException
  {
    Object value = getValue(fieldIndex);
    if (value == DIVERSE)
      // IBIS: Nochmals überdenken!
      return null;
    return (DataDocumentValue) value;
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
  
//  public DataLongText getLongTextValue(String fieldName) throws NoSuchFieldException, ClassCastException
//  {
//    return (DataLongText) getValueInternal(this.parent.getFieldIndexByFieldName(fieldName));
//  }
//
  public String getLongTextReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    throw new UnsupportedOperationException();
  }
  
  public void appendLongTextValue(IDataTransaction transaction, String fieldName, String value, boolean alwaysWriteChangeHeaderIfActivated) throws NoSuchFieldException, RecordLockedException
	{
    // perform transaction check first
    checkTransaction(transaction);

    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        IDataTransaction recordTransaction = tableRecord.getCurrentTransaction();
        if (recordTransaction != null)
        {
          tableRecord.appendLongTextValue(recordTransaction, fieldName, value, alwaysWriteChangeHeaderIfActivated);
        }
      }
    }
	}

	public void prependLongTextValue(IDataTransaction transaction, String fieldName, String value, boolean alwaysWriteChangeHeaderIfActivated) throws NoSuchFieldException, RecordLockedException
	{
    // perform transaction check first
    checkTransaction(transaction);

    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        IDataTransaction recordTransaction = tableRecord.getCurrentTransaction();
        if (recordTransaction != null)
        {
          tableRecord.prependLongTextValue(recordTransaction, fieldName, value, alwaysWriteChangeHeaderIfActivated);
        }
      }
    }
	}
  
  public String getBinaryReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    throw new UnsupportedOperationException();
  }

  public String getDocumentReference(String fieldName) throws NoSuchFieldException, ClassCastException
  {
    throw new UnsupportedOperationException();
  }

  public boolean setValue(IDataTransaction transaction, ITableField field, Object value, Locale locale) throws InvalidExpressionException, RecordLockedException
  {
    // perform transaction check first
    checkTransaction(transaction);

    if (value == DIVERSE)
      return false;

    boolean result = false;
    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        IDataTransaction recordTransaction = tableRecord.getCurrentTransaction();
        if (recordTransaction != null)
        {
          if (tableRecord.setValue(recordTransaction, field, value, locale))
            result = true;
        }
      }
    }
    return result;
  }
  
  public boolean setValue(IDataTransaction transaction, String fieldName, Object value, Locale locale) throws NoSuchFieldException, InvalidExpressionException, RecordLockedException
  {
    // perform transaction check first
    checkTransaction(transaction);
    
    if (value == DIVERSE)
      return false;

    boolean result = false;
    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        IDataTransaction recordTransaction = tableRecord.getCurrentTransaction();
        if (recordTransaction != null)
        {
          if (tableRecord.setValue(recordTransaction, fieldName, value, locale))
            result = true;
        }
      }
    }
    return result;
  }
  
  public void appendToHistory(IDataTransaction transaction, String note) throws RecordLockedException
  {
    // perform transaction check first
    checkTransaction(transaction);

    if (logger.isDebugEnabled())
      logger.debug("history note: " + note);

    for (int i = 0; i < this.tableRecords.length; i++)
    {
      DataTableRecord tableRecord = this.tableRecords[i];
      if (tableRecord != null)
      {
        IDataTransaction recordTransaction = tableRecord.getCurrentTransaction();
        if (recordTransaction != null)
        {
          tableRecord.appendToHistory(recordTransaction, note);
        }
      }
    }
  }
  
  /**
   * Internal method for rolling back changes after savepoint.
   * 
   * @param savepoint
   *          the savepoint
   */
  public void rollback(IDataSavepoint savepoint)
  {
    // IBIS: Check!
//    for (int fieldIndex = 0; fieldIndex < this.values.length; fieldIndex++)
//    {
//      if (savepoint.hasRollbackValue(fieldIndex))
//        this.values[fieldIndex] = savepoint.getRollbackValue(fieldIndex);
//    }
  }

	private void checkTransaction(IDataTransaction transaction) throws RecordLockedException
  {
    if (transaction == null)
      throw new NullPointerException("You must hand over a transaction.");

    if (!transaction.isValid())
    {
      throw new RuntimeException("Record cannot be modified by an already committed transaction");
    }

    if (!this.associatedTransaction.equals(transaction))
    {
      throw new RuntimeException("Record cannot be modified by multiple active transactions at once!");
    }
  }
  
	public IDataTransaction getCurrentTransaction()
	{
		// transaction already committed?
		if (!this.associatedTransaction.isValid())
		{
			// avoid returning inactive transactions 
			return null;
		}
		return associatedTransaction;
	}

  public IDataMultiUpdateTransaction getAssociatedTransaction()
  {
    return associatedTransaction;
  }

  public boolean isPersistent()
  {
    return false;
  }
}

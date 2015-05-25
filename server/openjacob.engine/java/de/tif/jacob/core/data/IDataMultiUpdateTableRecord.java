/*
 * Created on 27.02.2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data;

import java.util.List;

/**
 * 
 * 
 * @author Andreas Sonntag
 * @since 2.8.4
 */
public interface IDataMultiUpdateTableRecord extends IDataTableRecord
{
  /**
   * Singleton value instance which indicates diverse values of the underlying
   * records.
   * 
   * @see #getValue(int)
   * @see #getValue(String)
   * @see #getOldValue(int)
   * @see #getOldValue(String)
   * @see #getStringValue(int)
   * @see #getStringValue(String)
   */
  public static final String DIVERSE = new String("#####");

  /**
   * Same behaviour than {@link IDataRecord#getValue(int)} except that
   * {@link #DIVERSE} is returned in case underlying records have diverse
   * values.
   * 
   * @param fieldIndex
   *          the field index of the value
   * @return The field value, {@link #DIVERSE} or <code>null</code>
   */
  public Object getValue(int fieldIndex);

  /**
   * Same behaviour than {@link IDataRecord#getValue(String)} except that
   * {@link #DIVERSE} is returned in case underlying records have diverse
   * values.
   * 
   * @param fieldName
   *          the field name of the value
   * @return The field value, {@link #DIVERSE} or <code>null</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public Object getValue(String fieldName) throws NoSuchFieldException;

  /**
   * Same behaviour than {@link IDataTableRecord#getOldValue(int)} except that
   * {@link #DIVERSE} is returned in case underlying records have diverse old
   * values.
   * 
   * @param fieldIndex
   *          the field index of the value
   * @return The old field value, {@link #DIVERSE} or <code>null</code>
   */
  public Object getOldValue(int fieldIndex);

  /**
   * Same behaviour than {@link IDataTableRecord#getOldValue(String)} except
   * that {@link #DIVERSE} is returned in case underlying records have diverse
   * old values.
   * 
   * @param fieldName
   *          the field name of the value
   * @return The old field value, {@link #DIVERSE} or <code>null</code>
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public Object getOldValue(String fieldName) throws NoSuchFieldException;

  /**
   * Same behaviour than {@link IDataRecord#getStringValue(int)} except that
   * {@link #DIVERSE} is returned in case underlying records have diverse
   * values.
   * 
   * @param fieldIndex
   *          the field index of the value
   * @return the field value as string, {@link #DIVERSE} for diverse values or
   *         <code>null</code> if the value does not exist.
   */
  public String getStringValue(int fieldIndex);

  /**
   * Same behaviour than {@link IDataRecord#getStringValue(String)} except that
   * {@link #DIVERSE} is returned in case underlying records have diverse
   * values.
   * 
   * @param fieldName
   *          the field name of the value
   * @return the field value as string, {@link #DIVERSE} for diverse values or
   *         <code>null</code> if the value does not exist.
   * @throws NoSuchFieldException
   *           if no such field name exists for this record
   */
  public String getStringValue(String fieldName) throws NoSuchFieldException;
  
  /**
   * Returns the current active transaction, which is an instance of
   * {@link IDataMultiUpdateTransaction}.
   * 
   * @return Returns the current active transaction or <code>null</code>, if
   *         no current active transaction exists, e.g. the record has already
   *         been committed.
   * @see IDataMultiUpdateTransaction
   */
  public IDataTransaction getCurrentTransaction();
  
  /**
   * Returns the associated active transaction.
   * <p>
   * Note: In contrast to {@link #getCurrentTransaction()} this method always
   * returns a transaction, i.e. even if the transaction is not active (closed)
   * anymore. This is useful to obtain completion error information by means of
   * {@link IDataMultiUpdateTransaction#hasCompletionErrors()} or
   * {@link IDataMultiUpdateTransaction#getCompletionError(IDataRecord)}.
   * 
   * @return Returns the associated active transaction.
   * @see IDataMultiUpdateTransaction
   */
  public IDataMultiUpdateTransaction getAssociatedTransaction();
  
  /**
   * Returns the underlying table records which are involved by this multiple
   * update record instance.
   * 
   * @return the list of underlying table records, i.e. instances of
   *         {@link IDataTableRecord}
   */
  public List getUnderlyingRecords();
}

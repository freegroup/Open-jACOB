/*
 * Created on 27.02.2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.core.data;

import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.core.exception.UserRuntimeException;

/**
 * 
 * 
 * @author Andreas Sonntag
 * @since 2.8.4
 * @see IDataMultiUpdateTableRecord#getCurrentTransaction()
 */
public interface IDataMultiUpdateTransaction extends IDataTransaction
{
  /**
   * Checks whether this multiple update transaction runs in isolated mode, i.e.
   * each underlying record will be committed in an independent transaction, or
   * not.
   * 
   * @return <code>true</code> isolated mode is enabled, otherwise
   *         <code>false</code>
   */
  public boolean isIsolated();

  /**
   * Checks whether any transaction invocation errors exist.
   * 
   * @return <code>true</code> if invocation errors exist, otherwise
   *         <code>false</code>
   * @see #getInvocationError(IDataRecord)
   */
  public boolean hasInvocationErrors();

  /**
   * Checks whether for the given record a transaction invocation error has
   * occurred. This might happen is a record is locked by another user or not
   * found on the data source.
   * 
   * @param record
   *          the record to check for an invocation error
   * @return the invocation error or <code>null</null>
   * 
   * @see RecordLockedException
   * @see RecordNotFoundException
   */
  public Exception getInvocationError(IDataRecord record);

  /**
   * Checks whether any transaction completion errors exist.
   * 
   * @return <code>true</code> if completion errors exist, otherwise
   *         <code>false</code>
   * @throws IllegalStateException
   *           if transaction has not been committed or closed
   * @see #getCompletionError(IDataRecord)
   * @see IDataMultiUpdateTableRecord#getAssociatedTransaction()
   */
  public boolean hasCompletionErrors() throws IllegalStateException;

  /**
   * Checks whether for the given record a transaction completion error has
   * occurred. This might happen due to various reasons, e.g. required fields
   * missing, constraint violations, application specific business exceptions,
   * etc. Nevertheless only user exceptions will be handled. All other
   * exceptions will cause {@link IDataTransaction#commit()} to fail.
   * 
   * @param record
   *          the record to check for a completion error
   * @return the completion error or <code>null</null>
   * 
   * @throws IllegalStateException
   *           if transaction has not been committed or closed
   *           
   * @see IDataMultiUpdateTableRecord#getAssociatedTransaction()
   * @see UserException
   * @see UserRuntimeException
   */
  public Exception getCompletionError(IDataRecord record) throws IllegalStateException;
  
  /**
   * Returns the complete error count for this transaction.<br>
   * Useful for status message and user notification.
   * 
   * @return the count of non committed/canceled records.
   * @throws IllegalStateException
   *           if transaction has not been committed or closed
   */
  public int getCompletionErrorCount() throws IllegalStateException;
}

package de.tif.jacob.core.data;

import de.tif.jacob.core.data.IBrowserRecordList;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * Interface to return a list of {@link IDataBrowserRecord} instances.
 * <p>
 * Note: This interface is already implemented by {@link IDataBrowser}.
 * Nevertheless, an application programmer might implement this interface by
 * his own.
 * 
 * @see IBrowserEventHandler#getChildren(IClientContext, IBrowser,
 *      IDataBrowserRecord)
 * @author Andreas Sonntag
 * @since 2.7.4
 */
public interface IBrowserRecordList
{
  /**
   * Returns the number of records.
   * 
   * @return the record number
   */
  public int recordCount();
  
  /**
   * Returns the data browser record given by its index.
   * 
   * @param index
   *          the index of the record to get.
   * 
   * @return the desired record
   * @throws IndexOutOfBoundsException
   *           if <code>0 <= index < {@link IBrowserRecordList#recordCount()}</code>
   *           is not fulfilled
   */
  public IDataBrowserRecord getRecord(int index) throws IndexOutOfBoundsException;
}
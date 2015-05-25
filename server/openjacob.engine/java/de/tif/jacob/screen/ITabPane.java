/*
 * Created on 19.10.2007
 *
 */
package de.tif.jacob.screen;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

public interface ITabPane extends IPane
{

  /**
   * Returns the selected data table record of the tab pane related table alias.<br>
   * <br>
   * 
   * @return the selected data table record or <code>null</code> if no
   *               selected record exists
   * @since 2.10
   */
  public IDataTableRecord getSelectedRecord(IClientContext context) throws Exception;
 
  /**
   * Create new table record an fill them with the current input field values of the
   * group children (InputFields). Starts a new transaction.
   * 
   * @param context
   * @return
   * @since 2.9.1
   */
  public IDataTableRecord newRecord(IClientContext context) throws Exception;
  
  /**
   * Create new table record an fill them with the current input field values of the
   * group children (InputFields). 
   * 
   * @param context
   * @param trans the transaction to use
   * @return
   * @since 2.9.1
   */
  public IDataTableRecord newRecord(IClientContext context, IDataTransaction trans) throws Exception;
  
  /**
   * Install a new Action which will be displayed in a TabPane related position.
   * 
   * @param action
   * @throws Exception
   * @since 2.10
   */
  public void addAction(ITabPaneAction action) throws Exception;
}

/*
 * Created on Jun 29, 2004
 *
 */
package jacob.scheduler.system.callTaskSynchronizer;

import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.TaskContextSystem;
import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.castor.*;

/**
 *
 */
public interface IActor
{
  /**
   * 
   * @param update
   * @throws Exception
   */
  public void proceed(TaskContextSystem context,TaskUpdateType update) throws Exception;
  
  /**
   * 
   * @param insert
   * @throws Exception
   */
  public void proceed(TaskContextSystem context,TaskInsertType insert) throws Exception;
  
  /**
   * 
   * @param update
   * @throws Exception
   */
  public void proceed(TaskContextSystem context,CallUpdateType update) throws Exception;
  
  /**
   * 
   * @param insert
   * @throws Exception
   */
  public void proceed(TaskContextSystem context,CallInsertType insert) throws Exception;
  
}

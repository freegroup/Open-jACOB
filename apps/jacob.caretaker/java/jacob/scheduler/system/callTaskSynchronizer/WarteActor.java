/*
 * Created on Jun 29, 2004
 *
 */
package jacob.scheduler.system.callTaskSynchronizer;

import de.tif.jacob.scheduler.TaskContextSystem;
import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.castor.*;

/**
 *
 */
public class WarteActor  implements IActor
{
    static public final transient String RCS_ID = "";
    static public final transient String RCS_REV = "";


  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskUpdateType)
   */
  public void proceed(TaskContextSystem context,TaskUpdateType update) throws Exception
  {
    throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.update) verweigert."); 	
  }

  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskInsertType)
   */
  public void proceed(TaskContextSystem context,TaskInsertType insert) throws Exception
  {
    throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.insert) verweigert."); 	
  }

  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallUpdateType)
   */
  public void proceed(TaskContextSystem context,CallUpdateType update) throws Exception
  {
    throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (call.update) verweigert."); 	
  }

  /* 
   * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallInsertType)
   */
  public void proceed(TaskContextSystem contex,CallInsertType insert) throws Exception
  {
    throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (call.insert) verweigert."); 	
  }
}

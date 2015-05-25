/*
 * Created on 26.09.2005
 *
 */
package jacob.alert;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.messaging.alert.IAlertItemProvider;

/**
 * @author andreas
 *
 */
public class ReminderAlertItemProvider implements IAlertItemProvider
{

  /* (non-Javadoc)
   * @see de.tif.jacob.messaging.alert.IAlertItemProvider#getReceivedAlertItems(de.tif.jacob.core.Context)
   */
  public List getReceivedAlertItems(Context context) throws Exception
  {
    List result= new ArrayList();
    IDataTable callTable=context.getDataTable("reminderalert");
    callTable.clear();
    callTable.qbeClear();
    callTable.qbeSetKeyValue("addressee",context.getUser().getLoginId());
    callTable.search();
      for(int i=0;i<callTable.recordCount();i++)
      {
        IDataTableRecord call= callTable.getRecord(i);  
        result.add(new ReminderAlertItem(call));       
      }
    
    return result;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.messaging.alert.IAlertItemProvider#getSendedAlertItems(de.tif.jacob.core.Context)
   */
  public List getSendedAlertItems(Context context) throws Exception
  {
    // reminder alert items never appear on sender side
    return null;
  }
}

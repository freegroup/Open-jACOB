/*
 * Created on Jul 23, 2004
 *
 */
package jacob.alert;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.messaging.alert.*;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;

/**
 *
 */
public class BookmarkProvider implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: BookmarkProvider.java,v 1.1 2005/06/06 12:53:05 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  /*
   *  
   * @see de.tif.jacob.alert.IAlertItemProvider#getReceivedAlertItems(de.tif.jacob.core.Context)
   */
  public List getReceivedAlertItems(Context context) throws Exception
  {
    List result= new ArrayList();
    IDataTable alerts=context.getDataTable("qw_alert");
    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename","!NULL");
    alerts.qbeSetValue("addressee","="+context.getUser().getLoginId());
    if(alerts.search()>0)
    {
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        result.add(new Bookmark(alert));
      }
    }
    
    return result;
  }

  /*
   *  
   * @see de.tif.jacob.alert.IAlertItemProvider#getSendedAlertItems(de.tif.jacob.core.Context)
   */
  public List getSendedAlertItems(Context context) throws Exception
  {
    return getReceivedAlertItems(context);
  }
}

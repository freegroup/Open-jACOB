package jacob.alert;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.messaging.alert.IAlertItemProvider;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author mike
*
*/
public class AlertItemProvider implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: AlertItemProvider.java,v 1.1 2005/06/02 16:29:43 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public List getReceivedAlertItems(Context context) throws Exception
  {
    List result= new ArrayList();
    IDataTable alerts=context.getDataTable("qw_alert");
    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename","NULL");
    alerts.qbeSetValue("addressee","="+context.getUser().getLoginId());
    if(alerts.search()>0)
    {
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        result.add(new PersonalAlertItem(alert));
      }
    }
    
    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename","NULL");
    alerts.qbeSetValue("addressee","=Alle|=All");
    alerts.qbeSetValue("sender","!="+context.getUser().getLoginId());
    if(alerts.search()>0)
    {
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        result.add(new BroadcastAlertItem(alert));
      }
    }
    
    return result;
  }
  
  public List getSendedAlertItems(Context context) throws Exception
  {
    List result= new ArrayList();
    IDataTable alerts=context.getDataTable("qw_alert");
    
    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename","NULL");
    alerts.qbeSetValue("sender","="+context.getUser().getLoginId());
    if(alerts.search()>0)
    {
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        result.add(new PersonalAlertItem(alert));
      }
    }
    return result;
  }
}

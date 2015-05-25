/*
 * Created on Jul 23, 2004
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
 *
 */
public class AlertItemProvider implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: AlertItemProvider.java,v 1.9 2005/12/02 15:13:23 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.9 $";
  
  public List getReceivedAlertItems(Context context) throws Exception
  {
    List result= new ArrayList();
    IDataTable alerts=context.getDataTable("qw_alert");
    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("addressee","="+context.getUser().getLoginId()+"|=Alle|=All");
    alerts.search();
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        if (alert.getValue("tablename")==null) // kein Bookmark
        {
            String addressee = alert.getSaveStringValue("addressee");
            if (addressee.equals("Alle") || addressee.equals("All"))
            {
                result.add(new AllAlertItem(alert));
            }
            else
            {
                result.add(new UserAlertItem(alert));
            }
        }
        else
        {
            result.add(new Bookmark(alert));
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
    alerts.qbeSetValue("sender","="+context.getUser().getLoginId());
    if(alerts.search()>0)
    {
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        result.add(new UserAlertItem(alert));
      }
    }
    return result;
  }
}

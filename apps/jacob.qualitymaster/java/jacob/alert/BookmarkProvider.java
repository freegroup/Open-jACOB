/*
 * Created on Jul 23, 2004
 *
 */
package jacob.alert;

import java.util.List;

import de.tif.jacob.core.Context;

/**
 *
 */
public class BookmarkProvider// implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: BookmarkProvider.java,v 1.3 2009/01/29 07:58:14 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  /**
   *   
   * @see de.tif.jacob.alert.IAlertItemProvider#getReceivedAlertItems(de.tif.jacob.core.Context)
   */
// Das automatisch öffnende Meldungsfenster zeigt den Loginscreen und nicht die Meldungen/Alerts.
// Fehler kommt von der Umstellung des Sessionhandlings und muss erst noch bereinigt werden.
  
//  public List getReceivedAlertItems(Context context) throws Exception
//  {
//    List result= new ArrayList();
//    IDataTable alerts=context.getDataTable("incident");
//    alerts.clear();
//    alerts.qbeClear();
//    alerts.qbeSetValue("state","NEW");
//    alerts.qbeSetValue("organization_key",context.getUser().getMandatorId());
//    
//    if(alerts.search()>0)
//    {
//      for(int i=0;i<alerts.recordCount();i++)
//      {
//        IDataTableRecord alert= alerts.getRecord(i);
//        result.add(new IncidentBookmark(context, alert));
//      }
//    }
//    
//    return result;
//  }

  /*
   *  
   * @see de.tif.jacob.alert.IAlertItemProvider#getSendedAlertItems(de.tif.jacob.core.Context)
   */
//  public List getSendedAlertItems(Context context) throws Exception
//  {
//    return getReceivedAlertItems(context);
//  }
}

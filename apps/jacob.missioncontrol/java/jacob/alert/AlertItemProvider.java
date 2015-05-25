/*
 * Created on Jul 23, 2004
 *
 */
package jacob.alert;

import jacob.common.ENUM;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.messaging.alert.IAlertItemProvider;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;

/**
 *
 */
public class AlertItemProvider implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: AlertItemProvider.java,v 1.2 2005/09/15 20:01:44 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  public List getReceivedAlertItems(Context context) throws Exception
  {
    List result= new ArrayList();
    IDataTable callTable=context.getDataTable("call");
    IDataBrowser browser = context.getDataBrowser("callBrowser");
    callTable.clear();
    callTable.qbeClear();
    callTable.qbeSetValue("status","!"+ENUM.CALLSTATUS_CLOSED);
    callTable.qbeSetKeyValue("owner_key",context.getUser().getKey());
    browser.search(IRelationSet.LOCAL_NAME);
      for(int i=0;i<browser.recordCount();i++)
      {
        IDataTableRecord call= browser.getRecord(i).getTableRecord();  
        result.add(new UserAlertItem(call));       
      }
    
    return result;
  }
  
  public List getSendedAlertItems(Context context) throws Exception
  {
    return null;
  }
}

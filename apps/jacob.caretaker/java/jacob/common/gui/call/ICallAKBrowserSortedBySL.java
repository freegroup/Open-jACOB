/*
 * Created on 09.05.2006 by mike
 * 
 *
 */
package jacob.common.gui.call;

import java.util.Date;

import jacob.common.Call;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.impl.DataBrowser;
import de.tif.jacob.core.data.impl.IDataBrowserComparator;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.security.IUser;

;

public abstract class ICallAKBrowserSortedBySL  extends IButtonEventHandler
{
  public class SL_Comparator implements IDataBrowserComparator
  {
    private static final String SL_COlUMN = "browserSl";
    private static final String DATEREPORTED_COlUMN = "browserDatereported";
    private static final String PKEY_COLUMN = "browserKey";
    private static final String SL_OVERDUE_COLUMN = "browserDate_sl_overdue";
    
    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.impl.IDataBrowserComparator#compare(de.tif.jacob.core.data.IDataBrowserRecord, de.tif.jacob.core.data.IDataBrowserRecord)
     */
    public int compare(IDataBrowserRecord rec1, IDataBrowserRecord rec2)
    {
      {
        try
        {
          if (rec1.getValue(SL_COlUMN) != null)
          {
            if (rec2.getValue(SL_COlUMN) != null)
            {
              Date datereported1 = rec1.getTimestampValue(DATEREPORTED_COlUMN);
              Date datereported2 = rec2.getTimestampValue(DATEREPORTED_COlUMN);
              
              // prüfen, dass datereported nicht null (was nicht sein sollte)
              //
              if (datereported1 == null)
              {
                return datereported2 == null ? 0 : 1;
              }
              else
              {
                if (datereported2 == null)
                  return -1;
              }
              
              Date datesloverdue1 = rec1.getTimestampValue(SL_OVERDUE_COLUMN);
              Date datesloverdue2 = rec2.getTimestampValue(SL_OVERDUE_COLUMN);
              
              // Ablaufzeit berechnen, wenn SL Ablaufzeit angegeben, dann hat diese Vorrang
              //
              long sldate1 = datesloverdue1 != null ? datesloverdue1.getTime() : datereported1.getTime() + 1000 * rec1.getlongValue(SL_COlUMN);
              long sldate2 = datesloverdue2 != null ? datesloverdue2.getTime() : datereported2.getTime() + 1000 * rec2.getlongValue(SL_COlUMN);
              
              if (sldate1 == sldate2)
              {
                // wir brauchen eine dedizierte Reihenfolge, sofern beide gleich sein sollten
                return (int) (rec1.getlongValue(PKEY_COLUMN) - rec2.getlongValue(PKEY_COLUMN));
              }
              else if (sldate1 < sldate2)
              {
                // rec1 sl läuft eher ab oder ist abgelaufen
                return -1;
              }
              else
              {  
//              rec2 sl läuft eher ab oder ist abgelaufen
                return 1;
              }
            }
           
            // rec1 hat SL aber rec2 hat keinen, also rec1 vor rec2
            return -1;
          }
          else
          {
            if (rec2.getValue(SL_COlUMN) != null)
            {
              // rec2 hat SL aber rec1 hat keinen, also rec2 vor rec1
              return 1;
            }
          }
        }
        catch (Exception ex)
        {
          throw new RuntimeException(ex);
        }
       
        // ansonsten ursprüngliche Reihenfolge
        return 0;
      }

    }
    
  }
  public void findByUserAndStateAndSortedbySL(IClientContext context, IUser user, String statusConstraint, String relationSet) throws Exception 
  {
//    AdhocBrowserDefinition browserDef = new AdhocBrowserDefinition(context.getApplicationDefinition(), "call");
//    browserDef.addBrowserField("call", "pkey", "Meldungsnr.", SortOrder.NONE);
//    browserDef.addBrowserField("call", "callstatus", "Status", SortOrder.NONE);
//    browserDef.addBrowserField("callworkgroup", "name", "AK", SortOrder.NONE);
//    browserDef.addBrowserField("call", "priority", "Priorität", SortOrder.DESCENDING);
//    browserDef.addBrowserField("call", "datereported", "datereported", SortOrder.DESCENDING);
//    browserDef.addBrowserField("call", "problem", "Problem", SortOrder.NONE);
//    browserDef.addBrowserField("call", "sl", "sl", SortOrder.NONE);
//    IDataBrowser  browser  = context.getDataAccessor().createBrowser(browserDef);
    IDataBrowser  browser = context.getDataBrowser();   
   IDataBrowserComparator comparator = new SL_Comparator();
   
   // Sort comparator setzen
   ((DataBrowser) browser).setGuiSortStrategy(comparator);

   Call.searchData(context,user,browser,statusConstraint,relationSet);
   
   // display the result set
   //
   context.getGUIBrowser().setData(context, browser);

   
  }
}

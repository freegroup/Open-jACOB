package jacob.common.gui.tc_capacity;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.common.tc.TC;
import jacob.exception.BusinessException;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.util.StringUtil;

public final class Capacity
{
  static private final transient Log logger = AppLogger.getLogger();


  /**
   * Helper class
   * 
   * @author Andreas Sonntag
   */
  public static class Result
  {
    private final boolean lockNotUnlock;
    private final int updatedRecNum;
    private final int concernedPlatforms;
    
    private Result(boolean lockNotUnlock, int updatedRecNum, int concernedPlatforms)
    {
      this.lockNotUnlock = lockNotUnlock;
      this.updatedRecNum = updatedRecNum;
      this.concernedPlatforms = concernedPlatforms;
      
      logger.info(getMessage());
    }
    
    public String getMessage()
    {
      StringBuffer buf = new StringBuffer();
      buf.append("Es wurden ");
      buf.append(this.updatedRecNum);
      buf.append(this.updatedRecNum == 1 ? " Kapazitätseintrag " : " Kapazitätseinträge ");
      if (this.updatedRecNum > 1)
      {
        buf.append("von ");
        buf.append(this.concernedPlatforms);
        buf.append(this.concernedPlatforms == 1 ? " Bühne " : " Bühnen ");
      }
      buf.append(lockNotUnlock ? "gesperrt" : "entsperrt");
      buf.append(".");
      return buf.toString();
    }
  }
  
  /**
   * Locks capacity entries for the given date.
   * 
   * @param context
   * @param lockDateStr
   * @return the number of entries locked
   * @throws Exception
   */
  public static Result lock(IClientContext context, String lockDateStr) throws Exception
  {
    return doit(context, lockDateStr, true);
  }
  
  protected static void unlock(IClientContext context) throws Exception
  {
    doit(context, false);
  }
  
  protected static void lock(IClientContext context) throws Exception
  {
    doit(context, true);
  }
  
  private static void doit(IClientContext context, boolean lockNotUnlock) throws Exception
  {
    // determine lock/unlock date
    //
    ISingleDataGuiElement lockDateElement = (ISingleDataGuiElement) context.getGroup().findByName("tc_capacityLockDate");
    String lockDateStr = StringUtil.toSaveString(lockDateElement.getValue()).trim();
    if (lockDateStr.length() == 0)
      throw new BusinessException("Kein Tag ausgewählt!");

    Result result = doit(context, lockDateStr, lockNotUnlock);
    context.createMessageDialog(result.getMessage()).show();
  }
  
  private static Result doit(IClientContext context, String lockDateStr, boolean lockNotUnlock) throws Exception
  {
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context);

    // retrieve records to lock/unlock 
    //
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    IDataTransaction trans = accessor.newTransaction();
    try
    {
      IDataTable capacityTable = accessor.getTable("tc_capacity");
      capacityTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);

      // remove unbooked capacity records which are outside the new time range
      //
      capacityTable.qbeClear();
      capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
      capacityTable.qbeSetValue("slot", lockDateStr);
//      capacityTable.qbeSetValue("tc_order_key", "null");
      capacityTable.qbeSetValue("locked", lockNotUnlock ? "0" : "1");
      int recNum = capacityTable.search();
      int updatedRecNum = 0;
      // set to count concerned platforms
      Set platforms = new HashSet();
      for (int i = 0; i < recNum; i++)
      {
        IDataTableRecord capacityRecord = capacityTable.getRecord(i);

//        // lock and reload record to ensure that it has not been booked in the
//        // meanwhile
//        //
//        trans.lock(capacityRecord);
//        capacityRecord = capacityTable.loadRecord(capacityRecord.getPrimaryKeyValue());
//
//        // booked in the meanwhile
//        if (capacityRecord.getValue("tc_order_key") != null)
//          continue;

        if (capacityRecord.setValue(trans, "locked", lockNotUnlock ? "1" : "0"))
        {
          platforms.add(capacityRecord.getValue("tc_platform_key"));
          updatedRecNum++;
        }
      }

      // and flush the changes to the database
      //
      trans.commit();
      
      return new Result(lockNotUnlock, updatedRecNum, platforms.size());
    }
    finally
    {
      trans.close();
    }
  }
}

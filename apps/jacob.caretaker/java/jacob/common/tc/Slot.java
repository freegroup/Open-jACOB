package jacob.common.tc;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to handle a template slot
 */
public final class Slot
{
  /**
   * All template slots
   */
  public static final Slot[] SLOTS =
    { new Slot("slot0600", "tc_dispoTemplateSlot0600", 6, 0), new Slot("slot0615", "tc_dispoTemplateSlot0615", 6, 15), //
        new Slot("slot0630", "tc_dispoTemplateSlot0630", 6, 30), new Slot("slot0645", "tc_dispoTemplateSlot0645", 6, 45), //
        new Slot("slot0700", "tc_dispoTemplateSlot0700", 7, 0), new Slot("slot0715", "tc_dispoTemplateSlot0715", 7, 15), //
        new Slot("slot0730", "tc_dispoTemplateSlot0730", 7, 30), new Slot("slot0745", "tc_dispoTemplateSlot0745", 7, 45), //
        new Slot("slot0800", "tc_dispoTemplateSlot0800", 8, 0), new Slot("slot0815", "tc_dispoTemplateSlot0815", 8, 15), //
        new Slot("slot0830", "tc_dispoTemplateSlot0830", 8, 30), new Slot("slot0845", "tc_dispoTemplateSlot0845", 8, 45), //
        new Slot("slot0900", "tc_dispoTemplateSlot0900", 9, 0), new Slot("slot0915", "tc_dispoTemplateSlot0915", 9, 15), //
        new Slot("slot0930", "tc_dispoTemplateSlot0930", 9, 30), new Slot("slot0945", "tc_dispoTemplateSlot0945", 9, 45), //
        new Slot("slot1000", "tc_dispoTemplateSlot1000", 10, 0), new Slot("slot1015", "tc_dispoTemplateSlot1015", 10, 15), //
        new Slot("slot1030", "tc_dispoTemplateSlot1030", 10, 30), new Slot("slot1045", "tc_dispoTemplateSlot1045", 10, 45), //
        new Slot("slot1100", "tc_dispoTemplateSlot1100", 11, 0), new Slot("slot1115", "tc_dispoTemplateSlot1115", 11, 15), //
        new Slot("slot1130", "tc_dispoTemplateSlot1130", 11, 30), new Slot("slot1145", "tc_dispoTemplateSlot1145", 11, 45), //
        new Slot("slot1200", "tc_dispoTemplateSlot1200", 12, 0), new Slot("slot1215", "tc_dispoTemplateSlot1215", 12, 15), //
        new Slot("slot1230", "tc_dispoTemplateSlot1230", 12, 30), new Slot("slot1245", "tc_dispoTemplateSlot1245", 12, 45), //
        new Slot("slot1300", "tc_dispoTemplateSlot1300", 13, 0), new Slot("slot1315", "tc_dispoTemplateSlot1315", 13, 15), //
        new Slot("slot1330", "tc_dispoTemplateSlot1330", 13, 30), new Slot("slot1345", "tc_dispoTemplateSlot1345", 13, 45), //
        new Slot("slot1400", "tc_dispoTemplateSlot1400", 14, 0), new Slot("slot1415", "tc_dispoTemplateSlot1415", 14, 15), //
        new Slot("slot1430", "tc_dispoTemplateSlot1430", 14, 30), new Slot("slot1445", "tc_dispoTemplateSlot1445", 14, 45), //
        new Slot("slot1500", "tc_dispoTemplateSlot1500", 15, 0), new Slot("slot1515", "tc_dispoTemplateSlot1515", 15, 15), //
        new Slot("slot1530", "tc_dispoTemplateSlot1530", 15, 30), new Slot("slot1545", "tc_dispoTemplateSlot1545", 15, 45), //
        new Slot("slot1600", "tc_dispoTemplateSlot1600", 16, 0), new Slot("slot1615", "tc_dispoTemplateSlot1615", 16, 15), //
        new Slot("slot1630", "tc_dispoTemplateSlot1630", 16, 30), new Slot("slot1645", "tc_dispoTemplateSlot1645", 16, 45), //
        new Slot("slot1700", "tc_dispoTemplateSlot1700", 17, 0), new Slot("slot1715", "tc_dispoTemplateSlot1715", 17, 15), //
        new Slot("slot1730", "tc_dispoTemplateSlot1730", 17, 30), new Slot("slot1745", "tc_dispoTemplateSlot1745", 17, 45), //
        new Slot("slot1800", "tc_dispoTemplateSlot1800", 18, 0), new Slot("slot1815", "tc_dispoTemplateSlot1815", 18, 15), //
        new Slot("slot1830", "tc_dispoTemplateSlot1830", 18, 30), new Slot("slot1845", "tc_dispoTemplateSlot1845", 18, 45), //
        new Slot("slot1900", "tc_dispoTemplateSlot1900", 19, 0), new Slot("slot1915", "tc_dispoTemplateSlot1915", 19, 15), //
        new Slot("slot1930", "tc_dispoTemplateSlot1930", 19, 30), new Slot("slot1945", "tc_dispoTemplateSlot1945", 19, 45) //
    };

  /**
   * Slot size in minutes
   */
  public static final int SLOT_SIZE = 15;
  
  /**
   * Slots per hour
   */
  public static final int SLOTS_PER_HOUR = 60 / SLOT_SIZE;
  
  /**
   * Hour of the first slot of a day
   */
  public static final int FIRST_HOUR = 6;
  
  /**
   * Total hour range of possible slots in a day
   */
  public static final int TOTAL_HOURS = (int) Math.floor((double) SLOTS.length / SLOTS_PER_HOUR);
  
  public final String dbName;
  public final String guiName;
  public final int hour;
  public final int mins;

  private Slot(String dbName, String guiName, int hour, int mins)
  {
    this.dbName = dbName;
    this.guiName = guiName;
    this.hour = hour;
    this.mins = mins;
  }

  public Date getTime(Calendar calendar)
  {
    calendar.set(Calendar.HOUR_OF_DAY, this.hour);
    calendar.set(Calendar.MINUTE, this.mins);
    calendar.set(Calendar.SECOND, 0);
    Date res = calendar.getTime();

    // reset hour and mins
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);

    return res;
  }
}

/**
 * 
 */
package jacob.common.sap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.core.exception.UserException;

/**
 * Formatiert Datum und Uhrzeit gemäß RFC Anforderung
 * 
 * @author Achim Böken
 */
public class DateFormater
{

  /**
   * 
   */
  private DateFormater()
  {
  }

  private static final SimpleDateFormat DAY_FMT = new SimpleDateFormat("yyyyMMdd");
  private static final SimpleDateFormat HOUR_FMT = new SimpleDateFormat("HHmmss");
  private static final SimpleDateFormat DAY_HOUR_FMT = new SimpleDateFormat("yyyyMMddHHmmss");

  /**
   * Formatierung des Datums in JJJJMMTT
   * 
   * @param datereported,
   *          Date, das zu formatierende Datum
   * @exception IllegalArgumentException
   *              datereported == null
   */
  public static String GetStringDate(Date datereported)
  {
    if (null == datereported)
      throw new IllegalArgumentException("Need a date");

    return DAY_FMT.format(datereported);

  }

  /**
   * Formatierung der Uhrzeit in HHMMSS Wobei SS immer 00 wird
   * 
   * @param datereported,
   *          Date, die zu formatierende Uhrzeit
   */
  public static String GetStringTime(Date datereported)
  {
    if (null == datereported)
      throw new IllegalArgumentException("Need a date");

    return HOUR_FMT.format(datereported);

  }

  // ----------------------------------------------------------------------------
  /**
   * @param sDay
   *          Tagesangabe aus SAP in der Form YYYYMMDD
   * @param sTime
   *          Zeitangabe aus SAP in der Form HHMMSS
   * 
   * @return Datum aus, null wenn Fehler
   */
  public static Date SAP2Java(final String sDay, final String sTime)
  {
    if (null == sTime || "".equals(sTime))
      throw new IllegalArgumentException("Need a time");

    if (null == sDay || "".equals(sDay))
      throw new IllegalArgumentException("Need a day");

    try
    {
      return DAY_HOUR_FMT.parse(sDay + sTime);
    }
    catch (ParseException oX)
    {
      oX.printStackTrace();
      return null;
    }
  }
  public static Date SAP2Java(final String sDay)
  {


    if (null == sDay || "".equals(sDay))
      throw new IllegalArgumentException("Need a day");

    try
    {
      return DAY_HOUR_FMT.parse(sDay + "000000");
    }
    catch (ParseException oX)
    {
      oX.printStackTrace();
      return null;
    }
  }
  private static final Map SAP_JCOB_OBJSITE = new HashMap();
  static
  // Mapping für Objekt status
  {
    SAP_JCOB_OBJSITE.put("50", "50");
    SAP_JCOB_OBJSITE.put("050", "50");
    SAP_JCOB_OBJSITE.put("59", "59");
    SAP_JCOB_OBJSITE.put("059", "59");
    SAP_JCOB_OBJSITE.put("066", "996");
    SAP_JCOB_OBJSITE.put("66", "996");
  }
  public static void main(String[] args) throws Exception
  {
//    String x ="50-34_2-SSTRAST";
//String[] aX =x.split("-");
//System.out.println(aX.length);
//System.out.println(aX[aX.length-1]);
//System.out.println("ösdflköflkölk\r\n2.zeile");
//StringBuffer buf= new StringBuffer(); 
//buf.append("Störmeldung 311892 bitte über die SAP-Meldung 10886987 abschließen (Tr. IW22)");
//String x = buf.substring(46, 54);
//System.out.println(x);
    
    
//    SimpleDateFormat ORA = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//    String sdate = new Date()+"";
//   System.out.println(ORA.format(new Date()));
    
    
    /*Date oDate = SAP2Java("20071231"); System.out.println(oDate);
     * 
     * Date oDate = SAP2Java("20071231","232411"); System.out.println(oDate);
     * 
     * oDate = SAP2Java("99991231","240000"); System.out.println(oDate);
     * 
     * oDate = new Date();
     * 
     * System.out.println(GetStringDate(oDate));
     * System.out.println(GetStringTime(oDate)); Calendar cal2 =
     * Calendar.getInstance(); System.out.println(cal2.getTime());
     * 
     * //cal2.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY) + 48);
     * cal2.add(Calendar.HOUR_OF_DAY, 48); System.out.println(cal2.getTime());
     * 
     * List stuff = Arrays.asList(new String[]{"Verworfen", "Dokumentiert",
     * "Geschlossen"}); if (stuff.contains("Dokumentiert ")) {
     * System.out.println("gut"); } else{ System.out.println("schlecht"); }
     */

/*for (int i = 0; i < 10; i++) 
{
  if (i==6)
  {
    break;
  }
  System.out.println(i+"www");
}

System.out.println("Ende");
  */
  if (SAP_JCOB_OBJSITE.containsKey("590"))
  {
    System.out.println(SAP_JCOB_OBJSITE.get("59"));
  } 
  }
}

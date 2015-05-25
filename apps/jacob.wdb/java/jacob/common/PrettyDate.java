package jacob.common;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class PrettyDate
{
  private static Map<String,String> mehrzahl = new HashMap<String,String>();
  static
  {
    mehrzahl.put("Jahr", "Jahren");
    mehrzahl.put("Monat", "Monaten");
    mehrzahl.put("Woche", "Wochen");
    mehrzahl.put("Tag", "Tagen");
    mehrzahl.put("Stunde", "Stunden");
    mehrzahl.put("Minute", "Minuten");
    mehrzahl.put("Sekunde", "Sekunden");
  }
  
  public static String toString(Date date)
  {
    long current = (new Date()).getTime(), timestamp = date.getTime(), diff = (current - timestamp) / 1000;
    int amount = 0;
    String what = "";
    String between ="";
    /**
     * Second counts 3600: hour 86400: day 604800: week 2592000: month 31536000:
     * year
     */
    if (diff > 31536000)
    {
      amount = (int) (diff / 31536000);
      what = "Jahr";
      between ="einem";
    }
    else if (diff > 2592000)
    {
      amount = (int) (diff / 2592000);
      what = "Monat";
      between ="einem";
    }
    else if (diff > 604800)
    {
      amount = (int) (diff / 604800);
      what = "Woche";
      between ="einer";
    }
    else if (diff > 86400)
    {
      amount = (int) (diff / 86400);
      what = "Tag";
      between ="einem";
    }
    else if (diff > 3600)
    {
      amount = (int) (diff / 3600);
      what = "Stunde";
      between ="einer";
    }
    else if (diff > 60)
    {
      amount = (int) (diff / 60);
      what = "Minute";
      between ="einer";
    }
    else
    {
      amount = (int) diff;
      what = "Sekunde";
      between ="einer";
      if (amount < 6)
      {
        return "Eben gerade";
      }
    }
    if (amount == 1)
    {
      if (what.equals("Tag"))
      {
        return "Gestern";
      }
      else if (what.equals("Jahr"))
      {
        return "Letztes " + what;
      }
    }
    else
    {
        what = mehrzahl.get(what);
    }
    return "vor "+(amount==1?between:Integer.toString(amount,10)) + " " + what;
  }
  
  public static void main(String[] args)
  {
    Calendar cal = new GregorianCalendar();
    Date now = new Date();
    System.out.println(toString(cal.getTime()));
    
    for(int i=0;i<10;i++)
    {
      cal.add(Calendar.SECOND, -1);
      System.out.println(toString(cal.getTime()));
    }
    
    for(int i=0;i<10;i++)
    {
      cal.add(Calendar.MINUTE, -1);
      System.out.println(toString(cal.getTime()));
    }
     
    for(int i=0;i<10;i++)
    {
      cal.add(Calendar.HOUR, -1);
      System.out.println(toString(cal.getTime()));
    }
    
    for(int i=0;i<70;i++)
    {
      cal.add(Calendar.DATE, -1);
      System.out.println(toString(cal.getTime()));
    }
   }
}

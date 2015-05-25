package jacob.common;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;

public class MenuManager
{
  private final static String PROPERTY_UUID = "5149886C-8ABE-11DF-908E-8DE8DED72085";
  private final static String TIMESTAMP_UUID = "133E62C0-A08C-11DF-9306-00A9DFD72085";
  
  private static long lastInvalidateTimestamp=0;

  public static void invalidate()
  {
    lastInvalidateTimestamp = System.currentTimeMillis();
  }
  
  public static MenuEntry[] getEntries(IClientContext context)
  {
    List<MenuEntry> entries = getSave(context);
    synchronized (entries)
    {
      return (MenuEntry[])entries.toArray(new MenuEntry[entries.size()]);
    }
  }
  
  /**
   * Bei deployment einer jACOB Anwendung wird die ClassDefinition von HistoryEvent ausgetauscht.
   * Es kommt somit zu einer ClassCastException. Die ignorieren wir einfach und schmeissen die History einfach
   * weg.
   * 
   * @return
   */
  private static List<MenuEntry> getSave(IClientContext context)
  {
    List<MenuEntry> entries = null;
    try
    {
      Long myReloadTimestmap = (Long)context.getProperty(TIMESTAMP_UUID);
      if(myReloadTimestmap!=null)
      {
        if(myReloadTimestmap >=lastInvalidateTimestamp)
          entries = (List<MenuEntry>)context.getProperty(PROPERTY_UUID);
      }
    }
    catch (ClassCastException e)
    {
      // ignore the ClassCastException
    }
    
    if(entries == null)
    {
      List<IDataTableRecord> records = ActiveMenutreeUtil.getRootNodes(context);
      entries = new ArrayList<MenuEntry>();
      for (IDataTableRecord record : records)
      {
        try
        {
          entries.add(new MenuEntry(record));
        }
        catch(Exception e)
        {
          //ignore and report in catalina.out
          ExceptionHandler.handle(e);
        }
      }
      context.setPropertyForWindow(PROPERTY_UUID, entries);
    }
    return entries;
  }
}

package jacob.common;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;

public class HistoryManager
{
  private final static String PROPERTY_UUID = "5149886C-8ABE-11DF-908E-8DE8DED72085";
  private final static int COUNT = 10;
  
  public static void append(IClientContext context, IGroup displayGroup) throws Exception
  {
    if(context.getSelectedRecord()==null)
      return;
    
    if(context.getSelectedRecord().isNewOrUpdated())
      return;
    
    try
    {
      List<HistoryEntry> entries = getSave(context);
      synchronized (entries)
      {
        HistoryEntry entry = new HistoryEntry(context, displayGroup);
        if(entries.contains(entry))
          return;
        entries.add(0, entry);
        
        if(entries.size()>=COUNT)
          entries.remove(entries.size()-1);
        for (HistoryEntry historyEntry : entries)
        {
          System.out.println(historyEntry);
        }
      }
    }
    catch(ClassCastException exc)
    {
      // kann bei einem hotdeployment passieren. Alte HistoryEntries einfach entfernen
      context.setPropertyForWindow(PROPERTY_UUID, new ArrayList<HistoryEntry>());
    }
  }

  public static HistoryEntry[] getEntries(IClientContext context)
  {
    List<HistoryEntry> entries = getSave(context);
    synchronized (entries)
    {
      return (HistoryEntry[])entries.toArray(new HistoryEntry[entries.size()]);
    }
  }
  
  /**
   * Bei deployment einer jACOB Anwendung wird die ClassDefinition von HistoryEvent ausgetauscht.
   * Es kommt somit zu einer ClassCastException. Die ignorieren wir einfach und schmeissen die History einfach
   * weg.
   * 
   * @return
   */
  private static List<HistoryEntry> getSave(IClientContext context)
  {
    List<HistoryEntry> entries = null;
    try
    {
      entries = (List<HistoryEntry>)context.getProperty(PROPERTY_UUID);
    }
    catch (ClassCastException e)
    {
      // ignore the ClassCastException
    }
    
    if(entries == null)
    {
      entries = new ArrayList<HistoryEntry>();
      context.setPropertyForWindow(PROPERTY_UUID, entries);
    }
    return entries;
  }
}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 08 20:38:45 CEST 2010
 */
package jacob.event.ui;

import jacob.common.HistoryEntry;
import jacob.common.HistoryManager;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.event.IDomainEventHandler;

/**
 *
 * @author andherz
 */
public class History extends IDomainEventHandler
{
	static public final transient String RCS_ID = "$Id: History.java,v 1.1 2010-09-17 08:42:21 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  class DomainNavigationPanel implements INavigationPanel
  {
    public INavigationEntry[] getNavigationEntries(IClientContext context, IDomain domain)
    {
      // die letzten Events aus der Datenbank lesen
      //
      try
      {
        HistoryEntry[] history = HistoryManager.getEntries(context);
        int recordCount = history.length;
        INavigationEntry[] entries = new INavigationEntry[recordCount];
        for (int i = 0; i < recordCount; i++)
        {
          String label = history[i].getLabel();
          entries[i] = new INavigationEntry(domain, "unused", label, "showx", ""+i);
        }
        return entries;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      return new INavigationEntry[0];
    }
  }


  @Override
  public INavigationPanel getNavigationPanel(IClientContext context, IDomain domain) throws Exception
  {
    return new DomainNavigationPanel();
  }


  @Override
  public void onNavigation(IClientContext context, IDomain domain, String navigationId, String navigationData) throws Exception
  {
    int index = Integer.parseInt(navigationData);
    HistoryManager.getEntries(context)[index].show(context);
  }
  
}

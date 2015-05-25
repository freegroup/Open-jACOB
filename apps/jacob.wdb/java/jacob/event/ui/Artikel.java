/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 05 13:24:13 CEST 2010
 */
package jacob.event.ui;

import jacob.common.MenuEntry;
import jacob.common.MenuManager;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDomain;
import de.tif.jacob.screen.event.IDomainEventHandler;
import de.tif.jacob.screen.impl.HTTPDomain;

/**
 *
 * @author andherz
 */
public class Artikel extends IDomainEventHandler
{
	static public final transient String RCS_ID = "$Id: Artikel.java,v 1.3 2010-10-21 10:29:21 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";


  private static final class DomainNavigationPanel implements INavigationPanel
  {
    public INavigationEntry[] getNavigationEntries(IClientContext context, IDomain domain)
    {
      // die letzten Events aus der Datenbank lesen
      //
      try
      {
        MenuEntry[] history = MenuManager.getEntries(context);
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
        ExceptionHandler.handle(e);
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
    MenuManager.getEntries(context)[index].show(context);
  }

  public static void setCurrentNavigationEntry(IClientContext context, IDataKeyValue menutreePkey) throws Exception
  {
    MenuEntry[] entries = MenuManager.getEntries(context);
    for (int i = 0; i < entries.length; i++)
    {
      if (entries[i].getMenutreeEntryPkey().equals(menutreePkey))
      {
        HTTPDomain domain = (HTTPDomain) context.getDomain();
        INavigationEntry[] naventries = domain.getNavigationEntries(context);
        if (naventries != null && naventries.length > i)
          domain.setCurrentNavigationEntry(naventries[i]);
      }
    }
  }
}

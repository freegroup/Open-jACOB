/*
 * Created on 07.07.2010
 *
 */
package de.tif.jacob.urlredirect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.deployment.DeployNotifyee;

/**
 * Redirectmanager
 * @since 2.10
 */
public class RedirectManager implements DeployNotifyee
{
  /*
   * A collection of redirect rules.
   */
  private static Map redirectRules = new HashMap();
  private static AbstractRule[] rules = new AbstractRule[0];
  private static boolean registerd = false;
  private final static RedirectManager INSTANCE = new RedirectManager();
  
  public synchronized void afterRedeploy(DeployEntry newEntry) throws Exception
  {
  }

  public synchronized void beforeRedeploy(DeployEntry oldEntry) throws Exception
  {
    IApplicationDefinition appDef= DeployMain.getApplication(oldEntry.getName(),oldEntry.getVersion());
    removeRelatedEntries(appDef);
  }

  public synchronized void onDeploy(DeployEntry entry) throws Exception
  {
  }

  public synchronized void onUndeploy(DeployEntry entry) throws Exception
  {
    IApplicationDefinition appDef= DeployMain.getApplication(entry.getName(),entry.getVersion());
    removeRelatedEntries(appDef);
  }

  /**
   * Return the install redirect rules
   * 
   * @return the rules. Return values is never <b>null</b>
   */
  protected synchronized static AbstractRule[] getRules()
  {
    if(registerd ==false)
    {
      registerd=true;
      DeployManager.registerNotifyee(INSTANCE);
    }
    
    return rules;
  }

  public synchronized static void installRedirect(IApplicationDefinition appDef, String urlPattern, String target)
  {
    if(registerd ==false)
    {
      registerd=true;
      DeployManager.registerNotifyee(INSTANCE);
    }
    
    redirectRules.put(urlPattern, new URLRedirectRule(appDef,urlPattern, target));
    rules = (AbstractRule[]) redirectRules.values().toArray(new AbstractRule[0]);
//    dump();
  }

  /**
   * Install a forward rule for the application/engine
   * 
   * @param appDef
   * @param urlPattern
   * @param target
   */
  public synchronized static void installForward(IApplicationDefinition appDef, String urlPattern, String target)
  {
    if(registerd ==false)
    {
      registerd=true;
      DeployManager.registerNotifyee(INSTANCE);
    }
    
    redirectRules.put(urlPattern, new URLForwardRule(appDef,urlPattern, target));
    rules = (AbstractRule[]) redirectRules.values().toArray(new AbstractRule[0]);
 //   dump();
  }

  private void removeRelatedEntries(IApplicationDefinition appDef)
  {
    List tmp = new ArrayList(new HashMap(redirectRules).keySet());
    Iterator iter = tmp.iterator();
    while(iter.hasNext())
    {
      String urlPattern = (String)iter.next();
      AbstractRule rule = (AbstractRule)redirectRules.get(urlPattern);
      if(rule.isFrom(appDef))
        redirectRules.remove(urlPattern);
    }
    rules = (AbstractRule[]) redirectRules.values().toArray(new AbstractRule[0]);
  }
  
  private static void dump()
  {
    System.out.println("Installed rules:");
    AbstractRule[] tmp = rules;
    for (int i = 0; i < tmp.length; i++)
    {
      System.out.println("\t"+tmp[i].toString());
    }
  }
}

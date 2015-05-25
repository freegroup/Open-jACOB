/*
 * Created on 20.04.2007
 *
 */
package de.tif.jacob.screen.impl.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.deployment.DeployEntry;
import de.tif.jacob.deployment.DeployMain;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.impl.HTTPApplication;
import de.tif.jacob.security.IUser;
import de.tif.jacob.security.UserManagement;

public final class CompositeClientSession extends ClientSession
{
  transient private ClientSession baseSession;
  transient private final Map sessionid2Session = new HashMap();
//  transient String firstBrowserId = null;
//  transient boolean init = false;
  transient private boolean isAdminSession = false;
  
  public CompositeClientSession(HttpServletRequest request, IUser user, String passwd, IApplicationDefinition applicationDef) throws Exception
  {
    super(request, user, applicationDef);
    isAdminSession = applicationDef.getName().equals("admin");
    
    if(isAdminSession)
      return;
    
    System.out.println("CREATE all applications............");
    
    IUser u=UserManagement.getValid(applicationDef.getName(),applicationDef.getVersion().toString(),user.getLoginId(),passwd);
    this.baseSession = new ClientSession(request,u,applicationDef);
    System.out.println("\t - "+applicationDef.getName());
    sessionid2Session.put(this.baseSession.getId(), this.baseSession);
    
    List entries = DeployManager.getDeployEntries();
    for (Iterator iter = entries.iterator(); iter.hasNext();)
    {
      DeployEntry entry = (DeployEntry) iter.next();
      if (entry.getStatus().isProductive() && entry.isDaemon()==false && !entry.getName().equals("admin"))
      {
        IApplicationDefinition appDef = DeployMain.getApplication(entry.getName(),entry.getVersion().toString());
        if (appDef.equals(applicationDef))
          continue;
        
        u=UserManagement.getValid(entry.getName(),entry.getVersion().toString(),user.getLoginId(),passwd);
        ClientSession session = new ClientSession(request,u,appDef);

        // FREEGROUP:
//        String  browserId=new java.rmi.server.UID().toString();
//        if(firstBrowserId==null)
//          firstBrowserId = browserId;
        System.out.println("\t - "+appDef.getName());
        sessionid2Session.put(session.getId(), session);
      }
    }
  }
  
  public synchronized HTTPApplication createApplication() throws Exception
  {
    if(isAdminSession)
      return super.createApplication();
    
    return this.baseSession.createApplication();
  }

  public synchronized IApplication getApplication(String browserId) throws Exception
  {
    if(isAdminSession)
      return super.getApplication(browserId);
    
    if (browserId == null)
      return null;
    
    // nicht mehr notwendig
//    // erster Aufruf
//    if(init==false)
//    {
//      ClientSession session = (ClientSession)browser2Session.remove(firstBrowserId);
//      browser2Session.put(browserId,session);
//      init = true;
//    }
    
    return ((ClientSession) sessionid2Session.get(getSessionIdFromApplicationId(browserId))).getApplication(browserId);
  }

  public synchronized void sendKeepAlive(String guid)
  {
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      session.sendKeepAlive(key);
    }
  }
  
  public List getDomains(String browserId) throws Exception
  {
    List result = new ArrayList();
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      result.addAll(session.getDomains(key));
    }
    return result;
  }

  public Collection createSessionTasks()
  {
    Collection result = new ArrayList();
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      result.addAll(session.createSessionTasks());
    }
    return result;
  }

  public synchronized String fetchAsynchronJavaScript()
  {
    StringBuffer result = new StringBuffer();
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      result.append(session.fetchAsynchronJavaScript());
    }
    return result.toString();
  }

  
  protected synchronized void setCurrentTheme(String newThemeId)
  {
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      session.setCurrentTheme(newThemeId);
    }
  }

  public synchronized void setAlertMustRefresh(boolean alertMustRefresh)
  {
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      session.setAlertMustRefresh(alertMustRefresh);
    }
  }

  public void setRuntimeProperty(String property, int value)
  {
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      session.setRuntimeProperty(property,value);
    }
  }

  public void setRuntimeProperty(String property, String value)
  {
    Iterator iter = sessionid2Session.keySet().iterator();
    while (iter.hasNext())
    {
      String key = (String) iter.next();
      ClientSession session = (ClientSession)sessionid2Session.get(key);
      session.setRuntimeProperty(property, value);
    }
  }
  
}

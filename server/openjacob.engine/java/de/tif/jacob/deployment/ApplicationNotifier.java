/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.deployment;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.event.IApplicationEventHandler;

/**
 * Falls eine Application deployed wird muss diese darüber informiert werden.
 * Dies wird allerdings nur dann getan, wenn die Applikation aktiv ist
 * 
 * onStartup
 * onShutdown
 */
public class ApplicationNotifier  extends BootstrapEntry implements DeployNotifyee
{
  private final static ApplicationNotifier instance = new ApplicationNotifier();
  
  public void onUndeploy(DeployEntry entry) throws Exception
  {
    // Eine auf 'active' geschaltete Applikation wird aus dem System entfernt
    //
    if(entry.getStatus().isActive()==true)
    {
      IApplicationEventHandler handler=getEventHandler(entry);
      if(handler!=null)
        handler.onShutdown(DeployMain.getApplication(entry.getName(),entry.getVersion()));
    }
  }

  public void onDeploy(DeployEntry entry) throws Exception
  {
    // eine auf 'active' geschaltete Application wird neu deployed
    //
    if(entry.getStatus().isActive()==true)
    {
      IApplicationEventHandler handler=getEventHandler(entry);
      if(handler!=null)
        handler.onStartup(DeployMain.getApplication(entry.getName(),entry.getVersion()));
    }
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.deployment.DeployNotifyee#afterRedeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void afterRedeploy(DeployEntry newEntry) throws Exception
  {
    onDeploy(newEntry);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.deployment.DeployNotifyee#beforeRedeploy(de.tif.jacob.deployment.DeployEntry)
   */
  public void beforeRedeploy(DeployEntry oldEntry) throws Exception
  {
    onUndeploy(oldEntry);
  }
  
  private IApplicationEventHandler getEventHandler(DeployEntry entry) throws Exception
  {
    AbstractApplicationDefinition appDef = (AbstractApplicationDefinition)DeployMain.getApplication(entry.getName(),entry.getVersion());
    String eventHandler = appDef.getEventHandler();
    Object obj = ClassProvider.getInstance(appDef,eventHandler);
    if(obj!=null)
    {
      if(!(obj instanceof IApplicationEventHandler))
        throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IApplicationEventHandler.class.getName()+"]");
    }
    return (IApplicationEventHandler)obj;
  }

  // BootstrapEntry interface implementations
  //
  public void destroy() throws Throwable
  {
  }
  
  public void init() throws Throwable
  {
    DeployManager.registerNotifyee(instance);
  }
}

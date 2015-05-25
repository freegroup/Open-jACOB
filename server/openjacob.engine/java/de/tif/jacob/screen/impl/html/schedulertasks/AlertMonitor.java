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

package de.tif.jacob.screen.impl.html.schedulertasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tif.jacob.core.Property;
import de.tif.jacob.core.definition.actiontypes.ActionTypeShowAlert;
import de.tif.jacob.messaging.alert.AlertManager;
import de.tif.jacob.messaging.alert.IAlertItem;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskUser;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.scheduler.iterators.SecondsIterator;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.ImageDecoration;
import de.tif.jacob.screen.impl.HTTPClientSession;
import de.tif.jacob.screen.impl.html.ClientSession;

/**
 *
 */
public class AlertMonitor extends SchedulerTaskUser
{
  private Set lastVisibleAlerts= new HashSet();
  private boolean first = true;
  
  public AlertMonitor(ClientSession session)
  {
    setSession(session);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTask#iterator()
   */
  public ScheduleIterator iterator()
  {
    return new SecondsIterator(Property.CHECKINTERVAL_ALERT);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.scheduler.SchedulerTaskUser#run(de.tif.jacob.scheduler.TaskContextUser)
   */
  public void run(TaskContextUser context) throws Exception
  {
    // Timeout counter für das Fenster herrunter zählen. Falls das Fenster keinen KeepALive sendet,
    // wird dieses dann von der Hauptanwendung geschlossen.
    //
    HTTPClientSession httpClientSession = (HTTPClientSession) context.getSession();
    if (httpClientSession.isAlertVisible())
      httpClientSession.decrementAlertBeforeDestroy(Property.CHECKINTERVAL_ALERT.getIntValue());

    Set newVisibleAlerts= new HashSet();
    boolean hasNewAlert=false;
    if (first)
    {
      // avoid that always the alert dialog is popped up after login. 
      // Should only pop up, if there are alerts not older than 5 mins!
      long timeBarrier = System.currentTimeMillis() - 5 * 60 * 1000;
      List list = AlertManager.getReceivedAlertItems(context);
      for (int i = 0; i < list.size(); i++)
      {
        IAlertItem alert = (IAlertItem) list.get(i);
        if (hasNewAlert == false && alert.getDate().getTime() > timeBarrier)
          hasNewAlert = true;
        newVisibleAlerts.add(alert.getKey());
      }
      first = false;
    }
    else
    {
      List list = AlertManager.getReceivedAlertItems(context);
      for (int i = 0; i < list.size(); i++)
      {
        IAlertItem alert = (IAlertItem) list.get(i);
        if (hasNewAlert == false && !lastVisibleAlerts.contains(alert.getKey()))
          hasNewAlert = true;
        newVisibleAlerts.add(alert.getKey());
      }
    }
    lastVisibleAlerts = newVisibleAlerts;
    
    if(hasNewAlert)
    {
      // set flag to indicate that new alerts have been received.
      // Note: This flag will be reset, if the alert dialog is opened or refreshed
      httpClientSession.setAlertMustRefresh(true);
      AlertManager.show(context);
    }
    
    // handle alert button decoration
    //
    synchronized (httpClientSession)
    {
      ActionTypeShowAlert actionTypeShowAlert = new ActionTypeShowAlert();
      Iterator iter = httpClientSession.getApplications().iterator();
      if (httpClientSession.isAlertMustRefresh())
      {
        while (iter.hasNext())
        {
          IApplication app = (IApplication) iter.next();
          app.getToolbar().getButton(actionTypeShowAlert).setDecoration(ImageDecoration.EMAIL);
          app.getToolbar().getButton(actionTypeShowAlert).setVisible(true);
          app.getToolbar().getButton(actionTypeShowAlert).setEmphasize(true);
        }
      }
      else
      {
        while (iter.hasNext())
        {
          IApplication app = (IApplication) iter.next();
          app.getToolbar().getButton(actionTypeShowAlert).setDecoration(ImageDecoration.NONE);
          app.getToolbar().getButton(actionTypeShowAlert).setEmphasize(false);
        }
      }
    }
  }
}

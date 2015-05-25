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

package de.tif.jacob.messaging.alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.Session;
import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.UserRuntimeException;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.scheduler.TaskContextUser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.impl.HTTPClientSession;

/**
 *
 */
public class AlertManager
{
  private static final String QW_ALERT_ALIAS = "qw_alert";
  
  public static final String ALL_BROADCAST_ROLE = "All"; 
  
  /**
   * Checks whether to use the Quintus alert mechanism.
   * 
   * @param applicationDefinition
   * @return 
   */
  private static boolean useQuintusAlerts(IApplicationDefinition applicationDefinition)
  {
    try
    {
      applicationDefinition.getTableAlias(QW_ALERT_ALIAS);
      return true;
    }
    catch (Exception ex)
    {
      // obviously no Quintus alert table present -> use jACOB mechanism
      return false;
    }
  }
  
  /**
   * Sends an alert message to the given recipien.
   * 
   * @param recipien
   * @param message
   * @throws Exception
   */
  public static void sendAlert(String recipien, String message, String url) throws Exception
  {
    Context context = Context.getCurrent();
    IApplicationDefinition applicationDefinition = context.getApplicationDefinition();

    // check which alert mechanismen to use
    if (useQuintusAlerts(applicationDefinition))
    {
      // Quintus mechanism
      //
      IDataAccessor accessor = context.getDataAccessor();
      IDataTable alertTable = accessor.getTable(QW_ALERT_ALIAS);
      IDataTransaction trans = accessor.newTransaction();
      try
      {
        IDataTableRecord alertRecord = alertTable.newRecord(trans);
        alertRecord.setValue(trans, "addressee", recipien);
        alertRecord.setValue(trans, "sender", context.getUser().getLoginId());
        // truncate message if too long
        alertRecord.setStringValueWithTruncation(trans, "message", message);
        // IBIS: Check severity!
        alertRecord.setIntValue(trans, "severity", 0);
        // IBIS: Check alerttype!
        alertRecord.setValue(trans, "alerttype", "Senden");
        alertRecord.setValue(trans, "dateposted", "now");
        
        trans.commit();
      }
      finally
      {
        trans.close();
      }
    }
    else
    {
      // jACOB mechanism
      //
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTable alertTable = accessor.getTable(JacobAlertItemProvider.JACOB_ALERT_ALIAS);
      IDataTransaction trans = accessor.newTransaction();
      try
      {
        // create alert message record
        IDataTableRecord alertRecord = alertTable.newRecord(trans);
        alertRecord.setValue(trans, "sender", context.getUser().getLoginId());
        // truncate message if too long
        alertRecord.setStringValueWithTruncation(trans, "message", message);
        alertRecord.setValue(trans, "url", url);
        alertRecord.setValue(trans, "severity", "normal");
        alertRecord.setValue(trans, "created", "now");
        alertRecord.setValue(trans, "sourceapplication", applicationDefinition.getName());
        
        // and create alert addressee record
        IDataTable addresseeTable = accessor.getTable(JacobAlertItemProvider.JACOB_ALERT_ADDRESSEE_ALIAS);
        IDataTableRecord addresseeRecord = addresseeTable.newRecord(trans);
        addresseeRecord.setValue(trans, "alertid", alertRecord.getValue("id"));
        addresseeRecord.setValue(trans, "addressee", recipien);
        if (ALL_BROADCAST_ROLE.equals(recipien))
        {
          // IBIS: Andere Rollen bitte auch behandeln!
          addresseeRecord.setValue(trans, "addresseetype", "role");
        }
        else
        {
          addresseeRecord.setValue(trans, "addresseetype", "user");
        }
        
        trans.commit();
      }
      finally
      {
        trans.close();
      }
    }
  }
  
  /**
   * Returns the alert item provider for the given application.
   * 
   * @param applicationDefinition
   *          the definition of the application
   * @return alert item provider or <code>null</code> if not existing.
   * @throws Exception
   *           on any error
   */
  private static List getProvider(IApplicationDefinition applicationDefinition) throws Exception
  {
    List result = new ArrayList();
    // Temporary for backward compatibility
    //
    List providers = ClassProvider.getInstancesFromPackage(applicationDefinition, "jacob.alert", IAlertItemProvider.class);
    Iterator iter = providers.iterator();
    while (iter.hasNext())
    {
      Object obj = iter.next();
      if (obj instanceof IAlertItemProvider)
      {
        result.add(obj);
      }
    }
    // FREEGROUP: dies sollte irgendwann mal entfallen wenn der Caretaker angepasst ist
    if(result.size()>0)
      return result;
    
    // check which alert mechanismen to use
    if (useQuintusAlerts(applicationDefinition))
    {
      // check whether module Quintus exists
      //
      try
      {
        result.add(Class.forName("de.tif.qes.alert.QeSAlertItemProvider").newInstance());
      }
      catch (ClassNotFoundException ex)
      {
        throw new UserRuntimeException(new CoreMessage(CoreMessage.FUNCTION_NOT_AVAILABLE_IN_DEMO_VERSION, "Quintus"));
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    }
    else
      result.add(new JacobAlertItemProvider());
    
    return result;
  }
  
  /**
   * Returns all alerts for the <code>context.getUser()</code>.
   * 
   * @param context The current working context of jACOB
   * @return List[IAlertItem]
   * @throws Exception
   */
  public static List getReceivedAlertItems(Context context) throws Exception
  {
    List result = new ArrayList();
    Iterator providerIter = getProvider(context.getApplicationDefinition()).iterator();
    while (providerIter.hasNext())
    {
      IAlertItemProvider obj = (IAlertItemProvider) providerIter.next();
      List items = obj.getReceivedAlertItems(context);
      if (items != null)
        result.addAll(items);
    }
    return result;
  }

  /**
   * Returns all alerts which the user has been send.
   * 
   * @param context
   *          The current working context of jACOB
   * @return List[IAlertItem]
   * @throws Exception
   */
  public static List getSendedAlertItems(Context context) throws Exception
  {
    List result = new ArrayList();
    Iterator providerIter = getProvider(context.getApplicationDefinition()).iterator();
    while (providerIter.hasNext())
    {
      IAlertItemProvider obj = (IAlertItemProvider) providerIter.next();
      List items = obj.getSendedAlertItems(context);
      if (items != null)
        result.addAll(items);
    }
    return result;
  }

  /**
   * Show the Alert window.
   * 
   * @param context
   */
  public static void show(TaskContextUser context)
  {
    // Avoid to display the window if the user don't whant them
    //
    if(doSuspend(context))
      return;
    
    HTTPClientSession httpClientSession = (HTTPClientSession)context.getSession();
    
    // Avoid to display two windows at the same time
    // => enforce a refresh of the existing window
    if (httpClientSession.isAlertVisible())
    {
      return;
    }

    // HACK: AS 4.11.2008: Create pseudo browser id. Reason: That Method HTTPClientSession.getSessionIdFromApplicationId() could extract Session ID.
    IUrlDialog dialog = context.createUrlDialog("./dialogs/AlertDialog.jsp?browser=" + httpClientSession.getId() + HTTPClientSession.SESSION_APPLICATION_KEY_SEPARATOR + "0");
    dialog.show(650, 350);
    
    httpClientSession.setAlertVisible(true);
  }

  /**
   * The user can suspend the Alert window. In this case the alert window will never
   * popup by itself.<br>
   * The user must press the Alert button in the toolbar.
   * 
   * @param context
   */
  public static boolean doSuspend(SessionContext context)
  {
    Session session=context.getSession();
    if(session instanceof HTTPClientSession)
    {
      HTTPClientSession cs= (HTTPClientSession)session;
      String suspend=cs.getRuntimeProperty("ui.alert.suspend");
      return (suspend!=null&& suspend.equalsIgnoreCase("true"));
    }
    
    return false;
  }

  /**
   * The user can suspend the Alert window. In this case the alert window will never
   * popup by itself.<br>
   * The user must press the Alert button in the toolbar.
   * 
   * @param context
   */
  public static void setSuspend(SessionContext context, boolean value)
  {
    Session session=context.getSession();
    if(session instanceof HTTPClientSession)
    {
      HTTPClientSession cs= (HTTPClientSession)session;
      cs.setRuntimeProperty("ui.alert.suspend",Boolean.toString(value));
    }
  }

  /**
   * Show the Alert window.
   * 
   * @param context
   */
  public static void close(SessionContext context)
  {
    ((HTTPClientSession)context.getSession()).setAlertVisible(false);
  }
  
  /**
   * Show the Alert window.
   * 
   * @param context
   */
  public static void show(IClientContext context)
  {
    IUrlDialog dialog=context.createUrlDialog("./dialogs/AlertDialog.jsp");
    dialog.show(650,350);
    
    ((HTTPClientSession)context.getSession()).setAlertVisible(true);
  }
  
}

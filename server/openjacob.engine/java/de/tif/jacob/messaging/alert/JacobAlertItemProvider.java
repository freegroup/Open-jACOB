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
import java.util.Set;
import java.util.TreeSet;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.security.IRole;
import de.tif.jacob.security.IUser;

public class JacobAlertItemProvider implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: JacobAlertItemProvider.java,v 1.2 2009/03/02 19:06:41 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  protected static final String JACOB_ALERT_ALIAS = "alert";
  protected static final String JACOB_ALERT_ADDRESSEE_ALIAS = "alertaddressee";
  protected static final String JACOB_ALERT_USER_STATUS = "alertuserstatus";
    
  public List getReceivedAlertItems(Context context) throws Exception
  {
    // use treeset for sorted order (descending by id!) and to avoid duplicates
    Set result = new TreeSet();
    
    IUser user = context.getUser();
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

    // fetch all undeleted alerts which are directly assigned to this user
    //
    IDataTable alertUserStatus = accessor.getTable(JACOB_ALERT_USER_STATUS);
    alertUserStatus.qbeSetOptional();
    alertUserStatus.qbeSetKeyValue("userid", user.getLoginId());
    alertUserStatus.qbeSetKeyValue("userstatus", "!deleted");

    IDataTable alertAddressee = accessor.getTable(JACOB_ALERT_ADDRESSEE_ALIAS);
    alertAddressee.qbeSetKeyValue("addressee", user.getLoginId());
    alertAddressee.qbeSetKeyValue("addresseetype", "user");
    
    IDataTable alerts = accessor.getTable(JACOB_ALERT_ALIAS);
    alerts.qbeSetKeyValue("sourceapplication", context.getApplicationDefinition().getName());
    IApplicationDefinition definition = AdminApplicationProvider.getApplication();
    if (alerts.search(definition.getDefaultRelationSet()) > 0)
    {
      for(int i=0;i<alerts.recordCount();i++)
      {
        IDataTableRecord alert= alerts.getRecord(i);
        result.add(new JacobAlertItem(alert));
      }
    }
    
    // fetch all undeleted alerts which are assigned to a role of this user
    //
    alerts.qbeSetValue("sender", "!=" + user.getLoginId());
    alertAddressee.qbeClear();
    alertAddressee.qbeSetKeyValue("addresseetype", "role");
    alertAddressee.qbeSetKeyValue("addressee", AlertManager.ALL_BROADCAST_ROLE);
    Iterator roleIter = user.getRoles();
    while (roleIter.hasNext())
    {
      IRole role = (IRole) roleIter.next();
      alertAddressee.qbeSetKeyValue("addressee", role.getName());
    }
    if (alerts.search(definition.getDefaultRelationSet()) > 0)
    {
      for (int i = 0; i < alerts.recordCount(); i++)
      {
        IDataTableRecord alert = alerts.getRecord(i);
        result.add(new JacobAlertItem(alert));
      }
    }
    
    return new ArrayList(result);
  }
  
  public List getSendedAlertItems(Context context) throws Exception
  {
    IUser user = context.getUser();
    IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();

    // use treeset for sorted order (descending by id!)
    Set result = new TreeSet();
    IDataTable alerts = accessor.getTable(JACOB_ALERT_ALIAS);
    alerts.qbeSetKeyValue("sender", user.getLoginId());
    alerts.qbeSetKeyValue("sourceapplication", context.getApplicationDefinition().getName());
    if (alerts.search() > 0)
    {
      for (int i = 0; i < alerts.recordCount(); i++)
      {
        IDataTableRecord alert = alerts.getRecord(i);
        result.add(new JacobAlertItem(alert));
      }
    }
    return new ArrayList(result);
  }
}

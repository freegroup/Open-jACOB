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
package de.tif.qes.alert;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.messaging.alert.IAlertItemProvider;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author {author}
 *  
 */
public class QeSAlertItemProvider implements IAlertItemProvider
{
  static public final transient String RCS_ID = "$Id: QeSAlertItemProvider.java,v 1.1 2006-12-21 11:32:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Quintus table alias for alerts.
   * <p>
   * 
   * By default, we use the existence of this table alias to distinguish whether
   * to use the Quintus or the jACOB alert mechanism.
   */
  public static final String QW_ALERT_ALIAS = "qw_alert";

  public List getReceivedAlertItems(Context context) throws Exception
  {
    List result = new ArrayList();
    IDataTable alerts = context.getDataTable(QW_ALERT_ALIAS);
    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename", "NULL");
    alerts.qbeSetKeyValue("addressee", context.getUser().getLoginId());
    if (alerts.search() > 0)
    {
      for (int i = 0; i < alerts.recordCount(); i++)
      {
        IDataTableRecord alert = alerts.getRecord(i);
        result.add(new QeSPersonalAlertItem(alert));
      }
    }

    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename", "NULL");
    // Quintus-Hack: Depending on the language setting Quintus writes the localized form of
    // the broadcast role to the database. Therefore, we will have to check for all possible
    // languages to be compatible to Quintus fat client.
    // TODO: Was machen wir, wenn wir zum Beispiel einen französischen Quintus Kunden bekommen?
    alerts.qbeSetValue("addressee", "=Alle|=All");
    alerts.qbeSetValue("sender", "!=" + context.getUser().getLoginId());
    if (alerts.search() > 0)
    {
      for (int i = 0; i < alerts.recordCount(); i++)
      {
        IDataTableRecord alert = alerts.getRecord(i);
        result.add(new QeSBroadcastAlertItem(alert));
      }
    }

    return result;
  }

  public List getSendedAlertItems(Context context) throws Exception
  {
    List result = new ArrayList();
    IDataTable alerts = context.getDataTable(QW_ALERT_ALIAS);

    alerts.clear();
    alerts.qbeClear();
    alerts.qbeSetValue("tablename", "NULL");
    alerts.qbeSetKeyValue("sender", context.getUser().getLoginId());
    if (alerts.search() > 0)
    {
      for (int i = 0; i < alerts.recordCount(); i++)
      {
        IDataTableRecord alert = alerts.getRecord(i);
        result.add(new QeSPersonalAlertItem(alert));
      }
    }
    return result;
  }
}

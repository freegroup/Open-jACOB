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

package de.tif.jacob.core.data.impl.sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.Property;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.deployment.DeployManager;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public final class SQLMonitor extends BootstrapEntry
{
  static public transient final String RCS_ID = "$Id: SQLMonitor.java,v 1.4 2011/02/15 08:13:56 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.4 $";

  /**
   * Interface of providers of a native SQL statement.
   * 
   * @author Andreas Sonntag
   */
  public interface NativeSQLProvider
  {
    public String getNativeSQL();
  }

  public static interface LogType
  {
    public void updateStatistic(SQLStatistic statistic, long executionTime);
  }

  public static final LogType UPDATE_LOG_TYPE = new LogType()
  {
    public void updateStatistic(SQLStatistic statistic, long executionTime)
    {
      statistic.incNumberOfUpdateRequests(executionTime);
    }
  };

  public static final LogType QUERY_LOG_TYPE = new LogType()
  {
    public void updateStatistic(SQLStatistic statistic, long executionTime)
    {
      statistic.incNumberOfQueryRequests(executionTime);
    }
  };

  public static final LogType OTHER_LOG_TYPE = new LogType()
  {
    public void updateStatistic(SQLStatistic statistic, long executionTime)
    {
      statistic.incNumberOfOtherRequests(executionTime);
    }
  };

  private final static DateFormat formater = new SimpleDateFormat("HH:mm:ss.SSS");
  private final static ThreadLocal threadLocal = new ThreadLocal();
  private final static Object DUMMY_OBJECT = new Object();
  private static boolean enabled = false;

  /**
   * @param start
   * @param end
   * @return
   */
  private static String getFormatedTimestamp(long start, long end)
  {
    // Note: date formatters are not synchronized
    synchronized (formater)
    {
      return formater.format(new Date(start)) + " (" + (end - start) + " ms)";
    }
  }

  public static void log(LogType type, long start, DataSource datasource, String nativeSQL)
  {
    log(type, start, datasource, null, nativeSQL);
  }

  public static void log(LogType type, long start, DataSource datasource, NativeSQLProvider nativeSQLProvider)
  {
    log(type, start, datasource, nativeSQLProvider, null);
  }

  private static void log(LogType type, long start, DataSource datasource, NativeSQLProvider nativeSQLProvider, String nativeSQL)
  {
    // already enabled?
    // Note: Will be enabled after startup.
    if (enabled == false)
      return;

    long end = System.currentTimeMillis();
    long executionTime = end - start;

    // Update SQL statistic
    //
    Context c = Context.getCurrent();
    SQLStatistic statistic = SQLStatistic.getStatisticForRequest(c);
    type.updateStatistic( statistic, executionTime);

    // Das Statement war sehr 'teuer' und muss in die Performance Datenbank
    //
    if (executionTime > Property.SQL_HISTORY_THRESHOLD.getLongValue() && (threadLocal.get() == null))
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTransaction transaction = accessor.newTransaction();
      try
      {
        threadLocal.set(DUMMY_OBJECT);

        // Das synchrone schreiben in die Datenbank kostet Zeit.
        // Da aber das zu loggende Statement selber SEHR viel Zeit verbraucht
        // hat, macht dies
        // jetzt auch nichts mehr aus. Es sollte eine absolute Ausnahme sein,
        // dass ein Statement gelogged werden muss.
        //

        // If there is no concrete application existing, we assign
        // SQL statement to the admin application
        //
        String application = DeployManager.ADMIN_APPLICATION_NAME;
        if (c.hasApplicationDefinition())
          application = c.getApplicationDefinition().getName();

        if (nativeSQL == null)
          nativeSQL = nativeSQLProvider.getNativeSQL();

        IDataTable table = accessor.getTable("sql_history");
        IDataTableRecord record = table.newRecord(transaction);
        record.setTimestampValue(transaction, "start_time", start);
        record.setTimestampValue(transaction, "end_time", end);
        record.setLongValue(transaction, "duration_ms", executionTime);
        record.setValue(transaction, "statement", nativeSQL);
        record.setValue(transaction, "application", application);
        record.setValue(transaction, "datasource", datasource.getName());
        transaction.commit();
      }
      catch (Exception e)
      {
        ExceptionHandler.handle(e);
      }
      finally
      {
        transaction.close();
        threadLocal.set(null);
      }
    }

    // if switched on, print SQL statement in separate window
    //
 
    if (c instanceof ClientContext)
    {
      ClientContext cc = (ClientContext) c;
      if (cc.getShowSQL())
      {
        if (nativeSQL == null)
          nativeSQL = nativeSQLProvider.getNativeSQL();

        // Damit man auch Serverseitig (AJAX und async calls) die SQL Statemenets abfangen kann.
       statistic.log(nativeSQL);
       
       // Clientseitige Darstellung
       cc.addAdditionalHTML("<script>showSQL(\"" + getFormatedTimestamp(start, end) + "\",\"" + StringUtil.htmlEncode(nativeSQL, "") + "\");</script>\n");
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
  {
    // simply enable SQL monitoring after server startup
    enabled = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // simply disable SQL monitoring on server shutdown
    enabled = false;
  }
}

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

package de.tif.jacob.util.db;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.exception.StartupException;
import de.tif.jacob.util.config.Config;

/**
 *  Description of the Class
 *
 * @author  Andreas Herz
 * @created  5. September 2003
 */
public class Database extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: Database.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static final Log logger = LogFactory.getLog(BootstrapEntry.class);
  private static class PoolEntry
  {
  	final String poolName;
  	final String connectString;
    final String sqlTestString;
    final String driverClass;
    
    PoolEntry(String poolName, String driverClass, String connectString, String sqlTestString)
    {
    	this.poolName = poolName;
    	this.driverClass = driverClass;
    	this.connectString = connectString;
    	this.sqlTestString = sqlTestString;
    }
  }
  
  private final static Map pools = new HashMap();
   
  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
   {
		try {
		  Class.forName("org.apache.commons.dbcp.PoolingDriver");
		}
		catch (Exception e) {
        logger.error("Cannot instantiate pooling driver", e);
		}

    Config conf = Config.getPlattformConfig();
    for (int i = 0; i < 100; i++)
		{
      String poolString    = conf.getProperty("datasource.pooling.name."+i);
      String connectString = conf.getProperty("datasource.pooling.source."+i);
      String sqlString     = conf.getProperty("datasource.pooling.testsql."+i);
      String driverClass   = conf.getProperty("datasource.pooling.driver."+i);
      
      // TODO: save the test SQL statement and schedule/test the databases in interval
      if(poolString !=null)
      {
        if(connectString==null || sqlString==null || driverClass==null)
        {
          // Parameter nicht mit loggen, da in diesem passwoerter vorhanden sein koennen
          //
          throw new StartupException(getClass(),"Missing parameter for SQL connection-pool ["+poolString+"]");
        }
        
        logger.info("\tloading connection pool ="+poolString);
        Driver driver = (Driver) Class.forName(driverClass).newInstance();
        DriverManager.registerDriver(driver);
        
        // create a jdbc-driver entry for the connection pool
        //
      	setupDriver(poolString, connectString);
      	
      	// and register pool info for reconnect
      	synchronized (pools)
      	{
      		PoolEntry entry= new PoolEntry(poolString, driverClass, connectString, sqlString);
      		pools.put(poolString,entry);
      	}
      }
		}
   }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    // do nothing here
  }
  
  public static Connection getConnection(String poolname, String username, String password) throws SQLException
	{
		PoolEntry entry;
		synchronized (pools)
		{
			entry = (PoolEntry) pools.get(poolname);
			if (entry == null)
			{
				throw new RuntimeException("Unknown pool: " + poolname);
			}
		}
		DriverManager.setLoginTimeout(10);
		Connection connection = DriverManager.getConnection(entry.connectString, username, password);
		return connection;
	}
  
   /**
    *  Description of the Method
    *
    * @param  poolname Description of Parameter
    * @param  connectURI Description of Parameter
    * @exception  java.lang.Exception Description of Exception
    */
   private static void setupDriver(String poolname, String connectURI)
      throws java.lang.Exception
   {
      //
      // First, we'll need a ObjectPool that serves as the
      // actual pool of connections.
      //
      // We'll use a GenericObjectPool instance, although
      // any ObjectPool implementation will suffice.
      //
      ObjectPool connectionPool = new GenericObjectPool(null);

      //
      // Next, we'll create a ConnectionFactory that the
      // pool will use to create Connections.
      // We'll use the DriverManagerConnectionFactory,
      // using the connect string passed in the command line
      // arguments.
      //
      ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);

      //
      // Now we'll create the PoolableConnectionFactory, which wraps
      // the "real" Connections created by the ConnectionFactory with
      // the classes that implement the pooling functionality.
      //
      PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

      //
      // Finally, we create the PoolingDriver itself...
      //
      PoolingDriver driver = new PoolingDriver();

      //
      // ...and register our pool with it.
      //
      driver.registerPool(poolname, connectionPool);

      //
      // Now we can just use the connect string "jdbc:apache:commons:dbcp:example"
      // to access our pool of Connections.
      //

   }

   
   /**
    *  only to test this class
    *
    * @param  args The command line arguments
    */
   public static void main(String[] args)
   {
      try
      {
         setupDriver("EDVIN/GEI", "jdbc:oracle:thin:edvin_smc/edvin@53.11.132.176:1526:GEIP");
         setupDriver("EDVIN/AP2", "jdbc:oracle:thin:edvin_smc/edvin@53.11.132.176:1526:AP2P");
         setupDriver("EDVIN/APA", "jdbc:oracle:thin:edvin_smc/edvin@53.11.132.176:1526:APAP");
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }


   /**
    * @param  r Description of Parameter
    */
   public static void dumpResultSetMetaInformation(ResultSet r)
   {
      try
      {
         ResultSetMetaData md = r.getMetaData();
         int count = md.getColumnCount();
         for (int i = 1; i <= count; i++)
         {
            System.out.println("Name:" + md.getColumnName(i) + "   Type:" + md.getColumnTypeName(i));
         }
         System.out.println("========================================");
      }
      catch (Exception ex)
      {
      }
   }
}

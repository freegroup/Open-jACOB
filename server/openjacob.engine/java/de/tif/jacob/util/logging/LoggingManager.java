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
package de.tif.jacob.util.logging;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.LogManager;

import org.apache.commons.io.IOUtils;

import de.tif.jacob.core.BootstrapEntry;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.impl.admin.AdminApplicationProvider;

/**
 * This logging manager enables log property changes at runtime.
 * 
 * @author Andreas Sonntag
 */
public class LoggingManager extends BootstrapEntry
{
  static public final transient String RCS_ID = "$Id: LoggingManager.java,v 1.2 2009/03/02 19:07:54 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private static final String LOGGING_PROPS = "logging.properties";

  private static boolean logPropertiesEnabled = false;
  
  private boolean warnings = false;

  /**
   * @return Returns the logPropertiesEnabled.
   */
  public synchronized static boolean isLogPropertiesEnabled()
  {
    return logPropertiesEnabled;
  }

  /**
   * @param logPropertiesEnabled The logPropertiesEnabled to set.
   */
  private static synchronized void setLogPropertiesEnabled(boolean logPropertiesEnabled)
  {
    LoggingManager.logPropertiesEnabled = logPropertiesEnabled;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.BootstrapEntry#destroy()
   */
  public void destroy() throws Throwable
  {
    setLogPropertiesEnabled(false);
    resetConfiguration();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.BootstrapEntry#hasWarnings()
   */
  public boolean hasWarnings()
  {
    return this.warnings;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.BootstrapEntry#init()
   */
  public void init() throws Throwable
  {
    readConfiguration(true);
  }

  public static String getDefaultConfiguration() throws Exception
  {
    InputStream inputStream = LoggingManager.class.getResourceAsStream(LOGGING_PROPS);
    if (null == inputStream)
    {
      throw new Exception("Could not open file '" + LOGGING_PROPS + "' as stream");
    }
    try
    {
      // IBIS: Avoid fixed encoding
      return IOUtils.toString(inputStream, "ISO-8859-1");
    }
    finally
    {
      IOUtils.closeQuietly(inputStream);
    }
  }
  
  private static void resetConfiguration()
  {
    // reset JDK1.4 logging
    System.out.println("INFO: JDK14 logging properties have been reset");
    LogManager.getLogManager().reset();
  }

  /**
   * @return
   */
  public static boolean readConfiguration()
  {
    return readConfiguration(false);
  }
  
  private static boolean readConfiguration(boolean noReset)
  {
    try
    {
      IDataAccessor accessor = AdminApplicationProvider.newDataAccessor();
      IDataTable dataTable = accessor.newAccessor().getTable("loggingconfig");
      dataTable.qbeSetKeyValue("status", "active");
      dataTable.qbeSetKeyValue("type", "jdk14");
      dataTable.search();
      if (dataTable.recordCount() == 1)
      {
        IDataTableRecord activeConfigRecord = dataTable.getRecord(0);

        try
        {
          // IBIS: Avoid fixed encoding
          InputStream is = new ByteArrayInputStream(activeConfigRecord.getStringValue("properties").getBytes("ISO-8859-1"));
          try
          {
            // initialize JDK1.4 logging
            LogManager.getLogManager().readConfiguration(is);
          }
          finally
          {
            is.close();
          }

          System.out.println("INFO: JDK14 logging properties '" + activeConfigRecord.getValue("name") + "' have been set");
          setLogPropertiesEnabled(true);
          return true;
        }
        catch (Exception ex)
        {
          System.err.println("WARNING: Could not set JDK14 logging properties '" + activeConfigRecord.getValue("name") + "'");
          ex.printStackTrace();
        }
      }
      else if (dataTable.recordCount() > 1)
      {
        System.err.println("WARNING: Could not set JDK14 logging properties: Multiple active logging configurations existing!");
      }
      else if (dataTable.recordCount() == 0 && noReset == false)
      {
        resetConfiguration();
      }
    }
    catch (Exception ex)
    {
      System.err.println("ERROR: Could not retrieve logging configuration");
      ex.printStackTrace();
    }
    setLogPropertiesEnabled(false);
    return false;
  }
}

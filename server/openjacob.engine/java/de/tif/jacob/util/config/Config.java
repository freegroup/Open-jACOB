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
package de.tif.jacob.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import de.tif.jacob.core.Bootstrap;

/**
 * Class making configuration settings accessable very easily.
 * There is one main/singleton configuration file which may be
 * obtained by calling getConfig ( ) and application specific
 * configuration files, which may be obtained by calling
 * getConfig ( String ) with an application name. In case of
 * the main/singleton configuration file the application name
 * holded by SINGLETON_APP_NAME_ID will be used to retrieve
 * the config file. The retrieval is done by checking if there
 * is a system property named like the application name plus
 * ".configfile". If none is found, the same check will be
 * repeated with ".home". If one is set, the config filename
 * will be build based on the home directory plus "/config/"
 * plus application name plus ".properties". If none of those
 * two properties, which are checked in this order, are set or
 * no config file is found, an exception will be raised.
 */

public class Config
{
  static public final transient String RCS_ID = "$Id: Config.java,v 1.1 2007/01/19 09:50:46 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  
   // Main singleton configuration instance; always available
   private static Config inst = null;

   // List of requested application configuration instances
   private static HashMap instances =new HashMap();

   private String filename;   // Filename of the properties file
   private String appName;    // name of the locical application
   private Properties props;  // Properties themselves
   private Date lastAccess;   // Time properties have been reloaded last
   private static final long RELOAD_INTERVAL = 10; // Seconds until properties reload

    /**
     * Get config instance based on application name.
     */
    synchronized public static Config getPlattformConfig ()
    {
      String appName = "plattform_"+Bootstrap.getApplicationName();
      // Check for application instance
      if ( ( inst = (Config)instances.get ( appName ) ) == null )
      {
             // Create new instance based on main application name
            inst = new Config ( appName );

            // Check for valid filename
            if ( inst.filename == null )
            {
                // Invalidate instance
                inst = null;
            }
            else
            {
                // Add instance to list of instances
                instances.put ( appName, inst );
            }
      }

      // Return instance
        return ( inst );
    }

    /**
     * Get config instance based on application name.
     */
    synchronized public static Config getCommonConfig()
    {
      String appName = "common_"+Bootstrap.getApplicationName();
      // Check for application instance
      if ( ( inst = (Config)instances.get ( appName ) ) == null )
      {
        // Create new instance based on main application name
        inst = new Config ( appName );

        // Check for valid filename
        if ( inst.filename == null )
        {
          // Invalidate instance
          System.err.println("unable to create Config-object");
          inst = null;
        }
        else
        {
          // Add instance to list of instances
          instances.put ( appName, inst );
        }
      }

      // Return instance
      return ( inst );
    }

    /**
     * Set needed instance fields
     */
    private Config ( String _appName )
    {
        appName = _appName;
        String propertie1 =  appName + ".configfile";

        // Set needed instance fields
        if ( ( filename = System.getProperty(propertie1 ) ) == null )
        {
//            System.err.println("property ["+propertie1 + "] not found");
            try
            {
                filename = appName + ".properties";
                File x = new File(filename);
                filename = x.getParent() + File.separator + java.net.InetAddress.getLocalHost().getHostName()+"_"+x.getName();
                x = new File(filename);
//                System.err.println("property ["+propertie1 + "] not found. Using default propertiefile ["+filename + "]");
            }
            catch (Exception ex)
            {
               filename = null;
            }
        }
        else
        {
            try
            {
                File x = new File(filename);
                String extendedFilename = x.getParent() + File.separator + java.net.InetAddress.getLocalHost().getHostName()+"_"+x.getName();
                x = new File(extendedFilename);
                if(x.exists())
                {
                  filename = extendedFilename;
//                  System.err.println("using propertiefile ["+filename + "]");
                }
                else
                {
                  System.out.println(extendedFilename+" not found - using propertiefile ["+filename + "]");
                }
            }
            catch (Exception ex)
            {
            }
        }

        props = new Properties ( );
        lastAccess = new Date ( 0 );
    }



    /**
     * Get property value by property name.
     */
    synchronized public String getProperty ( String name )
    {
        try
        {
          // Check if properties should be read again
          if ( isOutdated ( lastAccess ) )
          {
            // Load properties
            loadProperties ( );
            lastAccess = new Date ( );
          }

          // Return property
          return props.getProperty ( name );
      }
      catch (Exception exception)
      {
        System.err.println ( "Catched exception while trying to read property " + name + ": " + exception );
      }
        return null;
    }


    /**
     * Set property value assigned to property name.
     */
    synchronized public void setProperty ( String name, String value )
    {
        try
        {
            // Set property
            props.setProperty ( name, value );

            // Store properties
            storeProperties ( );
      }
      catch (Exception exception)
      {
        System.err.println ( "Catched exception while trying to write property " + name + " to value " + value + ": " + exception );
      }
    }

    /**
     * Check if the properties should be read again
     */
    private static boolean isOutdated ( Date lastAccess )
    {
      // Check if the properties should be read again
      return ( lastAccess.getTime ( ) < new Date ( ).getTime ( ) - RELOAD_INTERVAL * 1000 );
    }

    /**
     * Load properties from file.
     */
    private void loadProperties ( )
    {
      try
      {
          FileInputStream fileStream = new FileInputStream ( filename );
        props.load ( fileStream );
        fileStream.close();
      }
      catch (Exception exception)
      {
        System.err.println( "Catched exception while trying to read properties file " + filename + ": " + exception );
         exception.printStackTrace();
          instances.remove(appName );
      }
    }

    /**
     * Store properties into file.
     */
    private void storeProperties()
    {
      try
      {
          FileOutputStream fileStream = new FileOutputStream ( filename );
        props.store ( fileStream, "" );
        fileStream.close();
      }
      catch (Exception exception)
      {
        System.err.println("Catched exception while trying to write properties file " + filename + ": " + exception );
      }
    }
    
    /**
     * 
     * @param filename
     * @return
     */
    public static File getFileInConfigDir(String filename)
    {
      String configDir = System.getProperty("smc.configurationRoot");
      return new File(configDir + filename);
      //return null;
    }
	/**
	 * @return Returns the filename.
	 */
	public String getFilename()
	{
		return filename;
	}

}

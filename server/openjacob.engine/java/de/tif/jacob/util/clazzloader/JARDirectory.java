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
package de.tif.jacob.util.clazzloader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * Class source represented by a JAR directory.
 *
 * @created   27. April 2004
 */
public class JARDirectory implements IClassSource, IResourceSource
{
  static public final transient String RCS_ID = "$Id: JARDirectory.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private File jarDirectory = null;
    private Collection jarFiles = null;

    /**
     * Construct JARDirectory object.
     *
     * @param jarDirectory                JAR directory
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    public JARDirectory( File jarDirectory )
        throws ClassNotFoundException
    {
        if( ( jarDirectory != null ) && ( jarDirectory.isDirectory() ) )
        {
            this.jarDirectory = jarDirectory;
            this.jarFiles = new Vector();
        }
        else
        {
            throw new ClassNotFoundException( "Failed to instantiate class source!" );
        }
        scanJARDirectory();
    }

    /**
     * Get class defintion based on class name.
     *
     * @param name                        Name of class to be retrieved
     * @return                            Class defintion byte array of class to be retrieved
     * @exception ClassNotFoundException  Exception thrown when class to be retrieved not found
     */
    public byte[] getClassDefinition( String name )
        throws ClassNotFoundException
    {
        try
        {
            // Check known JAR files
            for( Iterator iter = jarFiles.iterator(); iter.hasNext();  )
            {
                try
                {
                    return ( ( JARFile )iter.next() ).getClassDefinition( name );
                }
                catch( ClassNotFoundException exception )
                {
                    // Try next one
                }
            }

            // Rescan JAR directory and check only newly found JAR files
            for( Iterator iter = scanJARDirectory().iterator(); iter.hasNext();  )
            {
                try
                {
                    return ( ( JARFile )iter.next() ).getClassDefinition( name );
                }
                catch( ClassNotFoundException exception )
                {
                    // Try next one
                }
            }
        }
        catch( Exception exception )
        {
            // Do nothing since an exception will be thrown automatically
        }
        throw new ClassNotFoundException( name );
    }

    /**
     * Check if class definition can be found within class path.
     *
     * @param name  Name of class to be looked for
     * @return      flag indicating successful localization
     */
    public boolean hasClassDefinition( String name )
    {
        // Check known JAR files
        for( Iterator iter = jarFiles.iterator(); iter.hasNext();  )
        {
            if( ( ( JARFile )iter.next() ).hasClassDefinition( name ) )
            {
                return true;
            }
            // Try next one
        }

        try
        {
            // Rescan JAR directory and check only newly found JAR files
            for( Iterator iter = scanJARDirectory().iterator(); iter.hasNext();  )
            {
                if( ( ( JARFile )iter.next() ).hasClassDefinition( name ) )
                {
                    return true;
                }
                // Try next one
            }
        }
        catch( ClassNotFoundException e )
        {
        }
        return false;
    }

    /**
     * Get resource location based on resource name.
     *
     * @param name  Name of resource to be searched
     * @return      URL of resource
     */
    public URL getResourceLocation( String name )
    {
        // Init resource location to null
        URL resourceLocation = null;

        try
        {
            // Check known JAR files
            for( Iterator iter = jarFiles.iterator(); iter.hasNext();  )
            {
                if( ( resourceLocation = ( ( JARFile )iter.next() ).getResourceLocation( name ) ) != null )
                {
                    return resourceLocation;
                }
            }

            // Rescan JAR directory and check only newly found JAR files
            for( Iterator iter = scanJARDirectory().iterator(); iter.hasNext();  )
            {
                if( ( resourceLocation = ( ( JARFile )iter.next() ).getResourceLocation( name ) ) != null )
                {
                    return resourceLocation;
                }
            }
        }
        catch( Exception exception )
        {
            // Do nothing since null will be returned automatically
        }
        return null;
    }

    /**
     * Get resource locations based on resource name.
     *
     * @param name  Name of resources to be searched
     * @return      Enumeration of URLs of resources
     */
    public Enumeration getResourceLocations( String name )
    {
        // Init resource location to null
        URL resourceLocation = null;
        Vector resourceLocations = new Vector();

        try
        {
            // Check known JAR files
            for( Iterator iter = jarFiles.iterator(); iter.hasNext();  )
            {
                if( ( resourceLocation = ( ( JARFile )iter.next() ).getResourceLocation( name ) ) != null )
                {
                    resourceLocations.add( resourceLocation );
                }
            }

            // Rescan JAR directory and check only newly found JAR files
            for( Iterator iter = scanJARDirectory().iterator(); iter.hasNext();  )
            {
                if( ( resourceLocation = ( ( JARFile )iter.next() ).getResourceLocation( name ) ) != null )
                {
                    resourceLocations.add( resourceLocation );
                }
            }

            // Return found resource locations
            return resourceLocations.elements();
        }
        catch( Exception exception )
        {
            // Do nothing since null will be returned automatically
        }
        return null;
    }

    /**
     * Get class path this class source references. Each entry including the last must be terminated
     * with a semicolon.
     *
     * @return   Class path this class source references
     */
    public String getClassPath()
    {
        StringBuffer result = new StringBuffer();
        for( Iterator iter = jarFiles.iterator(); iter.hasNext();  )
        {
            result.append( ( ( IClassSource )iter.next() ).getClassPath() );
        }
        return result.toString();
    }

    /**
     * Get resource path this resource source references. Each entry including the last must be
     * terminated with a semicolon.
     *
     * @return   Resource path this resource source references
     */
    public String getResourcePath()
    {
        return getClassPath();
    }

    /**
     * Scan JAR directory for available JAR files. The private member jarFiles will contain all
     * found JAR files. All newly found JAR files will be returned (positive delta information).
     *
     * @return                            Newly found JAR files.
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    private Collection scanJARDirectory()
        throws ClassNotFoundException
    {
        // Reset collections
        Collection newJARFiles = new Vector();
        Collection oldJARFiles = jarFiles;
        jarFiles = new Vector();

        // Look for files in JAR directory
        File[] jarDirectoryFiles = jarDirectory.listFiles(
            new FilenameFilter()
            {
                /**
                 * Accept only certain type of files.
                 *
                 * @param directory  Directory containing the file to be checked for filter criteria
                 * @param name       Name of file to be checked for filter criteria
                 * @return           Flag indicating if filter criteria matched
                 */
                public boolean accept( File directory, String name )
                {
                    return ( ( name != null ) &&
                        ( name.substring( name.length() - 3 ).equalsIgnoreCase( "jar" ) ) );
                }
            } );

        // Check files in JAR directory
        if( jarDirectoryFiles != null )
        {
            JARFile jarFile;
            for( int i = 0; i < jarDirectoryFiles.length; i++ )
            {
                // Add JAR file
                jarFile = new JARFile( jarDirectoryFiles[i] );
                jarFiles.add( jarFile );

                // Check if this JAR file was found before
                if( !oldJARFiles.contains( jarFile ) )
                {
                    newJARFiles.add( jarFile );
                }
            }
        }
        return newJARFiles;
    }
}

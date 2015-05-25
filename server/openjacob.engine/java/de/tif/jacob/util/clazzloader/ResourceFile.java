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
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Resource source represented by a resource file.
 *
 * @created   27. April 2004
 */
public class ResourceFile implements IResourceSource
{
  static public final transient String RCS_ID = "$Id: ResourceFile.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private File resourceFile = null;

    /**
     * Construct ResourceFile object.
     *
     * @param resourceFile               Resource file
     * @exception ClassNotFoundException  Exception thrown when resource source invalid
     */
    public ResourceFile( File resourceFile )
        throws ClassNotFoundException
    {
        if( ( resourceFile != null ) && ( resourceFile.exists() ) && ( resourceFile.isFile() ) )
        {
            this.resourceFile = resourceFile;
        }
        else
        {
            throw new ClassNotFoundException( "Failed to instantiate resource source!" );
        }
    }

    /**
     * Get resource location based on resource name.
     *
     * @param name  Name of resource to be searched
     * @return      URL of resource
     */
    public URL getResourceLocation( String name )
    {
        String resourceFileName = resourceFile.getAbsolutePath().replace( '\\', '/' );
        String wantedFileName = name;
        if( resourceFileName.toUpperCase().endsWith( wantedFileName.toUpperCase() ) )
        {
            try
            {
                return resourceFile.toURL();
            }
            catch( Exception exception )
            {
            }
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
        URL url = getResourceLocation( name );
        if( url != null )
        {
            Vector vector = new Vector();
            vector.add( url );
            return vector.elements();
        }
        return null;
    }

    /**
     * Get resource path this resource source references. Each entry including the last must be
     * terminated with a semicolon.
     *
     * @return   Resource path this resource source references
     */
    public String getResourcePath()
    {
        return resourceFile.getPath() + ";";
    }
}

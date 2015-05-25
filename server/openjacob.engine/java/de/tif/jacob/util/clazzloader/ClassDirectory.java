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
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Class source represented by a class directory.
 *
 * @created   27. April 2004
 */
public class ClassDirectory implements IClassSource
{
  static public final transient String RCS_ID = "$Id: ClassDirectory.java,v 1.1 2007/01/19 09:50:36 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private File classDirectory = null;

    /**
     * Construct ClassDirectory object.
     *
     * @param classDirectory              Class directory
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    public ClassDirectory( File classDirectory )
    {
        if (null == classDirectory)
            throw new NullPointerException();
        
        if( classDirectory.isDirectory() )
        {
            this.classDirectory = classDirectory;
        }
        else
        {
            throw new IllegalArgumentException( "File is not an directory: " + classDirectory );
        }
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
            File classFile = new File( classDirectory, name.replace( '.', File.separatorChar ) + ".class" );
            if( ( classFile != null ) && ( classFile.exists() ) && ( classFile.isFile() ) )
            {
                byte[] classDefintion = new byte[( int )classFile.length()];
                InputStream classStream = new FileInputStream( classFile );
                if( classStream != null )
                {
                    try
                    {
                        classStream.read( classDefintion );
                    }
                    finally
                    {
                        classStream.close();
                    }
                    return ( classDefintion );
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
        File classFile = new File( classDirectory, name.replace( '.', File.separatorChar ) + ".class" );
        if( ( classFile != null ) && ( classFile.exists() ) && ( classFile.isFile() ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Get class path this class source references. Each entry including the last must be terminated
     * with a semicolon.
     *
     * @return   Class path this class source references
     */
    public String getClassPath()
    {
        return classDirectory.getPath() + ";";
    }
}

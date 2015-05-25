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
import java.util.HashMap;
import java.util.Map;

/**
 * Class source represented by a java directory.
 *
 * @created   27. April 2004
 */
public class JavaDirectory implements IClassSource
{
  static public final transient String RCS_ID = "$Id: JavaDirectory.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private File javaDirectory = null;
    private String classPath = null;
    private Map javaFiles = null;

    /**
     * Construct JavaDirectory object.
     *
     * @param javaDirectory               Java directory
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    public JavaDirectory( File javaDirectory )
        throws ClassNotFoundException
    {
        if( ( javaDirectory != null ) && ( javaDirectory.isDirectory() ) )
        {
            this.javaDirectory = javaDirectory;
            this.javaFiles = new HashMap();
        }
        else
        {
            throw new ClassNotFoundException( "Failed to instantiate class source!" );
        }
    }

    /**
     * Construct JavaDirectory object.
     *
     * @param javaDirectory               Java directory
     * @param classPath                   Class path which should be used for compilation
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    public JavaDirectory( File javaDirectory, String classPath )
        throws ClassNotFoundException
    {
        if( ( javaDirectory != null ) && ( javaDirectory.isDirectory() ) )
        {
            this.javaDirectory = javaDirectory;
            this.classPath = classPath;
            this.javaFiles = new HashMap();
        }
        else
        {
            throw new ClassNotFoundException( "Failed to instantiate class source!" );
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
            // Check known class files
            JavaFile javaFile = ( JavaFile )javaFiles.get( name );
            if( javaFile == null )
            {
                javaFile = new JavaFile( new File( javaDirectory.getPath() + File.separatorChar + name.replace( '.', File.separatorChar ) + ".java" ), classPath );
                javaFiles.put( name, javaFile );
            }
            return javaFile.getClassDefinition( name );
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
        try
        {
            // Check known class files
            JavaFile javaFile = ( JavaFile )javaFiles.get( name );
            if( javaFile == null )
            {
                javaFile = new JavaFile( new File( javaDirectory.getPath() + File.separatorChar + name.replace( '.', File.separatorChar ) + ".java" ), classPath );
                javaFiles.put( name, javaFile );
            }
            return javaFile.hasClassDefinition( name );
        }
        catch( Exception exception )
        {
            // Do nothing since an exception will be thrown automatically
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
        return javaDirectory.getPath() + ";";
    }
}

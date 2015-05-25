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

/**
 * Class source represented by a java file.
 *
 * @created   27. April 2004
 */
public class JavaFile implements IClassSource
{
  static public final transient String RCS_ID = "$Id: JavaFile.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private File javaFile = null;
    private String classPath = null;
    private ClassFile classFile = null;

    /**
     * Construct JavaFile object.
     *
     * @param javaFile                    Java file
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    public JavaFile( File javaFile )
        throws ClassNotFoundException
    {
        if( ( javaFile != null ) && ( javaFile.exists() ) && ( javaFile.isFile() ) )
        {
            this.javaFile = javaFile;
        }
        else
        {
            throw new ClassNotFoundException( "Failed to instantiate class source!" );
        }
    }

    /**
     * Construct JavaFile object.
     *
     * @param javaFile                    Java file
     * @param classPath                   Class path which should be used for compilation
     * @exception ClassNotFoundException  Exception thrown when class source invalid
     */
    public JavaFile( File javaFile, String classPath )
        throws ClassNotFoundException
    {
        if( ( javaFile != null ) && ( javaFile.exists() ) && ( javaFile.isFile() ) )
        {
            this.javaFile = javaFile;
            this.classPath = classPath;
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
        if( classFile != null )
        {
            return classFile.getClassDefinition( name );
        }
        else
        {
            try
            {
                String classFilePath = javaFile.getPath().substring( 0, javaFile.getPath().length() - "java".length() ) + "class";
                String wantedFilePath = name.replace( '.', File.separatorChar ) + ".class";
                if( ( classFilePath != null ) && ( wantedFilePath != null ) &&
                    ( classFilePath.substring( classFilePath.length() - wantedFilePath.length() ).equalsIgnoreCase( wantedFilePath ) ) )
                {
                    try
                    {
                        classFile = new ClassFile( new File( classFilePath ) );
                        return classFile.getClassDefinition( name );
                    }
                    catch( ClassNotFoundException exception )
                    {
                        Process compileProcess = Runtime.getRuntime().exec( "javac -classpath " + classFilePath.substring( 0, classFilePath.length() - wantedFilePath.length() ) + ( ( classPath != null ) ? ";" + classPath : "" ) + " " + javaFile.getPath() );
                        compileProcess.waitFor();
                        if( compileProcess.exitValue() == 0 )
                        {
                            classFile = new ClassFile( new File( classFilePath ) );
                            return classFile.getClassDefinition( name );
                        }
                        else
                        {
                            throw new ClassNotFoundException( "Failed to compile class source " + javaFile + "!" );
                        }
                    }
                }
            }
            catch( Exception exception )
            {
                // Do nothing since an exception will be thrown automatically
            }
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
        if( classFile != null )
        {
            return classFile.hasClassDefinition( name );
        }
        else
        {
            try
            {
                String classFilePath = javaFile.getPath().substring( 0, javaFile.getPath().length() - "java".length() ) + "class";
                String wantedFilePath = name.replace( '.', File.separatorChar ) + ".class";
                if( ( classFilePath != null ) && ( wantedFilePath != null ) &&
                    ( classFilePath.substring( classFilePath.length() - wantedFilePath.length() ).equalsIgnoreCase( wantedFilePath ) ) )
                {
                    try
                    {
                        classFile = new ClassFile( new File( classFilePath ) );
                        return classFile.hasClassDefinition( name );
                    }
                    catch( ClassNotFoundException exception )
                    {
                        Process compileProcess = Runtime.getRuntime().exec( "javac -classpath " + classFilePath.substring( 0, classFilePath.length() - wantedFilePath.length() ) + ( ( classPath != null ) ? ";" + classPath : "" ) + " " + javaFile.getPath() );
                        compileProcess.waitFor();
                        if( compileProcess.exitValue() == 0 )
                        {
                            classFile = new ClassFile( new File( classFilePath ) );
                            return classFile.hasClassDefinition( name );
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
            }
            catch( Exception exception )
            {
                // Do nothing since an exception will be thrown automatically
            }
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
        if( classFile != null )
        {
            return classFile.getClassPath();
        }
        else
        {
            return javaFile.getPath().substring( 0, javaFile.getPath().length() - "java".length() ) + "class;";
        }
    }
}

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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class source represented by a JAR file.
 *
 * @created   27. April 2004
 */
public class JARFile implements IClassSource, IResourceSource
{
  static public final transient String RCS_ID = "$Id: JARFile.java,v 1.1 2007/01/19 09:50:36 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private final File jarFile;

  /**
   * Construct JARFile object.
   *
   * @param jarFile                     JAR file
   */
  public JARFile(File jarFile)
  {
    if (jarFile == null)
    {
      throw new NullPointerException("jarFile is null");
    }
    if (jarFile.exists() && jarFile.isFile())
    {
      this.jarFile = jarFile;
    }
    else
    {
      throw new RuntimeException("'" + jarFile + "' does not exist or is no file!");
    }
  }

  /**
   * Get class defintion based on class name.
   * 
   * @param name
   *          Name of class to be retrieved
   * @return Class defintion byte array of class to be retrieved
   * @exception ClassNotFoundException
   *              Exception thrown when class to be retrieved not found
   */
  public byte[] getClassDefinition(String name) throws ClassNotFoundException
  {
    try
    {
      JarFile jarFile = new JarFile(this.jarFile);
      try
      {
        JarEntry jarEntry = jarFile.getJarEntry(name.replace('.', '/') + ".class");
        if (jarEntry != null)
        {
          int size = (int) jarEntry.getSize();
          byte[] temp = new byte[size];
          byte[] classDefinition = new byte[size];
          InputStream classStream = jarFile.getInputStream(jarEntry);
          try
          {
            int read = 0;

            while (size > read)
            {
              int l = classStream.read(temp);
              System.arraycopy(temp, 0, classDefinition, read, l);
              read += l;
            }
          }
          finally
          {
            classStream.close();
          }
          return classDefinition;
        }
        throw new ClassNotFoundException(name);
      }
      finally
      {
        jarFile.close();
      }
    }
    catch (Exception ex)
    {
      throw new ClassNotFoundException(name, ex);
    }
  }

  /**
   * Check if class definition can be found within class path.
   * 
   * @param name
   *          Name of class to be looked for
   * @return flag indicating successful localization
   */
  public boolean hasClassDefinition(String name)
  {
    try
    {
      JarFile jarFile = new JarFile(this.jarFile);
      try
      {
        JarEntry jarEntry = jarFile.getJarEntry(name.replace('.', '/') + ".class");
        if (jarEntry != null)
        {
          return true;
        }
      }
      finally
      {
        jarFile.close();
      }
    }
    catch (IOException e)
    {
    }
    return false;
  }

  /**
   * Get resource location based on resource name.
   * 
   * @param name
   *          Name of resource to be searched
   * @return URL of resource
   */
  public URL getResourceLocation(String name)
  {
    try
    {
      while (name.startsWith("/"))
      {
        name = name.substring(1);
      }
      JarFile jarFile = new JarFile(this.jarFile);
      try
      {
        JarEntry jarEntry = jarFile.getJarEntry(name);
        if (jarEntry != null)
        {
          return new URL("jar:" + this.jarFile.toURL() + "!/" + name);
        }
      }
      finally
      {
        jarFile.close();
      }
    }
    catch (Exception exception)
    {
      // Do nothing since null will be returned automatically
    }
    return null;
  }

  /**
   * Get resource locations based on resource name.
   * 
   * @param name
   *          Name of resources to be searched
   * @return Enumeration of URLs of resources
   */
  public Enumeration getResourceLocations(String name)
  {
    URL url = getResourceLocation(name);
    if (url != null)
    {
      Vector vector = new Vector();
      vector.add(url);
      return vector.elements();
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
    return jarFile.getPath() + ";";
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
   * Determine whether this and the other object are equal.
   *
   * @param otherObject  The other object
   * @return             Flag indicating whether or not this and the other object are equal
   */
  public boolean equals(Object otherObject)
  {
    if (otherObject instanceof JARFile)
    {
      return ((JARFile) otherObject).getClassPath().equalsIgnoreCase(getClassPath());
    }
    return false;
  }
}

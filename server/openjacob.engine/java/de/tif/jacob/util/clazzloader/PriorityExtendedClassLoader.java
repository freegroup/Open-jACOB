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

import java.net.URL;
import java.util.Iterator;

/**
 * Custom class loader.
 * 
 * @created 26. April 2004
 */
public class PriorityExtendedClassLoader extends ExtendedClassLoader
{
  static public final transient String RCS_ID = "$Id: PriorityExtendedClassLoader.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
	 * Construct custom class loader.
	 * 
	 * @param parentClassLoader
	 *          Parent class loader
	 */
  public PriorityExtendedClassLoader(ClassLoader parentClassLoader)
  {
    // Call constructor of base class
    super(parentClassLoader);
  }

  /**
	 * Get class path this class source references. Each entry including the last must be terminated with a semicolon.
	 * 
	 * @return Class path this class source references
	 */
  public String getClassPath()
  {
    StringBuffer result = new StringBuffer();
    synchronized (classSources)
    {
      for (Iterator iter = classSources.iterator(); iter.hasNext();)
      {
        result.append(((IClassSource) iter.next()).getClassPath());
      }
    }
    if (getParent() instanceof IClassSource)
    {
      result.append(((IClassSource) getParent()).getClassPath());
    }
    return result.toString();
  }

  /**
	 * Get library path this library source references. Each entry including the last must be terminated with a semicolon.
	 * 
	 * @return Library path this library source references
	 */
  public String getLibraryPath()
  {
    StringBuffer result = new StringBuffer();
    synchronized (librarySources)
    {
      for (Iterator iter = librarySources.iterator(); iter.hasNext();)
      {
        result.append(((ILibrarySource) iter.next()).getLibraryPath());
      }
    }
    if (getParent() instanceof ILibrarySource)
    {
      result.append(((ILibrarySource) getParent()).getLibraryPath());
    }
    return result.toString();
  }

  /**
	 * Get resource path this resource source references. Each entry including the last must be terminated with a semicolon.
	 * 
	 * @return Resource path this resource source references
	 */
  public String getResourcePath()
  {
    StringBuffer result = new StringBuffer();
    synchronized (resourceSources)
    {
      for (Iterator iter = resourceSources.iterator(); iter.hasNext();)
      {
        result.append(((IResourceSource) iter.next()).getResourcePath());
      }
    }
    if (getParent() instanceof IResourceSource)
    {
      result.append(((IResourceSource) getParent()).getResourcePath());
    }
    return result.toString();
  }

  /**
	 * Load a class, prioritizing the inner class path before the parent's one.
	 * 
	 * @param name
	 *          Name of class to be loaded
	 * @param resolve
	 *          If true, resolve, i.e. link loaded class
	 * @return Class object of class to be loaded
	 * @exception ClassNotFoundException
	 *              Exception thrown when class to be searched not found
	 */
  protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    // Attention: This method must be synchronized to avoid deadlocks with synchronized (classSources)!!!
    // 
    synchronized (classSources)
    {
      // Try to find class among loaded classes
      Class classObject = findLoadedClass(name);
      if (classObject == null)
      {
        // Try to find class in class path
        try
        {
          classObject = findClass(name);
        }
        catch (ClassNotFoundException exception)
        {
        }
        if (classObject == null)
        {
          // Try to load class through the parent class loader
          try
          {
            classObject = super.loadClass(name, resolve);
          }
          catch (ClassNotFoundException exception)
          {
          }
          if (classObject == null)
          {
            throw new ClassNotFoundException(name);
          }
        }
      }

      // Linking class
      if (resolve)
      {
        resolveClass(classObject);
      }

      // Returning class
      return classObject;
    }
  }

  /**
	 * Load a resource, prioritizing the inner resource path before the parent's one.
	 * 
	 * @param name
	 *          Name of resource to be searched
	 * @return URL of resource
	 */
  public URL getResource(String name)
  {
    synchronized (resourceSources)
    {
      // Init resource location to null
      URL resourceLocation = null;

      // Try to find resource in resource path
      resourceLocation = findResource(name);
      if (resourceLocation == null)
      {
        // Try to load resource through the parent class loader
        resourceLocation = super.getResource(name);
      }

      // Returning resource
      return resourceLocation;
    }
  }
}

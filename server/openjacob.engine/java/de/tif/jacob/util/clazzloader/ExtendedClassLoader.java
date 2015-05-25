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
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * Custom class loader.
 * 
 * @created 26. April 2004
 */
public class ExtendedClassLoader extends SecureClassLoader implements IClassSource, ILibrarySource, IResourceSource
{
  static public final transient String RCS_ID = "$Id: ExtendedClassLoader.java,v 1.2 2009/03/16 13:56:48 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  // Sources
  protected final Vector classSources;
  protected final Vector librarySources;
  protected final Vector resourceSources;

  // Security access guid context
  private final AccessControlContext accessControlContext;

  /**
	 * Construct custom class loader.
	 * 
	 * @param parentClassLoader
	 *          Parent class loader
	 */
  public ExtendedClassLoader(ClassLoader parentClassLoader)
  {
    // Call constructor of base class
    super(parentClassLoader);

    // Instantiate private members
    classSources = new Vector();
    librarySources = new Vector();
    resourceSources = new Vector();
    accessControlContext = AccessController.getContext();
  }

  /**
	 * Get class path this class source references. Each entry including the last must be terminated with a semicolon.
	 * 
	 * @return Class path this class source references
	 */
  public String getClassPath()
  {
    StringBuffer result = new StringBuffer();
    if (getParent() instanceof IClassSource)
    {
      result.append(((IClassSource) getParent()).getClassPath());
    }
    synchronized (classSources)
    {
      for (Iterator iter = classSources.iterator(); iter.hasNext();)
      {
        result.append(((IClassSource) iter.next()).getClassPath());
      }
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
    if (getParent() instanceof ILibrarySource)
    {
      result.append(((ILibrarySource) getParent()).getLibraryPath());
    }
    synchronized (librarySources)
    {
      for (Iterator iter = librarySources.iterator(); iter.hasNext();)
      {
        result.append(((ILibrarySource) iter.next()).getLibraryPath());
      }
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
    if (getParent() instanceof IResourceSource)
    {
      result.append(((IResourceSource) getParent()).getResourcePath());
    }
    synchronized (resourceSources)
    {
      for (Iterator iter = resourceSources.iterator(); iter.hasNext();)
      {
        result.append(((IResourceSource) iter.next()).getResourcePath());
      }
    }
    return result.toString();
  }

  /**
	 * Append class source to the current class sources (at the end). Side effect: The class source will also be added to the resource sources if possible.
	 * 
	 * @param classSource
	 *          Class source to be added
	 */
  public void appendClassSource(IClassSource classSource)
  {
    if (classSource == null)
    {
      throw new NullPointerException();
    }
    synchronized (classSources)
    {
      classSources.add(classSource);

      if (classSource instanceof JARFile)
      {
        appendResourceSource((JARFile) classSource);
      }
      if (classSource instanceof JARDirectory)
      {
        appendResourceSource((JARDirectory) classSource);
      }
      if ((classSource instanceof ClassDirectory) || (classSource instanceof JavaDirectory))
      {
        String filePath = classSource.getClassPath();
        while (filePath.endsWith(";"))
        {
          filePath = filePath.substring(0, filePath.length() - 1);
        }
        appendResourceSource(new ResourceDirectory(new File(filePath)));
      }
    }
  }

  /**
	 * Insert class source into the current class sources (at the beginning). Side effect: The class source will also be added to the resource sources if possible.
	 * 
	 * @param classSource
	 *          Class source to be inserted
	 * @exception ClassNotFoundException
	 *              Exception thrown when class source invalid
	 */
  public void insertClassSource(IClassSource classSource) throws ClassNotFoundException
  {
    if (classSource == null)
    {
      throw new NullPointerException();
    }

    synchronized (classSources)
    {
      classSources.insertElementAt(classSource, 0);

      if (classSource instanceof JARFile)
      {
        insertResourceSource((JARFile) classSource);
      }
      if (classSource instanceof JARDirectory)
      {
        insertResourceSource((JARDirectory) classSource);
      }
      if ((classSource instanceof ClassDirectory) || (classSource instanceof JavaDirectory))
      {
        String filePath = classSource.getClassPath();
        while (filePath.endsWith(";"))
        {
          filePath = filePath.substring(0, filePath.length() - 1);
        }
        insertResourceSource(new ResourceDirectory(new File(filePath)));
      }
    }
  }

  /**
	 * Remove class source from the current class sources.
	 * 
	 * @param classSource
	 *          Class source to be removed
	 * @exception ClassNotFoundException
	 *              Exception thrown when class source invalid
	 */
  public void removeClassSource(IClassSource classSource) throws ClassNotFoundException
  {
    if (classSource == null)
    {
      throw new NullPointerException();
    }

    synchronized (classSources)
    {
      classSources.remove(classSource);
    }
  }

  /**
	 * Remove all class sources from the current class sources.
	 */
  public void removeAllClassSources()
  {
    synchronized (classSources)
    {
      classSources.clear();
    }
  }

  /**
	 * Append library source to the current library sources (at the end).
	 * 
	 * @param librarySource
	 *          library source to be added
	 */
  public void appendLibrarySource(ILibrarySource librarySource) throws ClassNotFoundException
  {
    if (librarySource == null)
    {
      throw new NullPointerException("librarySource is null");
    }
    synchronized (librarySources)
    {
      librarySources.add(librarySource);
    }
  }

  /**
	 * Insert library source into the current library sources (at the beginning).
	 * 
	 * @param librarySource
	 *          library source to be inserted
	 */
  public void insertLibrarySource(ILibrarySource librarySource) throws ClassNotFoundException
  {
    if (librarySource == null)
    {
      throw new NullPointerException("librarySource is null");
    }
    synchronized (librarySources)
    {
      librarySources.insertElementAt(librarySource, 0);
    }
  }

  /**
	 * Remove library source from the current library sources.
	 * 
	 * @param librarySource
	 *          library source to be removed
	 * @exception ClassNotFoundException
	 *              Exception thrown when library source invalid
	 */
  public void removeLibrarySource(ILibrarySource librarySource) throws ClassNotFoundException
  {
    if (librarySource == null)
    {
      throw new NullPointerException("librarySource is null");
    }
    synchronized (librarySources)
    {
      librarySources.remove(librarySource);
    }
  }

  /**
   * Remove all library sources from the current library sources.
   */
  public void removeAllLibrarySources()
  {
    synchronized (librarySources)
    {
      librarySources.clear();
    }
  }

  /**
	 * Append resource source to the current resource sources (at the end).
	 * 
	 * @param resourceSource
	 *          resource source to be added
	 * @exception ClassNotFoundException
	 *              Exception thrown when resource source invalid
	 */
  public void appendResourceSource(IResourceSource resourceSource)
  {
    if (resourceSource == null)
    {
      throw new NullPointerException("resourceSource is null");
    }
    synchronized (resourceSources)
    {
      resourceSources.add(resourceSource);
    }
  }

  /**
	 * Insert resource source into the current resource sources (at the beginning).
	 * 
	 * @param resourceSource
	 *          resource source to be inserted
	 * @exception ClassNotFoundException
	 *              Exception thrown when resource source invalid
	 */
  public void insertResourceSource(IResourceSource resourceSource) throws ClassNotFoundException
  {
    if (resourceSource == null)
    {
      throw new NullPointerException("resourceSource is null");
    }
    synchronized (resourceSources)
    {
      resourceSources.insertElementAt(resourceSource, 0);
    }
  }

  /**
   * Remove resource source from the current resource sources.
   * 
   * @param resourceSource
   *          resource source to be removed
   * @exception ClassNotFoundException
   *              Exception thrown when resource source invalid
   */
  public void removeResourceSource(IResourceSource resourceSource) throws ClassNotFoundException
  {
    if (resourceSource == null)
    {
      throw new NullPointerException("resourceSource is null");
    }
    synchronized (resourceSources)
    {
      resourceSources.remove(resourceSource);
    }
  }

  /**
   * Remove all resource sources from the current resource sources.
   */
  public void removeAllResourceSources()
  {
    synchronized (resourceSources)
    {
      resourceSources.clear();
    }
  }

  /**
	 * Suggest class source based on file.
	 * 
	 * @param classSourceFile
	 *          file to be used to suggest class source
	 * @return suggested class source
	 * @exception ClassNotFoundException
	 *              Exception raised in failure situation
	 */
  public IClassSource suggestClassSource(File classSourceFile) throws ClassNotFoundException
  {
    if (classSourceFile.exists())
    {
      // Check for directory
      if (classSourceFile.isDirectory())
      {
        if (classSourceFile.getName().startsWith("class"))
        {
          return new ClassDirectory(classSourceFile);
        }
        if (classSourceFile.getName().startsWith("lib"))
        {
          return new JARDirectory(classSourceFile);
        }
        if ((classSourceFile.getName().startsWith("java")) || (classSourceFile.getName().startsWith("source")) || (classSourceFile.getName().startsWith("src")))
        {
          return new JavaDirectory(classSourceFile);
        }
      }

      // Check for file
      if (classSourceFile.isFile())
      {
        if (classSourceFile.getName().endsWith(".class"))
        {
          return new ClassFile(classSourceFile);
        }
        if (classSourceFile.getName().endsWith(".jar"))
        {
          return new JARFile(classSourceFile);
        }
        if (classSourceFile.getName().endsWith(".java"))
        {
          return new JavaFile(classSourceFile);
        }
      }

      // Nothing matched
      throw new ClassNotFoundException("Failed to suggest class source");
    }
    else
    {
      throw new ClassNotFoundException("File doesn't exist; can't suggest class source [" + classSourceFile.getPath() + "]");
    }
  }

  /**
	 * Suggest library source based on file.
	 * 
	 * @param librarySourceFile
	 *          file to be used to suggest library source
	 * @return suggested library source
	 * @exception ClassNotFoundException
	 *              Exception raised in failure situation
	 */
  public ILibrarySource suggestLibrarySource(File librarySourceFile) throws ClassNotFoundException
  {
    if (librarySourceFile.exists())
    {
      // Check for directory
      if (librarySourceFile.isDirectory())
      {
        return new LibraryDirectory(librarySourceFile);
      }

      // Check for file
      if (librarySourceFile.isFile())
      {
        return new LibraryFile(librarySourceFile);
      }

      // Nothing matched
      throw new ClassNotFoundException("Failed to suggest library source");
    }
    else
    {
      throw new ClassNotFoundException("File doesn't exist; can't suggest library source");
    }
  }

  /**
	 * Suggest resource source based on file.
	 * 
	 * @param resourceSourceFile
	 *          file to be used to suggest resource source
	 * @return suggested resource source
	 * @exception ClassNotFoundException
	 *              Exception raised in failure situation
	 */
  public IResourceSource suggestResourceSource(File resourceSourceFile) throws ClassNotFoundException
  {
    if (resourceSourceFile.exists())
    {
      // Check for directory
      if (resourceSourceFile.isDirectory())
      {
        if (resourceSourceFile.getName().startsWith("lib"))
        {
          return new JARDirectory(resourceSourceFile);
        }
        else
        {
          return new ResourceDirectory(resourceSourceFile);
        }
      }

      // Check for file
      if (resourceSourceFile.isFile())
      {
        if (resourceSourceFile.getName().endsWith(".jar"))
        {
          return new JARFile(resourceSourceFile);
        }
        else
        {
          return new ResourceFile(resourceSourceFile);
        }
      }

      // Nothing matched
      throw new ClassNotFoundException("Failed to suggest resource source");
    }
    else
    {
      throw new ClassNotFoundException("File doesn't exist; can't suggest resource source");
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
    synchronized (classSources)
    {
      // Init class definition to null
      byte[] classDefinition = null;

      // Look for class in class path
      for (Iterator iter = classSources.iterator();(iter.hasNext()) && (classDefinition == null);)
      {
        try
        {
          IClassSource classSource = (IClassSource) iter.next();
          if ((classDefinition = classSource.getClassDefinition(name)) != null)
          {
            // Returning class definition
            return classDefinition;
          }
        }
        catch (ClassNotFoundException exception)
        {
          // Try next one
        }
      }
      throw new ClassNotFoundException(name);
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
    synchronized (classSources)
    {
      // Look for class in class path
      for (Iterator iter = classSources.iterator();(iter.hasNext());)
      {
        IClassSource classSource = (IClassSource) iter.next();
        if (classSource.hasClassDefinition(name))
        {
          return true;
        }
        // Try next one
      }
      return false;
    }
  }

  private final Class defineClass_FAKE_FOR_COMPILER_BUG_(String name, byte[] b, int off, int len) throws ClassFormatError
  {
    return defineClass(name, b, off, len, determineCodeSource(name));
  }
  
  protected CodeSource determineCodeSource(String name)
  {
    return null;
  }

  /**
	 * Find a class. This method is called in response to the loadClass() call.
	 * 
	 * @param name
	 *          Name of class to be searched
	 * @return Class object of class to be searched
	 * @exception ClassNotFoundException
	 *              Exception thrown when class to be searched not found
   * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
  protected synchronized Class findClass(final String name) throws ClassNotFoundException
  {
    // Attention: This method must be synchronized to avoid deadlocks with synchronized (classSources)!!!
    // 
    try
    {
      synchronized (classSources)
      {
        // Add the package information
        final int packageIndex = name.lastIndexOf('.') ;
        if (packageIndex != -1) {
          final String packageName = name.substring(0, packageIndex) ;
          final Package classPackage = getPackage(packageName) ;
          if (classPackage == null) {
            definePackage(packageName, null, null, null, null, null, null, null) ;
          }
        }
        
        // Perform priviledged action
        return (Class) AccessController.doPrivileged(new PrivilegedExceptionAction()
        {
          /**
					 * Main processing method for the ExtendedClassLoader object.
					 * 
					 * @return Class object of class to be searched
					 * @exception ClassNotFoundException
					 *              Exception thrown when class to be searched not found
					 */
          public Object run() throws ClassNotFoundException
          {
            byte[] classDefinition = getClassDefinition(name);
            return defineClass_FAKE_FOR_COMPILER_BUG_(name, classDefinition, 0, classDefinition.length);
          }
        }, accessControlContext);
      }
    }
    catch (java.security.PrivilegedActionException exception)
    {
      throw new ClassNotFoundException(exception.getException().getMessage());
    }
  }

  /**
	 * Get library location based on library name.
	 * 
	 * @param name
	 *          Name of library to be searched
	 * @return Absolute path name of native library
	 */
  public String getLibraryLocation(String name)
  {
    synchronized (librarySources)
    {
      // Init library location to null
      String libraryLocation = null;

      // Look for library in library path
      for (Iterator iter = librarySources.iterator();(iter.hasNext()) && (libraryLocation == null);)
      {
        ILibrarySource librarySource = (ILibrarySource) iter.next();
        if ((libraryLocation = librarySource.getLibraryLocation(name)) != null)
        {
          // Returning library location
          return libraryLocation;
        }
      }
      return libraryLocation;
    }
  }

  /**
	 * Find a library.
	 * 
	 * @param name
	 *          Name of library to be searched
	 * @return Absolute path name of a native library
	 */
  public String findLibrary(final String name)
  {
    try
    {
      synchronized (librarySources)
      {
        // Perform priviledged action
        return (String) AccessController.doPrivileged(new PrivilegedExceptionAction()
        {
          /**
					 * Main processing method for the ExtendedClassLoader object.
					 * 
					 * @return Absolute path name of a native library
					 */
          public Object run()
          {
            return getLibraryLocation(name);
          }
        }, accessControlContext);
      }
    }
    catch (java.security.PrivilegedActionException exception)
    {
      return null;
    }
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
    synchronized (resourceSources)
    {
      // Init resource location to null
      URL resourceLocation = null;

      // Look for resource in resource path
      for (Iterator iter = resourceSources.iterator();(iter.hasNext()) && (resourceLocation == null);)
      {
        IResourceSource resourceSource = (IResourceSource) iter.next();
        if ((resourceLocation = resourceSource.getResourceLocation(name)) != null)
        {
          // Returning resource location
          return resourceLocation;
        }
      }
      return resourceLocation;
    }
  }

  /**
	 * Find a resource. This method is called in response to the getResource() call.
	 * 
	 * @param name
	 *          Name of resource to be searched
	 * @return URL of resource
	 */
  protected URL findResource(final String name)
  {
    try
    {
      synchronized (resourceSources)
      {
        // Perform priviledged action
        return (URL) AccessController.doPrivileged(new PrivilegedExceptionAction()
        {
          /**
					 * Main processing method for the ExtendedClassLoader object.
					 * 
					 * @return URL of resource
					 */
          public Object run()
          {
            return getResourceLocation(name);
          }
        }, accessControlContext);
      }
    }
    catch (java.security.PrivilegedActionException exception)
    {
      return null;
    }
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
    synchronized (resourceSources)
    {
      // Init resource locations to null
      URL resourceLocation = null;
      Enumeration resourceLocations = null;
      Vector resourceAllLocations = new Vector();

      // Look for resource in resource path
      for (Iterator iter = resourceSources.iterator(); iter.hasNext();)
      {
        IResourceSource resourceSource = (IResourceSource) iter.next();
        if ((resourceLocations = resourceSource.getResourceLocations(name)) != null)
        {
          // Adding resource locations
          while (resourceLocations.hasMoreElements())
          {
            resourceLocation = (URL) resourceLocations.nextElement();
            resourceAllLocations.add(resourceLocation);
          }
        }
      }
      return resourceAllLocations.elements();
    }
  }

  /**
	 * Find an enumeration of resources. This method is called in response to the getResources() call.
	 * 
	 * @param name
	 *          Name of resources to be searched
	 * @return Enumeration of URLs of resources
	 * @exception IOException
	 *              Exception raised in failure situation
   * @see java.lang.ClassLoader#findResources(java.lang.String)
	 */
  protected Enumeration findResources(final String name) throws IOException
  {
    try
    {
      synchronized (resourceSources)
      {
        // Perform priviledged action
        return (Enumeration) AccessController.doPrivileged(new PrivilegedExceptionAction()
        {
          /**
					 * Main processing method for the ExtendedClassLoader object.
					 * 
					 * @return Enumeration of URLs of resources
					 */
          public Object run()
          {
            return getResourceLocations(name);
          }
        }, accessControlContext);
      }
    }
    catch (java.security.PrivilegedActionException exception)
    {
      return null;
    }
  }
}

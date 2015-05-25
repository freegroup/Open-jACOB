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

package de.tif.jacob.deployment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Version;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.util.clazzloader.ClassDirectory;
import de.tif.jacob.util.clazzloader.JARFile;
import de.tif.jacob.util.clazzloader.ResourceDirectory;
import de.tif.jacob.util.file.Directory;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClassProvider
{
  static public final transient String RCS_ID = "$Id: ClassProvider.java,v 1.5 2010/08/15 00:38:13 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.5 $";
  
  private static final Log logger = LogFactory.getLog(ClassProvider.class);
  
  // dummy handler 
  private static final Object NOT_EXISTING_HANDLER = new Object();
  
  // classLoader -> mapOfInstances (className->object)
  private static final HashMap instanceMap    = new HashMap();
  
  // appDef.name()+appDef.version() -> Classloader
  private static final HashMap classLoaderMap = new HashMap();
  

  /**
   * Return the instance of the class 'clazzName'. The returnd object is always the same instance
   * for the hadns over application/version.
   * 
   * @param applicationName
   * @param version
   * @param clazzName
   * @return
   */
  public static Object createInstance(IApplicationDefinition appDef, String clazzName)
  {
    try
    {
      return getClassLoader(appDef.getName(), appDef.getVersion()).loadClass(clazzName).newInstance();
    }
    catch (ClassNotFoundException e)
    {
      if (logger.isDebugEnabled())
      	logger.debug("No event class ["+clazzName+"] found.");
    }
    catch(InstantiationException ie)
    {
      //ie.printStackTrace();
      // this can happen from anonymous inner classes. Ignore them
    }
    catch(Exception e)
    {
      if (logger.isWarnEnabled())
				logger.warn("Instantiation of class " + clazzName + " failed.", e);
    }
    return null;
  }

  /**
   * Retrieve all event handler from one package.
   * 
   * @param packageName
   * @return List{Object} with single instances of the classes in the hands over package
   */
  public static List createInstancesFromPackage(IApplicationDefinition app, String packageName, Class superClazz) throws Exception
  {
    ArrayList result = new ArrayList();
    
    String jacobDir = DeployManager.getDeployClassDir(app.getName(), app.getVersion());
    String clazzDirectory = jacobDir + packageName.replace('.', File.separatorChar) + File.separator;
    ArrayList files = Directory.getAll(new File(clazzDirectory),false);
    for (Iterator iter = files.iterator(); iter.hasNext();)
    {
      File element = (File) iter.next();
      if(element.getName().endsWith(".class"))
      {  
        String clazzName = packageName+"."+element.getName().substring(0,element.getName().length()-6);
        Object handler = createInstance(app,clazzName);
        // DON'T(!!!) use 'instanceof' because they are loaded with different class loaders.
        // In this case the 'instanceof' returns always false.
        // Do the long way - compare the names of the super classes.
        //
        if(handler!=null /*&& Beans.isInstanceOf(handler, superClazz)*/)
          result.add(handler);
      }
    }
    return result;
  }
  
  /**
   * Return the instance of the class 'clazzName'. The returned object is always the same instance
   * for the hands over application/version.
   * 
   * @param applicationName
   * @param version
   * @param clazzName
   * @return
   */
  public static Object getInstance(String applicationName, String version, String clazzName)
	{
		try
		{
		  if(clazzName==null || clazzName.length()==0)
		    return null;
		  
			  ApplicationClassLoader loader = getClassLoader(applicationName, Version.parseVersion(version));

			  Map objects = null;
			  
			  // sync. Zugriff auf die allgemeine instanceMap
			  //
				synchronized (instanceMap)
				{
				  // all cached instances of this application (class loader)
					objects = (Map) instanceMap.get(loader);
					if (objects == null)
					{
					  objects = new HashMap();
				    instanceMap.put(loader, objects);
					}
				}
				
				// sync. Zugriff auf die Application spezifische instanceMap
				//
				synchronized (objects)
				{
					// has been requested the object before?
					Object handler = objects.get(clazzName);
					if (handler == null)
					{
						// try to load handler
						try
						{
							Class clazz = loader.loadClass(clazzName);
							handler = clazz.newInstance();
						}
						catch (ClassNotFoundException e)
						{
							// put dummy handler in map to avoid expensive load class
							// operation the next time.
					    handler = NOT_EXISTING_HANDLER;
						}
	
					  objects.put(clazzName, handler);
					}
	
					if (handler == NOT_EXISTING_HANDLER)
					{
						if (logger.isDebugEnabled())
							logger.debug("No event class ["+clazzName+"] found.");
						handler = null;
					}
	
					return handler;
				}
		}
    catch (InstantiationException ie)
    {
      // this can happen from anonymous inner classes. Ignore them
    }
    catch (IllegalAccessException ie)
    {
      // this can happen by ExceptionHandler.getExtendedStackTrace() accessing a private inner class
    }
		catch (Exception e)
		{
			if (logger.isWarnEnabled())
				logger.warn("Instantiation of class " + clazzName + " failed.", e);
		}
		return null;
	}
  
  /**
   * Liefert ein Object von der übergebenen Klasse zurück -wenn möglich. Objekte werden dabei in einem
   * Cache gehalten. D.h. ein mehrfacher Aufruf dieser Methode mit dem gleichen Klassennamen liefert auch
   * immer das identische Objekt zurück.<br>
   * <br>
   * Ausnahme: Falls ein deployment einer Anwendung durchgeführt wurde, wird der Objektcache verworfen.
   * @param app
   * @param clazzName
   * @return
   */
  public static Object getInstance(IApplicationDefinition app, String clazzName)
  {
    Object obj = getInstance(app.getName(), app.getVersion().toString(), clazzName);

    if(obj!=null && obj.getClass().getClassLoader()instanceof ApplicationClassLoader)
      ((ApplicationClassLoader)obj.getClass().getClassLoader()).setApplicationDefinition(app);
    return obj;
  }
  
  /**
   * Retrieve all event handler from one package.
   * 
   * @param packageName
   * @return List{Object} with single instances of the classes in the hands over package
   * TODO: Die ï¿½berprï¿½fung auf die superClazz ist nicht implementiert!!
   */
  public static List getInstancesFromPackage(IApplicationDefinition app, String packageName, Class superClazz) throws Exception
  {
    ArrayList result = new ArrayList();
    
    String jacobDir = DeployManager.getDeployClassDir(app.getName(), app.getVersion());
    String clazzDirectory = jacobDir+packageName.replace('.',File.separatorChar)+File.separator;
    ArrayList files = Directory.getAll(new File(clazzDirectory),false);
    for (Iterator iter = files.iterator(); iter.hasNext();)
    {
      File element = (File) iter.next();
      if(element.getName().endsWith(".class"))
      {  
	      String clazzName = packageName+"."+element.getName().substring(0,element.getName().length()-6);
	      Object handler = getInstance(app,clazzName);
	      // DON'T(!!!) use 'instanceof' because they are loaded with different class loaders.
	      // In this case the 'instanceof' returns always false.
	      // Do the long way - compare the names of the super classes.
	      //
	      if(handler!=null /*&& Beans.isInstanceOf(handler, superClazz)*/)
	      	result.add(handler);
      }
    }
    
    return result;
  }
  

  /**
   * Remove application version class loader
   * 
   * @param entry
   */
  protected static void removeClassLoader(DeployEntry entry)
	{
		// remove the old class loader of the application...if any exists
  	Object classLoader=null;
		synchronized (classLoaderMap)
		{
      // attention: use toShortString() to do not distinguish version fix number
			classLoader = classLoaderMap.remove(entry.getName() + entry.getVersion().toShortString());
			if(classLoader!=null && logger.isInfoEnabled())
				logger.info("Old Class loader removed for:" + entry);
		}
		
		// remove all cached object instances of this class loader
		if (classLoader != null)
		{
			synchronized(instanceMap)
			{
				instanceMap.remove(classLoader);
			}
		}
		
	}

  private static ApplicationClassLoader getClassLoader(String applicationName, Version version) throws ClassNotFoundException
  {
    synchronized (classLoaderMap)
    {
      String key = applicationName + version.toShortString();
		  ApplicationClassLoader loader = (ApplicationClassLoader) classLoaderMap.get(key);
	
		  if(loader!=null)
		  	return loader;
		  
		  loader = new ApplicationClassLoader(ClassProvider.class.getClassLoader(), applicationName);
	    String clazzDir  = DeployManager.getDeployClassDir(applicationName, version);
	    String libDir    = clazzDir+"lib"+File.separator;
	    ArrayList jars = Directory.getAll(new File(libDir),false);
	    Iterator iter = jars.iterator();
	    while(iter.hasNext())
	    {
	    	File file = (File)iter.next();
	    	if(file.isFile() && file.getName().endsWith(".jar"))
	    	{
          if(logger.isInfoEnabled())
            logger.info("Adding jacapp related jar:" + file);
	    		loader.appendClassSource(new JARFile(file));
	    	}
	    }
			loader.appendClassSource(new ClassDirectory(new File(clazzDir)));
			loader.appendResourceSource(new ResourceDirectory(new File(clazzDir)));
	
	    classLoaderMap.put(key,loader);
	    return loader;
    }
  }
}

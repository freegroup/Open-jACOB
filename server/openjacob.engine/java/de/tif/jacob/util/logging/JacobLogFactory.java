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
/*
 * Created on 20.03.2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.util.logging;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;
import org.apache.commons.logging.impl.LogFactoryImpl;

public final class JacobLogFactory extends LogFactoryImpl
{
  protected static LogFactory originalFactory;

  /**
   * Not used so far, but keep for future!
   */
  public static void oldinit()
  {
    try
    {
      String factoryProperty = System.getProperty(LogFactory.FACTORY_PROPERTY);
      if (factoryProperty != null)
      {
        System.out.println("JacobLogFactory#init(): Log factory set by system property: " + factoryProperty);
        return;
      }

      // running in a web container?
      ClassLoader loader = LogFactory.getContextClassLoader();
      if (loader == null)
      {
        System.out.println("JacobLogFactory#init(): Could not determine context class loader");
        return;
      }

      LogFactory factory = LogFactory.getFactory();
      if (factory == null)
      {
        System.out.println("JacobLogFactory#init(): Could not determine original factory");
        return;
      }

      if (JacobLogFactory.class.equals(factory.getClass()))
      {
        System.out.println("JacobLogFactory#init(): jACOB factory already set");
      }
      else
      {
        System.out.println("Before: Logger factory class=" + factory);

        synchronized (LogFactory.factories)
        {
          // temporary set system property to enforce creation of our factory
          System.setProperty(LogFactory.FACTORY_PROPERTY, JacobLogFactory.class.getName());
          try
          {
            // release old factory
            LogFactory.release(loader);

            // create and cache new factory
            LogFactory jacobFactory = LogFactory.getFactory();
            jacobFactory.getInstance(JacobLogFactory.class);

            // remember old factory
            originalFactory = factory;

            System.out.println("After: Logger factory class=" + jacobFactory);
          }
          finally
          {
            // reset system property
            System.setProperty(LogFactory.FACTORY_PROPERTY, factory.getClass().getName());
          }
        }
      }
    }
    catch (Exception ex)
    {
      System.err.println("JacobLogFactory#init(): Error: " + ex.toString());
      ex.printStackTrace();
    }
  }

  /**
   * Not used so far, but keep for future!
   */
  public static void init()
  {
    try
    {
      // running in a web container?
      ClassLoader loader = LogFactory.getContextClassLoader();
      if (loader == null)
      {
        System.out.println("JacobLogFactory#init(): Could not determine context class loader");
        return;
      }

      LogFactory factory = LogFactory.getFactory();
      System.out.println("Before: Logger factory class=" + factory);

      // release old factory
      LogFactory.release(loader);

      // create and cache new factory
      LogFactory jacobFactory = LogFactory.getFactory();
      jacobFactory.getInstance(JacobLogFactory.class);

      System.out.println("After: Logger factory class=" + jacobFactory);
    }
    catch (Exception ex)
    {
      System.err.println("JacobLogFactory#init(): Error: " + ex.toString());
      ex.printStackTrace();
    }
  }

  /**
   * Must be public!
   */
  public JacobLogFactory()
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.impl.LogFactoryImpl#getLogClassName()
   */
  protected String getLogClassName()
  {
    if (originalFactory != null)
      return JacobLog.class.getName();

    // always use JDK14 logging
    return Jdk14Logger.class.getName();
  }
}
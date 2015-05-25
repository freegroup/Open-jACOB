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

package de.tif.jacob.util.top;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class Top
{
  private static final Log logger = LogFactory.getLog(Top.class);
  
  private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
  
  private static Map topImplementations = new HashMap();
  
  private static ITop myImplementation = null;
  
  static 
  {
    topImplementations.put("windows 2000",de.tif.jacob.util.top.win32.Top.class);
    topImplementations.put("windows 95"  ,de.tif.jacob.util.top.win32.Top.class);
    topImplementations.put("windows nt"  ,de.tif.jacob.util.top.win32.Top.class);
    topImplementations.put("windows ce"  ,de.tif.jacob.util.top.win32.Top.class);
    topImplementations.put("windows xp"  ,de.tif.jacob.util.top.win32.Top.class);
    
// FREEGROUP: Linux Implementierung ist Fehlerhaft    
//    topImplementations.put("linux"       ,de.tif.jacob.util.top.linux.Top.class);
    
    topImplementations.put("solaris"     ,de.tif.jacob.util.top.solaris.Top.class);
    topImplementations.put("sunos"       ,de.tif.jacob.util.top.solaris.Top.class);
    
    Class clazz = (Class) topImplementations.get(OS_NAME);
    if (clazz != null)
    {
      try
      {
        myImplementation = (ITop) clazz.newInstance();
      }
      catch (Exception ex)
      {
        logger.error("Could not create ITop instance for class: " + clazz.getName(), ex);
      }
    }
    else
    {
      logger.warn("No implementation found for command [top] and os.name ["+OS_NAME+"]");
    }
  }
  
  /**
   * Returns the top implementation for this operating system.
   * 
   * @return the top implementation or <code>null</code>, if no
   *         implementation accessible for this operating system.
   */
  public static ITop get()
  {
    return myImplementation;
  }
  
  /**
   * ONLY TO TEST THIS CLASS
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception
  {
    ITop itop = get();
    if (itop != null)
    {
      Iterator iter = itop.getProcessInformation().iterator();
      while (iter.hasNext())
      {
        IProcessInformation obj = (IProcessInformation) iter.next();
        System.out.println(obj);
      }
    }
  }
}

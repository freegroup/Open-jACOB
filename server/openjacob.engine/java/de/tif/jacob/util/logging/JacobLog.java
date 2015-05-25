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
 * Created on 21.03.2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.util.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Jdk14Logger;

import de.tif.jacob.core.Bootstrap;

public class JacobLog implements Log
{
  private final Log jdk14Log;
  private final Log origLog;

  public JacobLog(String name)
  {
    this.jdk14Log = new Jdk14Logger(name);
    this.origLog = JacobLogFactory.originalFactory.getInstance(name);
  }

  private Log getLog()
  {
    return LoggingManager.isLogPropertiesEnabled() && Bootstrap.isOk() ? this.jdk14Log : this.origLog;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#debug(java.lang.Object,
   *      java.lang.Throwable)
   */
  public void debug(Object arg0, Throwable arg1)
  {
    getLog().debug(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#debug(java.lang.Object)
   */
  public void debug(Object arg0)
  {
    getLog().debug(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#error(java.lang.Object,
   *      java.lang.Throwable)
   */
  public void error(Object arg0, Throwable arg1)
  {
    getLog().error(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#error(java.lang.Object)
   */
  public void error(Object arg0)
  {
    getLog().error(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#fatal(java.lang.Object,
   *      java.lang.Throwable)
   */
  public void fatal(Object arg0, Throwable arg1)
  {
    getLog().fatal(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
   */
  public void fatal(Object arg0)
  {
    getLog().fatal(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#info(java.lang.Object,
   *      java.lang.Throwable)
   */
  public void info(Object arg0, Throwable arg1)
  {
    getLog().info(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#info(java.lang.Object)
   */
  public void info(Object arg0)
  {
    getLog().info(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#isDebugEnabled()
   */
  public boolean isDebugEnabled()
  {
    return getLog().isDebugEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#isErrorEnabled()
   */
  public boolean isErrorEnabled()
  {
    return getLog().isErrorEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#isFatalEnabled()
   */
  public boolean isFatalEnabled()
  {
    return getLog().isFatalEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#isInfoEnabled()
   */
  public boolean isInfoEnabled()
  {
    return getLog().isInfoEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#isTraceEnabled()
   */
  public boolean isTraceEnabled()
  {
    return getLog().isTraceEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#isWarnEnabled()
   */
  public boolean isWarnEnabled()
  {
    return getLog().isWarnEnabled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#trace(java.lang.Object,
   *      java.lang.Throwable)
   */
  public void trace(Object arg0, Throwable arg1)
  {
    getLog().trace(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#trace(java.lang.Object)
   */
  public void trace(Object arg0)
  {
    getLog().trace(arg0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#warn(java.lang.Object,
   *      java.lang.Throwable)
   */
  public void warn(Object arg0, Throwable arg1)
  {
    getLog().warn(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.commons.logging.Log#warn(java.lang.Object)
   */
  public void warn(Object arg0)
  {
    getLog().warn(arg0);
  }
}

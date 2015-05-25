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
package de.tif.jacob.webdav.impl.log;

import de.tif.jacob.webdav.impl.WebDAV;

public class Logger implements org.apache.slide.util.logger.Logger
{
  static public final transient String RCS_ID = "$Id: Logger.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public final static int DEFAULT_LEVEL = INFO;

  private int slideLevel = DEFAULT_LEVEL;
  
  public Logger()
  {
  }

  public void log(Object data, Throwable throwable, String channel, int slideLevel)
  {
    if (this.slideLevel < slideLevel)
      return;
    
    switch (slideLevel)
    {
      case EMERGENCY:
        WebDAV.logger.fatal(data.toString(), throwable);
        break;
        
      case CRITICAL:
      case ERROR:
        WebDAV.logger.error(data.toString(), throwable);
        break;
        
      case WARNING:
        WebDAV.logger.warn(data.toString(), throwable);
        break;
        
      case INFO:
        WebDAV.logger.info(data.toString(), throwable);
        break;
        
      case DEBUG:
        WebDAV.logger.debug(data.toString(), throwable);
        break;
    }
  }

  public void log(Object data, String channel, int slideLevel)
  {
    if (this.slideLevel < slideLevel)
      return;
    
    switch (slideLevel)
    {
      case EMERGENCY:
        if (data instanceof Throwable)
        {
          WebDAV.logger.fatal(data.toString(), (Throwable) data);
        }
        else
        {
          WebDAV.logger.fatal(data);
        }
        break;
        
      case CRITICAL:
      case ERROR:
        if (data instanceof Throwable)
        {
          WebDAV.logger.error(data.toString(), (Throwable) data);
        }
        else
        {
          WebDAV.logger.error(data);
        }
        break;
        
      case WARNING:
        if (data instanceof Throwable)
        {
          WebDAV.logger.warn(data.toString(), (Throwable) data);
        }
        else
        {
          WebDAV.logger.warn(data);
        }
        break;
        
      case INFO:
        if (data instanceof Throwable)
        {
          WebDAV.logger.info(data.toString(), (Throwable) data);
        }
        else
        {
          WebDAV.logger.info(data);
        }
        break;
        
      case DEBUG:
        if (data instanceof Throwable)
        {
          WebDAV.logger.debug(data.toString(), (Throwable) data);
        }
        else
        {
          WebDAV.logger.debug(data);
        }
        break;
    }
  }

  public void log(Object data, int level)
  {
    this.log(data, DEFAULT_CHANNEL, level);
  }

  public void log(Object data)
  {
    this.log(data, DEFAULT_CHANNEL, DEFAULT_LEVEL);
  }

  public int getLoggerLevel(String channel)
  {
    // ignore channel
    return getLoggerLevel();
  }

  public int getLoggerLevel()
  {
    if (DEBUG <= this.slideLevel && WebDAV.logger.isDebugEnabled())
      return DEBUG;

    if (INFO <= this.slideLevel && WebDAV.logger.isInfoEnabled())
      return INFO;

    if (WARNING <= this.slideLevel && WebDAV.logger.isWarnEnabled())
      return WARNING;

    if (ERROR <= this.slideLevel && WebDAV.logger.isErrorEnabled())
      return ERROR;

    return EMERGENCY;

  }

  public void setLoggerLevel(int slideLevel)
  {
    this.slideLevel = slideLevel;
  }

  public void setLoggerLevel(String channel, int slideLevel)
  {
    // ignore channel
    setLoggerLevel(slideLevel);
  }

  public boolean isEnabled(String channel, int slideLevel)
  {
    // ignore channel
    return isEnabled(slideLevel);
  }

  public boolean isEnabled(int slideLevel)
  {
    if (this.slideLevel < slideLevel)
      return false;
    
    switch (slideLevel)
    {
      case EMERGENCY:
        return WebDAV.logger.isFatalEnabled();
        
      case CRITICAL:
      case ERROR:
        return WebDAV.logger.isErrorEnabled();
        
      case WARNING:
        return WebDAV.logger.isWarnEnabled();
        
      case INFO:
        return WebDAV.logger.isInfoEnabled();
        
      case DEBUG:
        return WebDAV.logger.isDebugEnabled();
    }
    
    return WebDAV.logger.isTraceEnabled();
  }
}

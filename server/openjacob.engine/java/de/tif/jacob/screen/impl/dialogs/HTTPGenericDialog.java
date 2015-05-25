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

package de.tif.jacob.screen.impl.dialogs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.Property;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.HTTPDomain;
import de.tif.jacob.screen.impl.HTTPGroup;
import de.tif.jacob.screen.impl.IDProvider;

/**
 *
 */
public abstract class HTTPGenericDialog implements ICallbackDialog
{
  private boolean autoclose = true;

  transient static private final Log logger  = LogFactory.getLog(HTTPGenericDialog.class);

  int secondsBeforeTimeout=Property.TIMEOUTINTERVAL_DIALOG.getIntValue();

  protected final HTTPDomain      domain;
  protected final HTTPGroup       group;
  protected final int id;

  public abstract void processEvent(IClientContext context, String event, String value) throws Exception;

  protected HTTPGenericDialog( HTTPDomain domain, HTTPGroup group)
  {
    this.domain = domain;
    this.group  = group;
    this.id = IDProvider.next();
  }
  
  public final int getSecondsBeforeTimeout()
  {
    return secondsBeforeTimeout;
  }
  
  public final void setSecondsBeforeTimeout(int sec)
  {
    this.secondsBeforeTimeout = sec;
  }
  /**
   * @param secondsBeforeTimeout The secondsBeforeTimeout to set.
   */
  public synchronized final void decrementSecondsBeforeTimeout(int amount)
  {
    this.secondsBeforeTimeout-=amount;
  }
  
  
  public final boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    if( guid!=getId())
    {
      logger.error("processEvent for wrong dialog called.");
      return false;
    }
    
    HTTPDomain oldDomain= (HTTPDomain)context.getDomain();
    HTTPGroup  oldGroup = (HTTPGroup)context.getGroup();
    
    try
    {
      ((HTTPClientContext)context).setDomain(domain);
      ((HTTPClientContext)context).setGroup(group);
      
      processEvent(context,event,value);
    }
    finally
    {
      ((HTTPClientContext)context).setDomain(oldDomain);
      ((HTTPClientContext)context).setGroup(oldGroup);
    }
    
    return true;
  }
  
  public final void setAutoclose(boolean autoclose)
  {
    this.autoclose = autoclose;
  }
  
  public final boolean getAutoclose()
  {
    return autoclose;
  }
  
  public final int getId()
  {
    return this.id;
  }
}

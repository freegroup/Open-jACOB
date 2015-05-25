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

package de.tif.jacob.thread;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.deployment.ApplicationClassLoader;

/**
 *
 */
public abstract class ManagedThread extends Thread
{
  protected final ApplicationThreadContext context;
  public ManagedThread()
  {
    ApplicationClassLoader loader= (ApplicationClassLoader)this.getClass().getClassLoader();
    this.context = new ApplicationThreadContext(loader.getApplicationDefinition());
    setDaemon(true);
  }
  
  protected IDataAccessor getDataAccessor()
  {
    return context.getDataAccessor();
  }
  
  public final void run()
  {
    try
    {
      Context.setCurrent(this.context);
      run(context);
    }
    finally
    {
      Context.setCurrent(null);
    }
  }
  
  public abstract void run(ApplicationThreadContext context);
}

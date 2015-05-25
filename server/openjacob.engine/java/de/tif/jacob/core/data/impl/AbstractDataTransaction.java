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

package de.tif.jacob.core.data.impl;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.internal.IDataTransactionInternal;
import de.tif.jacob.core.definition.ITableAlias;

/**
 * Base abstract transaction implementation
 * 
 * @author Andreas Sonntag
 */
public abstract class AbstractDataTransaction implements IDataTransactionInternal
{
	static public transient final String RCS_ID = "$Id: AbstractDataTransaction.java,v 1.2 2009/03/17 21:06:50 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

  static private final Object LOCK = IDataTransaction.class;
  
  private static long idCounter = 1;
  
  // Set<Integer>
  private static Set activeTransactionIds = new HashSet();
  
  private final Long id;
  
  protected AbstractDataTransaction()
  {
    synchronized (LOCK)
    {
      this.id = new Long(idCounter++);
      activeTransactionIds.add(this.id);
    }
  }
  
  public static boolean isActive(long transactionId)
  {
    synchronized (LOCK)
    {
      return activeTransactionIds.contains(new Long(transactionId));
    }
  }
  
  protected final void unregister()
  {
    synchronized (LOCK)
    {
      activeTransactionIds.remove(this.id);
    }
  }
  
  public final long getId()
  {
    return this.id.longValue();
  }
  
  public final Locale getApplicationLocale()
  {
    return Context.getCurrent().getApplicationLocale();
  }
  
  protected abstract IDataTableRecord getRecord(ITableAlias tableAlias, IDataKeyValue primaryKey);
}

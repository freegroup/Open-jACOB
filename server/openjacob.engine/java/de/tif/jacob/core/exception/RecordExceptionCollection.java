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

package de.tif.jacob.core.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import de.tif.jacob.core.Context;
import de.tif.jacob.i18n.Message;

/**
 * Internal collection exception.
 * 
 * @author Andreas Sonntag
 * @since 2.8.5
 */
public final class RecordExceptionCollection extends UserException
{
  private final Collection exceptions = new ArrayList();
  private final Collection unmodifiable = Collections.unmodifiableCollection(this.exceptions);

  public RecordExceptionCollection(Message message)
  {
    super(message);
  }

  public RecordExceptionCollection(String message)
  {
    super(message);
  }

  public void add(RecordException exc)
  {
    this.exceptions.add(exc);
  }

  public Collection getExceptions()
  {
    return this.unmodifiable;
  }

  public String getDetails()
  {
    Context context = Context.getCurrent();
    StringBuffer details = new StringBuffer("");
    Iterator iter = this.exceptions.iterator();
    while (iter.hasNext())
    {
      RecordException recordException = (RecordException) iter.next();
      
      details.append(recordException.getLocalizedMessage(context.getLocale()));
      details.append("\n");
    }

    return details.toString();
  }
}

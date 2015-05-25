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
package de.tif.jacob.webdav.impl;

import java.util.Date;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.ManagedResource;

/**
 * Abstract WebDav document proxy implementation.
 * 
 * @author Andreas Sonntag
 */
public abstract class WebdavDocument extends ManagedResource
{
  static public final transient String RCS_ID = "$Id: WebdavDocument.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private final String id;
  private final String owner;
  private final Date dummy = new Date();
  
  public WebdavDocument()
  {
    this.id = Integer.toString(hashCode());
    
    Context context = Context.getCurrent();
    this.owner = context.getUser().getLoginId();
    context.registerForWindow(this);
    WebdavDocumentManager.register(this);
  }
  
  /**
   * @return Returns the id.
   */
  public final String getId()
  {
    return id;
  }
  
  public final String getOwner()
  {
    return this.owner;
  }
  
  public final Date getCreationDate()
  {
    return this.dummy;
  }
  
  public final Date getModificationDate()
  {
    return this.dummy;
  }
  
  public abstract int getContentLength();
  
  public abstract byte[] getContent();

  public abstract boolean setContent(byte[] content);
  
  public abstract String getName();
  
  /**
   * Returns the document name as follows:<br>
   * 1234567/myname, i.e. docId/docname
   * 
   * @return the full document name
   */
  public final String getFullName()
  {
    String name = getName();
    if (name != null)
      return this.id + "/" + name;
    return null;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public final String toString()
  {
    return this.id;
  }
  
  public final void close()
  {
    WebdavDocumentManager.unregister(this);
    Context.getCurrent().unregister(this);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.ManagedResource#release()
   */
  public final void release()
  {
    WebdavDocumentManager.unregister(this);
  }

}

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

package de.tif.jacob.core.data.impl.index;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * In-memory Lucene data source implementation.
 * 
 * @since 2.10
 * @author Andreas Sonntag
 */
public final class InMemoryLuceneDataSource extends LuceneDataSource
{
  static public transient final String RCS_ID = "$Id: InMemoryLuceneDataSource.java,v 1.1 2010/07/15 19:24:20 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  private RAMDirectory directory;

  /**
   * Constructor
   * 
   * @param name
   */
  public InMemoryLuceneDataSource(String name)
  {
    super(name, null);
  }

  protected synchronized Directory getDirectory() throws Exception
  {
    if (this.directory == null)
      this.directory = new RAMDirectory();
    return this.directory;
  }

  public synchronized void destroy()
  {
    // IBIS: Should be closed in connection object or similar
    if (this.directory != null)
    {
      this.directory.close();
      this.directory = null;
    }

    super.destroy();
  }
}

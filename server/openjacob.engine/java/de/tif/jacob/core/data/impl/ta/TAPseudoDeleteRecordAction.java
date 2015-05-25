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

package de.tif.jacob.core.data.impl.ta;

import de.tif.jacob.core.data.impl.DataExecutionContext;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TAPseudoDeleteRecordAction extends TADeleteRecordAction
{
	/**
	 * @param record
	 */
	public TAPseudoDeleteRecordAction(DataTableRecord record)
	{
		super(record);
	}

  public void execute(DataSource dataSource, DataExecutionContext context) throws Exception
  {
    // pseudo action, i.e. nothing more to do
  }
  
  public void executeAfterCommit() throws Exception
  {
    getRecord().getAccessorInternal().actualizeRecordCache(this);
  }

  public void executeUpdateIndex(IIndexUpdateContext context) throws Exception
  {
    // do nothing here
  }
}

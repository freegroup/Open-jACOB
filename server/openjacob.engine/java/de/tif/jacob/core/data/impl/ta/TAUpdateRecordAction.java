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

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.impl.DataExecutionContext;
import de.tif.jacob.core.data.impl.DataRecordMode;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.exception.UserException;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class TAUpdateRecordAction extends TARecordAction
{
  static public transient final String        RCS_ID = "$Id: TAUpdateRecordAction.java,v 1.3 2010/07/13 17:55:22 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.3 $";
  
  
	public TAUpdateRecordAction(DataTableRecord record)
	{
		super(record);
	}

  public DataRecordMode getRecordMode()
  {
    return DataRecordMode.UPDATE;
  }
  
	public void execute(DataSource dataSource, DataExecutionContext context) throws Exception, UserException
	{
    dataSource.execute(context, this);
  }

  public void executeAfterCommit() throws Exception
  {
    getRecord().getAccessorInternal().actualizeRecordCache(this);
  }

  public void executeUpdateIndex(IIndexUpdateContext context) throws Exception
  {
    DataTableRecord record = getRecord();
    IDataKeyValue oldPrimaryKeyValue = record.getOldPrimaryKeyValue();
    if (oldPrimaryKeyValue != null)
    {
      // the primary key has been modified
      // -> delete entry with old pkey and add entry with new key
      context.removeFromIndex(record.getTableAlias(), oldPrimaryKeyValue);
      context.addToIndex(record);
    }
    else
    {
      context.updateWithinIndex(record);
    }
  }
}

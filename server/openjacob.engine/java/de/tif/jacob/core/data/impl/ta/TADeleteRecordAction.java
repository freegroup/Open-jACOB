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
import de.tif.jacob.core.data.impl.DataRecordMode;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.DataSource;
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TADeleteRecordAction extends TARecordAction
{
  static public transient final String        RCS_ID = "$Id: TADeleteRecordAction.java,v 1.3 2010/07/13 17:55:22 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.3 $";
  
  private static final String ILLEGAL_STRING = "Not allowed for records to delete!";
  
	public TADeleteRecordAction(DataTableRecord record)
	{
		super(record);
	}

  public final DataRecordMode getRecordMode()
  {
    return DataRecordMode.DELETE;
  }
  
  public final void addValue(int fieldIndex, Object newValue, Object oldValue, boolean afterSavepoint)
  {
    throw new IllegalStateException(ILLEGAL_STRING);
  }
  
  public boolean hasOldValue(int fieldIndex)
  {
    return false;
  }
  
	public void execute(DataSource dataSource, DataExecutionContext context) throws Exception
	{
    dataSource.execute(context, this);
  }

  public void executeAfterCommit() throws Exception
  {
    DataTableRecord record = getRecord();
    record.setPersistent(false);
    record.getAccessorInternal().actualizeRecordCache(this);
  }

  public void executeUpdateIndex(IIndexUpdateContext context) throws Exception
  {
    context.removeFromIndex(getRecord().getTableAlias(), getRecord().getPrimaryKeyValue());
  }
}

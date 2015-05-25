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
import de.tif.jacob.core.data.impl.index.update.IIndexUpdateContext;
import de.tif.jacob.core.exception.UserException;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class TAAction
{
  static public transient final String        RCS_ID = "$Id: TAAction.java,v 1.3 2010/07/13 17:55:22 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.3 $";
  
  private final String dataSourceName;
  
  private boolean enableForSavepointRollback = false;
  
  protected TAAction(String dataSourceName)
  {
    this.dataSourceName = dataSourceName;
  }
  
  public abstract void execute(DataSource dataSource, DataExecutionContext context) throws Exception, UserException;
  
  public abstract void executeAfterCommit() throws Exception;
  
  public abstract void executeUpdateIndex(IIndexUpdateContext context) throws Exception;
  
	/**
	 * @return Returns the dataSourceName.
	 */
	public final String getDataSourceName()
	{
		return dataSourceName;
	}

  /**
   * @return Returns the enableForSavepointRollback.
   */
  public final boolean isEnableForSavepointRollback()
  {
    return enableForSavepointRollback;
  }

  /**
   * @param enableForSavepointRollback The enableForSavepointRollback to set.
   */
  public final void setEnableForSavepointRollback()
  {
    this.enableForSavepointRollback = true;
  }

}

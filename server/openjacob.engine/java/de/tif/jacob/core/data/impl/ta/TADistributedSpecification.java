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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableDefinition;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class TADistributedSpecification
{
  static public transient final String        RCS_ID = "$Id: TADistributedSpecification.java,v 1.5 2009/02/19 11:07:41 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.5 $";
  
	// Map<String->TASpecification>
	private final Map dataSourceToSpecificationMap;

	public TADistributedSpecification()
	{
		this.dataSourceToSpecificationMap = new HashMap();
	}

  public void addDataSourceNames(Set dataSourceNames)
	{
    dataSourceNames.addAll(this.dataSourceToSpecificationMap.keySet());
	}

  public void rollbackToSavepoint()
  {
    Iterator iter = this.dataSourceToSpecificationMap.values().iterator();
    while (iter.hasNext())
    {
      TASpecification spec = (TASpecification) iter.next();
      spec.rollbackToSavepoint();
    }
  }
  
  public void rollback()
  {
    Iterator iter = this.dataSourceToSpecificationMap.values().iterator();
    while (iter.hasNext())
    {
      TASpecification spec = (TASpecification) iter.next();
      spec.rollback();
    }
  }
  
	public void markActionsAsExecuted(String dataSourceName)
	{
		this.dataSourceToSpecificationMap.remove(dataSourceName);
	}

  public void addActionsToExecute(String dataSourceName, List actions)
  {
    TASpecification result = (TASpecification) this.dataSourceToSpecificationMap.get(dataSourceName);
    if (null != result)
    {
      result.addActionsToExecute(actions);
    }
  }
  
  public IDataTableRecord getRecord(ITableAlias tableAlias, IDataKeyValue primaryKey)
  {
    String dataSourceName = tableAlias.getTableDefinition().getDataSourceName();
    TASpecification result = (TASpecification) this.dataSourceToSpecificationMap.get(dataSourceName);
    if (result == null)
    {
      return null;
    }
    return result.getRecord(tableAlias, primaryKey);
  }
  
  public IDataTableRecord getRecord(ITableDefinition tableDefinition, IDataKeyValue primaryKey)
  {
    String dataSourceName = tableDefinition.getDataSourceName();
    TASpecification result = (TASpecification) this.dataSourceToSpecificationMap.get(dataSourceName);
    if (result == null)
    {
      return null;
    }
    return result.getRecord(tableDefinition, primaryKey);
  }
  
  public TARecordAction getRecordAction(IDataTableRecord record)
	{
		String dataSourceName = record.getTableAlias().getTableDefinition().getDataSourceName();
		TASpecification result = (TASpecification) this.dataSourceToSpecificationMap.get(dataSourceName);
		if (result == null)
		{
			return null;
		}
		return result.getRecordAction(record);
	}

  public void addAction(TAAction action, boolean afterSavepoint)
  {
    String dataSourceName = action.getDataSourceName();
    TASpecification result = (TASpecification) this.dataSourceToSpecificationMap.get(dataSourceName);
    if (result == null)
    {
      result = new TASpecification();
      this.dataSourceToSpecificationMap.put(dataSourceName, result);
    }
    result.addAction(action, afterSavepoint);
  }

  public void removeAction(TAAction action)
  {
    String dataSourceName = action.getDataSourceName();
    TASpecification result = (TASpecification) this.dataSourceToSpecificationMap.get(dataSourceName);
    result.removeAction(action);
  }
}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public final class TASpecification
{
  static public transient final String        RCS_ID = "$Id: TASpecification.java,v 1.3 2009/02/19 11:07:41 ibissw Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.3 $";
  
  private List actionList;
  
  // Map{IDataTableRecord->TARecordAction}
  private Map recordToActionMap;
  
  public TASpecification()
  {
  	this.actionList = new ArrayList();
    this.recordToActionMap = new HashMap();
  }
  
  protected IDataTableRecord getRecord(ITableAlias tableAlias, IDataKeyValue primaryKey)
  {
    // IBIS: Improve performance
    Iterator iter = this.recordToActionMap.keySet().iterator();
    while (iter.hasNext())
    {
      IDataTableRecord record = (IDataTableRecord) iter.next();
      if (tableAlias.equals(record.getTableAlias()) && primaryKey.equals(record.getPrimaryKeyValue()))
        return record;
    }
    return null;
  }
  
  protected IDataTableRecord getRecord(ITableDefinition tableDefinition, IDataKeyValue primaryKey)
  {
    // IBIS: Improve performance
    Iterator iter = this.recordToActionMap.keySet().iterator();
    while (iter.hasNext())
    {
      IDataTableRecord record = (IDataTableRecord) iter.next();
      if (tableDefinition.equals(record.getTableAlias().getTableDefinition()) && primaryKey.equals(record.getPrimaryKeyValue()))
        return record;
    }
    return null;
  }
  
  public TARecordAction getRecordAction(IDataTableRecord record)
  {
    return (TARecordAction) this.recordToActionMap.get(record);
  }
  
  protected void addAction(TAAction action, boolean afterSavepoint)
  {
    if (action instanceof TARecordAction)
    {
      TARecordAction recordAction = (TARecordAction) action;
      TARecordAction oldAction = (TARecordAction) this.recordToActionMap.put(recordAction.getRecord(), recordAction);
      if (oldAction != null)
        oldAction.skipCallingEventHandler(afterSavepoint);
    }
    this.actionList.add(action);
  }
  
  protected void removeAction(TAAction action)
  {
    if (action instanceof TARecordAction)
    {
      TARecordAction recordAction = (TARecordAction) action;
      this.recordToActionMap.remove(recordAction.getRecord());
    }
    this.actionList.remove(action);
  }
  
  public void addActionsToExecute(List actions)
  {
    for (int i = 0; i < this.actionList.size(); i++)
    {
      TAAction action = (TAAction) this.actionList.get(i);

//      if (action instanceof TARecordAction)
//      {
//        TARecordAction recordAction = (TARecordAction) action;
//        if (recordAction.isSkipCallingEventHandler())
//          continue;
//      }
      actions.add(action);
    }
  }
  
  protected void rollbackToSavepoint()
  {
    this.recordToActionMap.clear();
    
    List newActionList = new ArrayList();
    for (int i = 0; i < this.actionList.size(); i++)
    {
      TAAction action = (TAAction) this.actionList.get(i);
      
      // Step 1: rollback action and data values in record
      //         This must be before step 2, because the 
      //         record must be cleaned up in any case!
      //
      if (action instanceof TARecordAction)
      {
        TARecordAction recordAction = (TARecordAction) action;
        recordAction.rollbackToSavepoint();
      }
      
      // Step 2: skip action completely?
      if (action.isEnableForSavepointRollback())
      {
        // skip this action because it has been created after the savepoint
        continue;
      }
      
      // Step 3: register again
      newActionList.add(action);
      if (action instanceof TARecordAction)
      {
        TARecordAction recordAction = (TARecordAction) action;
        this.recordToActionMap.put(recordAction.getRecord(), recordAction);
      }
    }
    this.actionList = newActionList;
  }
  
  /**
   * Rollback all actions.
   */
  protected void rollback()
  {
    this.recordToActionMap.clear();
    
    for (int i = 0; i < this.actionList.size(); i++)
    {
      TAAction action = (TAAction) this.actionList.get(i);
      
      if (action instanceof TARecordAction)
      {
        TARecordAction recordAction = (TARecordAction) action;
        recordAction.rollback();
      }
    }
    this.actionList.clear();
  }
}

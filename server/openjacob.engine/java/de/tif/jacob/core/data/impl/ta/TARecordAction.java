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
import java.util.List;

import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.impl.DataRecordMode;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.IDataSavepoint;
import de.tif.jacob.core.data.impl.misc.Nullable;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class TARecordAction extends TAAction implements IDataSavepoint
{
  static public transient final String RCS_ID = "$Id: TARecordAction.java,v 1.7 2010/07/13 17:55:22 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.7 $";

  private final static Object NULL = new Object()
  {
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException
    {
      throw new UnsupportedOperationException("NULL may not be cloned");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object anObject)
    {
      if (null == anObject)
      {
        return true;
      }
      if (this == anObject)
      {
        return true;
      }
      return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return "NULL";
    }
  };

  private final DataTableRecord record;
  private final Object[] origOldValues;
  private boolean[] origNonEffectiveValueChange;
  private List historyNotes;
  
  /**
   * Determines whether for this action
   * {@link DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)}
   * and
   * {@link DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)}
   * should not be called.
   */
  private boolean skipCallingEventHandler = false;

  /**
   * Copy of {@link #skipCallingEventHandler} at save point time.
   */
  private boolean savepointSkipCallingEventHandler = false;

  /**
   * The data values at save point time.
   */
  private Object[] savepointValues;

  /**
   * Copy of {@link #origOldValues} at save point time.
   */
  private Object[] savepointOrigOldValues;

  /**
   * Copy of {@link #origNonEffectiveValueChange} at save point time.
   */
  private boolean[] savepointOrigNonEffectiveValueChange;

  /**
   * Copy of {@link #historyNote} at save point time.
   */
  private String[] savepointHistoryNotes;

  /**
   * Flag indicating whether any modification has been done after setting save point.
   */
  private boolean savepointFlag;

  protected TARecordAction(DataTableRecord record)
  {
    super(record.getParent().getTableAlias().getTableDefinition().getDataSourceName());

    this.record = record;
    this.origOldValues = new Object[record.getFieldNumber()];
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.IDataSavepoint#getRollbackValue(int)
   */
  public final Object getRollbackValue(int fieldIndex)
  {
    if (this.savepointValues[fieldIndex] == null)
    {
      throw new IllegalStateException();
    }
    return this.savepointValues[fieldIndex] == NULL ? null : this.savepointValues[fieldIndex];
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.data.impl.IDataSavepoint#hasRollbackValue(int)
   */
  public final boolean hasRollbackValue(int fieldIndex)
  {
    return this.savepointValues[fieldIndex] != null;
  }

  protected final void rollbackToSavepoint()
  {
    if (savepointFlag)
    {
      // rollback status in record 
      this.record.rollback(this);

      System.arraycopy(this.savepointOrigOldValues, 0, this.origOldValues, 0, this.origOldValues.length);
      this.savepointOrigOldValues = null;

      if (this.savepointOrigNonEffectiveValueChange != null)
      {
        System.arraycopy(this.savepointOrigNonEffectiveValueChange, 0, this.origNonEffectiveValueChange, 0, this.origNonEffectiveValueChange.length);
        this.savepointOrigNonEffectiveValueChange = null;
      }
      else
      {
        this.origNonEffectiveValueChange = null;
      }

      if (this.savepointHistoryNotes != null)
      {
        this.historyNotes = new ArrayList(this.savepointHistoryNotes.length);
        for (int i = 0; i < this.savepointHistoryNotes.length; i++)
          this.historyNotes.add(this.savepointHistoryNotes[i]);
        this.savepointHistoryNotes = null;
      }
      else
        this.historyNotes = null;

      this.savepointValues = null;

      savepointFlag = false;
    }
    
    this.skipCallingEventHandler = this.savepointSkipCallingEventHandler;
  }

  protected final void rollback()
  {
    this.savepointValues = this.origOldValues;

    // rollback status in record
    this.record.rollback(this);

    for (int i = 0; i < this.origOldValues.length; i++)
      this.origOldValues[i] = null;
    this.origNonEffectiveValueChange = null;
    this.historyNotes = null;
    this.skipCallingEventHandler = false;

    this.savepointValues = null;

    this.savepointOrigNonEffectiveValueChange = null;
    this.savepointHistoryNotes = null;
    this.savepointFlag = false;
    this.savepointSkipCallingEventHandler = false;
  }
  
  public final boolean isSkipCallingEventHandler()
  {
    return skipCallingEventHandler;
  }

  protected final void skipCallingEventHandler(boolean afterSavepoint)
  {
    this.skipCallingEventHandler = true;
    if (!afterSavepoint)
      this.savepointSkipCallingEventHandler = true;
  }

  /**
   * @param fieldIndex
   * @param newValue
   * @param oldValue
   * @param afterSavepoint
   */
  public void addValue(int fieldIndex, Object newValue, Object oldValue, boolean afterSavepoint)
  {
    if (afterSavepoint)
    {
      if (!savepointFlag)
      {
        // make a copy of the old orig values at savepoint time
        this.savepointOrigOldValues = new Object[this.origOldValues.length];
        System.arraycopy(this.origOldValues, 0, this.savepointOrigOldValues, 0, this.origOldValues.length);

        if (this.origNonEffectiveValueChange != null)
        {
          this.savepointOrigNonEffectiveValueChange = new boolean[this.origNonEffectiveValueChange.length];
          System.arraycopy(this.origNonEffectiveValueChange, 0, this.savepointOrigNonEffectiveValueChange, 0, this.origNonEffectiveValueChange.length);
        }

        if (this.historyNotes != null)
          this.savepointHistoryNotes = (String[]) this.historyNotes.toArray(new String[this.historyNotes.size()]);

        this.savepointValues = new Object[this.record.getFieldNumber()];

        savepointFlag = true;
      }

      if (this.savepointValues[fieldIndex] == null)
      {
        this.savepointValues[fieldIndex] = oldValue == null ? NULL : oldValue;
      }
    }

    // first change of value within the transaction
    //
    if (this.origOldValues[fieldIndex] == null)
    {
      // no old value registered so far
      this.origOldValues[fieldIndex] = oldValue == null ? NULL : oldValue;
    }
    else
    {
      // it is a non effective change, if the value is set back to the orig
      // value
      boolean nonEffectiveValueChange = getOldValueInternal(fieldIndex).equals(newValue);
      if (nonEffectiveValueChange && this.origNonEffectiveValueChange == null)
      {
        this.origNonEffectiveValueChange = new boolean[record.getFieldNumber()];
        this.origNonEffectiveValueChange[fieldIndex] = true;
      }
      else
      {
        if (this.origNonEffectiveValueChange != null)
          this.origNonEffectiveValueChange[fieldIndex] = nonEffectiveValueChange;
      }
    }
  }

  public final void appendToHistory(String note)
  {
    if (null == note)
      throw new NullPointerException();

    if (this.historyNotes == null)
      this.historyNotes = new ArrayList();

    this.historyNotes.add(note);
  }

  public boolean hasOldValue(int fieldIndex)
  {
    boolean effectiveValueChange = this.origNonEffectiveValueChange == null || this.origNonEffectiveValueChange[fieldIndex] == false;
    return this.origOldValues[fieldIndex] != null && effectiveValueChange;
  }

  public final Object getOldValue(int fieldIndex)
  {
    Object oldValue = getOldValueInternal(fieldIndex);
    return oldValue == NULL ? null : oldValue;
  }

  private Object getOldValueInternal(int fieldIndex)
  {
    Object oldValue = this.origOldValues[fieldIndex];

    // plausibility check: only allow to call getOldValue() if hasOldValue()==true
    if (null == oldValue)
      throw new RuntimeException("No old value existing for field '" + this.record.getFieldDefinition(fieldIndex) + "'");

    return ((oldValue instanceof Nullable) && ((Nullable) oldValue).isNull()) ? NULL : oldValue;
  }

  public abstract DataRecordMode getRecordMode();

  /**
   * @return Returns the record.
   */
  public final DataTableRecord getRecord()
  {
    return record;
  }
  
  /**
   * @return Returns the historyNote.
   */
  public final String[] getHistoryNotes()
  {
    if (this.historyNotes == null)
      return null;
    
    return (String[]) this.historyNotes.toArray(new String[this.historyNotes.size()]);
  }
  
  public final String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getClass().getName()).append("[");
    buffer.append("record = ").append(this.record);
    buffer.append("]");
    return buffer.toString();
  }
}

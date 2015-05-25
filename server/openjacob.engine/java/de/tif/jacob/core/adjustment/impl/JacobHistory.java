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
package de.tif.jacob.core.adjustment.impl;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataAccessor;
import de.tif.jacob.core.data.impl.DataTableRecord;
import de.tif.jacob.core.data.impl.ta.TADeleteRecordAction;
import de.tif.jacob.core.data.impl.ta.TAInsertRecordAction;
import de.tif.jacob.core.data.impl.ta.TAUpdateRecordAction;
import de.tif.jacob.core.data.internal.IDataTransactionInternal;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.BinaryFieldType;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.impl.AbstractKey;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.i18n.I18N;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class JacobHistory extends History
{
	// IBIS: Zur Zeit noch identisch mit Quintus history -> use own!?
  
	static public final transient String RCS_ID = "$Id: JacobHistory.java,v 1.5 2010/06/22 22:34:07 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";
	
	private static final int VALUE_IDENT = 54;
	private static final int NEWVALUE_IDENT = 32;
	private static final int MAX_IDENT = 54;
	private static char[] identChars;
	
	static
	{
	  identChars = new char [MAX_IDENT];
	  for (int i=0; i<MAX_IDENT; i++)
	  {
	    identChars [i] = ' '; 
	  }
	}

  private void printNotes(StringBuffer history, String[] notes)
	{
		if (notes != null)
    {
      for (int i = 0; i < notes.length; i++)
        history.append("\n ").append(notes[i]);
      history.append("\n");
    }
	}

  private void printValue(StringBuffer history, String value)
  {
    if (value != null)
      history.append(value);
  }

  private void printIdent(StringBuffer history, int ident)
  {
    if (ident <= 0)
      return;
    if (ident > MAX_IDENT)
      ident = MAX_IDENT;
    history.append(identChars, 0, ident);
  }

  private void printValue(StringBuffer history, DataAccessor accessor, DataTableRecord record, ITableField field, String strValue, Object value)
  {
    if (strValue == null)
    {
      return;
    }

    // field is a foreign key?
    if (field.getMatchingForeignKey() == null)
    {
      // field is not a foreign key
      printValue(history, strValue);
      return;
    }

    //
    // determine representative field
    String representativeValue = null;
    ITableDefinition table = ((AbstractKey) field.getMatchingForeignKey()).getLinkedForeignTable();
    if (table != null && table.getRepresentativeField() != null)
    {
      // fetch representive field if this field is not itself the primary key
      if (table.getPrimaryKey().getTableFields().size() != 1 || !table.getPrimaryKey().getTableFields().get(0).equals(table.getRepresentativeField()))
      {
        IDataKeyValue primaryKey = new IDataKeyValue(value);

        // look in accessor cache first
        DataTableRecord linkedRecord = accessor.getCachedRecord(table, primaryKey);
        if (linkedRecord == null)
        {
          // not found -> look for uncommitted records
          IDataTransaction trans = record.getCurrentTransaction();
          if (trans != null)
          {
            // should always be the case
            linkedRecord = (DataTableRecord) ((IDataTransactionInternal) trans).getRecord(table, primaryKey);
          }

          // if record not in cache -> fetch it from data source
          if (linkedRecord == null)
          {
            try
            {
              linkedRecord = (DataTableRecord) accessor.loadRecord(table, primaryKey);
            }
            catch (RecordNotFoundException ex)
            {
              logger.warn("Fetching linked record failed: " + ex.toString());
            }
          }
        }
        if (linkedRecord != null)
        {
          representativeValue = linkedRecord.getStringValue(table.getRepresentativeField().getFieldIndex(), Context.getCurrent().getApplicationLocale());
        }
      }
    }
    
    if (representativeValue == null)
    {
      printValue(history, strValue);
      history.append(" (Key)");
    }
    else
    {
      printValue(history, representativeValue);
      history.append(" (Key ");
      printValue(history, strValue);
      history.append(")");
    }
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.misc.History#build(de.tif.jacob.core.data.ta.TAUpdateRecordAction)
	 */
	public void build(TAUpdateRecordAction action) throws Exception
  {
    DataTableRecord record = action.getRecord();
    
    // Do not write history, if already deleted. This might happen, if an
    // IDataTableRecord instance is updated first and delete afterwards in the
    // same transaction.
    if (record.isDeleted())
      return;

    ITableDefinition table = record.getTable().getTableAlias().getTableDefinition();
    if (table.getHistoryField() != null)
    {
      Context context = Context.getCurrent();
      DataAccessor accessor = (DataAccessor) record.getTable().getAccessor();
      String valueHasBeenChanged = null;
      
      StringBuffer history = new StringBuffer();
      for (int i = 0; i < record.getFieldNumber(); i++)
      {
        if (action.hasOldValue(i))
        {
          ITableField field = record.getFieldDefinition(i);
          if (field.isEnabledForHistory())
          {
            String localizedLabel = I18N.localizeLabel(field.getLabel(), context, context.getApplicationLocale());
            if (localizedLabel == null || localizedLabel.length() == 0)
              localizedLabel = field.getLabel();
            history.append("  ").append(localizedLabel).append(":");
            printIdent(history, VALUE_IDENT - 3 - localizedLabel.length());
            
            // ignore longtext and binary fields
            if (field.getType() instanceof LongTextFieldType || field.getType() instanceof BinaryFieldType)
            {
              if (valueHasBeenChanged == null)
                valueHasBeenChanged = I18N.getCoreLocalized("VALUE_HAS_BEEN_CHANGED", context, context.getApplicationLocale());
              
              history.append(valueHasBeenChanged);
            }
            else
            {
              printValue(history, accessor, record, field, record.getOldStringValue(field.getFieldIndex(), context.getApplicationLocale()), record.getOldValue(field.getFieldIndex()));
              history.append(" ==>\n");
              printIdent(history, NEWVALUE_IDENT);
              printValue(history, accessor, record, field, record.getStringValue(field.getFieldIndex(), context.getApplicationLocale()), record.getValue(field));
            }
            history.append("\n");
          }
        }
      }
      printNotes(history, action.getHistoryNotes());

      // IBIS: Änderung fieldName -> field bei Aufruf
      // Note: The header containing the user and the timestamp is added be appendLongTextValue()
      record.appendLongTextValue(record.getCurrentTransaction(), table.getHistoryField().getName(), history.toString());
    }
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.misc.History#build(de.tif.jacob.core.data.ta.TAInsertRecordAction)
	 */
	public void build(TAInsertRecordAction action) throws Exception
  {
    DataTableRecord record = action.getRecord();

    ITableDefinition table = record.getTable().getTableAlias().getTableDefinition();
    if (table.getHistoryField() != null)
    {
      Context context = Context.getCurrent();
      DataAccessor accessor = (DataAccessor) record.getTable().getAccessor();
      StringBuffer history = new StringBuffer();
      for (int i = 0; i < record.getFieldNumber(); i++)
      {
        ITableField field = record.getFieldDefinition(i);
        if (field.isEnabledForHistory())
        {
          // ignore longtext and binary fields
          if (field.getType() instanceof LongTextFieldType || field.getType() instanceof BinaryFieldType)
          {
            continue;
          }

          String value = record.getStringValue(i);
          if (null != value)
          {
            String localizedLabel = I18N.localizeLabel(field.getLabel(), context, context.getApplicationLocale());
            if (localizedLabel == null || localizedLabel.length() == 0)
              localizedLabel = field.getLabel();
            history.append("  ").append(localizedLabel).append(":");
            printIdent(history, VALUE_IDENT - 3 - localizedLabel.length());
            printValue(history, accessor, record, field, record.getStringValue(field.getFieldIndex(), context.getApplicationLocale()), record.getValue(field));
            history.append("\n");
          }
        }
      }
      printNotes(history, action.getHistoryNotes());

      // IBIS: Änderung fieldName -> field bei Aufruf
      // Note: The header containing the user and the timestamp is added be appendLongTextValue()
      record.appendLongTextValue(record.getCurrentTransaction(), table.getHistoryField().getName(), history.toString());
    }
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.data.misc.History#build(de.tif.jacob.core.data.ta.TADeleteRecordAction)
	 */
	public void build(TADeleteRecordAction action) throws Exception
	{
		// nothing more to do
	}

}

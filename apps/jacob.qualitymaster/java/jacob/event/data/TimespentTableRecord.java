/*
 * Created on 14.04.2004
 * by mike
 *
 */
package jacob.event.data;

import jacob.model.Budget;
import jacob.model.Timespent;
import jacob.resources.I18N;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.event.IDataBrowserModifiableRecord;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.exception.UserException;

/**
 * 
 * @author mike
 * 
 */
public final class TimespentTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: TimespentTableRecord.java,v 1.7 2009-12-24 11:40:12 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {

  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    IDataTableRecord budget = tableRecord.getLinkedRecord(Budget.NAME);

    // timespent vom Budget abrechnen
    //
    if (tableRecord.isDeleted())
    {
      subtract(budget, transaction, tableRecord.getlongValue(Timespent.timespent));
    }

    // Record wurde eben erst angelegt
    //
    else if (tableRecord.isNew())
    {
      checkBudgetClosed(budget);
      add(budget, transaction, tableRecord.getlongValue(Timespent.timespent));
    }

    // Ein bestehender Record wurde geändert
    //
    else if (tableRecord.isUpdated())
    {
      // das zugeordnete Budget wurde geändert
      //
      if (tableRecord.hasChangedValue(Timespent.budget_key))
      {
        long oldValue = tableRecord.getOldlongValue(Timespent.timespent);

        // altes timespent an dem alten Budget abziehen
        //
        String key = tableRecord.getOldStringValue(Timespent.budget_key);
        if (key != null)
        {
          IDataAccessor acc = tableRecord.getAccessor().newAccessor();
          IDataTable oldBudgetTable = acc.getTable(Budget.NAME);
          oldBudgetTable.qbeSetKeyValue(Budget.pkey, key);
          oldBudgetTable.search();
          IDataTableRecord oldBudget = oldBudgetTable.getSelectedRecord();
          subtract(oldBudget, transaction, oldValue);
        }

        // neues timespent an dem neuen Budget verrechnen.
        //
        checkBudgetClosed(budget);
        add(budget, transaction, tableRecord.getlongValue(Timespent.timespent));
      }
      // Das zugeordnete Budget wurde NICHT geändert
      //
      else
      {
        // ...aber das 'timespent' wurde geändert
        //
        if (tableRecord.hasChangedValue(Timespent.timespent))
        {
          long oldValue = tableRecord.getOldlongValue(Timespent.timespent);
          long newValue = tableRecord.getlongValue(Timespent.timespent);
          subtract(budget, transaction, oldValue - newValue);
        }
      }
    }

    // Aufwandsdatum mit heute füllen, sofern der Benutzer noch nichts
    // eingetragen hat.
    //
    if (tableRecord.hasNullValue(Timespent.timespentdate))
      tableRecord.setValue(transaction, Timespent.timespentdate, "today");
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }

  private static void checkBudgetClosed(IDataTableRecord budget) throws Exception
  {
    if (budget == null)
      return;

    if (Budget.state_ENUM._close.equals(budget.getStringValue(Budget.state)))
      throw new UserException(I18N.MESSAGE_BUDGET_CLOSED.get(Context.getCurrent()));
  }

  private static void subtract(IDataTableRecord budget, IDataTransaction transaction, long amount) throws Exception
  {
    if (budget == null)
      return;

    IDataTable budgetTable = budget.getTable();

    // budget locken und refreshen, damit keiner die berechnete Zeit verändern
    // kann
    //
    transaction.lock(budget);
    budget = budgetTable.loadRecord(budget.getPrimaryKeyValue());

    // berechnete Zeit auslesen
    //
    long used = budget.getlongValue(Budget.used_amount);

    budget.setLongValue(transaction, Budget.used_amount, used - amount);
  }

  private static void add(IDataTableRecord budget, IDataTransaction transaction, long amount) throws Exception
  {
    if (budget == null)
      return;

    IDataTable budgetTable = budget.getTable();

    // budget locken und refreshen, damit keiner die berechnete Zeit verändern
    // kann
    //
    transaction.lock(budget);
    budget = budgetTable.loadRecord(budget.getPrimaryKeyValue());

    // berechnete Zeit auslesen
    //
    long used = budget.getlongValue(Budget.used_amount);

    budget.setLongValue(transaction, Budget.used_amount, used + amount);
  }

  public void afterSearchAction(IDataBrowserModifiableRecord browserRecord) throws Exception
  {
    // Append unit/type to timespent effort, e.g. "3 hours" or "1 day"
    //
    IBrowserDefinition definition = browserRecord.getBrowser().getBrowserDefinition();
    IBrowserTableField timespentField = null;
    String timesspentTypeString = null;
    for (int i = 0; i < definition.getFieldNumber(); i++)
    {
      IBrowserField field = definition.getBrowserField(i);
      if (field instanceof IBrowserTableField)
      {
        IBrowserTableField tablefield = (IBrowserTableField) field;
        if (tablefield.getTableAlias().getName().equals(Timespent.NAME))
        {
          if (tablefield.getTableField().getName().equals(Timespent.timespent))
            timespentField = tablefield;
          else if (tablefield.getTableField().getName().equals(Timespent.timespenttype))
            timesspentTypeString = browserRecord.getStringValue(i);
        }
      }
    }
    if (timespentField != null && timesspentTypeString != null)
    {
      int effort = browserRecord.getintValue(timespentField.getName());
      if (effort == 1 && timesspentTypeString.endsWith("s"))
        timesspentTypeString = timesspentTypeString.substring(0, timesspentTypeString.length() - 1);
      browserRecord.setValue(timespentField.getName(), effort + " " + timesspentTypeString);
    }
  }
}

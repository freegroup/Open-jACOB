/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 19 23:30:27 CEST 2010
 */
package jacob.event.data;

import jacob.model.Menutree_no_condition;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author andreas
 */
public final class Menutree_no_conditionTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: Menutree_no_conditionTableRecord.java,v 1.1 2010-10-20 21:00:48 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    IDataTable table = tableRecord.getAccessor().newAccessor().getTable(tableRecord.getTableAlias());
    table.qbeSetKeyValue(Menutree_no_condition.menutree_parent_key, tableRecord.getValue(Menutree_no_condition.pkey));
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      if (Menutree_no_condition.lifecycle_ENUM._recyclebin.equals(childRecord.getValue(Menutree_no_condition.lifecycle)))
        throw new UserException("Löschen nicht möglich.\n Es müssen zuerst alle untergeordneten Menüeinträge im Papierkorb gelöscht werden!");
      childRecord.delete(transaction);
    }
  }

	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

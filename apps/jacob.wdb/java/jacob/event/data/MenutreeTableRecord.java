/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 05 12:31:51 CEST 2010
 */
package jacob.event.data;

import jacob.common.MenuManager;
import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andherz
 */
public class MenutreeTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: MenutreeTableRecord.java,v 1.3 2010-08-29 12:54:03 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	  // delete all sub-menutrees
	  //
	  IDataTable table = tableRecord.getAccessor().newAccessor().getTable(tableRecord.getTableAlias());
	  table.qbeSetKeyValue(Menutree.menutree_parent_key, tableRecord.getValue(Menutree.pkey));
	  table.searchAndDelete(transaction);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

		// enter your code here
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	  MenuManager.invalidate();
	}
}

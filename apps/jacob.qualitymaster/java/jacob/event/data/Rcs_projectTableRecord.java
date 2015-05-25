/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Feb 15 20:25:13 CET 2006
 */
package jacob.event.data;

import jacob.model.Rcs_branch;
import jacob.model.Rcs_project;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andreas
 */
public class Rcs_projectTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: Rcs_projectTableRecord.java,v 1.1 2006/02/24 02:16:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord projectRecord, IDataTransaction transaction) throws Exception
  {
  }

	/*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
	public void afterDeleteAction(IDataTableRecord projectRecord, IDataTransaction transaction) throws Exception
	{
    // delete the head branch record as well
    // Note: The transaction will succeed, if no further dependencies exist
    //
    IDataTable branchTable = projectRecord.getAccessor().getTable(Rcs_branch.NAME);
    branchTable.qbeClear();
    branchTable.qbeSetKeyValue(Rcs_branch.version, Rcs_branchTableRecord.HEAD);
    branchTable.qbeSetKeyValue(Rcs_branch.rcs_project_key, projectRecord.getValue(Rcs_project.pkey));
    branchTable.searchAndDelete(transaction);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord projectRecord, IDataTransaction transaction) throws Exception
  {
    if (projectRecord.isNew())
    {
      // create an head branch entry as well
      //
      IDataTableRecord headRecord = projectRecord.getAccessor().getTable(Rcs_branch.NAME).newRecord(transaction);
      headRecord.setLinkedRecord(transaction, projectRecord);
      headRecord.setValue(transaction, Rcs_branch.version, Rcs_branchTableRecord.HEAD);
      headRecord.setValue(transaction, Rcs_branch.versionnbr, Rcs_branchTableRecord.HEAD_VERSIONNBR);
      headRecord.setValue(transaction, Rcs_branch.status, Rcs_branch.status_ENUM._Open);

      Rcs_branchTableRecord.actualizeRecord(headRecord, transaction);
    }
  }

	/*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

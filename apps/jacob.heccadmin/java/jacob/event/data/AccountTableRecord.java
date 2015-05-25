/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Dec 18 16:02:40 CET 2008
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Account;
import jacob.model.Project;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;

/**
 *
 * @author R.Spoor
 */
public class AccountTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: AccountTableRecord.java,v 1.3 2009/02/24 15:55:51 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public AccountTableRecord()
	{
	    super(
	        Account.created_by, Account.create_date,
	        Account.changed_by, Account.change_date
	    );
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
    @Override
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
    @Override
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
    @Override
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
        super.beforeCommitAction(tableRecord, transaction);
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

		if (tableRecord.hasChangedValue(Account.account))
		{
		    IDataAccessor accessor = tableRecord.getAccessor().newAccessor();
		    IDataTable projectTable = accessor.getTable(Project.NAME);
		    projectTable.qbeClear();
		    Object key = tableRecord.getValue(Account.pkey);
		    projectTable.qbeSetKeyValue(Project.account_key, key);
		    int count = projectTable.search();
		    for (int i = 0; i < count; i++)
		    {
		        IDataTableRecord projectRecord = projectTable.getRecord(i);
		        ProjectTableRecord.updateAccountProjectNames(transaction, tableRecord, projectRecord);
		    }
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
    @Override
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

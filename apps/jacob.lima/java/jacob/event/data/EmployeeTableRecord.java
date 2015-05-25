/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 28 23:34:46 CET 2006
 */
package jacob.event.data;

import jacob.model.Employee;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author andreas
 */
public class EmployeeTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: EmployeeTableRecord.java,v 1.1 2006/03/07 19:20:35 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord employeeRecord, IDataTransaction transaction) throws Exception
	{
		if(employeeRecord.isDeleted())
			return;

    // Set the fullname from first and last names
    //
    if (employeeRecord.hasChangedValue(Employee.firstname) || employeeRecord.hasChangedValue(Employee.lastname))
    {
      String firstname = employeeRecord.getSaveStringValue(Employee.firstname);
      String lastname = employeeRecord.getSaveStringValue(Employee.lastname);

      employeeRecord.setValue(transaction, Employee.fullname, firstname + " " + lastname);
    }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

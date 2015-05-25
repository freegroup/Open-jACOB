/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.i18n.ApplicationMessage;

/**
 *
 * @author andreas
 */
public class EmployeeTableRecord extends DataTableRecordEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void beforeCommitAction(IDataTableRecord employeeRecord, IDataTransaction transaction) throws Exception
	{
    if(employeeRecord.isDeleted())
      return;
    
    // Check whether loginname is unique
    //
    IDataTable employeeTable = employeeRecord.getTable();
    if (employeeRecord.hasChangedValue("loginname"))
    {
      String loginName = employeeRecord.getStringValue("loginname");
      if (loginName != null && employeeTable.exists("loginname", loginName))
      {
        throw new BusinessException(new ApplicationMessage("LOGINNAME_NOT_UNIQUE", loginName));
      }
    }

    // Check whether the employee id is unique
    //
    if (employeeRecord.hasChangedValue("employeeid"))
    {
      String employeeid = employeeRecord.getStringValue("employeeid");
      if (employeeid != null && employeeTable.exists("employeeid", employeeid))
      {
        throw new BusinessException(new ApplicationMessage("EMPLOYEEID_NOT_UNIQUE", employeeid));
      }
    }

    // Check whether the email is unique
    //
    if (employeeRecord.hasChangedValue("email"))
    {
      // normalize email
      employeeRecord.setValue(transaction, "email", employeeRecord.getSaveStringValue("email").trim().toLowerCase());
      
      String email = employeeRecord.getStringValue("email");
      if (email != null && employeeTable.exists("email", email))
      {
        throw new BusinessException(new ApplicationMessage("EMAIL_NOT_UNIQUE", email));
      }
    }

    // Set the fullname from first and last names
    //
    if (employeeRecord.hasChangedValue("firstname") || employeeRecord.hasChangedValue("lastname"))
    {
      String firstname = employeeRecord.getSaveStringValue("firstname");
      String lastname = employeeRecord.getSaveStringValue("lastname");

      employeeRecord.setValue(transaction, "fullname", firstname + " " + lastname);
    }
	}
}

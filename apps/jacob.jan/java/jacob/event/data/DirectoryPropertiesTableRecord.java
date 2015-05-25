/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Mar 21 22:04:19 EET 2010
 */
package jacob.event.data;

import jacob.model.DirectoryProperties;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author andreas
 */
public class DirectoryPropertiesTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: DirectoryPropertiesTableRecord.java,v 1.1 2010-03-21 22:41:13 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
	}

	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		if(tableRecord.isDeleted())
			return;

    if (DirectoryProperties.state_ENUM._active.equals(tableRecord.getValue(DirectoryProperties.state)))
    {
      IDataTable table = tableRecord.getTable();
      table.qbeClear();
      table.qbeSetValue(DirectoryProperties.pkey, "!" + tableRecord.getSaveStringValue(DirectoryProperties.pkey));
      table.qbeSetKeyValue(DirectoryProperties.state, DirectoryProperties.state_ENUM._active);
      if (table.exists())
        throw new UserException("Only one directory configuration can be active!");
    }
	}

	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

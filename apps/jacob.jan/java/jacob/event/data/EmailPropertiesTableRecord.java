/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Sep 11 19:14:59 CEST 2006
 */
package jacob.event.data;

import jacob.model.EmailProperties;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author andreas
 */
public class EmailPropertiesTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: EmailPropertiesTableRecord.java,v 1.2 2010-03-21 22:41:13 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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

    if (EmailProperties.state_ENUM._active.equals(tableRecord.getValue(EmailProperties.state)))
    {
      IDataTable table = tableRecord.getTable();
      table.qbeClear();
      table.qbeSetValue(EmailProperties.pkey, "!" + tableRecord.getSaveStringValue(EmailProperties.pkey));
      table.qbeSetKeyValue(EmailProperties.state, EmailProperties.state_ENUM._active);
      if (table.exists())
        throw new UserException("Only one SMTP server configuration can be active!");
    }
	}

	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

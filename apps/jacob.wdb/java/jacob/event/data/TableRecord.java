/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 13 10:04:25 CEST 2010
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.model.Menutree;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class TableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: TableRecord.java,v 1.1 2010-08-13 09:45:17 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
	 */
	public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
    Context context = Context.getCurrent();

    saveSetValue(tableRecord, transaction, "create_username", context.getUser().getFullName());
    if(!context.getUser().isSystem())
      saveSetValue(tableRecord, transaction, "create_userid", context.getUser().getKey());
	}
	
	private static void saveSetValue(IDataTableRecord tableRecord, IDataTransaction transaction, String field, String value)
	{
    try
    {
      tableRecord.setValue(transaction, field, value);
    }
    catch (Exception e)
    {
      // ignore: leider kann man nicht prüfen ob ein Rekord/Tabelle ein Feld enthält ohne dass man eine Exception um
      // die Ohren gehauen bekommt.
    }
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
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		// Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
		//
		if(tableRecord.isDeleted())
			return;

    Context context = Context.getCurrent();

    saveSetValue(tableRecord, transaction, "change_username", context.getUser().getFullName());
    saveSetValue(tableRecord, transaction, "change_date", "now");
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

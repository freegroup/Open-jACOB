/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 01 16:15:13 CET 2007
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.model.Objectgroup;
import jacob.model.Parentobjectgroup;

import org.apache.commons.logging.Log;

/**
 *
 * @author andherz
 */
public class ObjectgroupTableRecord extends DataTableRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: ObjectgroupTableRecord.java,v 1.1 2007/03/02 14:06:19 herz Exp $";
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

		IDataTable parent = tableRecord.getAccessor().getTable(Parentobjectgroup.NAME);
		IDataTableRecord parentRecord  =parent.getSelectedRecord();
		if(parentRecord!=null)
		{
			String longName = parentRecord.getSaveStringValue(Parentobjectgroup.longname);
			tableRecord.setStringValue(transaction, Objectgroup.longname, longName+"/"+tableRecord.getSaveStringValue(Objectgroup.name));
		}
		else
		{
			tableRecord.setStringValue(transaction, Objectgroup.longname, tableRecord.getSaveStringValue(Objectgroup.name));
		}
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

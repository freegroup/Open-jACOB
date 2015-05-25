/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.model.Sapadmin;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.exception.UserException;

/**
 *
 * @author E050_FWT-ANT_o_test
 */
public class SapadminTableRecord extends DataTableRecordEventHandler
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
	public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
	{
		if (tableRecord.getintValue("active")==1) 
		{
			IDataTable admin = tableRecord.getAccessor().newAccessor().getTable("sapadmin");
			admin.qbeClear();
			admin.qbeSetValue("active", "1");
			admin.search();
			if (admin.recordCount()> 1) 
			{
				throw new UserException("Es gibt mehrere aktive Verbindungsdatensätze. \\\n Bitte beheben Sie das Problem");
			}
			if (admin.recordCount()== 1) 
			{
				IDataTableRecord rec = admin.getRecord(0);
				if (!tableRecord.getValue(Sapadmin.pkey).equals(rec.getValue(Sapadmin.pkey))) 
				{
					throw new UserException("Es kann immer nur ein Verbindungsdatensatz aktiv sein!");
				}
			}
		}
	}
}

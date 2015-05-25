/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author mike
 */
public class WorkgrouphwgTableRecord extends DataTableRecordEventHandler
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
        // Sicherstellen, daß nur AKs und OWNER (für Imsys) Handwerkergruppen zugewiesen bekommen können
        IDataTable iTable = tableRecord.getAccessor().getTable("workgroup");
        IDataKeyValue workgroupKeyvalue = tableRecord.getKeyValue("workgroup_FKey");
        if (null != workgroupKeyvalue)
        {
            IDataTableRecord workgroupRecord = iTable.getRecord(workgroupKeyvalue);
            String wrkgrptype = workgroupRecord.getStringValue("wrkgrptype");
            if (!"AK".equals(wrkgrptype) && !"OWNER".equals(wrkgrptype))
            {
                throw new BusinessException("Handwerkergruppen können nur an Auftragskoordinatoren zugewiesen werden");
            }
        }

	}
}

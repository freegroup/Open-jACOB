/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Jun 17 16:00:09 CEST 2009
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Project;
import jacob.model.Projectlanguage_MN;
import org.apache.commons.logging.Log;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 *
 * @author achim
 */
public class Projectlanguage_MNTableRecord extends DataTableRecordEventHandler
{

    @Override
    public void beforeSearchAction(IDataTable table) throws Exception
    {
        IDataTable project = table.getAccessor().getTable(Project.NAME);
        Context context = Context.getCurrent();
        project.qbeSetKeyValue(Project.application, context.getApplicationDefinition().getName());
        project.search();
        if (project.recordCount() == 1)
        {
            table.qbeSetKeyValue(Projectlanguage_MN.project_key, project.getRecord(0).getValue(Project.pkey));
        }
    }

    static public final transient String RCS_ID = "$Id: Projectlanguage_MNTableRecord.java,v 1.2 2009/11/23 11:33:40 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";

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
        if (tableRecord.isDeleted())
        {
            return;
        }

        // enter your code here
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}

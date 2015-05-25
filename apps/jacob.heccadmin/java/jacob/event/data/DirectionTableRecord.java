/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 24 10:53:29 CET 2009
 */
package jacob.event.data;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Direction;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;

/**
 *
 * @author R.Spoor
 */
public class DirectionTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: DirectionTableRecord.java,v 1.1 2009/02/24 15:55:53 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

    public DirectionTableRecord()
    {
        super(
            Direction.created_by, Direction.create_date,
            Direction.changed_by, Direction.change_date
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

        // enter your code here
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
     */
    @Override
    public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
    {
    }
}

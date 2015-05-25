/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Dec 11 11:29:29 CET 2008
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Number;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;
import com.hecc.jacob.util.exception.InvalidFieldValueException;

/**
 *
 * @author R.Spoor
 */
public class NumberTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: NumberTableRecord.java,v 1.3 2009/02/24 15:55:51 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public NumberTableRecord()
	{
	    super(
	        Number.created_by, Number.create_date,
	        Number.changed_by, Number.change_date
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

	    String local = tableRecord.getStringValue(jacob.model.Number.localnumber);
	    boolean validNumber = true;
        try
        {
            if (Integer.parseInt(local) < 0)
            {
                validNumber = false;
            }
        }
        catch (NumberFormatException e)
        {
            validNumber = false;
        }
        if (!validNumber)
        {
            Context context = Context.getCurrent();
            String message = I18N.NUMBER_ADMIN_INVALID_NUMBER.get(context);
            throw new InvalidFieldValueException(tableRecord, jacob.model.Number.localnumber, message);
        }
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	@Override
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Dec 11 13:57:27 CET 2008
 */
package jacob.event.data;

import java.text.MessageFormat;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import jacob.common.AppLogger;
import jacob.model.Vdn;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;
import com.hecc.jacob.util.exception.InvalidFieldValueException;

/**
 *
 * @author R.Spoor
 */
public class VdnTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: VdnTableRecord.java,v 1.2 2009/02/24 15:55:51 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
     * The format for creating the search field.
     */
    private static final String SEARCH_FORMAT = "{0} - {1}";


	public VdnTableRecord()
	{
	    super(
	        Vdn.created_by, Vdn.create_date,
	        Vdn.changed_by, Vdn.change_date
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

        String number = tableRecord.getStringValue(Vdn.vdnnumber);
	    boolean validNumber = true;
	    try
	    {
	        if (Integer.parseInt(number) < 0)
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
            String message = I18N.VDN_NUMBER_INVALID.get(context);
            throw new InvalidFieldValueException(tableRecord, Vdn.vdnnumber, message);
	    }
        String outbound = tableRecord.getStringValue(Vdn.outboundcode);
        if (outbound != null && outbound.length() > 0)
        {
            OutboundCheck.checkOutboundCode(tableRecord, Vdn.outboundcode, outbound);
        }
        String name = tableRecord.getStringValue(Vdn.vdnname);
        String search = MessageFormat.format(SEARCH_FORMAT, number, name);
        tableRecord.setStringValue(transaction, Vdn.vdnsearch, search);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	@Override
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

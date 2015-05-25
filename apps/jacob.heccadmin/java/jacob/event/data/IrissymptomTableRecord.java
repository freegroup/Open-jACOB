/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Nov 25 10:35:08 CET 2008
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;
import de.tif.jacob.core.exception.UserException;
import jacob.common.AppLogger;
import jacob.model.Irissymptom;
import jacob.model.Parentirissymptom;
import jacob.resources.I18N;

import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;
import com.hecc.jacob.util.exception.InvalidFieldValueException;

/**
 *
 * @author R.Spoor
 */
public class IrissymptomTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: IrissymptomTableRecord.java,v 1.4 2009/04/03 07:12:14 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

    public IrissymptomTableRecord()
    {
        super(
            Irissymptom.created_by, Irissymptom.create_date,
            Irissymptom.changed_by, Irissymptom.change_date
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
		String code = tableRecord.getStringValue(Irissymptom.iriscode);
		String text = tableRecord.getStringValue(Irissymptom.iristext);
		String symptom = getSymptom(text);
		tableRecord.setStringValue(transaction, Irissymptom.irissymptom, symptom);
		IDataAccessor accessor = tableRecord.getAccessor();
		IDataTransaction parentTransaction = transaction.newEmbeddedTransaction(EmbeddedTransactionMode.PREPEND);
		String parentCode = addParent(tableRecord, code, text, accessor, parentTransaction);
		tableRecord.setStringValue(transaction, Irissymptom.parentiriscode, parentCode);
		// change children IRIS text
		changeChildrenText(tableRecord, text, transaction);
	}

	private static int getLevel(String text)
	{
		int slashes = 0;
		for (int i = 0; i < text.length(); i++)
		{
			if (text.charAt(i) == '/')
			{
				slashes++;
			}
		}
		return slashes + 1;
	}

	private static String getSymptom(String text)
	{
		int index = text.lastIndexOf('/');
		if (index == -1)
		{
			return text;
		}
		return text.substring(index + 1);
	}

	private static String getParentText(String text)
	{
		int index = text.lastIndexOf('/');
		if (index == -1)
		{
			return null;
		}
		return text.substring(0, index);
	}

	private static String addParent(IDataTableRecord record, String code,
	                                String text, IDataAccessor accessor,
									IDataTransaction transaction)
	        throws Exception
	{
		int level = getLevel(text);
		if (code.length() != level)
		{
			Context context = Context.getCurrent();
			String message = I18N.IRIS_CODE_LEVEL_MISMATCH.get(context);
			if (record == null)
			{
			    throw new UserException(message);
			}
			TableFieldExceptionCollection collection = new TableFieldExceptionCollection(message);
			collection.add(new InvalidFieldValueException(record, Irissymptom.iriscode, message));
            collection.add(new InvalidFieldValueException(record, Irissymptom.iristext, message));
			throw collection;
		}
		if (level == 1)
		{
			return null;
		}
		code = code.substring(0, level - 1);
		text = getParentText(text);
		IDataTable table = accessor.getTable(Parentirissymptom.NAME);
		table.qbeClear();
		table.qbeSetKeyValue(Irissymptom.iriscode, code);
		table.setMaxRecords(1);
		int count = table.search();
		if (count == 0)
		{
			// create including parent
			String parentCode = addParent(null, code, text, accessor, transaction);
			IDataTableRecord parentRecord = table.newRecord(transaction);
			parentRecord.setValue(transaction, Irissymptom.iriscode, code);
			parentRecord.setValue(transaction, Irissymptom.iristext, text);
			String symptom = getSymptom(text);
			parentRecord.setValue(transaction, Irissymptom.irissymptom, symptom);
			parentRecord.setValue(transaction, Irissymptom.parentiriscode, parentCode);
			// leave description empty
		}
		else
		{
			IDataTableRecord parentRecord = table.getRecord(0);
			if (!text.equals(parentRecord.getStringValue(Irissymptom.iristext)))
			{
				Context context = Context.getCurrent();
				String message = I18N.IRIS_TEXT_PARENT_MISMATCH.get(context);
				throw new UserException(message);
			}
		}
		return code;
	}

	private static void changeChildrenText(IDataTableRecord record, String parentText,
										   IDataTransaction transaction) throws Exception
	{
		IDataAccessor accessor = record.getAccessor().newAccessor();
		// abuse parentirissymptom here
		IDataTable table = accessor.getTable(Parentirissymptom.NAME);
		table.qbeClear();
		table.qbeSetKeyValue(Irissymptom.parentiriscode, record.getValue(Irissymptom.iriscode));
		int count = table.search();
		for (int i = 0; i < count; i++)
		{
			IDataTableRecord childRecord = table.getRecord(i);
			String symptom = childRecord.getStringValue(Irissymptom.irissymptom);
			String childText = parentText + "/" + symptom;
			childRecord.setValue(transaction, Irissymptom.iristext, childText);
			changeChildrenText(childRecord, childText, transaction);
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

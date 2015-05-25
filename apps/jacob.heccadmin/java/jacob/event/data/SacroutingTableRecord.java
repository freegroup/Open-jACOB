/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Nov 26 12:12:52 CET 2008
 */
package jacob.event.data;

import jacob.common.AppLogger;
import jacob.model.Sacrouting;
import jacob.model.Sacschemas;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.CreateChangeRecordEventHandler;
import com.hecc.jacob.util.exception.InvalidFieldValueException;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;

/**
 *
 * @author R.Spoor
 */
public class SacroutingTableRecord extends CreateChangeRecordEventHandler
{
	static public final transient String RCS_ID = "$Id: SacroutingTableRecord.java,v 1.3 2009/02/24 15:55:51 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	public SacroutingTableRecord()
	{
	    super(
	        Sacrouting.created_by, Sacrouting.create_date,
	        Sacrouting.changed_by, Sacrouting.change_date
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
		Integer vdnKey = tableRecord.getIntegerValue(Sacrouting.vdn_key);
		Integer schemaKey = tableRecord.getIntegerValue(Sacrouting.sacschema_key);
		String routing = tableRecord.getStringValue(Sacrouting.sacrouting);
		Integer subvdnKey = tableRecord.getIntegerValue(Sacrouting.subvdn_key);
		String target = tableRecord.getStringValue(Sacrouting.targetnumber);
		String outboundCode = tableRecord.getStringValue(Sacrouting.outboundcode);

		Context context = Context.getCurrent();

		if (vdnKey == null || schemaKey == null || routing == null || routing.length() == 0)
		{
			// let the engine handle the required fields
			return;
		}
		if (vdnKey.equals(subvdnKey))
		{
			String message = I18N.SAC_ROUTING_ORIGIN_END_MATCH.get(context);
			throw new InvalidFieldValueException(tableRecord, Sacrouting.subvdn_key, message);
		}
		if (!isValidRoutingNumber(routing))
		{
			String message = I18N.SAC_ROUTING_NUMBER_INVALID.get(context);
            throw new InvalidFieldValueException(tableRecord, Sacrouting.sacrouting, message);
		}
		IDataAccessor accessor = tableRecord.getAccessor().newAccessor();
		IDataTable schemaTable = accessor.getTable(Sacschemas.NAME);
		schemaTable.qbeClear();
		schemaTable.qbeSetValue(Sacschemas.pkey, schemaKey);
		schemaTable.qbeSetValue(Sacschemas.leadingnumber, "^" + routing + "$");
		int count = schemaTable.search();
		if (count == 0)
		{
			String message = I18N.SAC_ROUTING_SCHEMA_MISMATCH.get(context);
            throw new InvalidFieldValueException(tableRecord, Sacrouting.sacrouting, message);
		}
		IDataTable routingTable = accessor.getTable(Sacrouting.NAME);
		routingTable.qbeClear();
		routingTable.qbeSetValue(Sacrouting.pkey, "!" + tableRecord.getValue(Sacrouting.pkey));
		routingTable.qbeSetValue(Sacrouting.vdn_key, vdnKey);
		count = routingTable.search();
		for (int i = 0; i < count; i++)
		{
			IDataTableRecord routingRecord = routingTable.getRecord(i);
			if (routingNumbersMatch(routing, routingRecord.getStringValue(Sacrouting.sacrouting)))
			{
				String message = I18N.SAC_ROUTING_NUMBER_CONFLICT.get(context);
	            throw new InvalidFieldValueException(tableRecord, Sacrouting.sacrouting, message);
			}
		}
		if (target == null || target.length() == 0)
		{
			if (subvdnKey == null)
			{
				String message = I18N.SAC_ROUTING_DUPLICATE_OR_NO_TARGET.get(context);
				TableFieldExceptionCollection collection = new TableFieldExceptionCollection(message);
				collection.add(new InvalidFieldValueException(tableRecord, Sacrouting.subvdn_key, message));
	            collection.add(new InvalidFieldValueException(tableRecord, Sacrouting.targetnumber, message));
				throw collection;
			}
		}
		else
		{
			if (subvdnKey != null)
			{
				String message = I18N.SAC_ROUTING_DUPLICATE_OR_NO_TARGET.get(context);
                TableFieldExceptionCollection collection = new TableFieldExceptionCollection(message);
                collection.add(new InvalidFieldValueException(tableRecord, Sacrouting.subvdn_key, message));
                collection.add(new InvalidFieldValueException(tableRecord, Sacrouting.targetnumber, message));
                throw collection;
			}
			boolean validTarget = true;
			try
			{
				if (Long.parseLong(target) <= 0)
				{
					validTarget = false;
				}
			}
			catch (NumberFormatException e)
			{
				validTarget = false;
			}
			if (!validTarget)
			{
				String message = I18N.SAC_ROUTING_INVALID_TARGET.get(context);
                throw new InvalidFieldValueException(tableRecord, Sacrouting.targetnumber, message);
			}
			if (outboundCode == null || outboundCode.length() == 0)
			{
				ITableField field = tableRecord.getFieldDefinition(Sacrouting.outboundcode);
				throw new RequiredFieldException(field);
			}
			OutboundCheck.checkOutboundCode(tableRecord, Sacrouting.outboundcode, outboundCode);
		}
		tableRecord.setIntValue(transaction, Sacrouting.wildcardcount, getWildCardCount(routing));
	}

	private static boolean isValidRoutingNumber(String number)
	{
		if (number == null || number.length() == 0)
		{
			return false;
		}
		for (int i = 0, len = number.length(); i < len; i++)
		{
			char c = number.charAt(i);
			if (!Character.isDigit(c) && c != '_')
			{
				return false;
			}
		}
		return true;
	}

	private static boolean routingNumbersMatch(String number1, String number2)
	{
		if (number1.length() != number2.length())
		{
			return false;
		}
		int wildcards1 = 0;
		int wildcards2 = 0;
		for (int i = 0, len = number1.length(); i < len; i++)
		{
			char c1 = number1.charAt(i);
			char c2 = number2.charAt(i);
			if (c1 != c2 && c1 != '_' && c2 != '_')
			{
				return false;
			}
			if (c1 == '_')
			{
				wildcards1++;
			}
			if (c2 == '_')
			{
				wildcards2++;
			}
		}
		// all characters are either equal or _
		return wildcards1 == wildcards2;
	}

	private static int getWildCardCount(String number)
	{
		int count = 0;
		for (int i = 0, len = number.length(); i < len; i++)
		{
			if (number.charAt(i) == '_')
			{
				count++;
			}
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
	 */
	@Override
	public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
	{
	}
}

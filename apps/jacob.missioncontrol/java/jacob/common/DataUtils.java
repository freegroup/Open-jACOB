/*
 * Created on 24.03.2004 by mike
 *  
 */
package jacob.common;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.InvalidExpressionException;
import de.tif.jacob.messaging.Message;

/**
 * @author mike
 *  
 */
/**
 * @author mike
 *  
 */

public class DataUtils
{
	static public final transient String RCS_ID = "$Id: DataUtils.java,v 1.3 2005/09/19 15:19:19 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

    public static String getAppprofileValue(Context context, String fieldName) throws Exception
    {
        return getAppprofileRecord(context).getSaveStringValue(fieldName);
    }

    public static String getAppprofileValue(IDataAccessor accessor, String fieldName) throws Exception
    {
            Context context =Context.getCurrent();
            return getAppprofileRecord(context).getSaveStringValue(fieldName);
    
    
    }
    public static IDataTableRecord getAppprofileRecord(Context context) throws Exception
    {
        IDataTable table = context.getDataTable("parameter");
    
    // the one and only record is already there?
        IDataTableRecord appprofileRecord = table.getSelectedRecord();
        if (null == appprofileRecord)
        {
            table.qbeClear();
            if (table.search() != 1)
            {
                throw new RuntimeException("Parameter table record not found");
            }
            appprofileRecord = table.getRecord(0);
        }
        return appprofileRecord;
    }
    
    
	public static String getValueWhere(IDataAccessor accessor, String alias, String primaryKeyName, String primaryKeyValue, String fieldName)
	{
		try
		{
			IDataAccessor searchAccessor = accessor.newAccessor();
			IDataTable table = searchAccessor.getTable(alias);
			table.qbeClear();
			table.qbeSetValue(primaryKeyName, primaryKeyValue);

			if (table.search() == 1)
			{
				return table.getRecord(0).getStringValue(fieldName);
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.toString());
		}
	}
  
	/**
	 * wenn im Accessor der Alias gefüllt ist, dann wird in record der Alias <br>
	 * im Feld foreignKeyname verlinked
	 * 
	 * @param accessor
	 * @param record
	 * @param foreignKeyName
	 * @param alias
	 * @param primaryKeyname
	 * @return true wenn verlinked
	 */
	public static boolean linkThroughTable(IDataAccessor accessor, IDataTableRecord record, String foreignKeyName, String alias, String primaryKeyname)
	{
		try
		{
			IDataTable linktable = accessor.getTable(alias);
			if (linktable.recordCount() == 1)
			{
				record.setStringValue(record.getCurrentTransaction(), foreignKeyName, linktable.getRecord(0).getStringValue(primaryKeyname));
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.toString());
		}
	}
  
	/**
	 * setzt explizit ein Foreignfield mit gegebenen Key
	 * 
	 * @param context
	 * @param currentTransaction
	 * @param record
	 * @param foreignKeyName
	 * @param alias
	 *          welches gefüllt werden soll
	 * @param primaryKeyName
	 *          der
	 * @param primaryKeyValue
	 * @return true wenn erfolgreich
	 */
	public static void linkTable(Context context, IDataTransaction currentTransaction, IDataTableRecord record, String foreignKeyName, String alias, String primaryKeyName, String primaryKeyValue) throws Exception
	{

		IDataTable table = context.getDataTable(alias);
		table.clear();
		table.qbeClear();
		table.qbeSetKeyValue(primaryKeyName, primaryKeyValue);

		if (table.search() == 1)
		{
			record.setValue(currentTransaction, foreignKeyName, primaryKeyValue);

		}

	}
	/**
	 * setzt explizit ein Foreignfield mit gegebenen Key
	 * 
	 * @param context
	 * @param currentTransaction
	 * @param record
	 * @param foreignKeyName
	 * @param alias
	 *          welches gefüllt werden soll
	 * @param primaryKeyName
	 *          der
	 * @param primaryKeyValue
	 * @return true wenn erfolgreich
	 */
	public static void linkTable(IDataAccessor accessor, IDataTransaction currentTransaction, IDataTableRecord record, String foreignKeyName, String alias, String primaryKeyName, String primaryKeyValue) throws Exception
	{

		IDataTable table = accessor.getTable(alias);
		table.clear();
		table.qbeClear();
		table.qbeSetKeyValue(primaryKeyName, primaryKeyValue);

		if (table.search() == 1)
		{
			record.setValue(currentTransaction, foreignKeyName, primaryKeyValue);

		}

	}
}

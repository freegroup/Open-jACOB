/*
 * Created on 05.10.2005
 *
 */
package jacob.common;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;

/**
 * @author andreas
 *
 */
public class AppProfile
{
	public static String getValue(Context context, String fieldName) throws Exception
  {
    return getRecord(context).getSaveStringValue(fieldName);
  }
	
	public static IDataTableRecord getRecord(Context context) throws Exception
	{
		IDataTable table = context.getDataTable("appprofile");
    
    // the one and only record is already there?
		IDataTableRecord appprofileRecord = table.getSelectedRecord();
		if (null == appprofileRecord)
		{
			table.qbeClear();
			if (table.search() != 1)
			{
                if (table.recordCount()==0)
                {
                    IDataTransaction trans = table.startNewTransaction();
                    try
                    {
                        table.newRecord(trans); // create a new record with default values
                        trans.commit();
                    }
                    finally 
                    {
                        trans.close();
                    }
                }
				
			}
			appprofileRecord = table.getRecord(0);
		}
		return appprofileRecord;
	}
}

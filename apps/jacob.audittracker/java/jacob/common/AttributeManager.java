package jacob.common;

import jacob.model.Object;
import jacob.model.Object_to_attribute;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;

public class AttributeManager 
{
	/*
	 * returns List[String]
	 */
	public static List getAttributePkeys(Context context, IDataTableRecord object) throws Exception
	{
		List result = new ArrayList();
		// neuen DataAccessor holen um die Daten im UI nicht zu verändern/beeinflussen
		//
		IDataAccessor acc= context.getDataAccessor().newAccessor();
		IDataTable attributeTable = acc.getTable(Object_to_attribute.NAME);
		attributeTable.qbeSetKeyValue(Object_to_attribute.object_key, object.getSaveStringValue(Object.pkey));
		
		if(attributeTable.search()>0)
		{
			for (int i = 0; i < attributeTable.recordCount(); i++) 
			{
				IDataTableRecord record = attributeTable.getRecord(i);
				result.add(record.getSaveStringValue(Object_to_attribute.attribute_key));
			}
		}
		return result;
	}
}

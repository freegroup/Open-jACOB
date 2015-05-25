package jacob.rule;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.ruleengine.Decision;

public class Common extends Decision 
{
	public boolean isNull(String table, String field) throws Exception
	{
		IDataTable dataTable = getContext().getDataTable(table);
		if(dataTable.getSelectedRecord()==null)
			return true;
		
		return dataTable.getSelectedRecord().getValue(field)==null;
	}
}

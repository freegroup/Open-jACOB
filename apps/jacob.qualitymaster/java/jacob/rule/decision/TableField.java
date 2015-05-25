package jacob.rule.decision;

import de.tif.jacob.ruleengine.Decision;
import de.tif.jacob.util.ObjectUtil;

public class TableField extends Decision 
{

	public boolean hasChanged(String tableField) throws Exception
	{
		String[] data= tableField.split("[.]");
		if(data.length!=2)
			throw new IllegalArgumentException("tableField must have the form 'tablealias.fieldname'");
		String alias = data[0];
		String field = data[1];
		
		return getContext().getDataTable(alias).getSelectedRecord().hasChangedValue(field);
	}
	
	public boolean isEquals(String tableField1, String tableField2) throws Exception
	{
		String[] data1= tableField1.split("[.]");
		String[] data2= tableField2.split("[.]");
		if(data1.length!=2)
			throw new IllegalArgumentException("tableField must have the form 'tablealias.fieldname'");
		if(data2.length!=2)
			throw new IllegalArgumentException("tableField must have the form 'tablealias.fieldname'");
		String alias1 = data1[0];
		String field1 = data1[1];
		
		String alias2 = data2[0];
		String field2 = data2[1];

		Object value1 = getContext().getDataTable(alias1).getSelectedRecord().getValue(field1);
		Object value2 = getContext().getDataTable(alias2).getSelectedRecord().getValue(field2);
		return ObjectUtil.equalsIgnoreNull(value1,value2);
	}
}

package jacob.common;

import java.util.Iterator;

import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.EnumerationFieldType;
import de.tif.jacob.screen.IClientContext;

public class EnumValues 
{
	public static void list(IClientContext context)
	{
		IApplicationDefinition app = context.getApplicationDefinition();
		Iterator aliasIter = app.getTableAliases().iterator();
		while(aliasIter.hasNext()) 
		{
			ITableAlias alias = (ITableAlias)aliasIter.next();
			Iterator fieldIter = alias.getTableDefinition().getTableFields().iterator();
			while (fieldIter.hasNext()) 
			{
				ITableField field = (ITableField) fieldIter.next();
				FieldType type = field.getType();
				if(type instanceof EnumerationFieldType)
				{
					
					EnumerationFieldType enumType = (EnumerationFieldType)type;
					System.out.println("\t"+field.getName());
					for(int i=0;i<enumType.enumeratedValueCount();i++)
					{
						System.out.println("\t\t"+enumType.getEnumeratedValue(i));
					}
				}
			}
			
		}
		
	}
}

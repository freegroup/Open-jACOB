package jacob.common.dblayer;

import jacob.model.Contact_type;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;

public class ContactTypeManager
{
    public static IDataTableRecord findByType(Context context,String type) throws Exception
    {
        return findByANY(context, Contact_type.media_type, type);
    }

    public static IDataTableRecord findByPkey(Context context,String pkey) throws Exception
    {
        return findByANY(context, Contact_type.pkey, pkey);
    }

    private static IDataTableRecord findByANY(Context context,String field, String value) throws Exception
    {
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTable typeTable = acc.getTable(Contact_type.NAME);
        typeTable.qbeSetKeyValue(field, value);
        typeTable.search(IRelationSet.LOCAL_NAME);
        if (typeTable.recordCount() > 1)
        {
            throw new UserException("Unable to find unique Record for media type with " + field + "=" + value);
        }
        return typeTable.getSelectedRecord();
    }
}

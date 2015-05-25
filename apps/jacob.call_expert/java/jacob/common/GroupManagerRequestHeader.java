package jacob.common;

import jacob.model.Request;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;

public class GroupManagerRequestHeader extends AbstractGroupManager
{
    public static void enableChildren(IClientContext context, boolean enableFlag)
    {
        enableChildren(context, get(context), enableFlag);
    }

    public static IGroup get(IClientContext context)
    {
        return (IGroup)FormManager.getCallhandling(context).findByName("requestHeaderGroup");
    }

    public static boolean isNewOrUpdate(IClientContext context)
    {
        IDataTableRecord record = context.getDataTable(Request.NAME).getSelectedRecord();
        return record != null && record.isNewOrUpdated();
    }

    public static boolean isSelected(IClientContext context)
    {
        return context.getDataTable(Request.NAME).getSelectedRecord()!=null;
    }

    //  public static void reload(IClientContext context) throws Exception
    //  {
    //    IDataTable table = context.getDataTable(Request.NAME);
    //    IDataTableRecord record = table.getSelectedRecord();
    //    table.qbeClear();
    //    table.qbeSetKeyValue(Request.pkey, -1);
    //    table.search();
    //    table.qbeClear();
    //    table.qbeSetKeyValue(Request.pkey, record.getValue(Request.pkey));
    //    table.search();
    //  }
    //  public static void clear(IClientContext context) throws Exception
    //  {
    //    IDataTable table = context.getDataTable(Request.NAME);
    //    table.qbeClear();
    //    table.qbeSetKeyValue(Request.pkey, -1);
    //    table.search();
    //  }
}

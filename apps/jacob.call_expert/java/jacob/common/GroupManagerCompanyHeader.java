package jacob.common;

import jacob.model.Company;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;

public class GroupManagerCompanyHeader extends AbstractGroupManager
{
    public static void enableChildren(IClientContext context, boolean enableFlag)
    {
        enableChildren(context, get(context), enableFlag);
    }

    public static IGroup get(IClientContext context)
    {
        return (IGroup)FormManager.getCallhandling(context).findByName("companyHeaderGroup");
    }

    public static boolean isNewOrUpdate(IClientContext context)
    {
        IDataTableRecord record = context.getDataTable(Company.NAME).getSelectedRecord();
        return record != null && record.isNewOrUpdated();
    }
    public static boolean isSelected(IClientContext context)
    {
        return context.getDataTable(Company.NAME).getSelectedRecord()!=null;
    }

    public static void reload(IClientContext context) throws Exception
    {
        IDataTable table = context.getDataTable(Company.NAME);
        IDataTableRecord record = table.getSelectedRecord();
        table.qbeClear();
        table.qbeSetKeyValue(Company.pkey, -1);
        table.search();
        table.qbeClear();
        table.qbeSetKeyValue(Company.pkey, record.getValue(Company.pkey));
        table.search();
    }
    public static void clear(IClientContext context) throws Exception
    {
        IDataTable table = context.getDataTable(Company.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Company.pkey, -1);
        table.search();
    }
}

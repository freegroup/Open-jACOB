package jacob.common;

import jacob.model.Customer;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;

public class GroupManagerCustomerHeader extends AbstractGroupManager
{
    public static void enableChildren(IClientContext context, boolean enableFlag)
    {
        enableChildren(context, get(context), enableFlag);
    }

    public static IGroup get(IClientContext context)
    {
        return (IGroup)FormManager.getCallhandling(context).findByName("customerHeaderGroup");
    }

    public static boolean isNewOrUpdate(IClientContext context)
    {
        IDataTableRecord record = context.getDataTable(Customer.NAME).getSelectedRecord();
        return record != null && record.isNewOrUpdated();
    }

    public static boolean isSelected(IClientContext context)
    {
        return context.getDataTable(Customer.NAME).getSelectedRecord()!=null;
    }

    public static void reload(IClientContext context) throws Exception
    {
        IDataTable table = context.getDataTable(Customer.NAME);
        IDataTableRecord record = table.getSelectedRecord();
        table.qbeClear();
        table.qbeSetKeyValue(Customer.pkey, -1);
        table.search();
        table.qbeClear();
        table.qbeSetKeyValue(Customer.pkey, record.getValue(Customer.pkey));
        table.search();
    }

    public static void clear(IClientContext context) throws Exception
    {
        IDataTable table = context.getDataTable(Customer.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Customer.pkey, -1);
        table.search();
    }
}

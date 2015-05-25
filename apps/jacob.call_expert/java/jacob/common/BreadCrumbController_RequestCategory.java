package jacob.common;

import jacob.model.Request;
import jacob.model.Request_category;
import jacob.resources.I18N;
import java.util.HashMap;
import java.util.Map;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IBreadCrumb;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableListBox;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;

public class BreadCrumbController_RequestCategory
{
    public static String ROOT_PATH = null;
    public static String ROOT_NAME = null;

    private final static String ROOT_CONTAINER="requestDetailContainer";
    private final static String BREADCRUMB="requestBreadCrumb";
    private final static String LISTBOX="requestCategoryListbox";
    private final static String SEARCH_FIELD="requestCategoryFilterText";
    private final static String SEARCH_BUTTON="requestApplyFilterButton";
    private final static String RESET_BUTTON="requestBreadCrumbResetImage";

    private static final String[] NO_OPTIONS = {};


    public static void onSearch(IClientContext context, String filter) throws Exception
    {
        try
        {
            IMutableListBox list = getListbox(context);
            list.removeOptions();
            list.setOptions(NO_OPTIONS);

            Map<String, IDataTableRecord> entryMap = new HashMap<String, IDataTableRecord>();
            IDataTable categoryTable = context.getDataAccessor().newAccessor().getTable(Request_category.NAME);
            categoryTable.qbeClear();
            categoryTable.qbeSetValue(Request_category.longname, filter);
            categoryTable.search();
            // Add additional ".." for the "up" navigation


            for (int i = 0; i < categoryTable.recordCount(); i++)
            {
                IDataTableRecord record = categoryTable.getRecord(i);
                //String name = record.getSaveStringValue(Request_category.name);
                String name = record.getSaveStringValue(Request_category.longname);
                Boolean leaf = record.getBooleanValue(Request_category.leaf);
                // enter a name only once.
                if (!entryMap.containsKey(name))
                {
                    // list.addOption(name);
                    if (leaf)
                    {
                        list.addOption(name, Icon.bullet_green, null);
                    }
                    else
                    {
                        list.addOption(name, Icon.folder_go, null);
                    }
                    entryMap.put(name, record);
                }
            }
            if (entryMap.isEmpty())
            {
                String message = I18N.REQUEST_BREADCRUMB_NO_SEARCH_RESULTS.get(context);
                list.addOption(message);
                list.enableOption(message, false);
            }

            context.setPropertyForWindow(list.getPathName(), entryMap);
        }
        catch(Exception exc)
        {
            ExceptionHandler.handle(exc);
        }

    }


    public static void onGroupStatusChanged(IClientContext context, GroupState state) throws Exception
    {
        determineRootCategory(context);

        IBreadCrumb breadcrumb  = getBreadcrumb(context);
        IMutableListBox listbox = getListbox(context);
        IButton button = getSearchButton(context);
        IText textfield = getSearchField(context);

        IDataTableRecord requestRecord  = context.getDataTable(Request.NAME).getSelectedRecord();

        if (requestRecord != null && requestRecord.isNormal())
        {
            IDataTableRecord categoryRecord = requestRecord.getLinkedRecord(Request_category.NAME);
            breadcrumb.setBreadCrumb(categoryRecord.getSaveStringValue(Request_category.longname));
            breadcrumb.setEnable(false);
            listbox.setEnable(false);
            listbox.removeOptions();
            button.setEnable(false);
            textfield.setEnable(false);
        }
        else if (requestRecord != null && requestRecord.isNew())
        {
            breadcrumb.setEnable(true);
            fillByPath(context, ROOT_PATH);
            listbox.setEnable(true);
            button.setEnable(true);
            textfield.setEnable(true);
        }
        else if (requestRecord != null && requestRecord.isUpdated())
        {
            breadcrumb.setEnable(true);
            fillByPath(context, requestRecord.getLinkedRecord(Request_category.NAME).getSaveStringValue(Request_category.longname));
            listbox.setEnable(true);
            button.setEnable(true);
            textfield.setEnable(true);
        }
        else if (requestRecord == null)
        {
            breadcrumb.setBreadCrumb((ROOT_PATH == null ? "/" : ROOT_PATH));
            breadcrumb.setEnable(false);
        }
    }

    public static void onSelect(IClientContext context, IMutableListBox emitter) throws Exception
    {
        Map<String, IDataTableRecord> entryMap = (Map<String, IDataTableRecord>) context.getProperty(emitter.getPathName());
        if (entryMap == null)
            return;

        IMutableListBox listbox = getListbox(context);

        String selection =  listbox.getValue();
        IDataTableRecord requestRecord  = context.getDataTable(Request.NAME).getSelectedRecord();
        IDataTableRecord categoryRecord = entryMap.get(selection);
        if (categoryRecord == null)
        {
            return;
        }

        String path = categoryRecord.getSaveStringValue(Request_category.path);
        String name = categoryRecord.getSaveStringValue(Request_category.name);
        String crumb = path + name + "/";
        if (null != requestRecord)
        {
            IDataTransaction transaction = requestRecord.getCurrentTransaction();
            if (null != transaction)
            {
                requestRecord.setLinkedRecord(transaction, categoryRecord);
            }
        }
        fillByPath(context, crumb);
    }

    public static void onReset(IClientContext context) throws Exception
    {
        IDataTableRecord requestRecord  = context.getDataTable(Request.NAME).getSelectedRecord();
        IDataTransaction transaction = requestRecord.getCurrentTransaction();
        requestRecord.resetLinkedRecord(transaction, Request_category.NAME);
        fillByPath(context, ROOT_PATH);
    }

    public static void onNavigate(IClientContext context, String pathToSegment, String segment) throws Exception
    {
        String crumb = pathToSegment + segment + "/";
        fillByPath(context, crumb);
        IDataTable categoryTable = context.getDataAccessor().newAccessor().getTable(Request_category.NAME);
        categoryTable.qbeSetKeyValue(Request_category.longname, crumb);
        categoryTable.search();
        if (categoryTable.getSelectedRecord() != null)
        {
            IDataTableRecord requestRecord  = context.getDataTable(Request.NAME).getSelectedRecord();
            IDataTransaction transaction = requestRecord.getCurrentTransaction();
            if (null!=transaction)
                requestRecord.setLinkedRecord(transaction, categoryTable.getSelectedRecord());
        }
    }

    private static void fillByPath(IClientContext context, String path) throws Exception
    {
        determineRootCategory(context);

        try
        {
            IMutableListBox list = getListbox(context);
            list.removeOptions();

            Map<String, IDataTableRecord> entryMap = new HashMap<String, IDataTableRecord>();
            IDataTable categoryTable = context.getDataAccessor().newAccessor().getTable(Request_category.NAME);
            categoryTable.qbeSetKeyValue(Request_category.path, path);
            categoryTable.search();
            // Add additional ".." for the "up" navigation
            if (!ROOT_PATH.equals(path))
            {
                IDataTable parentCategoryTable = context.getDataAccessor().newAccessor().getTable(Request_category.NAME);
                parentCategoryTable.qbeSetKeyValue(Request_category.longname, path);
                parentCategoryTable.search();
                if(parentCategoryTable.recordCount() > 0)
                {
                    parentCategoryTable.qbeClear();
                    parentCategoryTable.qbeSetKeyValue(Request_category.longname, parentCategoryTable.getRecord(0).getValue(Request_category.path));
                    parentCategoryTable.search();
                    if (parentCategoryTable.recordCount() > 0)
                    {
                        entryMap.put("..", parentCategoryTable.getRecord(0));
                        list.addOption("..", Icon.bullet_arrow_up,"");
                    }
                }
            }

            for (int i = 0; i < categoryTable.recordCount(); i++)
            {
                IDataTableRecord record = categoryTable.getRecord(i);
                String name = record.getSaveStringValue(Request_category.name);
                Boolean leaf = record.getBooleanValue(Request_category.leaf);
                // enter a name only once.
                if (!entryMap.containsKey(name))
                {
                    // list.addOption(name);
                    if (leaf)
                    {
                        list.addOption(name, Icon.bullet_green, null);
                    }
                    else
                    {
                        list.addOption(name, Icon.folder_go, null);
                    }
                    entryMap.put(name, record);
                }
            }
            if (categoryTable.recordCount() == 0)
            {
                String message = I18N.REQUEST_BREADCRUMB_LOWEST_LEVEL.get(context);
                list.addOption(message);
                list.enableOption(message, false);
            }
            context.setPropertyForWindow(list.getPathName(), entryMap);
            getBreadcrumb(context).setBreadCrumb(path);
        }
        catch(Exception exc)
        {
            ExceptionHandler.handle(exc);
        }
    }


    private static IMutableListBox getListbox(IClientContext context)
    {
        return (IMutableListBox)FormManager.getCallhandling(context).findByName(ROOT_CONTAINER).findByName(LISTBOX);
    }

    private static IButton getSearchButton(IClientContext context)
    {
        return (IButton)FormManager.getCallhandling(context).findByName(ROOT_CONTAINER).findByName(SEARCH_BUTTON);
    }

    private static IText getSearchField(IClientContext context)
    {
        return (IText)FormManager.getCallhandling(context).findByName(ROOT_CONTAINER).findByName(SEARCH_FIELD);
    }

    private static IBreadCrumb getBreadcrumb(IClientContext context)
    {
        return (IBreadCrumb)FormManager.getCallhandling(context).findByName(ROOT_CONTAINER).findByName(BREADCRUMB);
    }

    private static void determineRootCategory(IClientContext context) throws Exception
    {
        if (ROOT_PATH == null)
        {
            IDataTable categoryTable = context.getDataAccessor().newAccessor().getTable(Request_category.NAME);
            categoryTable.qbeSetValue(Request_category.parentcategorie_key, "null");
            categoryTable.search(IRelationSet.LOCAL_NAME);
            if(categoryTable.recordCount() == 0)
            {
                throw new UserException("Unable to determine root category for request handling");
            }
            ROOT_PATH = categoryTable.getRecord(0).getSaveStringValue(Request_category.longname);
            ROOT_NAME = categoryTable.getRecord(0).getSaveStringValue(Request_category.name);
        }
    }
}

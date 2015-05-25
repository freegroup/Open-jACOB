package jacob.common.masterdetail;

import jacob.common.FormManager;
import jacob.common.UIUtil;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.exception.RequiredFieldException;
import de.tif.jacob.core.exception.TableFieldExceptionCollection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.impl.GuiElement;
import de.tif.jacob.screen.impl.html.SingleDataGUIElement;

public class MasterDetailManager
{
    public static void onSelect(MasterDetailManagerConfiguration conf, IClientContext context, IDataTableRecord selectedRecord) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();
        if (detailRecord.isNewOrUpdated())
        {
            transferFromRecordToGui(conf, context);
        }
        else
        {
            // check the foreingAlias whenever this is in update mode
            if (foreignRecord.isNewOrUpdated())
            {
                // switch the record in the update mode. We need to modify any field of
                // the
                // record.....so - we use the first field of the table definition.
                //
                ITableField anyField = (ITableField) detailRecord.getTableAlias().getTableDefinition().getTableFields().get(0);
                detailRecord.setValue(foreignRecord.getCurrentTransaction(), anyField, detailRecord.getValue(anyField));
            }
        }
        conf.onDetailBackfill(context, selectedRecord);
    }

    public static void refresh(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        if (detailRecord != null)
        {
            conf.onDetailBackfill(context, detailRecord);
        }
    }

    public static boolean beforeSelect(MasterDetailManagerConfiguration conf, IClientContext context, IDataBrowserRecord record) throws Exception
    {
        if (null != context.getProperty("DETAILCHANGED") && (Boolean) context.getProperty("DETAILCHANGED"))
        {
            IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
            if (detailRecord != null && detailRecord.isNewOrUpdated())
            {
                transferFromGuiToRecord(conf, context, getTransaction(conf, context));
                context.setPropertyForWindow("DETAILCHANGED", false);
            }
        }
        return true;
    }

    public static boolean onUpdateClicked(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        context.setPropertyForWindow("DETAILCHANGED", true);
        ITableListBox listbox = (ITableListBox) context.getForm().findByName(conf.getMasterList());
        IDataBrowserRecord record = listbox.getSelectedBrowserRecord(context);
        if (record == null)
        {
            return false;
        }

        MasterDetailManager.beforeSelect(conf, context, record);
        int recno = listbox.getSelectedBrowserRecord(context).getBrowser().getSelectedRecordIndex();
        listbox.setSelectedRecordIndex(context, -1);
        listbox.setSelectedRecordIndex(context, recno);
        context.showTransparentMessage("Record Updated");
        return true;
    }

    public static void onGroupStatusChanged(MasterDetailManagerConfiguration conf, IClientContext context, GroupState state) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();

        if (foreignRecord == null)
        {
            setDisabledMode(conf, context);
        }
        else
        {
            ITableListBox listbox = getMasterList(conf, context);
            
            if (foreignRecord.isNewOrUpdated())
            {
                setUpdateMode(conf, context);
                conf.onDetailBackfill(context, detailRecord);
            }
            else if (detailRecord != null && detailRecord.isPersistent())
            {
                setSelectedMode(conf, context);
                conf.onDetailBackfill(context, detailRecord);
            }
            else
            {
                setUnselectedMode(conf, context);
                conf.onDetailBackfill(context, null);
                listbox.setSelectedRecordIndex(context, -1);
            }

            if (listbox.getData().recordCount() > 0)
            {
                if (listbox.getSelectedRecord(context) == null)
                {
                    listbox.setSelectedRecordIndex(context, 0);
                    setSelectedMode(conf, context);
                }

                if (state == IGroup.UPDATE)
                {
                    ITableField anyField = (ITableField) detailRecord.getTableAlias().getTableDefinition().getTableFields().get(0);
                    detailRecord.setValue(getTransaction(conf, context), anyField, detailRecord.getValue(anyField));
                }

                Integer index = (Integer) context.getProperty(getMasterList(conf, context).getPathName());
                if (index != null)
                {
                    listbox.setSelectedRecordIndex(context, index.intValue());
                }
            }
        }
    }

    public static void onTopClicked(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();
        int index = getMasterList(conf, context).getData().getSelectedRecordIndex();
        int index2 = index - 1;

        try
        {
            while (index2 >= 0)
            {
                IDataTableRecord detailRecord1 = getMasterList(conf, context).getData().getRecord(index).getTableRecord();
                IDataTableRecord detailRecord2 = getMasterList(conf, context).getData().getRecord(index2).getTableRecord();
                int sortId1 = detailRecord1.getintValue(conf.getSortField());
                int sortId2 = detailRecord2.getintValue(conf.getSortField());
                detailRecord1.setValue(trans, conf.getSortField(), sortId2);
                detailRecord2.setValue(trans, conf.getSortField(), sortId1);
                index2--;
            }
            trans.commit();
            context.getDataTable(conf.getDetailAlias()).clearRecords();
            context.getDataAccessor().propagateRecord(foreignRecord, Filldirection.BOTH);
            context.setPropertyForRequest(getMasterList(conf, context).getPathName(), new Integer(0));
        }
        finally
        {
            trans.close();
        }
    }

    public static void onUpClicked(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();
        int index = getMasterList(conf, context).getData().getSelectedRecordIndex();
        if (index > 0)
        {
            IDataTableRecord detailRecord2 = getMasterList(conf, context).getData().getRecord(index - 1).getTableRecord();
            int sortId1 = detailRecord.getintValue(conf.getSortField());
            int sortId2 = detailRecord2.getintValue(conf.getSortField());
            IDataTransaction trans = context.getDataAccessor().newTransaction();
            try
            {
                detailRecord.setValue(trans, conf.getSortField(), sortId2);
                detailRecord2.setValue(trans, conf.getSortField(), sortId1);
                trans.commit();
                context.getDataTable(conf.getDetailAlias()).clearRecords();
                context.getDataAccessor().propagateRecord(foreignRecord, conf.getPropagationRelationset(), Filldirection.BOTH);
                context.setPropertyForRequest(getMasterList(conf, context).getPathName(), new Integer(index - 1));
            }
            finally
            {
                trans.close();
            }
        }

    }

    public static void onDownClicked(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();
        int index = getMasterList(conf, context).getData().getSelectedRecordIndex();
        if (index < getMasterList(conf, context).getData().recordCount() - 1)
        {
            IDataTableRecord detailRecord2 = getMasterList(conf, context).getData().getRecord(index + 1).getTableRecord();
            int sortId1 = detailRecord.getintValue(conf.getSortField());
            int sortId2 = detailRecord2.getintValue(conf.getSortField());
            IDataTransaction trans = context.getDataAccessor().newTransaction();
            try
            {
                detailRecord.setValue(trans, conf.getSortField(), sortId2);
                detailRecord2.setValue(trans, conf.getSortField(), sortId1);
                trans.commit();
                context.getDataTable(conf.getDetailAlias()).clearRecords();
                context.getDataAccessor().propagateRecord(foreignRecord, conf.getPropagationRelationset(), Filldirection.BOTH);
                context.setPropertyForRequest(getMasterList(conf, context).getPathName(), new Integer(index + 1));
            }
            finally
            {
                trans.close();
            }
        }
    }

    public static void onNewClicked(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        TableFieldExceptionCollection coll = new TableFieldExceptionCollection("Required fields missing");
        ITabPane pane = getDetailContainer(conf, context).getPane(0);
        Iterator iter = pane.getChildren().iterator();
        while (iter.hasNext())
        {
            IGuiElement child = (IGuiElement) iter.next();
            if (child instanceof SingleDataGUIElement)
            {
                SingleDataGUIElement dataElement = (SingleDataGUIElement) child;
                ITableField field = dataElement.getDataField().getField();
                if (field.isRequired() && StringUtils.isBlank(dataElement.getValue()))
                {
                    coll.add(new RequiredFieldException(field));
                }
            }
        }
        if (!coll.isEmpty())
        {
            throw coll;
        }

        IDataTransaction transaction = getTransaction(conf, context);
        if (transaction == null)
        {
            transaction = context.getDataAccessor().newTransaction();
        }
        context.getDataTable(conf.getDetailAlias()).newRecord(transaction);

        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();

        // link the record to the anchor table record
        detailRecord.setValue(detailRecord.getCurrentTransaction(), conf.getFkey(), foreignRecord.getValue(conf.getPkey()));
        transferFromGuiToRecord(conf, context, transaction);
        getMasterList(conf, context).add(context, detailRecord);

        getMasterList(conf, context).setSelectedRecordIndex(context, 0);

        transaction.setProperty(conf.getTransactionProperty(), Boolean.TRUE);
        setUpdateMode(conf, context);
    }

    public static void onDeleteClicked(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        // Rob: moved getting the detailRecord to the top; if it is null nothing is done at all
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        if (detailRecord == null)
        {
            return;
        }
        if (getMasterList(conf, context).getData().recordCount() == 1 && conf.isRequired())
        {
            // don't delete the last
            context.createMessageDialog("Cannot delete last record").show();
            return;
        }

        IDataTransaction trans = getTransaction(conf, context);
        boolean canCommit = (trans == null);

        try
        {
            if (trans == null)
            {
                trans = context.getDataAccessor().newTransaction();
            }

            // remove them from the database
            detailRecord.delete(trans);

            // remove them from the listbox
            int index = removeSelected(conf, context);

            int recordCount = getMasterList(conf, context).getData().recordCount();
            trans.setProperty(conf.getTransactionProperty(), recordCount > 0);
            index = Math.min(index, recordCount - 1);
            // Set Default Rec
            if (recordCount > 0)
            {
                IDataTableRecord defaultRec = getMasterList(conf, context).getData().getRecord(0).getTableRecord();
                context.getDataTable(conf.getDetailAlias()).clearRecords();
            }

            if (canCommit)
            {
                trans.commit();
            }

            getMasterList(conf, context).setSelectedRecordIndex(context, index);
            // set update mode always instead of only when a record is selected
            setUpdateMode(conf, context);
            /*
            if (index >= 0)
                setUpdateMode(conf, context);
            else
                setUnselectedMode(conf, context);
             */

            conf.onDetailBackfill(context, getMasterList(conf, context).getSelectedRecord(context));
        }
        finally
        {
            if (canCommit)
            {
                trans.close();
            }
        }
    }

    public static void clear(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        getMasterList(conf, context).getData().clear();
        ((GuiElement) getMasterList(conf, context)).resetCache();
    }

    private static void transferFromGuiToRecord(MasterDetailManagerConfiguration conf, IClientContext context, IDataTransaction transaction) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        ITabPane pane = getDetailContainer(conf, context).getPane(0);
        UIUtil.transferFromGuiToRecord(context, transaction, pane, detailRecord);
    }

    private static void transferFromRecordToGui(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        ITabPane pane = getDetailContainer(conf, context).getPane(0);
        Iterator iter = pane.getChildren().iterator();
        while (iter.hasNext())
        {
            IGuiElement child = (IGuiElement) iter.next();
            if (child instanceof SingleDataGUIElement)
            {
                SingleDataGUIElement dataElement = (SingleDataGUIElement) child;
                if (!dataElement.getDataField().getTableAlias().getName().equals(pane.getGroupTableAlias()))
                {
                    ITableAlias alias = dataElement.getDataField().getTableAlias();
                    IDataTableRecord foreignRecord = detailRecord.getLinkedRecord(alias);
                    if (foreignRecord != null)
                    {
                        context.getDataAccessor().getTable(alias).setSelectedRecord(foreignRecord.getPrimaryKeyValue());
                    }
                    else
                    {
                        context.getDataAccessor().getTable(alias).clearRecords();
                    }
                }
            }
        }
    }

    private static IDataTransaction getTransaction(MasterDetailManagerConfiguration conf, IClientContext context) throws Exception
    {
        IDataTableRecord detailRecord = context.getDataTable(conf.getDetailAlias()).getSelectedRecord();
        IDataTableRecord foreignRecord = context.getDataTable(conf.getForeignAlias()).getSelectedRecord();

        if (foreignRecord != null && foreignRecord.isNewOrUpdated())
        {
            return foreignRecord.getCurrentTransaction();
        }

        if (detailRecord != null && detailRecord.isNewOrUpdated())
        {
            return detailRecord.getCurrentTransaction();
        }

        return null;
    }

    /**
     * Returns the index of the deleted Record.
     *
     * @param context
     * @return
     */
    private static int removeSelected(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        IDataBrowser browser = getMasterList(conf, context).getData();
        int index = browser.getSelectedRecordIndex();

        if (index >= 0)
        {
            IDataBrowserRecord listBrowserRecord = browser.getRecord(index);
            browser.removeRecord(listBrowserRecord);
            ((GuiElement) getMasterList(conf, context)).resetCache();
            return index;
        }
        return -1;
    }

    private static void setUnselectedMode(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        getElement(context, conf.getButtonNew()).setEnable(false);
        getElement(context, conf.getButtonDelete()).setEnable(false);
        getElement(context, conf.getButtonUp()).setEnable(false);
        getElement(context, conf.getButtonDown()).setEnable(false);
        getElement(context, conf.getButtonTop()).setEnable(false);

        if (null != conf.getButtonUpdate())
        {
            getElement(context, conf.getButtonUpdate()).setEnable(false);
        }
    }

    private static void setSelectedMode(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        getElement(context, conf.getButtonNew()).setEnable(false);
        getElement(context, conf.getButtonDelete()).setEnable(false);
        getElement(context, conf.getButtonUp()).setEnable(true);
        getElement(context, conf.getButtonDown()).setEnable(true);
        getElement(context, conf.getButtonTop()).setEnable(true);
        if (null != conf.getButtonUpdate())
        {
            getElement(context, conf.getButtonUpdate()).setEnable(false);
        }
    }

    private static void setDisabledMode(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        getElement(context, conf.getButtonNew()).setEnable(false);
        getElement(context, conf.getButtonDelete()).setEnable(false);
        getElement(context, conf.getButtonUp()).setEnable(false);
        getElement(context, conf.getButtonDown()).setEnable(false);
        getElement(context, conf.getButtonTop()).setEnable(false);
        if (null != conf.getButtonUpdate())
        {
            getElement(context, conf.getButtonUpdate()).setEnable(false);
        }
    }

    private static void setUpdateMode(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        getElement(context, conf.getButtonNew()).setEnable(true);
        getElement(context, conf.getButtonDelete()).setEnable(true);
        getElement(context, conf.getButtonUp()).setEnable(false);
        getElement(context, conf.getButtonDown()).setEnable(false);
        getElement(context, conf.getButtonTop()).setEnable(false);
        if (null != conf.getButtonUpdate())
        {
            getElement(context, conf.getButtonUpdate()).setEnable(true);
        }
    }

    private static ITableListBox getMasterList(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        return (ITableListBox)FormManager.getCallhandling(context).findByName(conf.getMasterList());
    }

    private static ITabContainer getDetailContainer(MasterDetailManagerConfiguration conf, IClientContext context)
    {
        return (ITabContainer)FormManager.getCallhandling(context).findByName(conf.getDetailContainer());
    }

    static IGuiElement getElement(IClientContext context, String name)
    {
        return FormManager.getCallhandling(context).findByName(name);
    }
}

/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Fri Nov 14 15:17:35 CET 2008
 */
package jacob.event.data;

import jacob.common.dblayer.AbstractHistoryTableRecord;
import jacob.model.Category;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 *
 * @author achim
 */
public class CategoryTableRecord extends AbstractHistoryTableRecord
{
    static public final transient String RCS_ID = "$Id: CategoryTableRecord.java,v 1.3 2009/11/23 11:33:41 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";


    /* (non-Javadoc)
     * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord, de.tif.jacob.core.data.IDataTransaction)
     */
    public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
    {
        super.beforeCommitAction(tableRecord, transaction);

        // Be in mind: It is not possible to modify the 'tableRecord', if we want delete it
        //
        if (tableRecord.isDeleted())
        {
            return;
        }

        // Pfad berechnen
        if (tableRecord.hasNullValue(Category.parentcategorie_key))
        {
            tableRecord.setValue(transaction, Category.path, "/");
            tableRecord.setValue(transaction, Category.longname, "/"+tableRecord.getSaveStringValue(Category.name)+"/");
        }
        else
        {
            IDataTable parentcat = tableRecord.getAccessor().newAccessor().getTable(Category.NAME);
            parentcat.qbeSetKeyValue(Category.pkey, tableRecord.getValue(Category.parentcategorie_key));
            parentcat.search();
            IDataTableRecord parentcatrec = parentcat.getRecord(0);
            String parentLongname = parentcatrec.getSaveStringValue(Category.longname);
            tableRecord.setValue(transaction, Category.path, parentLongname);
            tableRecord.setValue(transaction, Category.longname, parentLongname+tableRecord.getSaveStringValue(Category.name)+"/");

        }
        // determine all children of this node to recalculate the longname/path of the nodes
        Context context = Context.getCurrent();
        recalculateChildren(context, transaction, tableRecord);
    }

    private void recalculateChildren(Context context, IDataTransaction transaction, IDataTableRecord parentcatrec) throws Exception
    {
        IDataAccessor acc = context.getDataAccessor().newAccessor();
        IDataTable categoryTable = acc.getTable(Category.NAME);
        categoryTable.qbeSetKeyValue(Category.parentcategorie_key, parentcatrec.getSaveStringValue(Category.pkey));
        int count = categoryTable.search();
        if (count == 0)
        {
            parentcatrec.setBooleanValue(transaction, Category.leaf, true);
        }
        else
        {
            parentcatrec.setBooleanValue(transaction, Category.leaf, false);
        }
        for(int i = 0; i < count; i++)
        {
            IDataTableRecord category = categoryTable.getRecord(i);
            String parentLongname = parentcatrec.getSaveStringValue(Category.longname);
            category.setValue(transaction, Category.path, parentLongname);
            category.setValue(transaction, Category.longname,parentLongname+category.getSaveStringValue(Category.name)+"/");
            recalculateChildren(context, transaction, category);
        }
    }
}

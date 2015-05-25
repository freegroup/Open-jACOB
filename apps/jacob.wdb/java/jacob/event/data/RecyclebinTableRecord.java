/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Oct 18 10:22:38 CEST 2010
 */
package jacob.event.data;

import jacob.model.Article_no_condition;
import jacob.model.Menutree_no_condition;
import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * 
 * @author andreas
 */
public final class RecyclebinTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: RecyclebinTableRecord.java,v 1.1 2010-10-20 21:00:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private static final String RESTORE_FLAG_PROPERTY = RecyclebinTableRecord.class.getName() + "RESTORE_FLAG";

  /**
   * Set flag which indicates that the recyclebin record is deleted due to an restore operation.
   * 
   * @param transaction
   */
  public static void setRestoreFlag(IDataTransaction transaction)
  {
    transaction.setProperty(RESTORE_FLAG_PROPERTY, Boolean.TRUE);
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    tableRecord.setValue(transaction, Recyclebin.delete_by, transaction.getUser().getFullName());
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord recyclebinRecord, IDataTransaction transaction) throws Exception
  {
    if (recyclebinRecord.isDeleted() && transaction.getProperty(RESTORE_FLAG_PROPERTY) == null)
    {
      // Delete records behind the recyclebin record
      //
      String objectType = recyclebinRecord.getStringValue(Recyclebin.object_type);
      if (Recyclebin.object_type_ENUM._menutree.equals(objectType))
      {
        IDataTable table = recyclebinRecord.getAccessor().getTable(Menutree_no_condition.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Menutree_no_condition.pkey, recyclebinRecord.getValue(Recyclebin.object_pkey));
        table.search();
        IDataTableRecord menutreeRecord = table.getSelectedRecord();
        if (menutreeRecord != null)
        {
          // try to delete: see Menutree_no_conditionTableRecord
          menutreeRecord.delete(transaction);
        }
      }
      else if (Recyclebin.object_type_ENUM._article.equals(objectType))
      {
        IDataTable table = recyclebinRecord.getAccessor().getTable(Article_no_condition.NAME);
        table.qbeClear();
        table.qbeSetKeyValue(Article_no_condition.pkey, recyclebinRecord.getValue(Recyclebin.object_pkey));
        table.search();
        IDataTableRecord articleRecord = table.getSelectedRecord();
        if (articleRecord != null)
        {
          // try to delete: see Article_no_conditionTableRecord
          articleRecord.delete(transaction);
        }
      }
      else
      {
        throw new RuntimeException("Unsupported object type '" + objectType + "'");
      }

    }
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}

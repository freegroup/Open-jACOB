/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 07 20:21:43 CET 2006
 */
package jacob.event.data;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import jacob.common.AppLogger;
import jacob.model.CommonContact;

import org.apache.commons.logging.Log;

/**
 * 
 * @author andherz
 */
public class CommonContactTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: CommonContactTableRecord.java,v 1.1 2007/11/25 22:12:37 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterNewAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    Context context = Context.getCurrent();

    tableRecord.setValue(transaction, CommonContact.type, CommonContact.type_ENUM._common);
    tableRecord.setValue(transaction, CommonContact.mandator_id, context.getUser().getMandatorId());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterDeleteAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#beforeCommitAction(de.tif.jacob.core.data.IDataTableRecord,
   *      de.tif.jacob.core.data.IDataTransaction)
   */
  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // Be in mind: It is not possible to modify the 'tableRecord', if we want
    // delete it
    //
    if (tableRecord.isDeleted())
      return;

    if (tableRecord.hasChangedValue(CommonContact.firstname) || tableRecord.hasChangedValue(CommonContact.lastname))
    {
      String value = tableRecord.getSaveStringValue(CommonContact.firstname) + " " + tableRecord.getSaveStringValue(CommonContact.lastname);
      tableRecord.setValue(transaction, CommonContact.fullname, value);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.core.data.event.DataTableRecordEventHandler#afterCommitAction(de.tif.jacob.core.data.IDataTableRecord)
   */
  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
  }
}

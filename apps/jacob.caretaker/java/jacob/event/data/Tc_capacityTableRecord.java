/*
 * Created on 22.03.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jacob.event.data;

import jacob.exception.BusinessException;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Tc_capacityTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_capacityTableRecord.java,v 1.3 2006/08/21 12:13:21 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
  }

  public void beforeCommitAction(IDataTableRecord orderRecord, IDataTransaction transaction) throws Exception
  {
    if (!orderRecord.isDeleted())
    {
      // Nur zur Sicherheit, daß die Doppelverlinkung zwischen Order und
      // Capacity nicht zerstört wird.
      // Anmerkung: beforeCommitAction() wird nicht von
      // Tc_orderTableRecord.beforeCommitAction() aufgerufen!
      //
      if (orderRecord.hasChangedValue("tc_order_key") && !Tc_orderTableRecord.isOrderStornoActive())
      {
        throw new BusinessException("Der Buchungsverweis kann nicht geändert werden");
      }
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

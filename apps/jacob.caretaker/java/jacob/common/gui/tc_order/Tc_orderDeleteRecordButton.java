/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 28 12:50:18 CEST 2006
 */
package jacob.common.gui.tc_order;

import java.sql.Timestamp;

import jacob.common.tc.TC;
import jacob.exception.BusinessException;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for a delete button.<br>
 * 
 * @author andreas
 *
 */
public class Tc_orderDeleteRecordButton extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_orderDeleteRecordButton.java,v 1.3 2006/08/09 15:37:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    IDataTableRecord orderRecord = context.getSelectedRecord();
    IDataTableRecord capacityRecord = orderRecord.getLinkedRecord("tc_capacity");
    Timestamp slot = capacityRecord.getTimestampValue("slot");
    if (slot.getTime() < System.currentTimeMillis())
      throw new BusinessException("Buchung liegt in der Vergangenheit und kann nicht mehr storniert werden!");

    return true;
  }

  public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    // Im Hintergrund wurde ein Ticket angelegt (Klasse Tc_orderTableRecord).
    // Für dieses wollen wir nun die Koordinationszeit erfragen und
    // gegebenenfalls setzen.
    //
    TC.requestAndSetCoordinationTime(context);
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

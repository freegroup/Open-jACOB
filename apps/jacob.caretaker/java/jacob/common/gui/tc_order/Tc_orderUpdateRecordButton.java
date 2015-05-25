/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 28 13:39:13 CEST 2006
 */
package jacob.common.gui.tc_order;

import jacob.common.gui.tc_object.ChartState;
import jacob.common.tc.TC;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the Tc_orderUpdateRecordButton update button.<br>
 * 
 * @author andreas
 */
public class Tc_orderUpdateRecordButton extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_orderUpdateRecordButton.java,v 1.4 2006/08/21 11:29:19 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  /**
   * This event handler will be called, if the corresponding button has been pressed.
   * You can prevent the execution of the UPDATE action if you return <code>false</code>.<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    return true;
  }

  public void onSuccess(IClientContext context, IGuiElement button) throws Exception
  {
    // Im Hintergrund wurde gegebenenfalls (bei NEW) ein Ticket angelegt (Klasse Tc_orderTableRecord).
    // Für dieses wollen wir nun die Koordinationszeit erfragen und
    // gegebenenfalls setzen.
    //
    if (TC.requestAndSetCoordinationTime(context))
    {
      // Chart zurücksetzen
      ChartState.discardChartState(context);
      ((IChart) context.getForm().findByName("tc_objectGroup").findByName("slotSelectChart")).refresh();
    }
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

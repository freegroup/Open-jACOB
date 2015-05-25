/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 12:32:43 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for the CustomerUpdateRecordButton update button.<br>
 * 
 * @author achim
 */
public class CustomerUpdateRecordButton extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CustomerUpdateRecordButton.java,v 1.3 2009/04/02 13:40:16 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * This event handler will be called, if the corresponding button has been
   * pressed. You can prevent the execution of the UPDATE action if you return
   * <code>false</code>.<br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of
   *         the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
   return true;
  }



  /**
   * This event method will be called, if the UPDATE action has been
   * successfully executed.<br>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button)
  {
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setVisible(IGroup.UPDATE == status || IGroup.NEW == status);
  }
}

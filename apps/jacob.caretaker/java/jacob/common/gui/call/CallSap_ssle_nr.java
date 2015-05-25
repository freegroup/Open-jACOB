/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Mar 05 09:58:34 CET 2008
 */
package jacob.common.gui.call;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;

/**
 * 
 * @author achim
 */
public class CallSap_ssle_nr extends ITextFieldEventHandler
{
  static public final transient String RCS_ID = "$Id: CallSap_ssle_nr.java,v 1.1 2008/03/07 13:15:05 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Use this logger to write messages and NOT the
   * <code>System.out.println(..)</code> ;-)
   */
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group status
   * @param emitter
   *          The corresponding GUI element of this event handler
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
  {
    if (status.equals(IGroup.SEARCH)||context.getForm().getName().equals("stoerstelle"))
    {
      emitter.setEnable(true);
    }
    else
    {
      emitter.setEnable(false);
    }

  }
}
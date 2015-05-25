/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 11 12:19:08 CEST 2006
 */
package jacob.common.gui.tc_parameter;

import jacob.scheduler.system.TCForecast;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the Tc_parameterCreateForecast generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_parameterCreateForecast extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_parameterCreateForecast.java,v 1.1 2006/10/12 09:32:10 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    TCForecast.show(context);
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

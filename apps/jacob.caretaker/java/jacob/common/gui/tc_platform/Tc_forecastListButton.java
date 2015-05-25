/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 11 12:41:53 CEST 2006
 */
package jacob.common.gui.tc_platform;

import jacob.scheduler.system.TCForecast;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Tc_forecastListButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_forecastListButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Tc_forecastListButton.java,v 1.1 2006/10/12 09:32:10 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    TCForecast.show(context);
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

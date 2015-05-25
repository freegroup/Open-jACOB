/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jul 26 14:01:06 CEST 2006
 */
package jacob.common.gui.tc_platform;

import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the RefreshCharts record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class RefreshCharts extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RefreshCharts.java,v 1.1 2006/07/28 14:37:30 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    doit(context);
  }

  public static void doit(IClientContext context)
  {
    ((IChart) context.getGroup().findByName("availableCapacityChart")).refresh();
    ((IChart) context.getGroup().findByName("workloadChart")).refresh();
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
  }
}

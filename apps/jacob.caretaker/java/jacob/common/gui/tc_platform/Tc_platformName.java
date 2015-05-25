/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 12 11:17:03 CEST 2006
 */
package jacob.common.gui.tc_platform;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

/**
 * 
 * @author andreas
 */
public class Tc_platformName extends ITextFieldEventHandler
{
  static public final transient String RCS_ID = "$Id: Tc_platformName.java,v 1.1 2006/10/12 09:32:10 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
  {
    if (status == IGuiElement.SELECTED && PlatformChartData.isOverallEnabled(context))
    {
      // TODO: Hack, da Chart-Element immer einen selektierten Record braucht
      ((IText) emitter).setValue("Alle Bühnen");
    }
  }
}

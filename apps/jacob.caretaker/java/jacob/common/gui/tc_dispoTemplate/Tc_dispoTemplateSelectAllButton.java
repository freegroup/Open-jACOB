/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 28 15:44:41 CEST 2006
 */
package jacob.common.gui.tc_dispoTemplate;

import jacob.common.tc.Slot;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Tc_dispoTemplateSelectAllButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Tc_dispoTemplateSelectAllButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Tc_dispoTemplateSelectAllButton.java,v 1.2 2006/08/03 17:20:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IGroup group = context.getGroup();
    for (int i = 0; i < Slot.SLOTS.length; i++)
    {
      ((ISingleDataGuiElement) group.findByName(Slot.SLOTS[i].guiName)).setValue("1");
    }
  }
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.UPDATE || status == IGuiElement.NEW);
  }
}

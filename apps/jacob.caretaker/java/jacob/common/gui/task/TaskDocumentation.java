/*
 * Created on 20.08.2004
 * by mike
 *
 */
package jacob.common.gui.task;

import jacob.model.Ext_system;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Springt in Abhängigkeit vom externen System zur ensprechen Auftragsmaske
 * @author mike
 *
 */
public class TaskDocumentation extends IButtonEventHandler
{
static public final transient String RCS_ID = "$Id: TaskDocumentation.java,v 1.2 2008/02/19 14:49:59 achim Exp $";
static public final transient String RCS_REV = "$Revision: 1.2 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord task = context.getSelectedRecord();
    String targetForm = null;
		if (task.hasLinkedRecord("ext_system"))
		{
      IDataTableRecord extSystem = task.getLinkedRecord(Ext_system.NAME);
      if (Ext_system.systemtype_ENUM._EDVIN.equals(extSystem.getValue(Ext_system.systemtype)))
      {
        targetForm = "taskEdvin";
      }       
      else if (Ext_system.systemtype_ENUM._SAPiPRO.equals(extSystem.getValue(Ext_system.systemtype)))
      {
        targetForm = "taskSAP";
      }    
      else if (Ext_system.systemtype_ENUM._virtuell.equals(extSystem.getValue(Ext_system.systemtype)))
      {
        targetForm = "taskSAP";
      }     
    }
		context.setCurrentForm(targetForm);
	}

}

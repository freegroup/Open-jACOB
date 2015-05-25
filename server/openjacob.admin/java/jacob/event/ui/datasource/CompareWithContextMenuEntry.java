/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Dec 05 17:13:30 CET 2008
 */
package jacob.event.ui.datasource;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the CompareWithContextMenuEntry record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public final class CompareWithContextMenuEntry extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CompareWithContextMenuEntry.java,v 1.1 2008/12/05 17:26:54 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    DatasourceReconfigure.reconfigure(context, true);
	}
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    button.setEnable(status == IGuiElement.SELECTED);
	}
}


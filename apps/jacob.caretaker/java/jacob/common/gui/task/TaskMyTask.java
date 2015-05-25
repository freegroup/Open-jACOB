/*
 * Created on 29.07.2004
 *
 */
package jacob.common.gui.task;

import jacob.common.Task;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class TaskMyTask extends IButtonEventHandler 

{
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
			Task.findByUserAndState(context, context.getUser(), "Neu|Angelegt|Freigegeben|In Arbeit|Fertig gemeldet|Dokumentiert", "r_queues");
	}
  
	/**
	 * Enable disable the button when state of the group has been changed
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}
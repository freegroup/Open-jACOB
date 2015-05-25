/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 13 20:22:28 CEST 2010
 */
package jacob.event.ui.article.edit;

import jacob.common.TextblockEditController;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Chapter01TakeEditButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class AbstractTakeEditButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: AbstractTakeEditButton.java,v 1.5 2010-09-29 13:29:52 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
    TextblockEditController.endEdit(context);
	}

}

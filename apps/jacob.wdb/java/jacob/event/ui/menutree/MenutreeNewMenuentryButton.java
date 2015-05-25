/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 05 14:45:35 CEST 2010
 */
package jacob.event.ui.menutree;

import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the MenutreeNewMenuentryButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class MenutreeNewMenuentryButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: MenutreeNewMenuentryButton.java,v 1.3 2010-09-29 11:26:29 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

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
    IDataTableRecord currentMenuRecord = context.getSelectedRecord();
    context.clearGroup();
   	IDataTableRecord newMenuRecord  = Menutree.newRecord(context);
 	  IDataTransaction trans = newMenuRecord.getCurrentTransaction();
 	  newMenuRecord.setValue(trans, Menutree.menutree_parent_key, currentMenuRecord.getValue(Menutree.pkey));
	}
 
}

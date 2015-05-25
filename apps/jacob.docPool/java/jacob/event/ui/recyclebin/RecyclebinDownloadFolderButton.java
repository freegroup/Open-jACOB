/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 12 11:17:07 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the RecyclebinDownloadFolderButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class RecyclebinDownloadFolderButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: RecyclebinDownloadFolderButton.java,v 1.1 2010-07-16 14:26:14 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IDocumentDialog dialog = context.createDocumentDialog(currentRecord.getDocumentValue(Recyclebin.content));
    dialog.show();
	}
}

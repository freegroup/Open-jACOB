/*
 * Created on 01.12.2004
 * by mike
 *
 */
package jacob.common.gui.object;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Zeigt das Feld "object.specification" in einem Dialog an
 * 
 * @author mike
 *  
 */
public class ObjectInfo extends IButtonEventHandler
{


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord currentRecord = context.getSelectedRecord();
		String specification = currentRecord.getSaveStringValue("specification");

		FormLayout layout = new FormLayout("10dlu, 350dlu,10dlu", // columns
				"10dlu,300dlu,10dlu"); // rows
		CellConstraints cc = new CellConstraints();
		IFormDialog dialog = context.createFormDialog("Objektinformation", layout, null);
		dialog.addTextArea("specification", specification, cc.xy(1, 1));
		// Show the dialog with a prefered size. The dialog trys to resize to the
		// optimum size!
		dialog.show(350, 350);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement.GroupState,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
	{
		if (status == IGuiElement.SELECTED)
		{
			IDataTableRecord record = context.getSelectedRecord();
			button.setEnable(record.getValue("specification") != null);
		}
		else
		{
			button.setEnable(false);
		}
	}
}

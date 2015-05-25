/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 08 11:43:43 CET 2007
 */
package jacob.event.ui.ppt;

import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.impl.html.Document;
import jacob.common.AppLogger;
import jacob.model.Ppt;

import org.apache.commons.logging.Log;


/**
 * The event handler for the PptUploadENButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class PptUploadENButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: PptUploadENButton.java,v 1.1 2007/03/09 16:42:08 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		context.createUploadDialog(new IUploadDialogCallback() 
		{
			public void onOk(IClientContext context, String fileName, byte[] fileData)throws Exception 
			{
				IDataTransaction trans = context.getSelectedRecord().getCurrentTransaction();
				context.getSelectedRecord().setDocumentValue(trans, Ppt.document_en, DataDocumentValue.create(fileName, fileData));
				((Document)context.getGroup().findByName("pptDocumentEN")).setValue(context, context.getSelectedRecord());
			}
		
			public void onCancel(IClientContext context) throws Exception 
			{
			}
		}).show();
	}
   
	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		button.setEnable(status==IGuiElement.UPDATE ||status==IGuiElement.NEW);
		((IButton)button).setIcon(Icon.page_white_powerpoint);
	}
}


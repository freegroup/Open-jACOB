/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Mar 06 14:24:48 CET 2007
 */
package jacob.event.ui.composition;

import java.net.URLEncoder;

import jacob.common.AppLogger;
import jacob.model.Composition;
import jacob.model.Ppt;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the CompositionComposeButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class CompositionComposeButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CompositionComposeButton.java,v 1.3 2007/10/02 10:53:07 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

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
		IDataTableRecord currentRecord = context.getSelectedRecord();
		String encodedName =URLEncoder.encode(currentRecord.getSaveStringValue(Composition.name),"ISO-8859-1");
		IUrlDialog dialog= context.createUrlDialog("./application/"+AppLogger.NAME+"/"+AppLogger.VERSION+"/composer.jsp?pkey="+currentRecord.getSaveStringValue(Ppt.pkey)+"&name="+encodedName);
		dialog.enableScrollbar(true);
		dialog.show(750,600);
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
		((IButton)button).setIcon(Icon.sitemap_color);
    button.setVisible(status == IGuiElement.SELECTED);
    button.setEnable(status == IGuiElement.SELECTED);
	}

}



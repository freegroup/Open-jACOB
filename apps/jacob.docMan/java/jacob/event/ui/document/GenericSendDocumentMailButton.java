/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 06 15:48:24 CEST 2010
 */
package jacob.event.ui.document;

import java.net.URLEncoder;

import jacob.common.AppLogger;
import jacob.common.DocumentUtil;
import jacob.model.Document;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DocumentMailDocumentUrlToButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class GenericSendDocumentMailButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: GenericSendDocumentMailButton.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
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
	  // wird nicht gerufen, da der Button zu einem "Link" transformiert wurde.
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement element) throws Exception
	{
	  IDataTableRecord docRecord = context.getDataTable(Document.NAME).getSelectedRecord();
	  if(docRecord!=null)
	  {
	    IButton button = (IButton)element;
	    String name = URLEncoder.encode(docRecord.getSaveStringValue(Document.name),"ISO-8859-1");
	    String url = URLEncoder.encode(DocumentUtil.getUrl(context, docRecord),"ISO-8859-1");
	    button.setLink("mailto:?subject="+name+"&amp;body="+url);
	  }
	}
}


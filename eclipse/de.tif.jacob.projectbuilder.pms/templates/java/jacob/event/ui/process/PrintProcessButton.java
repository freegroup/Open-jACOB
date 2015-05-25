/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 26 14:42:22 CEST 2006
 */
package jacob.event.ui.process;

import java.net.URL;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.xml.Serializer;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.transformer.Transformer;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * The event handler for the PrintProcessButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class PrintProcessButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: PrintProcessButton.java,v 1.1 2007/11/25 22:17:56 freegroup Exp $";
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
		/*
		IDataTableRecord currentRecord = context.getSelectedRecord();

		// Transform the current record (and all linked record) to a XML document
		//
    StringBuffer sb =new StringBuffer(2048);
    sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
    sb.append("<data>\n");
    // insert the current record and all linked records (depends on the given relationset and filldirection) to the XML
    Serializer.appendXml(sb,currentRecord,context.getApplicationDefinition().getDefaultRelationSet(),Filldirection.BOTH);
    sb.append("</data>");
    
    System.out.println(sb.toString());
    // Retrieve the URL of the stylesheet for the XSLT document transformation.
    //
    URL xsl = jacob.resources.ResourceProvider.class.getResource("./stylesheet/process2pdf.xsl");
    
    // Render the XML data with the xsl stylesheet to a PDF document.
    // Valid mime types for the transformer are:
		//  -  application/vnd.hp-PCL
		//  -  application/postscript
		//  -  application/pdf
		//  -  text/plain
		//  -  text/formated
		//	-  text/html
    //
   	byte[] document=Transformer.render(sb.toString(),xsl,"application/pdf");
   	
   	// Send the result of the transformation to the user
   	//
    IDocumentDialog dialog = context.createDocumentDialog("application/pdf","product.pdf",document);
    dialog.show();
    */
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
		//button.setEnable(true/false);
	}
}


package jacob.event.ui.product;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Feb 28 13:23:16 CET 2005
 *
 */
import jacob.common.AppLogger;

import java.net.URL;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.xml.Serializer;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.transformer.Transformer;



 /**
  * The Event handler for the ProductButtonExportTEXT-Button.<br>
  * The onAction will be called, if the user clicks on this button.<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class ProductButtonExportTEXT extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProductButtonExportTEXT.java,v 1.1 2007/11/25 22:19:39 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
 		// Retrieve the current selected record of the group. 
 		//
		IDataTableRecord currentRecord = context.getSelectedRecord();

		// Serialize the current record (and all related tables) to an XML document.
		//
    StringBuffer sb =new StringBuffer(2048);
    sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
    sb.append("<data>\n");
    Serializer.appendXml(sb,currentRecord,context.getApplicationDefinition().getDefaultRelationSet(),Filldirection.BACKWARD);
    sb.append("</data>");

    // Retrieve the URL of the XSLT Stylesheet
    //
    URL xsl = jacob.resources.ResourceProvider.class.getResource("./stylesheet/product2text.xsl");
    
    // Render the XML data with the xsl stylesheet to an TEXT document.
    // Valid mime types for the transformer are:
		//  -  application/vnd.hp-PCL
		//  -  application/postscript
		//  -  application/pdf
		//  -  text/plain
		//  -  text/formated
		//	-  text/html
    //
   	byte[] document=Transformer.render(sb.toString(),xsl,"text/plain");
   	
   	// Send the result of the transformation to the user
   	//
    IDocumentDialog dialog = context.createDocumentDialog("text/plain","product.txt",document);
    dialog.show(550,400);
  }

   
  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}


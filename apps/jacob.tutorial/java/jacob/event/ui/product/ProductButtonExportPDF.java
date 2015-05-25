package jacob.event.ui.product;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Sun Feb 27 22:21:06 CET 2005
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
  * The Event handler for the ProductButtonExportPDF-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class ProductButtonExportPDF extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProductButtonExportPDF.java,v 1.1 2005/03/17 12:07:53 herz Exp $";
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
		IDataTableRecord currentRecord = context.getSelectedRecord();

		// Transform the current record (and all linked record) to a XML document
		//
    StringBuffer sb =new StringBuffer(2048);
    sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
    sb.append("<data>\n");
    // insert the current record and all linked records (depends on the given relationset and filldirection) to the XML
    Serializer.appendXml(sb,currentRecord,context.getApplicationDefinition().getDefaultRelationSet(),Filldirection.BACKWARD);
    sb.append("</data>");
    
    // Retrieve the URL of the stylesheet for the XSLT document transformation.
    //
    URL xsl = jacob.resources.ResourceProvider.class.getResource("./stylesheet/product2pdf.xsl");
    
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
  }

   
  /**
   * 
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}


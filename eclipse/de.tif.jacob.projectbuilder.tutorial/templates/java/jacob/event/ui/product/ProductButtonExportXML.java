package jacob.event.ui.product;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Feb 28 09:14:44 CET 2005
 *
 */
import jacob.common.AppLogger;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.xml.Serializer;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;



 /**
  * Export the current selected record to an XML document.
  * 
  * @author andherz
  *
  */
public class ProductButtonExportXML extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProductButtonExportXML.java,v 1.1 2007/11/25 22:19:39 freegroup Exp $";
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
	   
	   // Display the XML data in a new window.
	   //
	   IDocumentDialog dialog = context.createDocumentDialog("text/xml","product.xml",sb.toString().getBytes());
	   dialog.show(550,400);
  }

   
  /**
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
	}
}


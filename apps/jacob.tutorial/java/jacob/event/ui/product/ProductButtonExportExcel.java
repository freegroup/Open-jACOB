package jacob.event.ui.product;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Mon Feb 28 14:25:24 CET 2005
 *
 */
import jacob.common.AppLogger;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.transformer.ITransformer;
import de.tif.jacob.transformer.TransformerFactory;



 /**
  * The Event handler for the ProductButtonExportExcel-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author andherz
  *
  */
public class ProductButtonExportExcel extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ProductButtonExportExcel.java,v 1.1 2005/03/17 12:07:53 herz Exp $";
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
 		// The current selected record in the group
 		//
		IDataTableRecord currentRecord = context.getSelectedRecord();
		
		// generate the header for the excel sheet
		//
		String[]   header = new String[]{"Id","Name","Description"};
		
		// generate the data itself for the sheet
		//
 		String[][] data   = new String[1][3];
 		data[0][0] =  currentRecord.getSaveStringValue("pkey");
 		data[0][1] =  currentRecord.getSaveStringValue("name");
 		data[0][2] =  currentRecord.getSaveStringValue("description");
 		
		// get an transformer from the factory for the excel mime type
		//
		ITransformer trans =TransformerFactory.get("application/excel");
		
		// create the output stream for the transformer
		//
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// generate the excel sheet
		//
		trans.transform(out,header,data);
		
		// get the data of the sheet
		//
		byte[] sheet=out.toByteArray();
		
		// send the data to the user
		//
		IDocumentDialog dialog = context.createDocumentDialog("application/excel","product.xls", sheet);
		dialog.show();
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


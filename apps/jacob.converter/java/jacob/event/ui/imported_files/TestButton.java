/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon May 04 17:43:14 CEST 2009
 */
package jacob.event.ui.imported_files;

import jacob.model.Converter;
import jacob.model.Imported_files;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the TestButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class TestButton extends IButtonEventHandler 
{
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
	    try
	    {
	      IDataTableRecord converterRecord = context.getDataTable(Converter.NAME).getSelectedRecord();
	      IDataTableRecord currentRecord = context.getSelectedRecord();
	      byte[] doc  = currentRecord.getDocumentValue(Imported_files.excel).getContent();
	      String name = currentRecord.getDocumentValue(Imported_files.excel).getName();
	      String xml = converterRecord.getSaveStringValue(Converter.xml);

	      ExcelImporter importer = new ExcelImporter(name,doc);
	      
	      importer.read(context, name, new ByteArrayInputStream(doc),xml, null);
	      currentRecord.setValue(currentRecord.getCurrentTransaction(), Imported_files.state, Imported_files.state_ENUM._tested);
	      IInFormLongText text = (IInFormLongText)context.getGroup().findByName("protocolArea");
	      text.setValue(importer.getLog());
	      if (importer.hasErrors())
	        throw new UserException("Import kann wegen den angezeigten Fehlern nicht ausgeführt werden");
	    }
	    catch (IOException e)
	    {
	      alert("Keine gültige Exceldatei für den Import");
	    }
	    
	    ButtonController.updateButtons(context);
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
	}

}

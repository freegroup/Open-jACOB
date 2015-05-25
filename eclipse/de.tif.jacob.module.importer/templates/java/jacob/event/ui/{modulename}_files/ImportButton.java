/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon May 04 17:49:03 CEST 2009
 */
package jacob.event.ui.{modulename}_files;

import jacob.model.{Modulename}_definition;
import jacob.model.{Modulename}_files;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ImportButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ImportButton extends IButtonEventHandler 
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
	    IDataTableRecord converterRecord = context.getDataTable({Modulename}_definition.NAME).getSelectedRecord();
	    IDataTableRecord currentRecord = context.getSelectedRecord();
	    IDataTransaction trans = currentRecord.getCurrentTransaction();

	    try
	    {
	      byte[] doc = currentRecord.getDocumentValue({Modulename}_files.excel).getContent();
	      String xml = converterRecord.getSaveStringValue({Modulename}_definition.xml);
	      String name = currentRecord.getDocumentValue({Modulename}_files.excel).getName();
	      ExcelImporter importer = new ExcelImporter(name, doc);
	      importer.read(context, name, new ByteArrayInputStream(doc),xml, trans);
	      currentRecord.setValue(trans, {Modulename}_files.state, {Modulename}_files.state_ENUM._imported);
	      currentRecord.setValue(trans, {Modulename}_files.log, importer.getLog());
	
	      IInFormLongText text = (IInFormLongText)context.getGroup().findByName("protocolArea");
	      text.setValue(importer.getLog());
	      
	      if (importer.hasErrors())
	        throw new UserException("Unable to import the data. See the log for more details");
	      
	      trans.commit();
	      
	      alert("Import successfully.");
	    }
	    catch (IOException e)
	    {
	      alert("Invalid file format");
	    }
	    finally
	    {
	      trans.close();
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

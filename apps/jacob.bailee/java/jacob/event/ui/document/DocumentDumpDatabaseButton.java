/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Feb 01 10:03:23 CET 2010
 */
package jacob.event.ui.document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;

import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Document;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;


/**
 * The event handler for the DocumentDumpDatabaseButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentDumpDatabaseButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DocumentDumpDatabaseButton.java,v 1.1 2010/02/08 16:23:38 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
    if(context.isDemoMode())
    {
      alert("not allowed in demo mode");
      return;
    }

    try
    {
      IApplicationDefinition app = context.getApplicationDefinition();
      IDataTable documentTable = context.getDataTable(Document.NAME);
      documentTable.search();
      for(int i=0;i<documentTable.recordCount();i++)
      {
        IDataTableRecord tableRecord = documentTable.getRecord(i);
        String pkey = tableRecord.getSaveStringValue(Document.pkey);
        DataDocumentValue doc= tableRecord.getDocumentValue(Document.file);
        String tag = tableRecord.getSaveStringValue(Document.tag);
        String sender = tableRecord.getSaveStringValue(Document.owner_email);
        String documentPath = Bootstrap.getApplicationRootPath()+"application/"+app.getName()+"/"+app.getVersion().toShortString()+"/dump/"+pkey+".document";
        String infoPath     = Bootstrap.getApplicationRootPath()+"application/"+app.getName()+"/"+app.getVersion().toShortString()+"/dump/"+pkey+".description";
        
        FileUtils.writeByteArrayToFile(new File(documentPath),doc.getContent());

        Properties props = new Properties();
        props.setProperty("file-name", doc.getName());
        props.setProperty("tag", tag);
        props.setProperty("sender", sender);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        props.store(stream, "");
        FileUtils.writeByteArrayToFile(new File(infoPath), stream.toByteArray());
      }
    }
    catch(Exception exc)
    {
      exc.printStackTrace();
    }
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
		// You can enable/disable the button in relation to your conditions.
		//
		//emitter.setEnable(true/false);
	}
}

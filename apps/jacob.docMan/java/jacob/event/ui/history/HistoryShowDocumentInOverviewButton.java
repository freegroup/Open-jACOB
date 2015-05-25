/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 13 14:17:55 CEST 2010
 */
package jacob.event.ui.history;

import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.BoUtil;
import jacob.model.Bo;
import jacob.model.History;

import org.apache.commons.logging.Log;


/**
 * The event handler for the HistoryShowDocumentInOverviewButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class HistoryShowDocumentInOverviewButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: HistoryShowDocumentInOverviewButton.java,v 1.1 2010-09-17 08:42:25 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

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
		String aliasName = currentRecord.getSaveStringValue(History.related_alias);
		String serializedPkey = currentRecord.getSaveStringValue(History.record_serialized_pkey);
		
		IDataTable table = context.getDataTable(aliasName);
		
    IDataKeyValue pkeyvalue = table.getTableAlias().getTableDefinition().getPrimaryKey().convertStringToKeyValue(serializedPkey);
    
    try
    {
      IDataTableRecord record = table.loadRecord(pkeyvalue);
      IDataTableRecord boRecord = BoUtil.findByPkey(context, record.getStringValue(Bo.pkey));
      context.setCurrentForm("documents", "documents_overview");
      context.getGUIBrowser().add(context, boRecord, true);
    }
    catch (RecordNotFoundException e)
    {
      alert("Dokument wurde zwischenzeitlich gelöscht");
    }
	}
}


/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 01 11:06:26 CET 2007
 */
package jacob.event.ui.object;

import jacob.common.AppLogger;
import jacob.model.Attribute;
import jacob.model.Object_to_attribute;

import java.util.Properties;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ObjectRemoveAttributeButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ObjectRemoveAttributeButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ObjectRemoveAttributeButton.java,v 1.1 2007/03/01 14:42:45 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	class Callback implements IGridTableDialogCallback
	{
		IDataTable records;
		public Callback(IDataTable records) 
		{
			this.records = records;
		}

		public void onSelect(IClientContext context, int index, Properties values) throws Exception 
		{
      IDataTransaction trans = context.getDataAccessor().newTransaction();
			try 
			{
				// es muss dem Defect jetzt das Audit zugewisen werden und der Auditrecord muss
				// wieder aktualisiert werden
				//
				IDataTableRecord record = records.getRecord(index);
				record.delete(trans);
				trans.commit();
				context.getDataAccessor().propagateRecord(context.getSelectedRecord(), Filldirection.BOTH);
			} 
			finally
			{
				trans.close();
			}			
		}
	}
	
	
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
		// Alle Defect finden welchge noch keinem audit zugeornet sind
		// und noch den status [new, open] haben.
		//
		
		IDataAccessor acc= context.getDataAccessor();
		IDataTable linkTable = acc.getTable(Object_to_attribute.NAME);
		linkTable.qbeSetValue(Object_to_attribute.object_key, currentRecord.getSaveStringValue(jacob.model.Object.pkey));
		if(linkTable.search()>0)
		{
			IGridTableDialog dialog = context.createGridTableDialog(button, new Callback(linkTable) );
			dialog.setHeader(new String[]{"Attribute Name"});
			String[][] data = new String[linkTable.recordCount()][1];
			for (int i = 0; i < linkTable.recordCount(); i++) 
			{
				IDataTableRecord record = linkTable.getRecord(i);
				IDataTableRecord attributeRecord = record.getLinkedRecord(Attribute.NAME);
				
				data[i][0]=attributeRecord.getSaveStringValue(Attribute.name);
			}
			dialog.setData(data);
			dialog.show();
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
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}


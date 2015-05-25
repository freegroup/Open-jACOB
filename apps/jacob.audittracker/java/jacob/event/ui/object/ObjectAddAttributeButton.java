/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Mar 01 11:06:20 CET 2007
 */
package jacob.event.ui.object;

import jacob.common.AppLogger;
import jacob.common.AttributeManager;
import jacob.model.Attribute;
import jacob.model.Object_to_attribute;

import java.util.ArrayList;
import java.util.List;
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
 * The event handler for the ObjectAddAttributeButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ObjectAddAttributeButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ObjectAddAttributeButton.java,v 1.1 2007/03/01 14:42:45 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	class ExistsCallback implements IGridTableDialogCallback
	{
		private final List records;
		private final IDataTableRecord object;
		
		public ExistsCallback(List records, IDataTableRecord object) 
		{
			this.records = records;
			this.object  = object;
		}

		public void onSelect(IClientContext context, int index, Properties values) throws Exception 
		{
			IDataTransaction trans = context.getDataAccessor().newTransaction();
			try 
			{
				// es muss dem Object jetzt das Attribute zugewisen werden und der Objectrecord muss
				// wieder aktualisiert werden
				//
				IDataTableRecord attribute = (IDataTableRecord)records.get(index);
				IDataTable obj2attrTable = context.getDataTable(Object_to_attribute.NAME);
				IDataTableRecord linkRecord = obj2attrTable.newRecord(trans);
				linkRecord.setLinkedRecord(trans, object);
				linkRecord.setLinkedRecord(trans, attribute);
				trans.commit();
				context.getDataAccessor().propagateRecord(context.getSelectedRecord(), Filldirection.BOTH);
			} 
			finally
			{
				trans.close();
			}			
		}
	}
	
	class NewCallback implements IGridTableDialogCallback
	{
		private final List records;
		private final IDataTableRecord object;
		private final IDataTransaction trans;

		public NewCallback(IDataTransaction currentTransaction, List records, IDataTableRecord object) 
		{
			this.records = records;
			this.object  = object;
			this.trans   = currentTransaction;
		}

		public void onSelect(IClientContext context, int index, Properties values) throws Exception 
		{
			// es muss dem Object jetzt das Attribute zugewisen werden und der Objectrecord muss
			// wieder aktualisiert werden
			//
			IDataTableRecord attribute = (IDataTableRecord)records.get(index);
			IDataTable obj2attrTable = context.getDataTable(Object_to_attribute.NAME);
			IDataTableRecord linkRecord = obj2attrTable.newRecord(trans);
			linkRecord.setLinkedRecord(trans, object);
			linkRecord.setLinkedRecord(trans, attribute);
			context.getDataAccessor().propagateRecord(context.getSelectedRecord(), Filldirection.BOTH);
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
		List currentAttributePkeys = AttributeManager.getAttributePkeys(context,currentRecord);
		
		// neuen DataAccessor holen um die Daten im UI nicht zu verändern/beeinflussen
		//
		IDataAccessor acc= context.getDataAccessor().newAccessor();
		IDataTable attributeTable = acc.getTable(Attribute.NAME);
		
		attributeTable.search();
		List recordsToShow = new ArrayList();
		for (int i = 0; i < attributeTable.recordCount(); i++) 
		{
			IDataTableRecord record = attributeTable.getRecord(i);
			if(!currentAttributePkeys.contains(record.getSaveStringValue(Attribute.pkey)))
				recordsToShow.add(record);
		}

		if(recordsToShow.size()>0)
		{
			IGridTableDialog dialog = null;
			
			if(currentRecord.isNew())
				dialog = context.createGridTableDialog(button, new NewCallback(currentRecord.getCurrentTransaction(), recordsToShow, currentRecord) );
			else
				dialog = context.createGridTableDialog(button, new ExistsCallback(recordsToShow, currentRecord) );
			
			dialog.setHeader(new String[]{"Attribute Name"});
			String[][] data = new String[recordsToShow.size()][1];
			for (int i = 0; i < recordsToShow.size(); i++) 
			{
				IDataTableRecord record =(IDataTableRecord) recordsToShow.get(i);
				data[i][0]=record.getSaveStringValue(Attribute.name);
			}
			dialog.setData(data);
			dialog.show();
		}
		else
		{
			alert("No unassigned Defects found.");
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


/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Oct 27 12:52:21 CEST 2006
 */
package jacob.event.ui.audit;

import java.util.Properties;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.dialogs.IGridTableDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.model.Audit;
import jacob.model.Defect;

import org.apache.commons.logging.Log;


/**
 * The event handler for the AuditRecordSelectedButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class AuditRecordSelectedButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: AuditRecordSelectedButton.java,v 1.1 2007/11/25 22:17:54 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	class ExistsCallback implements IGridTableDialogCallback
	{
		IDataTable records;
		public ExistsCallback(IDataTable records) 
		{
			this.records = records;
		}

		public void onSelect(IClientContext context, int index, Properties values) throws Exception 
		{
			IDataTransaction trans = new DataTransaction();
			try 
			{
				// es muss dem Defect jetzt das Audit zugewisen werden und der Auditrecord muss
				// wieder aktualisiert werden
				//
				IDataTableRecord record = records.getRecord(index);
				record.setValue(trans, Defect.audit_key, context.getSelectedRecord().getSaveStringValue(Audit.pkey));
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
		private final IDataTable records;
		private final IDataTransaction trans;

		public NewCallback(IDataTransaction currentTransaction, IDataTable defectTable) 
		{
			records = defectTable;
			trans   = currentTransaction;
		}

		public void onSelect(IClientContext context, int index, Properties values) throws Exception 
		{
				// es muss dem Defect jetzt das Audit zugewisen werden und der Auditrecord muss
				// wieder aktualisiert werden
				//
				IDataTableRecord record = records.getRecord(index);
				record.setValue(trans, Defect.audit_key, context.getSelectedRecord().getSaveStringValue(Audit.pkey));
				context.getDataAccessor().propagateRecord(context.getSelectedRecord(), Filldirection.BOTH);
		}
	}
	
	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
		
		// neuen DataAccessor holen um die Daten im UI nicht zu verändern/beeinflussen
		//
		IDataAccessor acc= context.getDataAccessor().newAccessor();
		IDataTable defectTable = acc.getTable(Defect.NAME);
		defectTable.qbeSetValue(Defect.audit_key, "null");
		defectTable.qbeSetValue(Defect.state, "new|open");
		if(defectTable.search()>0)
		{
			IGridTableDialog dialog = null;
			
			if(currentRecord.isNew())
				dialog = context.createGridTableDialog(button, new NewCallback(currentRecord.getCurrentTransaction(), defectTable) );
			else
				dialog = context.createGridTableDialog(button, new ExistsCallback(defectTable) );
			
			dialog.setHeader(new String[]{"Nr","Angelegt am","Betreff"});
			String[][] data = new String[defectTable.recordCount()][3];
			for (int i = 0; i < defectTable.recordCount(); i++) 
			{
				IDataTableRecord record = defectTable.getRecord(i);
				data[i][0]=record.getSaveStringValue(Defect.pkey);
				data[i][1]=record.getSaveStringValue(Defect.create_date);
				data[i][2]=record.getSaveStringValue(Defect.subject);
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
		button.setEnable(status != IGuiElement.SEARCH);
		((IButton)button).setIcon(Icon.add);
		((IButton)button).setLabel("");
	}
}


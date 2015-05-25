/*
 * Created on 18.08.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import jacob.common.AppLogger;
import jacob.common.Call;
import jacob.common.data.DataUtils;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;
/**
 * erstellt eine Untermeldung<br>
 * Möglich nur im Status Angenommen und wenn selbst keine Untemeldung ist.
 * @author mike
 *
 */
public class CallSubCall extends IButtonEventHandler
{
	static protected final transient Log logger = AppLogger.getLogger();
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataAccessor accessor = context.getDataAccessor();
		IDataTableRecord oldcall = context.getSelectedRecord();
		IDataTable callTable = context.getDataTable();
//		context.clearGroup();
		IDataTransaction newTrans = callTable.startNewTransaction();
		IDataTableRecord newcall = callTable.newRecord(newTrans);
		
		if (logger.isDebugEnabled())
			logger.debug("Untermeldung mit pkey = "+newcall.getStringValue("pkey"));
		
		// Wenn der Mastercall einen Location-Datensatz hat
		if (oldcall.hasLinkedRecord("location"))
		{		
			// dann diesen clonen und in einer eigenen Transaktion sofort committen, da ansonsten propagateRecord() nicht
			// funktioniert.
			// IBIS: propagateRecord() auch für nicht committed Records realisiert (zumindest für die Backward Richtung
			// sollte dies nicht schwierig sein)
			IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
			try
			{
				IDataTableRecord oldLocationRecord = oldcall.getLinkedRecord("location");
				IDataTableRecord newLocationRecord = accessor.cloneRecord(currentTransaction, oldLocationRecord, "location");
				currentTransaction.commit();
				newcall.setLinkedRecord(newTrans,newLocationRecord);			
			}
			finally
			{
				currentTransaction.close();
			}
		}
		// linktable ist notwendig, da der Melder Record bei SetDefault benutzt wird!
		//newcall.setValue(newTrans,"employeecall",context.getUser().getKey());
		String userKey = context.getUser().getKey();
		DataUtils.linkTable(context,newTrans,newcall,"employeecall","customerint","pkey",userKey);
		logger.debug("customerint verlinkt");

		
		Call.setDefault(context,newcall,newTrans);
		newcall.setValue(newTrans,"mastercall_key",oldcall.getValue("pkey"));
		newcall.setValue(newTrans,"priority",oldcall.getValue("priority"));
		newcall.setValue(newTrans,"accountingcode_key", oldcall.getValue("accountingcode_key"));
		newcall.setValue(newTrans,"categorycall", oldcall.getValue("categorycall"));
		newcall.setValue(newTrans,"object_key", oldcall.getValue("object_key"));
		accessor.propagateRecord(newcall,accessor.getApplication().getRelationSet("r_call"), Filldirection.BOTH);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
	{
		if(status == IGuiElement.SELECTED)
		{
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String callstatus = currentRecord.getStringValue("callstatus");
			String mastercall = currentRecord.getStringValue("mastercall_key");
			button.setEnable(callstatus.equals("Angenommen") && mastercall == null);
		}
	}
}

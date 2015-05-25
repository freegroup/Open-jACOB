/*
 * Created on 30.08.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_entry.callEntryCaretaker;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.common.sap.CWriteSAPExchange;
import jacob.common.sap.CheckCallChange;
import jacob.exception.BusinessException;
/**
 * 
 * @author mike
 *
 */
public class CallClosedforNMAK extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CallClosedforNMAK.java,v 1.3 2008/05/30 08:42:55 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
	static protected final transient Log logger = AppLogger.getLogger();
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
  private static final Set validStatus = new HashSet();
  static 
  {
    validStatus.add("Durchgestellt");
    validStatus.add("AK zugewiesen");
    validStatus.add("Dokumentiert");
  }
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
	 IDataTableRecord callRec = context.getSelectedRecord();
	 if (!callRec.hasLinkedRecord("callworkgroup"))
	 {
	 	alert("Der Meldung muß ein AK zugewiesen sein.");
	 	return;
	 }
	 IDataTableRecord workgroup = callRec.getLinkedRecord("callworkgroup");
	 if ("Ja".equals(workgroup.getStringValue("migration")))
	 {
	 	alert("Der AK ist schon migriert. Schließen nicht möglich");
	 	return;
	 }
	 // firstlevelclosedCall versendet keine Nachrichten und der Status geht gleich auf
	 // dokumentiert, also anderen Alias verwenden, der keine Businessregel benutzt
		IDataAccessor accessor = callRec.getAccessor().newAccessor();
		IDataTable callTable = accessor.getTable("callmaster");
		callTable.qbeClear();
		callTable.qbeSetKeyValue("pkey",callRec.getStringValue("pkey"));
		callTable.search();
		IDataTableRecord callmasterRec = callTable.getRecord(0);
		IDataTransaction transaction = accessor.newTransaction();
		try
		{
			if (callmasterRec.getValue("dateassigned")==null)
			{
				callmasterRec.setValue(transaction, "dateassigned", "now");
			}
			if (callmasterRec.getValue("dateowned")==null)
			{
				callmasterRec.setValue(transaction, "dateowned", "now");
			}
			if (callmasterRec.getValue("dateresolved")==null)
			{
				callmasterRec.setValue(transaction, "dateresolved", "now");
			}

			callmasterRec.setValue(transaction, "callstatus", "Dokumentiert");
			callmasterRec.setValue(transaction, "datedocumented", "now");
			callmasterRec.setValue(transaction, "closedby_sd", "1");
			callmasterRec.setValue(transaction, "coordinationtime", "0");
			transaction.commit();
      //Die Änderung wurde nicht durch den scheduled Job "SAPExchange" erzeugt,
      //daher müssen sie an SAP Übertragen werden
      CWriteSAPExchange.updateSaveExchangeCall(callmasterRec); 
      //Wenn der Call geschlossen wurde, auch in SAP schließen
      //Das ist ein anderer Funktionsaufruf, daher erst die Änderungen
      //übertragen und dann schließen.
      CWriteSAPExchange.closeSaveExchangeCall(callmasterRec);  
			// den datensatz in der GUI refreshen!
			IRelationSet myRelation  = context.getApplicationDefinition().getRelationSet("r_call_entry");
			context.getDataAccessor().propagateRecord(callRec,myRelation,Filldirection.BOTH);

		}
		catch (RecordLockedException ex)
		{
			logger.debug(ex.toString());
			// Pech gehabt: die Meldung ist gerade gesperrt
		}
		catch (BusinessException ex)
		{
			logger.debug(ex.toString());
			// Pech gehabt: die Meldung läßt sich nicht dokumentieren

		}
		finally
		{
			transaction.close();
		}
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
			
			button.setEnable(validStatus.contains(callstatus));
		}
	}
}

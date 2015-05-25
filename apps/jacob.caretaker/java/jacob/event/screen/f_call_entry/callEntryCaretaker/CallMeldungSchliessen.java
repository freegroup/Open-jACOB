/*
 * Created on 31.08.2004
 * by mike
 *
 */
package jacob.event.screen.f_call_entry.callEntryCaretaker;

import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.common.gui.call.CallUpdate;
import jacob.exception.BusinessException;
import jacob.model.Calls;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
/**
 * 
 * @author mike
 *
 */
public class CallMeldungSchliessen extends IActionButtonEventHandler
{
	 static public final transient String RCS_ID = "$Id: CallMeldungSchliessen.java,v 1.13 2008/03/16 03:22:20 achim Exp $";
	  static public final transient String RCS_REV = "$Revision: 1.13 $";
		static protected final transient Log logger = AppLogger.getLogger();
		/* (non-Javadoc)
		 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
		 */
	  private static final Set validStatus = new HashSet();
	  static 
	  {
	    validStatus.add("Durchgestellt");
	    validStatus.add("AK zugewiesen");
	   // validStatus.add("Dokumentiert");
	  }
		public static class AskCallback implements IAskDialogCallback
		{
      IDataTableRecord callRec;
      
      AskCallback(IDataTableRecord callRec)
      {
        this.callRec = callRec;
      }
			/* Do noting if the users cancel the AskDialog */
			public void onCancel(IClientContext context)
			{
			}
			/**
			 * wenn ein Koordinationsaufwand angegeben wird, dann dem AK Aud Servicedesk setzen <br>
			 * (wenn nicht Wartenmitarbeiter) und Meldung dokumentieren 
			 * @param context
			 * @param coordtime
			 */
			/* Called if the user press [ok] in the AskDialog */
			public  void onOk(IClientContext context, String coordtime) throws Exception
			{
				int minutes=0;
				 // String in Integer umwandeln
				 try
				 {
				 	 minutes = Integer.parseInt(coordtime);
				 }
				 catch (NumberFormatException e)
				 {
				 	throw new BusinessException("Koordinationsaufwand ist ungültig");
				 }
				 
				 
				 IDataTransaction trans = callRec.getCurrentTransaction();
				 IDataTransaction transaction = trans!=null?trans:context.getDataAccessor().newTransaction();

						// Ak setzen
						if (!context.getUser().hasRole("CQ_WARTE")|| callRec.getValue("workgroupcall")== null)
						{
							String SD_Key = DataUtils.getAppprofileValue(context,"callworkgroup_key");
							DataUtils.linkTable(context,transaction,callRec,"workgroupcall","callworkgroup","pkey",SD_Key);
						}
						// Koordinationsaufwand speichern
						callRec.setLongValue(transaction, "coordinationtime", minutes*60);


						callRec.setLongValue(transaction, "coordinationtime_h", minutes / 60);
  					callRec.setLongValue(transaction, "coordinationtime_m", minutes % 60);
                    if (callRec.getValue("datereported")==null)
                    {
                        callRec.setValue(transaction, "datereported", "now");
                    }
						if (callRec.getValue("dateassigned")==null)
						{
							callRec.setValue(transaction, "dateassigned", "now");
						}
						if (callRec.getValue("dateowned")==null)
						{
							callRec.setValue(transaction, "dateowned", "now");
						}
						if (callRec.getValue("dateresolved")==null)
						{
							callRec.setValue(transaction, "dateresolved", "now");
						}
						if (callRec.getValue("callbackmethod")==null)
						{
							callRec.setValue(transaction, "callbackmethod", "Keine");
						}

						callRec.setValue(transaction, "callstatus", "Dokumentiert");
						callRec.setValue(transaction, "datedocumented", "now");
						// Flag setzen firstlevelclosedcall
						callRec.setValue(transaction, "firstlevelclosed", "1");
						
						// Check ob alle Felder gefüllt sind
						if (CallUpdate.checkValues(context,callRec))
            {
              jacob.event.data.CallTableRecord.checkCallLinks(callRec, transaction);
              jacob.event.data.CallTableRecord.checkValueChange(callRec, transaction);
                    if (jacob.event.data.CallTableRecord.checkSL_KSL(callRec, transaction))
                    {
                      jacob.event.data.CallTableRecord.writeSLOverdueToHistory(callRec, transaction);
                    }
              jacob.event.data.CallTableRecord.findSL_KSL(callRec, transaction) ;
						 		transaction.commit();
                transaction.close();
            }
						
						// den datensatz in der GUI refreshen!
						IRelationSet myRelation  = context.getApplicationDefinition().getRelationSet("r_call_entry");
						context.getDataAccessor().propagateRecord(callRec,myRelation,Filldirection.BOTH);
						
			}
		}
   
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement button) throws Exception
	{
		button.setLabel("1st Level schließen");
		if(status != IGuiElement.SEARCH && status != IGuiElement.UNDEFINED )
		{
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String callstatus = currentRecord.getStringValue("callstatus");
			
			button.setEnable(validStatus.contains(callstatus));
		}
		else button.setEnable(false);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IAskDialog dialog = context.createAskDialog( "Wie groß war der Koordinationsaufwand in Minuten?","0",new AskCallback(currentRecord));
		dialog.show();
		return false;
	}
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{

	}
}


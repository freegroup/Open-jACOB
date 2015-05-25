/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;


import jacob.common.AppLogger;
import jacob.common.Call;
import jacob.common.data.DataUtils;

import java.util.Map;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
/**
 * Der Status der Meldung wird auf "Fertig gemeldet" gesetzt <br>
 * Wenn kein Auftrag vorhanden ist, dann wird sicherheitshalber <br>
 * gefragt, ob man einen anlegen möchte. <br>
 * Wenn ein Auftrag vorhanden ist, dann wird nur eine Sicherheitsabfrage gemacht.<br>
 * ist der User eun Agent,dann wir auch "closedby_sd" gesetzt
 * 
 * @author mike
 *
 */
public class CallSetResolved extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallSetResolved.java,v 1.16 2005/12/13 12:44:01 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.16 $";
	
	static protected final transient Log logger = AppLogger.getLogger();
  
	public static void setStatusResolved(IClientContext context,  IDataTableRecord call)throws Exception
	{
		IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
		try
		{
			call.setValue(currentTransaction, "callstatus", "Fertig gemeldet");
			String domainName = context.getDomain().getName();
			if (context.getUser().hasRole("CQ_AGENT") && ("f_call_entry".equals(domainName) || "f_call_manage".equals(domainName) ))
			{
				call.setValue(currentTransaction, "closedby_sd","1");
			}
			else 
			{
				call.setValue(currentTransaction, "closedby_sd","0");
			}
			currentTransaction.commit();
		}
		finally
		{
			currentTransaction.close();
		}
	}
	/**
	 * Hier kommt der Callback der Sicherheitsabfrage wenn noch kein Auftrag vorhanden ist.
	 * @author mike
	 *
	 */
	class DialogCallback implements IFormDialogCallback
	{
        private IDataTableRecord call;
        
        public DialogCallback(IDataTableRecord call )
        {
            this.call = call;
        }
		/* (non-Javadoc)
		 * @see de.tif.jacob.screen.dialogs.form.IFormDialogCallback#onSubmit(de.tif.jacob.screen.IClientContext, java.lang.String, java.util.Map)
		 */
		public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
		{
			if ("no".equals(buttonId))
			{
				CallSetResolved.setStatusResolved(context,call);
				return;
			}
			// Sprung zu Auftrag anlegen und Maske vorbereiten!
			String priority = call.getStringValue("priority");
			IDataTable task = context.getDataTable("task");
			if (task.recordCount()!=1)
			{
				context.getDomain().setInputFieldValue("taskadd","taskPriority",priority);
			}
			context.setCurrentForm("taskadd");

		}	
	}
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
        IDataTableRecord callRecord = context.getSelectedRecord();
        if (!Call.accessallowed(callRecord))
        {
            IMessageDialog dialog = context.createMessageDialog("Sie haben keinen schreibenden Zugriff auf diese Meldung");
            dialog.show();
            return ;
        }


		// wenn es einen Auftrag gibt, dann nur fragen ob User wirklich fertigmelden will
		if (DataUtils.inDatabase(callRecord,"task","calltask",callRecord.getStringValue("pkey")))
		{
			// fragen ob angenommen werden soll
	    IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Wollen Sie die Meldung wirklich fertig melden?",new IOkCancelDialogCallback()
	    {
	      public void onOk(IClientContext context) throws Exception
	      {      
	        // wenn OK, dann den Status setzen
	      	setStatusResolved(context,context.getSelectedRecord());
	      }
	      public void onCancel(IClientContext context) throws Exception {}
	    });
	    
	    okCancelDialog.show();
	    return;
		}
		
		// Meldung hat keine Auftrag, also fragen ob User einen anlegen möchte
    CellConstraints cc= new CellConstraints();
    FormLayout  layout = new FormLayout("60dlu,260dlu,10dlu",  // columns
		"10dlu,p,2dlu,p,10dlu");      // rows
 
    // Dialog samt Callback-Klasse anlegen
    //
    IFormDialog dialog=context.createFormDialog("Kein Auftrag",layout,new DialogCallback(callRecord));
    // Elemente in den Dialog einfügen
    //
    dialog.addLabel("Diese Meldung hat keinen Auftrag.",cc.xy(1,1));   
    dialog.addLabel("Wollen Sie einen Auftrag anlegen?",cc.xy(1,3));
    dialog.setCancelButton("Abbruch");
    dialog.addSubmitButton("yes","Ja");
    dialog.addSubmitButton("no","Nein");

    
   
    // Dialog anzeigen
    //
    dialog.show(330,100);
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		if(status == IGuiElement.SELECTED)
		{
			IDataTableRecord currentRecord = context.getSelectedRecord();
			String callstatus = currentRecord.getStringValue("callstatus");
			button.setEnable(callstatus.equals("Angenommen"));
		}
	}
}

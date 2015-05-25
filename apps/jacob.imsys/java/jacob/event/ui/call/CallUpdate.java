/*
 * Created on 23.07.2004
 *
 */
package jacob.event.ui.call;

import org.apache.commons.lang.StringUtils;

import jacob.common.Call;
import jacob.common.data.DataUtils;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * @author achim
 *  
 */
public class CallUpdate extends IActionButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: CallUpdate.java,v 1.1 2005/06/03 15:18:54 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public static boolean checkValues(IClientContext context) throws Exception
	{
		IDataTableRecord callrecord = context.getSelectedRecord();
		String errmsg = "";

		if (context.getGroup().getDataStatus() == IGuiElement.SELECTED)
		{
			if (!Call.accessallowed(callrecord))
			{
				IMessageDialog dialog = context.createMessageDialog("Sie haben keinen schreibenden Zugriff auf diese Meldung");
				dialog.show();
				return false;
			}

		}
		else
		{
			// Überprüfung ob Kundenrückmeldung gesetzt ist
			if (StringUtil.toSaveString(callrecord.getStringValue("callbackmethod")).length() == 0)
				errmsg += "Sie müssen die Art der Kundenrückmeldung angeben.\\r\\n";

			//AK muss gefüllt sein
			if (StringUtil.toSaveString(callrecord.getStringValue("workgroupcall")).length() == 0)
				errmsg += "Der Meldung muß ein AK zugewiesen sein.\\r\\n";

			// Gewerk verlinken
			IDataAccessor accessor = context.getDataAccessor();
			if (!DataUtils.linkThroughTable(accessor, callrecord, "categorycall", "category", "pkey"))
				errmsg += "Die Meldung muß einem Gewerk zugeordnet sein.\\r\\n";

			// Tätigkeit verlinken
			if (!DataUtils.linkThroughTable(accessor, callrecord, "process_key", "process", "pkey"))
				errmsg += "Die Meldung muß einer Tätigkeit zugeordnet sein.\\r\\n";

			// Kunden verlinken
			if (!DataUtils.linkThroughTable(accessor, callrecord, "employeecall", "customerint", "pkey"))
				errmsg += "Die Meldung muß einem Kunden zugeordnet sein.\\r\\n";
            
            // custtext vorhanden?
            if (callrecord.getSaveStringValue("custtext").length()<2)
                errmsg += "Die Meldung muß einen Kundentext haben.\\r\\n";

            // Kunden verlinken
            if (callrecord.getValue("estimateddone")==null)
                errmsg += "Sie müssen ein 'voraussicht. Bearbeitungsende' der Meldung angeben.\\r\\n";

			if (errmsg.length() > 0)
			{
				IMessageDialog dialog = context.createMessageDialog(errmsg);
				dialog.show();
				return false;
			}

			// Gruppenruf

			// Wenn sich der AK geändert hat oder modus neu ist
			if (callrecord.hasChangedValue("workgroupcall") || context.getGroup().getDataStatus() == IGuiElement.NEW)
			{
				//Nur wenn eine Arbeitsgruppe vorhanden
				if (StringUtil.toSaveString(callrecord.getStringValue("workgroupcall")).length() != 0)
				{

					IDataTableRecord akrecord = callrecord.getLinkedRecord("callworkgroup");
					// Überprüfung ob bei diesem AK Gruppenruf notwendig ist
					if ("Ja".equals(akrecord.getStringValue("groupconferencecall")))
					{
						String msg = ""; // Bildschirmausgabe
						String msgAction = "Gruppenruf unter: "; // Ausgabe in Feld
																										 // "Aktion"
						msgAction += akrecord.getSaveStringValue("phone") + "; ";
						// Unterscheidung zwischen Normaler/Hauptmeldung und Untermeldung
						if (callrecord.getSaveStringValue("mastercall_key").length() == 0)
						{
							msg = "Bitte starten Sie einen Gruppenruf mit der Nummer:\\r\\n\\r\\n";
							msg += akrecord.getSaveStringValue("phone");
						}
						else
						{
							msg = "Gruppenruf: Bitte benachrichtigen Sie den AK (Tel. ";
							msg += akrecord.getSaveStringValue("phone") + " )\\r\\n";
							msg += "oder rufen Sie den Service Desk (Tel. 47111) an und teilen Sie die Nummer der\\r\\n";
							msg += "Untermeldung mit. ( " + callrecord.getSaveStringValue("pkey") + " )";
						}
						//Gruppenruf anzeigen und in Aktion schreiben
						IMessageDialog msgdialog = context.createMessageDialog(msg);
						msgdialog.show();
						msgAction += callrecord.getSaveStringValue("action");
						msgAction = StringUtils.left(msgAction, 240);
						callrecord.setValue(callrecord.getCurrentTransaction(), "action", msgAction);
					}
				}
			}
		}

		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		return checkValues(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement.GroupState,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
	}


}

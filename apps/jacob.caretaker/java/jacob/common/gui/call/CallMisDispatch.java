/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;

import jacob.common.Call;
import jacob.common.data.DataUtils;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class CallMisDispatch extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CallMisDispatch.java,v 1.12 2005/03/11 12:27:59 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.12 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button)throws Exception
	{
        IDataTableRecord callrecord = context.getSelectedRecord();
        if (!Call.accessallowed(callrecord))
        {
            IMessageDialog dialog = context.createMessageDialog("Sie haben keinen schreibenden Zugriff auf diese Meldung");
            dialog.show();
            return ;
        }

		IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
		try
		{
			IDataTableRecord call = context.getSelectedRecord();
			call.setValue(currentTransaction, "callstatus", "Fehlgeroutet");
			// Problemmanager setzen
			String problemmanagerKey = DataUtils.getAppprofileValue(context,"problemmanager_key");
			DataUtils.linkTable( context, currentTransaction, call,"workgroupcall", "callworkgroup", "pkey", problemmanagerKey);
			currentTransaction.commit();
		}
		finally
		{
			currentTransaction.close();
		}
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
			button.setEnable(callstatus.equals("AK zugewiesen"));
		}
	}
}

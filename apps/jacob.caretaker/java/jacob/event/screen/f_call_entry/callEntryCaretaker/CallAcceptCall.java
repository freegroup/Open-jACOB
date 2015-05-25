/*
 * Created on 31.08.2004
 *
 */
package jacob.event.screen.f_call_entry.callEntryCaretaker;

import jacob.common.AppLogger;
import jacob.exception.BusinessException;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author achim
 *
 */
public class CallAcceptCall extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: CallAcceptCall.java,v 1.3 2005/03/17 09:39:41 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
	static protected final transient Log logger = AppLogger.getLogger();
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
  
	private static final Set validStatus = new HashSet();
  static 
  {
    validStatus.add("AK zugewiesen");
    validStatus.add("Fehlgeroutet");
  }
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	/**
	 * Callback class für den AskDialog bei Meldung für AK Annehmen.
	 * Es wird abgefragt wer angerufen hat, und anschließend der Stus auf Angenommen gesetzt sowie 
	 * der Anrufer in die Historie geschrieben.
	 */
	public static class AskCallback implements IAskDialogCallback
	{
		/* Do noting if the users cancel the AskDialog */
		public void onCancel(IClientContext context)
		{
		}
		/* Called if the user press [ok] in the AskDialog */
		public void onOk(IClientContext context, String owner) throws Exception
		{
			IDataTransaction trans = context.getDataAccessor().newTransaction();
			try
			{
				
				if (owner.length()==0) 
				{
					throw new BusinessException("Bitte tragen Sie ein, für wen die Meldung angenommen werden soll!");
				}
				IDataTableRecord call = context.getSelectedRecord();
				call.setValue(trans,"callstatus","Angenommen");
				call.appendToHistory(trans," Meldung telefonisch angenommen von: " + owner);
				trans.commit();
			}
			finally
			{
				trans.close();
			}
		}
	}
  public void onAction(IClientContext context, IGuiElement button)
			throws Exception {
    IAskDialog dialog = context.createAskDialog( "Für wen soll die Meldung angenommen werden?",new AskCallback());
		dialog.show();

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

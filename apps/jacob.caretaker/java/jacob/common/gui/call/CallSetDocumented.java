/*
 * Created on 17.08.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import jacob.common.Call;
import jacob.common.data.DataUtils;
import jacob.common.gui.VoteDialog;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IMessageDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Setzt den Status aud Dokumentiert und überprüft <br>
 * ob die Meldung bewertet werden muss.
 * 
 * @author mike
 *
 */
public class CallSetDocumented extends IButtonEventHandler
{
static public final transient String RCS_ID = "$Id: CallSetDocumented.java,v 1.5 2005/03/11 12:27:59 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.5 $";
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
 
	   Call.setStatus(context,callrecord,"Dokumentiert");
	   // überprüfen ob bewertet werden muss  
	   int gradinginterval = Integer.parseInt(DataUtils.getAppprofileValue(context,"gradinginterval"));
	   // wenn gradinginterval ==0  dann keine Bewertung
	   if (gradinginterval==0) return;
	   int pkey = Integer.parseInt(context.getSelectedRecord().getStringValue("pkey"));
	   if ((pkey % gradinginterval) == 0 )
	   {
	   	VoteDialog.show(context,2,false);
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
			button.setEnable(callstatus.equals("Fertig gemeldet"));
		}

	}
}

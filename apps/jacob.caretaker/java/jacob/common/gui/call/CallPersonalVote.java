/*
 * Created on 04.08.2004
 * by mike
 *
 */
package jacob.common.gui.call;

import jacob.common.gui.VoteDialog;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Bewertung: Ein Dialog, der eine manuelle Bewertung der Meldung durch den AK ermöglicht <br>
 * 1.Eine Meldung kann nur einmal bewertet werden<br>
 * 2.Bei einer Benotung schlechter als 3 ist eine Begründung nötg<br>
 * 3 gespeichert wird die Berwertung in call.grade und call.gradestatement <br>
 * @author mike
 *
 */
public class CallPersonalVote extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: CallPersonalVote.java,v 1.8 2004/08/18 10:41:19 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.8 $";


		/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
	  VoteDialog.show(context,2,true);
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		// nur noch nicht bewertete Meldungen dürfen bewertet werden
		if (status == IGuiElement.SELECTED)
		{
			emitter.setEnable(context.getSelectedRecord().getValue("grade") == null);
		}
		else
		{
			emitter.setEnable(false);
		}
	}



}

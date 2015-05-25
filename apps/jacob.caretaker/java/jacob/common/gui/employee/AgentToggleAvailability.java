/*
 * Created on 04.08.2004
 * by mike
 *
 */
package jacob.common.gui.employee;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * Wechselt beim Agent den Flag availability
 * @author mike
 *
 */
public class AgentToggleAvailability extends IButtonEventHandler
{
static public final transient String RCS_ID = "$Id: AgentToggleAvailability.java,v 1.2 2005/03/03 15:38:15 sonntag Exp $";
static public final transient String RCS_REV = "$Revision: 1.2 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
		try
		{
			IDataTableRecord record = context.getSelectedRecord();
			if ("1".equals(record.getSaveStringValue("availability")))
			{
				record.setStringValue(currentTransaction,"availability","0");
			}
			else
			{
				record.setStringValue(currentTransaction,"availability","1");
			}
			currentTransaction.commit();
		}
		finally
		{
			currentTransaction.close();
		}
	}



	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{

	}

}

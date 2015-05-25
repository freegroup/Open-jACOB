/*
 * Created on 14.12.2004
 * by mike
 *
 */
package jacob.common.gui.object;

import jacob.common.data.DataUtils;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Objekte dürfen nur für ext.System MBTECH angelegt und geändert werden
 * 
 * @author mike
 *  
 */
public  class ObjectUpdate extends IActionButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: ObjectUpdate.java,v 1.2 2006/11/29 15:58:17 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		GroupState state = context.getForm().getDataStatus();
		if (state == IGuiElement.NEW || state == IGuiElement.UPDATE)
		{
			IDataTransaction trans = context.getSelectedRecord().getCurrentTransaction();
			DataUtils.linkTable(context.getDataAccessor(), trans, context.getSelectedRecord(), "ext_system_key", "objectext_system", "pkey", DataUtils.getAppprofileValue(context, "mbtechsystem_key"));
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext,
	 *      de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
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

		// Objektänderungen nür gültig für das MBTECH- ext.System
		if (status == IGuiElement.UPDATE || status == IGuiElement.SELECTED)
		{
			emitter.setEnable(DataUtils.getAppprofileValue(context, "mbtechsystem_key").equals(context.getSelectedRecord().getStringValue("ext_system_key")));
		}
	}
}

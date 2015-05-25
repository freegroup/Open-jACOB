/*
 * Initialisiert den Auftrag und verknüpft das externe System und Objekt des Auftrags <br>
 * aus dem Objekt der Meldung
 * Created on 20.08.2004
 * by mike
 *
 */
package jacob.common.gui.task;

import jacob.common.Task;
import jacob.common.data.DataUtils;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class TaskNew extends IActionButtonEventHandler
{
static public final transient String RCS_ID = "$Id: TaskNew.java,v 1.5 2004/09/27 13:59:37 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.5 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
		IDataTableRecord newtask = context.getSelectedRecord();
		IDataTransaction newtrans= newtask.getCurrentTransaction();
		Task.setDefault(context.getDataAccessor(),newtask,newtrans);

	}

}

/*
 * Created on 14.12.2004
 * by mike
 *
 */
package jacob.common.gui.object;

import jacob.common.data.DataUtils;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Objektort löschen und extSystem vorbelegen
 * @author mike
 *
 */
public class ObjectNew extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ObjectNew.java,v 1.2 2006/11/29 15:58:17 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		IDataTable locationTable = context.getDataTable("objectlocation");
		locationTable.clear();
		locationTable.qbeClear();
		IDataTable extsystemTable = context.getDataTable("objectext_system");
		extsystemTable.clear();
		extsystemTable.qbeClear();
		extsystemTable.qbeSetKeyValue("pkey", DataUtils.getAppprofileValue(context,"mbtechsystem_key"));
		extsystemTable.search();
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
	}

}

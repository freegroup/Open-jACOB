/*
 * Created on 15.07.2004
 * by mike
 *
 */
package jacob.common.gui.category;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Schränkt den Gewerkstatus auf "Gültig|Keine Zuordnung" ein
 * 
 * @author mike
 *
 */
public class CategorySearch extends IActionButtonEventHandler
{

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		IDataTable table = context.getDataTable();
		table.qbeSetValue("categorystatus", "Gültig|Keine Zuordnung");
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button)
	{
	
	}

	/* wenn das GUIElement categorySubcategory vorhanden ist,<br> 
	 * dann muss überprüft werden ob das Ende des Gewerkebaums erreicht ist <br>
	 * und das Flag gesetzt werden
	 * 
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		ISingleDataGuiElement checkbox =(ISingleDataGuiElement) context.getGroup().findByName("categorySubcategory");
		if (checkbox !=null)
		{
			if (status ==IGuiElement.SELECTED )
			{
				IDataAccessor searchAccessor = context.getDataAccessor().newAccessor();
				IDataTable table = searchAccessor.getTable("category"); 
				table.qbeClear();
				table.qbeSetValue("categorystatus", "Gültig|Keine Zuordnung");
				table.qbeSetValue("parentcategory_key", context.getSelectedRecord().getValue("pkey"));
				if (table.search()>0)
				{
					checkbox.setValue("0");
				}
				else
				{
					checkbox.setValue("1");
				}
			}
			else
			{
				checkbox.setValue("0");
			}

		}

	}

}

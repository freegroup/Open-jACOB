/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 03 17:23:26 CEST 2006
 */
package jacob.common.gui.tc_order;

import jacob.common.tc.TC;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;


/**
 * The event handler for the Tc_orderSearchButton search button.<br>
 * 
 * @author andreas
 */
public class Tc_orderSearchButton extends ISearchActionEventHandler 
{
	static public final transient String RCS_ID = "$Id: Tc_orderSearchButton.java,v 1.1 2006/08/03 17:20:14 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
    // Die Suche nach Buchungen nach der aktiven Kampagne beschränken
    //
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context);
    IDataTable tccapacityTable = context.getDataTable("tc_capacity");
    tccapacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
		return true;
	}

	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}

	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}

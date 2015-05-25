/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu May 20 19:22:53 CEST 2010
 */
package jacob.event.ui.recordlock;

import de.tif.jacob.core.data.impl.adhoc.AdminLockDataSource;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;


/**
 * The event handler for the RecordlockSearch search button.<br>
 * 
 * @author andreas
 */
public class RecordlockSearch extends ISearchActionEventHandler 
{
	static public final transient String RCS_ID = "$Id: RecordlockSearch.java,v 1.1 2010/05/20 17:41:26 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
	  if (context.getGroup().getDataStatus().equals(IGuiElement.SEARCH))
    {
	    // constrain fetching locks from datasources, if GUI field recordlockDatasource is filled
      ISingleDataGuiElement recordlockDatasource = (ISingleDataGuiElement) context.getGroup().findByName("recordlockDatasource");
      AdminLockDataSource.constrainDatasource(context, recordlockDatasource.getValue());
    }
    return true;
	}

	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}

	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}

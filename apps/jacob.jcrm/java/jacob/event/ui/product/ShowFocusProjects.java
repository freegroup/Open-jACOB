/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Sat Oct 15 15:25:08 CEST 2005
 */
package jacob.event.ui.product;

import jacob.common.AppLogger;

import java.util.List;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * 
 * @author andreas
 */
public abstract class ShowFocusProjects extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ShowFocusProjects.java,v 1.2 2005/10/17 17:37:19 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public final void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    List projectIds = SalesOpportunity.getPipeProjectIds(context, getFocus());
    if (projectIds.size()==0)
    {
      context.createMessageDialog(new ApplicationMessage("SalesOpportunity.NoFocusProjectsExisting", Integer.toString(getFocus()))).show();
      return;
    }

    IDataTable salesprojectTable = context.getDataTable("salesproject");
    salesprojectTable.clear();
    
    // constrain search with all desired sales project ids
    //
    salesprojectTable.getAccessor().qbeClearAll();
    for (int i=0; i< projectIds.size(); i++)
    {
      salesprojectTable.qbeSetKeyValue("pkey", projectIds.get(i));
    }

    // search and propagate the record, if exactly one
    //
    IDataBrowser salesprojectBrowser = context.getDataBrowser("salesprojectBrowser");
    salesprojectBrowser.search("r_sales", Filldirection.BOTH);
    if (salesprojectBrowser.recordCount() == 1)
    {
      salesprojectBrowser.setSelectedRecordIndex(0);
      salesprojectBrowser.propagateSelections();
    }
		
		context.setCurrentForm("salesproject");
	}
	
	protected abstract int getFocus();
   
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public final void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}

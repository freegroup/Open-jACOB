/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 02 15:15:32 CEST 2010
 */
package jacob.event.ui.bo.documents;

import jacob.model.Bo;
import jacob.relationset.BoRelationset;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class BoGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: BoGroup.java,v 1.2 2010-07-16 14:26:15 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return BoBrowserEventHandler.class;
  }

  
  /**
   * Will be called, if there is a state change from hidden=>visible.
   * 
   * This happens, if the user switches to a Form which contains this group.
   * 
   * @param context The current client context
   * @param group   The corresponding group for this event
   */
  public void onShow(IClientContext context, IGroup group) throws Exception 
  {
    IDataBrowser browser = group.getBrowser().getData();
    if (browser.recordCount() != 0)
      return;
    
    context.getDataAccessor().qbeClearAll();
    IDataTable boTable = context.getDataTable(Bo.NAME);
    boTable.qbeSetKeyValue(Bo.parent_bo_key, "null");
    browser.search(BoRelationset.NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
      context.getGUIBrowser().expand(context, browser.getRecord(0));
    }
  }
}

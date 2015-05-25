/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 05 13:02:50 CEST 2010
 */
package jacob.event.ui.menutree;

import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class MenutreeGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: MenutreeGroup.java,v 1.2 2010-10-20 21:00:47 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return MenutreeBrowserEventHandler.class;
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
    IDataTable menuTable = context.getDataTable(Menutree.NAME);
    menuTable.qbeClear();
    menuTable.qbeSetValue(Menutree.menutree_parent_key, "null");
    browser.search(IRelationSet.DEFAULT_NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
    }
  }
}

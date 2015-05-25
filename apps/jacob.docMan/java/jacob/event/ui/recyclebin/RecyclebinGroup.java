/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Jul 05 16:24:18 CEST 2010
 */
package jacob.event.ui.recyclebin;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class RecyclebinGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: RecyclebinGroup.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	
  
  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return RecycleBinBrowserEventHandler.class;
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
    context.getDataAccessor().clear();
    browser.search(IRelationSet.LOCAL_NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
  }
}

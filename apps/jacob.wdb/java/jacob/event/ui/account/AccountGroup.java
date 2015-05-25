/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 02 15:44:09 CEST 2010
 */
package jacob.event.ui.account;

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class AccountGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: AccountGroup.java,v 1.2 2010-08-05 08:21:48 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  public Class getSearchBrowserEventHandlerClass()
  {
    return BrowserEventHandler.class;
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
    context.getDataAccessor().clear();
    context.getDataBrowser().search(IRelationSet.LOCAL_NAME, Filldirection.NONE);
    if(context.getDataBrowser().recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
  }
}

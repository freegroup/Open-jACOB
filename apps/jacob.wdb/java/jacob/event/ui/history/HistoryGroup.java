/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 13:24:16 CEST 2010
 */
package jacob.event.ui.history;

import jacob.relationset.HistoryRelationset;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class HistoryGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: HistoryGroup.java,v 1.1 2010-08-17 13:43:13 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return HistoryBrowserEventHandler.class;
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
    context.getDataBrowser().search(HistoryRelationset.NAME, Filldirection.BOTH);
    if(context.getDataBrowser().recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
  }
}

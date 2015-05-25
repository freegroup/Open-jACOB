/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 29 17:47:15 CEST 2010
 */
package jacob.event.ui.news;

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
 public class NewsGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: NewsGroup.java,v 1.2 2010-08-06 16:00:27 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";


  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return NewsBrowserEventHandler.class;
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
    
    group.findByName("newsResetFilterLabel1").setVisible(false);
    group.findByName("newsResetFilterLabel2").setVisible(false);
    group.findByName("newsResetFilterLabel3").setVisible(false);
    group.findByName("newsResetFilterLabel4").setVisible(false);
    
    context.getDataAccessor().qbeClearAll();
    browser.search(IRelationSet.DEFAULT_NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
    }
  }
}

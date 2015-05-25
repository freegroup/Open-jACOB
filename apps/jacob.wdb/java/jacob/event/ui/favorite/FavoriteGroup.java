/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Aug 16 19:42:35 CEST 2010
 */
package jacob.event.ui.favorite;

import jacob.browser.FavoriteBrowser;
import jacob.relationset.FavoriteRelationset;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andherz
 */
 public class FavoriteGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: FavoriteGroup.java,v 1.2 2010-09-26 01:47:17 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	public static final class BrowserEventHandler extends IBrowserEventHandler
	{
	  @Override
	  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	  {
	  }

	  @Override
    public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
    {
      if (FavoriteBrowser.browserTitle.equals(browser.getDefinition().getBrowserField(column).getName()))
      {
        return Icon.star;
      }
      return Icon.NONE;
    }
	}
	
  @Override
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
    context.getDataBrowser().search(FavoriteRelationset.NAME, Filldirection.BOTH);
    if(context.getDataBrowser().recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
  }
}

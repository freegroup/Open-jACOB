/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 08:53:42 CEST 2010
 */
package jacob.event.ui.article;

import jacob.browser.ArticleTreeBrowser;
import jacob.model.Article;
import jacob.relationset.ArticleRelationset;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IGroupEventHandler;
import de.tif.jacob.screen.impl.html.Browser.DefaultConnectByKeyDragDropBrowserEventHandler;


/**
 *
 * @author andherz
 */
 public class ArticleGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: ArticleGroup.java,v 1.4 2010-10-20 21:00:48 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";


  public final static class BrowserEventHandler extends DefaultConnectByKeyDragDropBrowserEventHandler
  {
    public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord record) throws Exception
    {
    }

    @Override
    public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
    {
      if (ArticleTreeBrowser.browserTitle.equals(browser.getDefinition().getBrowserField(column).getName()))
      {
        if (record.getbooleanValue(ArticleTreeBrowser.browserFavorite))
        {
          return Icon.star;
        }
        // different icons for article entries with or without children
        return browser.hasChildren(context, record) ? Icon.table_multiple : Icon.table;
      }
      return Icon.NONE;
    }
  }

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
    // nur initial mal ein paar Records reinladen.....kann durch eine Suche angepasst werden.
    //
    if(context.getDataBrowser().recordCount()!=0)
      return;
    
    group.findByName("articleResetFilterLabel1").setVisible(false);
    group.findByName("articleResetFilterLabel2").setVisible(false);
    group.findByName("articleResetFilterLabel3").setVisible(false);
    group.findByName("articleResetFilterLabel4").setVisible(false);

    context.getDataAccessor().clear();
    IDataTable articleTable = context.getDataTable();
    articleTable.qbeClear();
    articleTable.qbeSetValue(Article.article_parent_key, "null");
    context.getDataBrowser().search(ArticleRelationset.NAME, Filldirection.BOTH);
    if(context.getDataBrowser().recordCount()>0)
      context.getGUIBrowser().setSelectedRecordIndex(context,0);
  }
}

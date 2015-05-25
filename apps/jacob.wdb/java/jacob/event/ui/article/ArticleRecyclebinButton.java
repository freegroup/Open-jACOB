/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 20 10:59:41 CEST 2010
 */
package jacob.event.ui.article;

import jacob.common.AppLogger;
import jacob.common.ArticleUtil;
import jacob.event.ui.menutree.MenutreeRecyclebinButton;
import jacob.model.Article;
import jacob.model.Article_no_condition;
import jacob.model.Article_parent;
import jacob.model.Menutree_no_condition;
import jacob.model.Recyclebin;
import jacob.relationset.ArticleRelationset;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IButton;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the ArticleRecyclebinButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class ArticleRecyclebinButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: ArticleRecyclebinButton.java,v 1.1 2010-10-20 21:00:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public static final Icon RECYCLE_BIN_SMALL = MenutreeRecyclebinButton.RECYCLE_BIN_SMALL;

  static private final transient Log logger = AppLogger.getLogger();

  private static int moveChildrenToRecyclebin(IDataTransaction trans, IDataTableRecord articleRecord) throws Exception
  {
    // neuen Accessor wegen Recursion
    IDataAccessor acc = articleRecord.getAccessor().newAccessor();
    IDataTable table = acc.getTable(Article_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Article_no_condition.article_parent_key, articleRecord.getValue(Article_no_condition.pkey));
    table.qbeSetKeyValue(Article_no_condition.lifecycle, Article_no_condition.lifecycle_ENUM._alive);
    table.search();
    int count = table.recordCount();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      childRecord.setValue(trans, Article_no_condition.lifecycle, Article_no_condition.lifecycle_ENUM._recyclebin2);
      childRecord.setBooleanValue(trans, Article_no_condition.favorite, false);
      detachMenutreeEntries(trans, childRecord);
      
      // and make recursive call
      count += moveChildrenToRecyclebin(trans, childRecord);
    }
    return count;
  }

  private static void detachMenutreeEntries(IDataTransaction trans, IDataTableRecord articleRecord) throws Exception
  {
    IDataAccessor acc = articleRecord.getAccessor();
    IDataTable table = acc.getTable(Menutree_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Menutree_no_condition.article_key, articleRecord.getValue(Article_no_condition.pkey));
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord menutreeRecord = table.getRecord(i);
      // remember linked article pkey
      menutreeRecord.setValue(trans, Menutree_no_condition.recyclebin_article_key, menutreeRecord.getValue(Menutree_no_condition.article_key));
      // and unlink article
      menutreeRecord.setValue(trans, Menutree_no_condition.article_key, null);
    }
  }

  @Override
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord articleRecord = context.getSelectedRecord();
    IDataTableRecord parentArticleRecord = articleRecord.getLinkedRecord(Article_parent.NAME);

    int index = context.getDataBrowser().getSelectedRecordIndex();

    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      // locken und nochmals lesen, damit wir sicher sind, dass der Record nicht
      // schon im Papierkorb ist
      trans.lock(articleRecord);
      articleRecord = articleRecord.getTable().reloadSelectedRecord();
      parentArticleRecord = articleRecord.getLinkedRecord(Article_parent.NAME);

      if (Article.lifecycle_ENUM._alive.equals(articleRecord.getStringValue(Article.lifecycle)))
      {
        String path = ArticleUtil.calculatePath(articleRecord);
        IDataTableRecord recycleRecord = Recyclebin.newRecord(context, trans);
        recycleRecord.setValue(trans, Recyclebin.object_type, Recyclebin.object_type_ENUM._article);
        recycleRecord.setValue(trans, Recyclebin.object_pkey, articleRecord.getValue(Article.pkey));
        recycleRecord.setValue(trans, Recyclebin.title, articleRecord.getValue(Article.title));
        recycleRecord.setValue(trans, Recyclebin.object_path, path);

        // als im Papierkorb befindlich markieren
        articleRecord.setValue(trans, Article.lifecycle, Article.lifecycle_ENUM._recyclebin);
        articleRecord.setBooleanValue(trans, Article.favorite, false);
        detachMenutreeEntries(trans, articleRecord);

        // nun noch alle Kinder als im Papierkorb befindlich markieren
        int count = moveChildrenToRecyclebin(trans, articleRecord);
        recycleRecord.setIntValue(trans, Recyclebin.children_count, count);

        trans.commit();

        logger.info("Article '" + path + "' with " + count + " children move to recyclebin");
      }
    }
    finally
    {
      trans.close();
    }

    if (parentArticleRecord != null)
    {
      context.getGUIBrowser().expand(context, parentArticleRecord);
      context.getGUIBrowser().refresh(context, parentArticleRecord);
      context.getGroup().clear(context, false);
    }
    else
    {
      IDataTable articleTable = context.getDataTable(Article.NAME);
      articleTable.qbeClear();
      articleTable.qbeSetValue(Article.article_parent_key, "null");
      context.getDataBrowser().search(ArticleRelationset.NAME, Filldirection.BOTH);
    }

    // Neuen Record selektieren
    if (context.getDataBrowser().recordCount() != 0)
    {
      if (index > 0)
        context.getGUIBrowser().setSelectedRecordIndex(context, index - 1);
      else
        context.getGUIBrowser().setSelectedRecordIndex(context, 0);
    }

    context.showTransparentMessage("Artikel wurde in den Papierkorb verschoben");
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    ((IButton) button).setIcon(RECYCLE_BIN_SMALL);
  }
}

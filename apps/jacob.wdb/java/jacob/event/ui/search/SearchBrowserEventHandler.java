package jacob.event.ui.search;

import jacob.browser.GlobalcontentBrowser;
import jacob.common.ui.ChapterArticlePaneEventHandler;
import jacob.common.ui.ArticlePaneEventHandler.Pane;
import jacob.model.Active_menutree;
import jacob.model.Article;
import jacob.model.Attachment;
import jacob.model.Chapter;
import jacob.model.Globalcontent;
import jacob.model.Link;
import jacob.model.Menutree;
import jacob.model.News;
import jacob.model.Textblock;
import jacob.relationset.ActiveMenuTreeRelationset;
import jacob.relationset.ArticleRelationset;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public final class SearchBrowserEventHandler extends IBrowserEventHandler
{
  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (GlobalcontentBrowser.browserArticle.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      String alias = record.getStringValue(GlobalcontentBrowser.browserTablealias);
      if (News.NAME.equals(alias))
        return Icon.bell;
      else if (Menutree.NAME.equals(alias))
        return Icon.folder;
      return Icon.table;
    }
    return Icon.NONE;
  }
  
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord record) throws Exception
  {
    IDataKeyValue articlePkey;
    IDataTableRecord chapterRecord=null;
    {
      String alias = record.getSaveStringValue(Globalcontent.tablealias);
      IDataTable table = context.getDataTable(alias);
      IDataKeyValue pkey = table.getTableAlias().getTableDefinition().getPrimaryKey().convertStringToKeyValue(record.getSaveStringValue(Globalcontent.primarykey));

      if (Menutree.NAME.equals(alias))
      {
        IDataTableRecord menutreeRecord = context.getDataTable(Active_menutree.NAME).setSelectedRecord(pkey);
        context.getDataAccessor().propagateRecord(menutreeRecord, ActiveMenuTreeRelationset.NAME, Filldirection.BOTH);
        return;
      }
      else if (News.NAME.equals(alias))
      {
        IDataTableRecord newsRecord = context.getDataTable(News.NAME).setSelectedRecord(pkey);
        context.getDataAccessor().propagateRecord(newsRecord, context.getApplicationDefinition().getLocalRelationSet(), Filldirection.NONE);
        return;
      }
      else if (Article.NAME.equals(alias))
      {
        articlePkey= pkey;
        chapterRecord = null;
      }
      else if (Chapter.NAME.equals(alias))
      {
        chapterRecord = context.getDataTable(Chapter.NAME).loadRecord(pkey);
        articlePkey= chapterRecord.getKeyValue("article_FKey");
      }
      else if (Textblock.NAME.equals(alias))
      {
        IDataTableRecord textblockRecord = context.getDataTable(Textblock.NAME).loadRecord(pkey);
        chapterRecord = context.getDataTable(Chapter.NAME).loadRecord(textblockRecord.getKeyValue("chapter_FKey"));
        articlePkey= chapterRecord.getKeyValue("article_FKey");
      }
      else if (Attachment.NAME.equals(alias))
      {
        IDataTableRecord attachmentRecord = context.getDataTable(Attachment.NAME).loadRecord(pkey);
        articlePkey= attachmentRecord.getKeyValue("article_FKey");
      }
      else if (Link.NAME.equals(alias))
      {
        IDataTableRecord linkRecord = context.getDataTable(Link.NAME).loadRecord(pkey);
        articlePkey= linkRecord.getKeyValue("article_FKey");
      }
      else
      {
        throw new IllegalStateException("Unknown alias '" + alias + "' in index");
      }
    }
    
    // Richtigen Tab anspringen, in welchem der Treffer aus dem Index zurückgeliefert wurde
    //
    IDataTableRecord articleRecord = context.getDataTable(Article.NAME).setSelectedRecord(articlePkey);
    if (chapterRecord != null)
    {
      Object chapterPkey = chapterRecord.getValue(Chapter.pkey);
      if (chapterPkey.equals(articleRecord.getValue(Article.chapter01_key)))
        ChapterArticlePaneEventHandler.setPaneOnShow(context, Pane.CHAPTER1);
      else if (chapterPkey.equals(articleRecord.getValue(Article.chapter02_key)))
        ChapterArticlePaneEventHandler.setPaneOnShow(context, Pane.CHAPTER2);
      else if (chapterPkey.equals(articleRecord.getValue(Article.chapter03_key)))
        ChapterArticlePaneEventHandler.setPaneOnShow(context, Pane.CHAPTER3);
      else if (chapterPkey.equals(articleRecord.getValue(Article.chapter04_key)))
        ChapterArticlePaneEventHandler.setPaneOnShow(context, Pane.CHAPTER4);
      else if (chapterPkey.equals(articleRecord.getValue(Article.chapter05_key)))
        ChapterArticlePaneEventHandler.setPaneOnShow(context, Pane.CHAPTER5);
      else if (chapterPkey.equals(articleRecord.getValue(Article.chapter06_key)))
        ChapterArticlePaneEventHandler.setPaneOnShow(context, Pane.CHAPTER6);
    }
    context.getDataAccessor().propagateRecord(articleRecord, ArticleRelationset.NAME, Filldirection.BOTH);
    
    // clear menutree entry 
    context.getDataTable(Active_menutree.NAME).clear();
  }
}

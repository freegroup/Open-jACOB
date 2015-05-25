/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Sep 17 11:05:32 CEST 2010
 */
package jacob.event.data;

import jacob.browser.GlobalcontentBrowser;
import jacob.common.AppLogger;
import jacob.event.ui.search.SearchBrowserEventHandler;
import jacob.model.Article;
import jacob.model.Attachment;
import jacob.model.Chapter;
import jacob.model.Link;
import jacob.model.Menutree;
import jacob.model.News;
import jacob.model.Textblock;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataRecordId;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.event.DataTableRecordEventHandler;
import de.tif.jacob.core.data.event.IDataBrowserModifiableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.RecordNotFoundException;
import de.tif.jacob.screen.IClientContext;

/**
 * 
 * @author andreas
 */
public class GlobalcontentTableRecord extends DataTableRecordEventHandler
{
  static public final transient String RCS_ID = "$Id: GlobalcontentTableRecord.java,v 1.8 2010-10-20 21:00:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.8 $";

  static private final transient Log logger = AppLogger.getLogger();

  public void afterNewAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // nothing to do here
  }

  public void afterDeleteAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // nothing to do here
  }

  public void beforeCommitAction(IDataTableRecord tableRecord, IDataTransaction transaction) throws Exception
  {
    // nothing to do here
  }

  public void afterCommitAction(IDataTableRecord tableRecord) throws Exception
  {
    // nothing to do here
  }

  public boolean isFilterSearchAction()
  {
    return true;
  }

  private static final String CACHE_PROPERTY = GlobalcontentTableRecord.class.getName() + ":CACHE";

  /**
   * Cache instance to map index records referencing also chapters, textblocks,
   * attachments to article records.
   * 
   * @author Andreas Sonntag
   */
  private static final class ArticleCache
  {
    private final IDataAccessor accessor;
    private final Map<Object, IDataTableRecord> documentId2ArticleRecordMap;
    private final Set<Object> articleRecordPkeys;
    /**
     * Record ids of all chapters and attachments which should be highlighted,
     * if the related article is selected from the index search result list.
     */
    private final Set<IDataRecordId> highlightRecordIds;

    private ArticleCache(Context context)
    {
      this.accessor = context.getDataAccessor().newAccessor();
      this.documentId2ArticleRecordMap = new HashMap<Object, IDataTableRecord>();
      this.articleRecordPkeys = new HashSet<Object>();
      this.highlightRecordIds = new HashSet<IDataRecordId>();
    }

    private IDataTableRecord getArticleRecord(IDataBrowserRecord browserRecord)
    {
      try
      {
        return this.documentId2ArticleRecordMap.get(browserRecord.getValue(GlobalcontentBrowser.browserId));
      }
      catch (NoSuchFieldException ex)
      {
        // should never occur
        throw new RuntimeException(ex);
      }
    }
    
    /**
     * @param browserRecord
     * @return
     * @throws RecordNotFoundException
     * @see SearchBrowserEventHandler#onRecordSelect()
     */
    private boolean register(IDataBrowserRecord browserRecord) throws RecordNotFoundException
    {
      try
      {
        String alias = browserRecord.getStringValue(GlobalcontentBrowser.browserTablealias);
        IDataTable table = this.accessor.getTable(alias);
        IDataKeyValue pkey = table.getTableAlias().getTableDefinition().getPrimaryKey().convertStringToKeyValue(browserRecord.getStringValue(GlobalcontentBrowser.primarykey));

        boolean visible = true;
        IDataTableRecord articleRecord;
        if (Article.NAME.equals(alias))
        {
          articleRecord = table.getRecord(pkey);
        }
        else if (Chapter.NAME.equals(alias))
        {
          IDataTableRecord chapterRecord = this.accessor.getTable(Chapter.NAME).getRecord(pkey);
          this.highlightRecordIds.add(chapterRecord.getId());
          articleRecord = this.accessor.getTable(Article.NAME).getRecord(chapterRecord.getKeyValue("article_FKey"));
        }
        else if (Textblock.NAME.equals(alias))
        {
          IDataTableRecord textblockRecord = this.accessor.getTable(Textblock.NAME).getRecord(pkey);
          this.highlightRecordIds.add(textblockRecord.getId());
          IDataTableRecord chapterRecord = this.accessor.getTable(Chapter.NAME).getRecord(textblockRecord.getKeyValue("chapter_FKey"));
          this.highlightRecordIds.add(chapterRecord.getId());
          articleRecord = this.accessor.getTable(Article.NAME).getRecord(chapterRecord.getKeyValue("article_FKey"));
        }
        else if (Attachment.NAME.equals(alias))
        {
          IDataTableRecord attachmentRecord = this.accessor.getTable(Attachment.NAME).getRecord(pkey);
          this.highlightRecordIds.add(attachmentRecord.getId());
          articleRecord = this.accessor.getTable(Article.NAME).getRecord(attachmentRecord.getKeyValue("article_FKey"));
        }
        else if (Link.NAME.equals(alias))
        {
          IDataTableRecord linkRecord = this.accessor.getTable(Link.NAME).getRecord(pkey);
          articleRecord = this.accessor.getTable(Article.NAME).getRecord(linkRecord.getKeyValue("article_FKey"));
        }
        else if (News.NAME.equals(alias))
        {
          IDataTableRecord newsRecord = table.getRecord(pkey);
          Date now = new Date();
          now.setYear(70);
          long start = newsRecord.getlongValue(News.normalized_start);
          long end = newsRecord.getlongValue(News.normalized_end);
          return start <= now.getTime() && end >= now.getTime();
        }
        else if (Menutree.NAME.equals(alias))
        {
          IDataTableRecord menutreeRecord = table.getRecord(pkey);
          IDataKeyValue articleFKey = menutreeRecord.getKeyValue("article_FKey");
          articleRecord = articleFKey == null ? null : this.accessor.getTable(Article.NAME).getRecord(articleFKey);
          visible = Menutree.state_ENUM._active.equals(menutreeRecord.getStringValue(Menutree.state)) //
              && Menutree.lifecycle_ENUM._alive.equals(menutreeRecord.getStringValue(Menutree.lifecycle));
        }
        else
        {
          throw new IllegalStateException("Unknown alias '" + alias + "' in index");
        }

        if (articleRecord != null)
        {
          this.documentId2ArticleRecordMap.put(browserRecord.getValue(GlobalcontentBrowser.browserId), articleRecord);

          // Attention: Each article should only appear once in search result
          // list
          if (!this.articleRecordPkeys.add(articleRecord.getValue(Article.pkey)))
            visible = false;
          
          // already in recyclebin?
          if (!Article.lifecycle_ENUM._alive.equals(articleRecord.getStringValue(Article.lifecycle)))
            visible = false;
        }
        return visible;
      }
      catch (NoSuchFieldException ex)
      {
        // should never occur
        throw new RuntimeException(ex);
      }
    }
  }
  
  /**
   * Checks whether the given record should be highlighted, because it has caused a hit in the index
   * @param context
   * @param record the record (chapter or attachment) to test
   * @return
   */
  public static boolean isHighlighted(IClientContext context, IDataRecord record)
  {
    ArticleCache cache = (ArticleCache) context.getProperty(CACHE_PROPERTY);
    if (cache != null)
      return cache.highlightRecordIds.contains(record.getId());
    return false;
  }

  @Override
  public void beforeSearchAction(IDataBrowser browser, IDataTable table, IRelationSet relationSet) throws Exception
  {
    if (browser.getBrowserDefinition().getName().equals(GlobalcontentBrowser.NAME))
    {
      // reset cache
      //
      Context context = Context.getCurrent();
      ArticleCache cache = (ArticleCache) context.getProperty(CACHE_PROPERTY);
      if (cache != null)
        context.setPropertyForWindow(CACHE_PROPERTY, null);
    }
    super.beforeSearchAction(browser, table, relationSet);
  }

  public boolean filterSearchAction(IDataBrowserRecord browserRecord) throws Exception
  {
    if (browserRecord.getBrowser().getBrowserDefinition().getName().equals(GlobalcontentBrowser.NAME))
    {
      Context context = Context.getCurrent();
      ArticleCache cache = (ArticleCache) context.getProperty(CACHE_PROPERTY);
      if (cache == null)
        context.setPropertyForWindow(CACHE_PROPERTY, cache = new ArticleCache(context));
      try
      {
        if (!cache.register(browserRecord))
          return false;
      }
      catch (RecordNotFoundException ex)
      {
        // might occur occasionally
        logger.info("Lucene index out of date: " + ex.toString());
        return false;
      }
    }
    return super.filterSearchAction(browserRecord);
  }

  public void afterSearchAction(IDataBrowserModifiableRecord browserRecord) throws Exception
  {
    if (browserRecord.getBrowser().getBrowserDefinition().getName().equals(GlobalcontentBrowser.NAME))
    {
      // News and menutree handling
      //
      String alias = browserRecord.getStringValue(GlobalcontentBrowser.browserTablealias);
      if (News.NAME.equals(alias) || Menutree.NAME.equals(alias))
      {
        browserRecord.setValue(GlobalcontentBrowser.browserArticle, browserRecord.getValue(GlobalcontentBrowser.subject));
        return;
      }
      
      // Article handling
      //
      ArticleCache cache = (ArticleCache) Context.getCurrent().getProperty(CACHE_PROPERTY);
      IDataTableRecord articleRecord = cache.getArticleRecord(browserRecord);
      
      // set article info
      browserRecord.setValue(GlobalcontentBrowser.browserArticle, articleRecord.getValue(Article.title));
      browserRecord.setValue(GlobalcontentBrowser.browserArticle_key, articleRecord.getValue(Article.pkey));
    }
    super.afterSearchAction(browserRecord);
  }
}

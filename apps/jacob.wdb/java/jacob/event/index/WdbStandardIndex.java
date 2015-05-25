package jacob.event.index;

import jacob.model.Article;
import jacob.model.Attachment;
import jacob.model.Chapter;
import jacob.model.Link;
import jacob.model.Menutree;
import jacob.model.News;
import jacob.model.Textblock;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.impl.index.analyser.SimpleGermanAnalyzer;
import de.tif.jacob.core.data.impl.index.event.StandardLuceneEventHandler;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;

/**
 * Event handler to fill up WDB article index.
 * 
 * @author Andreas Sonntag
 */
public final class WdbStandardIndex extends StandardLuceneEventHandler
{
  private static abstract class AliasIndexInfoProvider
  {
    public float getContentsFieldBoost(Context context, ITableField field) throws Exception
    {
      return 1.0f;
    }
    
    public String getContentType(Context context, ITableField field) throws Exception
    {
      return "text/plain";
    }
    
    public abstract boolean addToContentsField(Context context, ITableField field) throws Exception;
  }
  
  private static final Map<String, AliasIndexInfoProvider> INDEX_ALIAS = new HashMap<String, AliasIndexInfoProvider>();
  
  static
  {
    INDEX_ALIAS.put(Menutree.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Menutree.title))
          return 10.0f;

        return 0.0f;
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });
    
    INDEX_ALIAS.put(Article.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Article.synonyms))
          return 15.0f;

        if (field.getName().equals(Article.title))
          return 10.0f;

        return 0.0f;
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });
    
    INDEX_ALIAS.put(Chapter.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Chapter.title))
          return 5.0f;

        return 0.0f;
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });
    
    INDEX_ALIAS.put(Textblock.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Textblock.title))
          return 2.0f;

        if (field.getName().equals(Textblock.content))
          return 1.5f;

        return 0.0f;
      }

      @Override
      public String getContentType(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Textblock.content))
          return "text/html";
        return super.getContentType(context, field);
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });
    
    INDEX_ALIAS.put(Attachment.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Attachment.title))
          return 2.0f;

        if (field.getName().equals(Attachment.description))
          return 1.2f;

        if (field.getName().equals(Attachment.document))
          return 0.9f;

        return 0.0f;
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });
    
    INDEX_ALIAS.put(Link.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(Link.title))
          return 2.0f;

        if (field.getName().equals(Link.description))
          return 1.2f;

        return 0.0f;
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });

    INDEX_ALIAS.put(News.NAME, new AliasIndexInfoProvider()
    {
      public float getContentsFieldBoost(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(News.headline))
          return 2.0f;

        if (field.getName().equals(News.content))
          return 1.5f;

        return 0.0f;
      }

      @Override
      public String getContentType(Context context, ITableField field) throws Exception
      {
        if (field.getName().equals(News.content))
          return "text/html";
        return super.getContentType(context, field);
      }

      public boolean addToContentsField(Context context, ITableField field) throws Exception
      {
        return getContentsFieldBoost(context, field) > 0.0f;
      }
    });
  }
  
  @Override
  public Analyzer getAnalyzer(Locale locale)
  {
    // use SimpleGermanAnalyzer which does not do any word stemming but just simple umlaut substitutions 
    return new SimpleGermanAnalyzer(Version.LUCENE_29);
  }

  @Override
  public String getContentType(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    return INDEX_ALIAS.get(alias.getName()).getContentType(context, field);
  }

  public boolean containsRecordsOfTableAlias(ITableAlias tableAlias)
  {
    return INDEX_ALIAS.containsKey(tableAlias.getName());
  }

  public float getContentsFieldBoost(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    return INDEX_ALIAS.get(alias.getName()).getContentsFieldBoost(context, field);
  }

  public boolean addToContentsField(Context context, ITableAlias alias, ITableField field) throws Exception
  {
    return INDEX_ALIAS.get(alias.getName()).addToContentsField(context, field);
  }

  public String getSubjectFieldValue(Context context, IDataTableRecord record) throws Exception
  {
    return super.getSubjectFieldValue(context, record);
  }
}




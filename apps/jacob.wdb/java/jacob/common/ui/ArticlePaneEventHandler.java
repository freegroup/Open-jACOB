/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:50:19 CEST 2010
 */
package jacob.common.ui;

import jacob.model.Article;
import jacob.model.Attachment_thumbnail;
import jacob.model.Chapter01;
import jacob.model.Chapter02;
import jacob.model.Chapter03;
import jacob.model.Chapter04;
import jacob.model.Chapter05;
import jacob.model.Chapter06;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.ITabPaneEventListener;

/**
 * @author andreas
 *
 */
public class ArticlePaneEventHandler extends ITabPaneEventListener
{
  static public final transient String RCS_ID = "$Id: ArticlePaneEventHandler.java,v 1.1 2010-09-20 19:16:47 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public static enum Pane {
    CHAPTER1(Chapter01.NAME, Article.chapter01_key), //
    CHAPTER2(Chapter02.NAME, Article.chapter02_key), //
    CHAPTER3(Chapter03.NAME, Article.chapter03_key), //
    CHAPTER4(Chapter04.NAME, Article.chapter04_key), //
    CHAPTER5(Chapter05.NAME, Article.chapter05_key), //
    CHAPTER6(Chapter06.NAME, Article.chapter06_key), //
    THUMBNAILS(Attachment_thumbnail.NAME);

    protected final String alias;
    protected final String field;

    private Pane(String alias, String field)
    {
      this.alias = alias;
      this.field = field;
    }

    private Pane(String paneName)
    {
      this(paneName, null);
    }

    private boolean isValid(IDataTableRecord articleRec)
    {
      try
      {
        if (this.field != null)
          return !articleRec.hasNullValue(this.field);
        return true;
      }
      catch (NoSuchFieldException ex)
      {
        return false;
      }
    }
  };

  private static final String PANE_PROPERTY = ChapterArticlePaneEventHandler.class.getName() + ":Pane";

  public static void setPaneOnShow(IClientContext context, Pane pane)
  {
    context.setPropertyForRequest(PANE_PROPERTY, pane);
  }

  public static Pane getPaneOnShow(IClientContext context)
  {
    return (Pane) context.getProperty(PANE_PROPERTY);
  }
}

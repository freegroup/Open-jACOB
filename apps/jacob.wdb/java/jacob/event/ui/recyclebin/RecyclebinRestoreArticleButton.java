/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Oct 20 11:49:25 CEST 2010
 */
package jacob.event.ui.recyclebin;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import jacob.common.ArticleUtil;
import jacob.event.data.RecyclebinTableRecord;
import jacob.model.Article_no_condition;
import jacob.model.Menutree_no_condition;
import jacob.model.Recyclebin;

import org.apache.commons.logging.Log;


/**
 * The event handler for the RecyclebinRestoreArticleButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class RecyclebinRestoreArticleButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: RecyclebinRestoreArticleButton.java,v 1.1 2010-10-20 21:00:49 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Alle Kinder restoren welche als lifecycle_ENUM._recyclebin2 markiert sind
   * @param trans
   * @param articleRecord
   * @return
   * @throws Exception
   */
  private static int restoreChildrenFromRecyclebin(IDataTransaction trans, IDataTableRecord articleRecord) throws Exception
  {
    // new accessor because of recursive call
    IDataAccessor acc = articleRecord.getAccessor().newAccessor();
    IDataTable table = acc.getTable(Article_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Article_no_condition.article_parent_key, articleRecord.getValue(Article_no_condition.pkey));
    table.qbeSetKeyValue(Article_no_condition.lifecycle, Article_no_condition.lifecycle_ENUM._recyclebin2);
    table.search();
    int count = table.recordCount();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      childRecord.setValue(trans, Article_no_condition.lifecycle, Article_no_condition.lifecycle_ENUM._alive);
      
      // link menutree entries again which have been linked with this article and are currently unlinked
      attachMenutreeEntries(trans, childRecord);
      
      // recursive call
      count += restoreChildrenFromRecyclebin(trans, childRecord);
    }
    return count;
  }

  private static void attachMenutreeEntries(IDataTransaction trans, IDataTableRecord articleRecord) throws Exception
  {
    IDataAccessor acc = articleRecord.getAccessor();
    IDataTable table = acc.getTable(Menutree_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetValue(Menutree_no_condition.article_key, "null");
    table.qbeSetKeyValue(Menutree_no_condition.recyclebin_article_key, articleRecord.getValue(Article_no_condition.pkey));
    table.search();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord menutreeRecord = table.getRecord(i);
      // link to article
      menutreeRecord.setValue(trans, Menutree_no_condition.article_key, menutreeRecord.getValue(Menutree_no_condition.recyclebin_article_key));
      menutreeRecord.setValue(trans, Menutree_no_condition.recyclebin_article_key, null);
    }
  }

  /**
   * Ein Artikel darf erst wiederhergestellt werden, wenn auch alle Väter wiederhergestellt sind. 
   * @param articleRecord
   * @throws Exception
   */
  private static void checkParentsForRecyclebin(IDataTableRecord articleRecord) throws Exception
  {
    IDataKeyValue parentKey = articleRecord.getKeyValue("article_parent_FKey");
    if (parentKey != null)
    {
      IDataAccessor acc = articleRecord.getAccessor();
      IDataTable table = acc.getTable(Article_no_condition.NAME);
      IDataTableRecord parentRecord = table.loadRecord(parentKey);
      if (Article_no_condition.lifecycle_ENUM._recyclebin.equals(parentRecord.getStringValue(Article_no_condition.lifecycle)))
        throw new UserException("Wiederherstellung nicht möglich.\n Stellen sie erst folgenden Artikel wieder her:\n " + ArticleUtil.calculatePath(parentRecord));
      else if (Article_no_condition.lifecycle_ENUM._recyclebin2.equals(parentRecord.getStringValue(Article_no_condition.lifecycle)))
        checkParentsForRecyclebin(parentRecord);
    }
  }

  @Override
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // Index des Records im Browser merken
    int index = context.getDataBrowser().getSelectedRecordIndex();

    IDataTableRecord recyclebinRecord = context.getSelectedRecord();

    IDataTableRecord articleRecord;
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      String info = null;

      IDataTable table = context.getDataTable(Article_no_condition.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Article_no_condition.pkey, recyclebinRecord.getValue(Recyclebin.object_pkey));
      table.search();
      articleRecord = table.getSelectedRecord();
      if (articleRecord != null)
      {
        checkParentsForRecyclebin(articleRecord);

        if (!Article_no_condition.lifecycle_ENUM._alive.equals(articleRecord.getStringValue(Article_no_condition.lifecycle)))
        {
          articleRecord.setValue(trans, Article_no_condition.lifecycle, Article_no_condition.lifecycle_ENUM._alive);
          
          // link menutree entries again which have been linked with this article and are currently unlinked
          attachMenutreeEntries(trans, articleRecord);

          int count = restoreChildrenFromRecyclebin(trans, articleRecord);
          info = "Article entry '" + recyclebinRecord.getValue(Recyclebin.object_path) + "' with " + count + " children restored from recyclebin";
        }
      }

      RecyclebinTableRecord.setRestoreFlag(trans);
      recyclebinRecord.delete(trans);

      trans.commit();

      if (info != null)
        logger.info(info);
    }
    finally
    {
      trans.close();
    }

    context.getDataBrowser().removeRecord(context.getGUIBrowser().getSelectedBrowserRecord(context));

    // Neuen Record im Papierkorb selektieren
    if (context.getDataBrowser().recordCount() != 0)
    {
      if (index > 0)
        context.getGUIBrowser().setSelectedRecordIndex(context, index - 1);
      else
        context.getGUIBrowser().setSelectedRecordIndex(context, 0);
    }

    if (articleRecord == null)
    {
      context.createMessageDialog("Artikel wurde bereits endgültig gelöscht.").show();
    }
    else
    {
      // Verwaltungssicht synchronisieren
      {
        IForm form = (IForm) context.getApplication().findByName("verwaltung_article");
        if (form != null)
        {
          IBrowser browser = form.getCurrentBrowser();

          browser.add(context, articleRecord);
          browser.ensureVisible(context, articleRecord);
        }
      }

      context.showTransparentMessage("Artikel wurde erfolgreich wiederhergestellt.");
    }
  }
}


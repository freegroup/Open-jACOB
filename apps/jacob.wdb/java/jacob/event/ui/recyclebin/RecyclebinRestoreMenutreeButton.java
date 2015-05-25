/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 19 14:56:12 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.common.AppLogger;
import jacob.common.MenutreeUtil;
import jacob.event.data.RecyclebinTableRecord;
import jacob.model.Menutree_no_condition;
import jacob.model.Recyclebin;

import org.apache.commons.logging.Log;

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

/**
 * The event handler for the RecyclebinRestoreMenutreeButton record selected
 * button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public final class RecyclebinRestoreMenutreeButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RecyclebinRestoreMenutreeButton.java,v 1.1 2010-10-20 21:00:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  static private final transient Log logger = AppLogger.getLogger();

  /**
   * Alle Kinder restoren welche als lifecycle_ENUM._recyclebin2 markiert sind
   * @param trans
   * @param menutreeRecord
   * @return
   * @throws Exception
   */
  private static int restoreChildrenFromRecyclebin(IDataTransaction trans, IDataTableRecord menutreeRecord) throws Exception
  {
    // new accessor because of recursive call
    IDataAccessor acc = menutreeRecord.getAccessor().newAccessor();
    IDataTable table = acc.getTable(Menutree_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Menutree_no_condition.menutree_parent_key, menutreeRecord.getValue(Menutree_no_condition.pkey));
    table.qbeSetKeyValue(Menutree_no_condition.lifecycle, Menutree_no_condition.lifecycle_ENUM._recyclebin2);
    table.search();
    int count = table.recordCount();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      childRecord.setValue(trans, Menutree_no_condition.lifecycle, Menutree_no_condition.lifecycle_ENUM._alive);
      
      // recursive call
      count += restoreChildrenFromRecyclebin(trans, childRecord);
    }
    return count;
  }

  /**
   * Ein Menüeintrag darf erst wiederhergestellt werden, wenn auch alle Väter wiederhergestellt sind. 
   * @param menutreeRecord
   * @throws Exception
   */
  private static void checkParentsForRecyclebin(IDataTableRecord menutreeRecord) throws Exception
  {
    IDataKeyValue parentKey = menutreeRecord.getKeyValue("menutree_parent_FKey");
    if (parentKey != null)
    {
      IDataAccessor acc = menutreeRecord.getAccessor();
      IDataTable table = acc.getTable(Menutree_no_condition.NAME);
      IDataTableRecord parentRecord = table.loadRecord(parentKey);
      if (Menutree_no_condition.lifecycle_ENUM._recyclebin.equals(parentRecord.getStringValue(Menutree_no_condition.lifecycle)))
        throw new UserException("Wiederherstellung nicht möglich.\n Stellen sie erst folgenden Menüeintrag wieder her:\n " + MenutreeUtil.calculatePath(parentRecord));
      else if (Menutree_no_condition.lifecycle_ENUM._recyclebin2.equals(parentRecord.getStringValue(Menutree_no_condition.lifecycle)))
        checkParentsForRecyclebin(parentRecord);
    }
  }

  @Override
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // Index des Records im Browser merken
    int index = context.getDataBrowser().getSelectedRecordIndex();

    IDataTableRecord recyclebinRecord = context.getSelectedRecord();

    IDataTableRecord menutreeRecord;
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      String info = null;

      IDataTable table = context.getDataTable(Menutree_no_condition.NAME);
      table.qbeClear();
      table.qbeSetKeyValue(Menutree_no_condition.pkey, recyclebinRecord.getValue(Recyclebin.object_pkey));
      table.search();
      menutreeRecord = table.getSelectedRecord();
      if (menutreeRecord != null)
      {
        checkParentsForRecyclebin(menutreeRecord);

        if (!Menutree_no_condition.lifecycle_ENUM._alive.equals(menutreeRecord.getStringValue(Menutree_no_condition.lifecycle)))
        {
          menutreeRecord.setValue(trans, Menutree_no_condition.lifecycle, Menutree_no_condition.lifecycle_ENUM._alive);
          int count = restoreChildrenFromRecyclebin(trans, menutreeRecord);
          info = "Menutree entry '" + recyclebinRecord.getValue(Recyclebin.object_path) + "' with " + count + " children restored from recyclebin";
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

    if (menutreeRecord == null)
    {
      context.createMessageDialog("Menüeintrag wurde bereits endgültig gelöscht.").show();
    }
    else
    {
      // Verwaltungssicht synchronisieren
      {
        IForm form = (IForm) context.getApplication().findByName("verwaltung_menutree");
        if (form != null)
        {
          IBrowser browser = form.getCurrentBrowser();

          browser.add(context, menutreeRecord);
          browser.ensureVisible(context, menutreeRecord);
        }
      }

      context.showTransparentMessage("Menüeintrag wurde erfolgreich wiederhergestellt.");
    }
  }
}

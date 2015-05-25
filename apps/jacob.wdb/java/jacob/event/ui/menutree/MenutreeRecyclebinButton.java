/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Oct 18 09:48:56 CEST 2010
 */
package jacob.event.ui.menutree;

import de.tif.jacob.core.Context;
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
import jacob.common.AppLogger;
import jacob.common.MenutreeUtil;
import jacob.model.Menutree;
import jacob.model.Menutree_no_condition;
import jacob.model.Menutree_parent;
import jacob.model.Recyclebin;
import jacob.relationset.MenutreeRelationset;

import org.apache.commons.logging.Log;

/**
 * The event handler for the MenutreeRecyclebinButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class MenutreeRecyclebinButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: MenutreeRecyclebinButton.java,v 1.1 2010-10-20 21:00:47 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  public static final Icon RECYCLE_BIN_SMALL = new Icon((IClientContext) Context.getCurrent(), "Recyclebin_empty16x16.png");

  static private final transient Log logger = AppLogger.getLogger();
  
  private static int moveChildrenToRecyclebin(IDataTransaction trans, IDataTableRecord menutreeRecord) throws Exception
  {
    IDataAccessor acc = menutreeRecord.getAccessor().newAccessor();
    IDataTable table = acc.getTable(Menutree_no_condition.NAME);
    table.qbeClear();
    table.setUnlimitedRecords();
    table.qbeSetKeyValue(Menutree_no_condition.menutree_parent_key, menutreeRecord.getValue(Menutree_no_condition.pkey));
    table.qbeSetKeyValue(Menutree_no_condition.lifecycle, Menutree_no_condition.lifecycle_ENUM._alive);
    table.search();
    int count = table.recordCount();
    for (int i = 0; i < table.recordCount(); i++)
    {
      IDataTableRecord childRecord = table.getRecord(i);
      childRecord.setValue(trans, Menutree_no_condition.lifecycle, Menutree_no_condition.lifecycle_ENUM._recyclebin2);
      count += moveChildrenToRecyclebin(trans, childRecord);
    }
    return count;
  }

  @Override
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord menutreeRecord = context.getSelectedRecord();
    IDataTableRecord parentMenutreeRecord = menutreeRecord.getLinkedRecord(Menutree_parent.NAME);

    int index = context.getDataBrowser().getSelectedRecordIndex();

    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      // locken und nochmals lesen, damit wir sicher sind, dass der Record nicht
      // schon im Papierkorb ist
      trans.lock(menutreeRecord);
      menutreeRecord = menutreeRecord.getTable().reloadSelectedRecord();
      parentMenutreeRecord = menutreeRecord.getLinkedRecord(Menutree_parent.NAME);

      if (Menutree.lifecycle_ENUM._alive.equals(menutreeRecord.getStringValue(Menutree.lifecycle)))
      {
        String path = MenutreeUtil.calculatePath(menutreeRecord);
        IDataTableRecord recycleRecord = Recyclebin.newRecord(context, trans);
        recycleRecord.setValue(trans, Recyclebin.object_type, Recyclebin.object_type_ENUM._menutree);
        recycleRecord.setValue(trans, Recyclebin.object_pkey, menutreeRecord.getValue(Menutree.pkey));
        recycleRecord.setValue(trans, Recyclebin.title, menutreeRecord.getValue(Menutree.title));
        recycleRecord.setValue(trans, Recyclebin.object_path, path);

        // als im Papierkorb befindlich markieren
        menutreeRecord.setValue(trans, Menutree.lifecycle, Menutree.lifecycle_ENUM._recyclebin);
        
        // nun noch alle Kinder als im Papierkorb befindlich markieren
        int count = moveChildrenToRecyclebin(trans, menutreeRecord);
        recycleRecord.setIntValue(trans, Recyclebin.children_count, count);
        
        trans.commit();
        
        logger.info("Menutree entry '" + path + "' with " + count + " children move to recyclebin");
      }
    }
    finally
    {
      trans.close();
    }

    if (parentMenutreeRecord != null)
    {
      context.getGUIBrowser().expand(context, parentMenutreeRecord);
      context.getGUIBrowser().refresh(context, parentMenutreeRecord);
      context.getGroup().clear(context, false);
    }
    else
    {
      IDataTable menutreeTable = context.getDataTable(Menutree.NAME);
      menutreeTable.qbeClear();
      menutreeTable.qbeSetValue(Menutree.menutree_parent_key, "null");
      context.getDataBrowser().search(MenutreeRelationset.NAME, Filldirection.BOTH);
    }

    // Neuen Record selektieren
    if (context.getDataBrowser().recordCount() != 0)
    {
      if (index > 0)
        context.getGUIBrowser().setSelectedRecordIndex(context, index - 1);
      else
        context.getGUIBrowser().setSelectedRecordIndex(context, 0);
    }

    context.showTransparentMessage("Menüeintrag wurde in den Papierkorb verschoben");
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    ((IButton) button).setIcon(RECYCLE_BIN_SMALL);
  }
}

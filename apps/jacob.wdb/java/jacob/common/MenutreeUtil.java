package jacob.common;

import jacob.event.ui.Artikel;
import jacob.model.Active_menutree;
import jacob.model.Menutree;
import jacob.relationset.ActiveMenuTreeRelationset;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.HTTPClientContext;
import de.tif.jacob.screen.impl.html.JacobGroup;

public class MenutreeUtil
{
  public static long count(Context context) throws Exception
  {
    IDataTable menutreeTable = context.getDataAccessor().newAccessor().getTable(Menutree.NAME);
    return menutreeTable.count();
  }
  
  /**
   * Checks whether the menutree entry is locked or one of its parent records is
   * locked.
   * 
   * @param context
   * @param menutreeRecord
   * @return
   * @throws Exception
   */
  public static boolean isLocked(Context context, IDataTableRecord menutreeRecord) throws Exception
  {
    if (Menutree.state_ENUM._locked.equals(menutreeRecord.getStringValue(Menutree.state)))
      return true;

    IDataKeyValue parentPKey = menutreeRecord.getKeyValue("menutree_parent_FKey");
    if (parentPKey == null)
      return false;

    // get the data table of the same alias and get the parent record (might be
    // already cached)
    IDataTable menutreeTable = context.getDataAccessor().getTable(menutreeRecord.getTableAlias());
    return isLocked(context, menutreeTable.getRecord(parentPKey));
  }

  public static String calculatePath(IDataTableRecord menutreeRecord) throws Exception
  {
    if (menutreeRecord == null)
      return null;

    IDataKeyValue parentPKey = menutreeRecord.getKeyValue("menutree_parent_FKey");
    if (parentPKey == null)
      return " / " + menutreeRecord.getSaveStringValue(Menutree.title);

    // get the data table of the same alias and get the parent record (might be
    // already cached)
    return calculatePath(menutreeRecord.getTable().getRecord(parentPKey)) + " / " + menutreeRecord.getSaveStringValue(Menutree.title);
  }

  public static IDataTableRecord findByPkey(Context context, String boPkey) throws Exception
  {
    if(boPkey==null)
      return null;
    
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Active_menutree.NAME);
    boTable.qbeSetKeyValue(Active_menutree.pkey, boPkey);
    boTable.search(IRelationSet.LOCAL_NAME);
    return boTable.getSelectedRecord();
  }

  public static void show(IClientContext context, String pkey) throws Exception
  {
    IDataTableRecord displayRecord = findByPkey(context, pkey);
    if (displayRecord == null || isLocked(context, displayRecord))
    {
      throw new UserException("Artikel ist nicht mehr vorhanden oder gesperrt");
    }
    
    IDataTableRecord rootRecord = displayRecord;
    IDataTableRecord tmp = rootRecord;
    while(tmp!=null)
    {
      tmp = findByPkey(context, tmp.getStringValue(Menutree.menutree_parent_key));
      if(tmp!=null)
        rootRecord=tmp;
    }
    context.setCurrentForm("artikel", "artikel_menutree");
    
    // Falls dieser Code durch einen EntryPoint aufgerufen wurde, dann ist keine "auslösende" Gruppe
    // bekannt., Diese muss manuell gesetzt werden.
    JacobGroup group = ((JacobGroup)context.getForm().findByName("active_menutreeGroup"));
    ((HTTPClientContext)context).setGroup(group);
    
    context.getDataAccessor().clear();
    IDataBrowser browser = context.getDataBrowser();
    IDataTable table = context.getDataTable();
    
    table.qbeSetKeyValue(Active_menutree.pkey, rootRecord.getStringValue(Active_menutree.pkey));
    browser.search(ActiveMenuTreeRelationset.NAME, Filldirection.BOTH);
    if(browser.recordCount()>0)
    {
      if(displayRecord==rootRecord)
        context.getGUIBrowser().setSelectedRecordIndex(context,0);
      else
        context.getGUIBrowser().add(context, displayRecord, true);

      context.getGUIBrowser().expand(context, context.getGUIBrowser().getSelectedBrowserRecord(context));
      
      Artikel.setCurrentNavigationEntry(context, browser.getRecord(0).getPrimaryKeyValue());
    }
    
    // AS 14.10.2010 : macht Selektion wieder kaputt!
//    context.getGUIBrowser().setData(context, browser);
    
  }
}

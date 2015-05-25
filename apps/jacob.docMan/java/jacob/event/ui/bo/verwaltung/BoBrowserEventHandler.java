package jacob.event.ui.bo.verwaltung;

import jacob.browser.BoBrowser;
import jacob.common.HistoryManager;
import jacob.model.Bo;

import java.util.Iterator;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.event.IDragDropListener;
import de.tif.jacob.screen.impl.html.Browser;
import de.tif.jacob.util.StringUtil;

public class BoBrowserEventHandler extends IBrowserEventHandler implements IDragDropListener
{

  public void drop(IClientContext context, IGuiElement dropElement, Object dragObject, Object dropObject) throws Exception
  {
    Browser httpBrowser = (Browser)dropElement;
    
    IKey connectByKey = httpBrowser.getConnectByKey();
    if(connectByKey==null)
      return;
    
    // Eine Suche mit allen Key-Parameter auf der betroffenen Tabelle absetzen
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTransaction trans = acc.newTransaction();
    try
    {
      IDataBrowserRecord dragBrowserRecord = (IDataBrowserRecord)dragObject;
      IDataBrowserRecord dropBrowserRecord = (IDataBrowserRecord)dropObject;

      IDataTableRecord dragTableRecord = dragBrowserRecord.getTableRecord();
      
      // Zeile wurde auf eine leere stelle im Browser fallen gelassen
      //
      if(dropBrowserRecord==null)
      {
        Iterator foreignIter = connectByKey.getTableFields().iterator();
        while (foreignIter.hasNext())
        {
          ITableField foreignField = (ITableField) foreignIter.next();
          dragTableRecord.setValue(trans,foreignField, null);
        }
      }
      else
      {
        IDataBrowserRecord parent = httpBrowser.getParent( dropBrowserRecord);
        while(parent!=null)
        {
          if(parent.getPrimaryKeyValue().equals(dragBrowserRecord.getPrimaryKeyValue()))
            return;
          parent = httpBrowser.getParent(parent);
        }
      
        IDataTableRecord dropTableRecord = dropBrowserRecord.getTableRecord();
        IDataTable table = acc.getTable(dragTableRecord.getTableAlias().getName());
        IKey primaryKey = table.getTableAlias().getTableDefinition().getPrimaryKey();
        Iterator foreignIter = connectByKey.getTableFields().iterator();
        Iterator primaryIter = primaryKey.getTableFields().iterator();
        while (foreignIter.hasNext())
        {
          ITableField foreignField = (ITableField) foreignIter.next();
          ITableField primaryField = (ITableField) primaryIter.next();
    
          dragTableRecord.setValue(trans,foreignField, dropTableRecord.getValue(primaryField.getFieldIndex()));
        }
      }
      trans.commit();
      httpBrowser.refresh(context,dragTableRecord);
    }
    finally
    {
      trans.close();
    }
  }

  public Icon getFeedback(Context context, IGuiElement dropElement,boolean canDrop, Object dragObject, Object hoverObject) throws Exception
  {
    if(canDrop)
      return Icon.accept;
    return Icon.delete;
  }

  public boolean validateDrop(Context context, IGuiElement dropElement, Object dragObject, Object targetObject) throws Exception
  {
    IDataBrowserRecord target = (IDataBrowserRecord)targetObject;
    if(target.getSaveStringValue(BoBrowser.browserType).equals(Bo.type_ENUM._folder))
      return true;
    return false;
  }
  
  @Override
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    HistoryManager.append(context, browser.getGroup());
  }

  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    String name = browser.getDefinition().getBrowserField(column).getName();
    if(!StringUtil.saveEquals(name, BoBrowser.browserName))
      return null;
    
    Object folderPkey = record.getValue(BoBrowser.browserFolder_key);
    if(folderPkey!=null)
      return Icon.folder;
    else
    {
      if(record.getintValue(BoBrowser.browserFavorite_index)==0)
        return Icon.page_white;
      else
        return Icon.page_white_add;
    }
  }
  
}

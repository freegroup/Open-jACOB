package jacob.event.ui.bo.documents;

import jacob.browser.BoBrowser;
import jacob.common.HistoryManager;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.util.StringUtil;

public class BoBrowserEventHandler extends IBrowserEventHandler
{
  
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

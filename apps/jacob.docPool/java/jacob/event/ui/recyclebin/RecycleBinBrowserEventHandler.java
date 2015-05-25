package jacob.event.ui.recyclebin;

import jacob.browser.RecyclebinBrowser;
import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class RecycleBinBrowserEventHandler extends IBrowserEventHandler
{
  @Override
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    // TODO Auto-generated method stub
  }

  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if(RecyclebinBrowser.browserOriginal_path.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      String type = record.getSaveStringValue(RecyclebinBrowser.browserType);
      if(type.equals(Recyclebin.type_ENUM._folder))
        return Icon.folder;
      return Icon.page_white;
    }
    return Icon.NONE;
  }
  
}

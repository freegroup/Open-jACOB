package jacob.event.ui.menutree;

import jacob.browser.MenutreeBrowser;
import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.impl.html.Browser.DefaultConnectByKeyDragDropBrowserEventHandler;

public final class MenutreeBrowserEventHandler extends DefaultConnectByKeyDragDropBrowserEventHandler
{
  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (MenutreeBrowser.browserTitle.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      String state = record.getSaveStringValue(MenutreeBrowser.browserState);
      if (Menutree.state_ENUM._locked.equals(state))
      {
        return Icon.lock;
      }
      // different icons for menutree entries with or without attached article
      return record.getValue(MenutreeBrowser.browserArticle_key) == null ? Icon.folder : Icon.folder_table;
    }
    return Icon.NONE;
  }

  @Override
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    
  }
}

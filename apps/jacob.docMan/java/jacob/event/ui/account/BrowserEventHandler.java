package jacob.event.ui.account;
import jacob.browser.AccountBrowser;
import jacob.model.Account;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;
public class BrowserEventHandler extends IBrowserEventHandler
{
  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (column == 1)
    {
      if (record.getSaveStringValue(AccountBrowser.browserRole).equals(Account.role_ENUM._administrator))
        return Icon.user_orange;
      if (record.getSaveStringValue(AccountBrowser.browserRole).equals(Account.role_ENUM._anonymous))
        return Icon.user_go;
      return Icon.blank;
    }
    return Icon.NONE;
  }

  @Override
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
    // TODO Auto-generated method stub
  }
}

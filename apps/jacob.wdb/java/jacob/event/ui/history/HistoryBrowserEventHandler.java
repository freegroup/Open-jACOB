package jacob.event.ui.history;

import jacob.browser.HistoryBrowser;
import jacob.common.PrettyDate;
import jacob.model.History;

import java.util.Date;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class HistoryBrowserEventHandler extends IBrowserEventHandler
{
  @Override
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
  }

  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (HistoryBrowser.browserCreate_date.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      return Icon.time;
    }
    else if (HistoryBrowser.browserArticle.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      return Icon.table;
    }
    return Icon.NONE;
  }

  @Override
  public String filterCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    String name = browser.getData().getBrowserDefinition().getBrowserField(column).getName();
    if(name.equals(HistoryBrowser.browserCreate_date))
    {
      Date date = record.getDateValue(HistoryBrowser.browserCreate_date);
      return PrettyDate.toString(date);
    }
    return value;
  }

  @Override
  public void onDataChanged(IClientContext context, IBrowser browser, IDataBrowser data) throws Exception
  {
    for(int i=0;i<data.recordCount();i++)
    {
      IDataBrowserRecord browserRecord = data.getRecord(i);
      String action = browserRecord.getSaveStringValue(HistoryBrowser.browserType);
      if(action.equals(History.type_ENUM._delete))
        browser.setErrorDecoration(context, i, "Objekt wurde gelöscht");
    }
  }
  
}

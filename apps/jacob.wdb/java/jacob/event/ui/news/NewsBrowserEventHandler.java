package jacob.event.ui.news;

import java.util.Date;

import jacob.browser.NewsBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class NewsBrowserEventHandler extends IBrowserEventHandler
{

  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    String name = browser.getDefinition().getBrowserField(column).getName();
    if(NewsBrowser.browserHeadline.equals(name))
    {
      Date now = new Date();
      now.setYear(70);
      long start = record.getlongValue(NewsBrowser.browserNormalized_start);
      long end = record.getlongValue(NewsBrowser.browserNormalized_end);
      if(start<=now.getTime() && end>=now.getTime())
        return Icon.bell;
      return Icon.blank;
    }
    return Icon.NONE; 
  }

  @Override
  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
  }
}

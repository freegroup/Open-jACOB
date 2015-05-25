/*
 * Created on 06.02.2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jacob.event.ui.japplversion;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

/**
 * 
 * 
 * @author Andreas Sonntag
 */
public class JapplversionBrowserEventHandler extends IBrowserEventHandler
{
  public String filterCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if ("browserVersion".equals(record.getBrowser().getBrowserDefinition().getBrowserField(column).getName()))
    {
      int fix = record.getintValue("browserFix");
      if (fix > 0)
        return value + "." + fix;
    }
    return value;
  }

  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
  {
  }
}

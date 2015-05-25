package jacob.event.ui.request;

import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Request;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class RequestBrowser extends IBrowserEventHandler
{

  @Override
  public void onRecordSelect(IClientContext arg0, IBrowser arg1, IDataTableRecord arg2) throws Exception
  {
    TabManagerRequest.setActive(arg0, Request.NAME);
  }

}

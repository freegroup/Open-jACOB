package jacob.event.ui.customer;

import jacob.common.GroupManagerRequestHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.event.ui.request.RequestHeaderGroup;
import jacob.model.Customer;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class CustomerBrowser extends IBrowserEventHandler
{

  @Override
  public void onRecordSelect(IClientContext context, IBrowser arg1, IDataTableRecord arg2) throws Exception
  {
    if(!GroupManagerRequestHeader.isSelected(context))
      TabManagerRequest.setActive(context, Customer.NAME);
  }

}

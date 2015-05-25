package jacob.event.ui.company;

import jacob.common.GroupManagerRequestHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Company;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBrowserEventHandler;

public class CompanyBrowser extends IBrowserEventHandler
{

  @Override
  public void onRecordSelect(IClientContext context, IBrowser arg1, IDataTableRecord arg2) throws Exception
  {
    if(!GroupManagerRequestHeader.isSelected(context))
      TabManagerRequest.setActive(context, Company.NAME);
  }

}

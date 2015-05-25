/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Feb 10 13:49:43 CET 2009
 */
package jacob.event.ui.company;

import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
import jacob.model.Customer;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you want receive
 * the onClick events of the user.
 * 
 * @author achim
 */
public class CompanyContactDownImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    IDataTable custTable = context.getDataAccessor().getTable(Customer.NAME);
    IDataTableRecord custRecord = custTable.getSelectedRecord();
    MasterDetailManager.onDownClicked(MasterDetailManager_CompanyContact.INSTANCE, context);
    if (custRecord != null)
      custTable.setSelectedRecord(custRecord.getPrimaryKeyValue());
  }
}

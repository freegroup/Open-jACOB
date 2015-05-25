/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 14:50:11 CET 2008
 */
package jacob.event.ui.company;


import jacob.model.Company;
import jacob.model.Customer;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class CompanyUnlinkImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    IDataTableRecord selected = context.getSelectedRecord();
    IDataTableRecord customerRecord = context.getDataTable(Customer.NAME).getSelectedRecord();
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      context.getDataTable().clear();
      customerRecord.setLinkedRecord(trans, selected);
      trans.commit();
      element.setVisible(false);
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * The event handler if the group status has been changed.<br>
   * 
   * @param context The current work context of the jACOB application. 
   * @param status  The new state of the group.
   * @param emitter The emitter of the event.
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
  {
    image.setVisible(false);
    if(state==IGroup.SELECTED)
    {
      IDataTableRecord customerRecord = context.getDataTable(Customer.NAME).getSelectedRecord();
      if(customerRecord!=null)
      {
        String companyPkey = context.getSelectedRecord().getSaveStringValue(Company.pkey);
        String customerPkey = customerRecord.getSaveStringValue(Customer.company_key);
        image.setVisible(!companyPkey.equals(customerPkey));
      }
    }
  }
}


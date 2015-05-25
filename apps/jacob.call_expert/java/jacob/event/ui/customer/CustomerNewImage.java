/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 28 10:55:51 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.masterdetail.MasterDetailManager_CustomerContact;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Company;
import jacob.model.Customer;
import jacob.model.Customer_contact;
import jacob.model.Event;
import jacob.model.Event_edit_customer;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the onClick events of the user.
 * 
 * @author andherz
 */
public class CustomerNewImage extends IStaticImageEventHandler implements IOnClickEventHandler
{

  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    // show the corresponding TabPane in the center area
    String groupAliasName = element.getGroupTableAlias();
    TabManagerRequest.setActive(context, groupAliasName);

    IDataTableRecord companyRecord = context.getDataTable(Company.NAME).getSelectedRecord();
    IDataTransaction trans = context.getDataAccessor().newTransaction();

 // create the record via the UI-Group. This method propagates the input field into the new record.
 // The user inputs aren't lost    
//  IDataTableRecord customerRecord = Customer.newRecord(context, trans); 
     IDataTableRecord customerRecord = TabManagerRequest.getPane(context, Customer.NAME).newRecord(context, trans);
    
    IDataTableRecord eventRecord = Event_edit_customer.newRecord(context, trans);
    eventRecord.setValue(trans, Event.type, Event.type_ENUM._NewCustomer);

    trans.setProperty(MasterDetailManager_CustomerContact.CONTACT_ADDED, Boolean.FALSE);
    context.getDataTable(Customer_contact.NAME).clear();
    if (companyRecord != null)
    {
      customerRecord.setLinkedRecord(trans, companyRecord);
    }
  }

  /**
   * The event handler if the group status has been changed.<br>
   * 
   * @param context
   *          The current work context of the jACOB application.
   * @param status
   *          The new state of the group.
   * @param emitter
   *          The emitter of the event.
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
  {
    image.setEnable(state == IGroup.SEARCH || state == IGroup.SELECTED);
  }
}

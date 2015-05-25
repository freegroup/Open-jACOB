/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 26 14:48:02 CET 2008
 */
package jacob.event.ui.company;


import java.util.Iterator;

import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Company;
import jacob.model.Company_contact;
import jacob.model.Event;
import jacob.model.Event_edit_company;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class CompanyNewImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    String groupAliasName = element.getGroupTableAlias();
    IDataTable companyContact = context.getDataTable(Company_contact.NAME);
    companyContact.clear();
    
    
    //Inserted to clear the tab panes below. If i put the code in the lower tab-pane
    // the whole container woud be just white. (bug)
    //TODO: Achim see taht andreas fixes it!
    ITabContainer details = (ITabContainer) context.getForm().findByName("companyContainer");
    if (details != null)
    {
      for (Iterator i = details.getPanes().iterator(); i.hasNext();)
      {
        ITabPane childPane = (ITabPane)i.next();
        childPane.clear(context);
      }
    }
    TabManagerRequest.setActive(context, groupAliasName); 
    

    IDataTransaction trans = context.getDataAccessor().newTransaction();
    
// create the record via the UI-Group. This method propagates the input field into the new record.
// The user inputs aren't lost    
//    IDataTableRecord companyRecord = Company.newRecord(context, trans);
    IDataTableRecord companyRecord = TabManagerRequest.getPane(context, Company.NAME).newRecord(context, trans);
    
    IDataTableRecord eventRecord = Event_edit_company.newRecord(context, trans); 
    eventRecord.setValue(trans, Event.type, Event.type_ENUM._NewCompany);
    eventRecord.setValue(trans, Event.event_company_key, companyRecord.getValue(Company.pkey));

    //to make sure, that we will have at least one contact. This property will be checked in the table hook
    trans.setProperty(MasterDetailManager_CompanyContact.CONTACT_ADDED, Boolean.FALSE);
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
    image.setEnable(state == IGroup.SEARCH || state == IGroup.SELECTED );
  }
}


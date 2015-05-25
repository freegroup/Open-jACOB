/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 26 15:19:41 CET 2008
 */
package jacob.event.ui.company;


import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Company;
import jacob.model.Event;
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
public class CompanyEditImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    String groupAliasName = element.getGroupTableAlias();
    TabManagerRequest.setActive(context, groupAliasName); 

    IDataTransaction transaction= context.getDataAccessor().newTransaction();

    // Create a new Event record for this changes
    //
    Event.newRecord(context, transaction);
    
    // Retrieve the record to change and link them with the EVENT record
    //
    IDataTableRecord company = context.getDataTable(Company.NAME).getSelectedRecord();
    company.setValue(transaction, Company.pkey, company.getValue(Company.pkey));
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
    image.setVisible(state != IGroup.SEARCH && state != IGroup.NEW);
    image.setEnable(state == IGroup.SELECTED);
  }
}


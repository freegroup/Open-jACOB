/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 28 11:00:13 CET 2008
 */
package jacob.event.ui.request;


import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Customer;
import jacob.model.Event;
import jacob.model.Request;
import de.tif.jacob.core.data.IDataTable;
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
 * @author andherz
 */
public class RequestEditImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    String groupAliasName = element.getGroupTableAlias();
    TabManagerRequest.setActive(context, groupAliasName); 
    
    // Create a new Event record for this changes
    //
    IDataTransaction trans;
    IDataTable eventTable = context.getDataTable(Event.NAME);
    IDataTableRecord eventRecord = eventTable.getSelectedRecord();
    
    if(eventRecord!=null && eventRecord.isNew())
    {
      trans = eventRecord.getCurrentTransaction();
 //     eventRecord = context.getDataAccessor().cloneRecord(trans, eventRecord);
    }
    else
    {
      trans = context.getDataAccessor().newTransaction();
      eventRecord = Event.newRecord(context,trans);
      eventRecord.setValue(trans, Event.type, Event.type_ENUM._ChangeRequest);
   }

    // Retrieve the record to change and link them with the EVENT record
    //
    IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
    request.setValue(trans, Request.pkey, request.getValue(Request.pkey));
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


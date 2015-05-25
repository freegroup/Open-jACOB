/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 14 10:53:36 CET 2009
 */
package jacob.event.ui.event;

import jacob.common.GroupManagerRequestHeader;
import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Event;
import jacob.model.Request;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.screen.event.IOnClickEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class EventAttachLabel extends ILabelEventHandler implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: EventAttachLabel.java,v 1.2 2009/07/01 11:48:26 A.Herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {



    IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
    
    if (request ==null)
    {
      IGroup group = GroupManagerRequestHeader.get(context);
      context.showTipDialog(group.findByName("requestFilterText"), "Search and select Request first before attaching a new event.");
      return;
    }

    IDataTable event = context.getDataTable(Event.NAME);


    IDataTableRecord eventrecord = event.getSelectedRecord();
    if (null == eventrecord)
       eventrecord = event.newRecord(event.getAccessor().newTransaction());
 
    IDataTransaction transaction;

    if (GroupManagerRequestHeader.isNewOrUpdate(context))
    {
      transaction = eventrecord.getCurrentTransaction();

    }
    else
    {
      transaction = eventrecord.getAccessor().newTransaction();
      request.setValue(transaction, Request.pkey, request.getValue(Request.pkey));
    }
    
    TabManagerRequest.setActive(context, Request.NAME);
    ITabContainer requestdetailContainer = (ITabContainer) context.getForm().findByName("requestDetailContainer");
    requestdetailContainer.setActivePane(context, 1);
    
    // bring the Request in the update mode too.
    request.setValue(transaction, Request.pkey, request.getValue(Request.pkey));
  }

  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    // your code here...
  }

}

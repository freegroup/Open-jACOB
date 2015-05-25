/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 14 10:51:03 CET 2009
 */
package jacob.event.ui.event;

import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Event;
import jacob.model.Model;
import jacob.model.Product;
import jacob.model.Request;
import jacob.model.Serial;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
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
public class EventCreateLabel extends ILabelEventHandler implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: EventCreateLabel.java,v 1.2 2009/07/01 11:48:26 A.Herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    TabManagerRequest.setActive(context, Request.NAME);
    ITabContainer requestdetailContainer = (ITabContainer) context.getForm().findByName("requestDetailContainer");
    requestdetailContainer.setActivePane(context, 1);
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    Request.newRecord(context, trans);
    IDataTableRecord eventrecord = Event.newRecord(context, trans);
    context.getDataTable(Serial.NAME).clear();
    context.getDataTable(Model.NAME).clear();
    context.getDataTable(Product.NAME).clear();
  }

  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    // your code here...
  }

}

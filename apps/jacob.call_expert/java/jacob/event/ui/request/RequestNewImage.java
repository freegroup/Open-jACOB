/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Nov 28 11:04:44 CET 2008
 */
package jacob.event.ui.request;


import jacob.common.tabcontainer.TabManagerRequest;
import jacob.model.Company;
import jacob.model.Event;
import jacob.model.Model;
import jacob.model.Product;
import jacob.model.Request;
import jacob.model.Serial;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.IDataTransaction.EmbeddedTransactionMode;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you want receive
 * the onClick events of the user.
 * 
 * @author andherz
 */
public class RequestNewImage extends IStaticImageEventHandler implements IOnClickEventHandler
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
      IDataTransaction topTrans = trans.newEmbeddedTransaction(EmbeddedTransactionMode.PREPEND);
      // create the record via the UI-Group. This method propagates the input field into the new record.
      // The user inputs aren't lost    
      //Request.newRecord(context, topTrans);
      TabManagerRequest.getPane(context, Request.NAME).newRecord(context, topTrans);
    }
    else
    {
      trans = context.getDataAccessor().newTransaction();
      // create the record via the UI-Group. This method propagates the input field into the new record.
      // The user inputs aren't lost    
      // Request.newRecord(context, trans);
      TabManagerRequest.getPane(context, Request.NAME).newRecord(context, trans);
      
      eventRecord = Event.newRecord(context,trans);
      eventRecord.setValue(trans, Event.type, Event.type_ENUM._NewRequest);
   }


    context.getDataTable(Serial.NAME).clear();
    context.getDataTable(Model.NAME).clear();
    context.getDataTable(Product.NAME).clear();

    TabManagerRequest.getContainer(context).findByName("requestDescription").requestFocus();
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

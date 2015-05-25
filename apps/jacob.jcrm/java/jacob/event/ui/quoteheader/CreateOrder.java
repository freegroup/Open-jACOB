package jacob.event.ui.quoteheader;

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Wed Oct 12 03:04:11 CEST 2005
 *
 */
import java.util.HashSet;
import java.util.Set;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The Event handler for the CreateOrder-Button.<br>
 * The onAction will be called, if the user clicks on this button.<br>
 * Insert your custom code in the onAction-method.<br>
 * 
 * @author andreas
 *
 */
public class CreateOrder extends IButtonEventHandler
{
  /**
   * Valid quto states for creating an order
   */
  private static final Set enableStates = new HashSet();

  static
  {
    enableStates.add("Open");
    enableStates.add("Calculated");
    enableStates.add("Released");
  }

  /**
   * The user has been click on the corresponding button.
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord quoteheader = context.getSelectedRecord();
    IDataTable orderheaderTable = context.getDataTable("orderheader");
    IDataTransaction trans = orderheaderTable.startNewTransaction();
    
    // lock and refresh quote record
    // This will fail, if quote is locked by somebody else.
    trans.lock(quoteheader);
    quoteheader = quoteheader.getTable().loadRecord(quoteheader.getPrimaryKeyValue());

    // copy header
    //
    IDataTableRecord orderheader = orderheaderTable.newRecord(trans);
    orderheader.setValue(trans, "amount", quoteheader.getValue("amount"));
    orderheader.setValue(trans, "description", quoteheader.getValue("description"));
    orderheader.setValue(trans, "contact_key", quoteheader.getValue("contact_key"));
    orderheader.setValue(trans, "organization_key", quoteheader.getLinkedRecord("quoteContact").getValue("organization_key"));
    orderheader.setLinkedRecord(trans, quoteheader);

    // copy line items
    //
    IDataTable quotelineitemTable = context.getDataTable("quotelineitem");
    IDataTable orderlineitemTable = context.getDataTable("orderlineitem");
    quotelineitemTable.qbeClear();
    quotelineitemTable.qbeSetKeyValue("quoteheader_key", quoteheader.getValue("pkey"));
    quotelineitemTable.search();
    for (int i = 0; i < quotelineitemTable.recordCount(); i++)
    {
      IDataTableRecord quotelineitem = quotelineitemTable.getRecord(i);

      IDataTableRecord orderlineitem = orderlineitemTable.newRecord(trans);
      orderlineitem.setValue(trans, "description", quotelineitem.getValue("description"));
      orderlineitem.setValue(trans, "orderpart_key", quotelineitem.getValue("orderpart_key"));
      orderlineitem.setValue(trans, "position_amount", quotelineitem.getValue("position_amount"));
      orderlineitem.setValue(trans, "price", quotelineitem.getValue("price"));
      orderlineitem.setValue(trans, "quantity", quotelineitem.getValue("quantity"));
      orderlineitem.setValue(trans, "uom", quotelineitem.getValue("uom"));
      orderlineitem.setLinkedRecord(trans, orderheader);
    }

    // switch form
    context.setCurrentForm("order");

//    orderheader.getAccessor().propagateRecord(orderheader, "r_order", Filldirection.BOTH);
    trans.commit();

  }

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // an order could only be created if the quote has a proper status
    button.setEnable(status == IGuiElement.SELECTED && enableStates.contains(context.getSelectedRecord().getStringValue("status")));
  }
}

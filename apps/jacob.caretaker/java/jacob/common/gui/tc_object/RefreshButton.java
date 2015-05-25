/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 27 14:24:35 CEST 2006
 */
package jacob.common.gui.tc_object;

import jacob.common.tc.TC;
import jacob.exception.BusinessException;

import java.util.Map;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the RefreshButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class RefreshButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: RefreshButton.java,v 1.3 2006/08/10 19:13:54 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  private static final String PROCEED_BUTTON_ID = "proceed";
  private static final String SHOW_ORDER_BUTTON_ID = "show";

  private static class CustomerOrderExistsCallback implements IFormDialogCallback
  {
    private final IDataBrowser orderBrowser;

    private CustomerOrderExistsCallback(IDataBrowser orderBrowser)
    {
      this.orderBrowser = orderBrowser;
    }

    public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
    {
      if (PROCEED_BUTTON_ID.equals(buttonId))
      {
        doit(context);
      }
      else if (SHOW_ORDER_BUTTON_ID.equals(buttonId))
      {
        showOrders(context, orderBrowser, true);
      }
    }
  }
  
  private static class ObjectOrderExistsCallback implements IOkCancelDialogCallback
  {
    private final IDataBrowser orderBrowser;
    
    private ObjectOrderExistsCallback(IDataBrowser orderBrowser)
    {
      this.orderBrowser = orderBrowser;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      showOrders(context, orderBrowser, false);
    }
  }
  
	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
    IDataTableRecord activeCampaign = TC.getActiveCampagne(context);
    
    // check whether the given object already has an order in the future
    //
    IDataTableRecord objectRecord = context.getSelectedRecord();
    IDataAccessor accessor = context.getDataAccessor();
    accessor.qbeClearAll();
    
    IDataTable orderTable = accessor.getTable("tc_order");
    orderTable.qbeSetKeyValue("tc_object_key", objectRecord.getValue("pkey"));
    
    IDataTable capacityTable = accessor.getTable("tc_capacity");
    capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
    capacityTable.qbeSetValue("slot", ">now");
    
    IDataBrowser orderBrowser = accessor.getBrowser("tc_orderBrowser");
    orderBrowser.search("r_tc_order", Filldirection.BACKWARD);
    if (orderBrowser.recordCount() > 0)
    {
      IOkCancelDialog dialog = context.createOkCancelDialog("Für das Fahrzeug wurde bereits eine Buchung angelegt.\\n Buchung des Fahrzeuges anzeigen?", new ObjectOrderExistsCallback(orderBrowser));
      dialog.show();
      return;
    }

    // check whether a customer has been selected
    //
    IDataTableRecord customerRecord = context.getDataTable("tc_customer").getSelectedRecord();
    if (customerRecord == null)
    {
      throw new BusinessException("Es wurde bisher kein Kunde ausgewählt!");
    }
    
    // check whether the given customer already has an order in the future
    //
    accessor.qbeClearAll();
    orderTable.qbeSetKeyValue("tc_customer_key", customerRecord.getValue("pkey"));
    capacityTable.qbeSetKeyValue("tc_campaign_key", activeCampaign.getValue("pkey"));
    capacityTable.qbeSetValue("slot", ">now");
    
    orderBrowser.search("r_tc_order", Filldirection.BACKWARD);
    if (orderBrowser.recordCount() > 0)
    {
      FormLayout layout = new FormLayout("10dlu,p,10dlu", "20dlu,p,20dlu");

      IFormDialog dialog = context.createFormDialog("Buchung vorhanden", layout, new CustomerOrderExistsCallback(orderBrowser));
      CellConstraints c = new CellConstraints();
      if (orderBrowser.recordCount() == 1)
        dialog.addLabel("Der Kunde hat bereits eine Buchung beauftragt. Mit der Terminberechnung fortfahren oder Buchung anzeigen?", c.xy(1, 1));
      else
        dialog.addLabel("Der Kunde hat bereits mehrere Buchungen beauftragt. Mit der Terminberechnung fortfahren oder Buchungen anzeigen?", c.xy(1, 1));

      dialog.addSubmitButton(PROCEED_BUTTON_ID, "Fortfahren");
      if (orderBrowser.recordCount() == 1)
        dialog.addSubmitButton(SHOW_ORDER_BUTTON_ID, "Buchung anzeigen");
      else
        dialog.addSubmitButton(SHOW_ORDER_BUTTON_ID, "Buchungen anzeigen");
      dialog.setCancelButton("Abbrechen");

      dialog.show(350, 120);
      return;
    }

    // everything already -> proceed
    doit(context);
	}
  
  private static void doit(IClientContext context) throws Exception
  {
    ChartState.createChartState(context);
    ((IChart) context.getGroup().findByName("slotSelectChart")).refresh();
  }
   
  private static void showOrders(IClientContext context, IDataBrowser orderBrowser, boolean propagateFirst) throws Exception
  {
    // Wenn es nur einen Order-Record gibt, dann diesen gleich zurückfüllen
    //
    if (orderBrowser.recordCount() == 1 || (orderBrowser.recordCount() > 1 && propagateFirst))
    {
      orderBrowser.setSelectedRecordIndex(0);
      orderBrowser.propagateSelections();
    }
    
    // Searchbrowser in den Vordergrund bringen
    //
    IGroup group = (IGroup) context.getForm().findByName("tc_orderGroup");
    if (group != null)
    {
      context.getForm().setCurrentBrowser(group.getBrowser());
    }
  }

	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
	}
}


package jacob.common.masterdetail;

import jacob.model.Customer;
import jacob.model.Customer_contact;
import jacob.model.Customer_contact_type;
import jacob.model.Request;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IStyledText;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;

public class MasterDetailManager_CustomerContact extends MasterDetailManagerConfiguration
{
    public final static String CONTACT_ADDED= "CustomerContactAdded";
    public final static MasterDetailManager_CustomerContact INSTANCE = new MasterDetailManager_CustomerContact();

    public String getPropagationRelationset(){return IRelationSet.DEFAULT_NAME;}
    public String getButtonDelete() { return "customer_contactDeleteImage"; }
    public String getButtonDown() { return "customerContactDownImage"; }
    public String getButtonNew() { return "customer_contactNewImage"; }
    public String getButtonTop() { return "customerContactTopImage"; }
    public String getButtonUp() { return "customerContactUpImage"; }
    public String getDetailAlias() { return Customer_contact.NAME; }
    public String getDetailContainer() { return "customerContactDetailContainer"; }
    public String getFkey() { return  Customer_contact.customer_key; }
    public String getForeignAlias() { return Customer.NAME; }
    public String getMasterList() { return "customerContactMasterListbox"; }
    public String getPkey() { return Customer.pkey; }
    public String getSortField() { return Customer_contact.sort_id; }
    public String getTransactionProperty() { return CONTACT_ADDED; }

    public boolean isRequired() { return true; }

    public String getButtonUpdate()  {   return "customer_contactUpdateImage";  }

    private static String BUTTON_OUTBOUNDCALL   = "customer_contactOutboundContactButton";
    private static String INFOTEXT_OUTBOUNDCALL = "customer_contactOutboundInfoText";

    public void onDetailBackfill(IClientContext context, IDataTableRecord selectedRecord) throws Exception
    {
        if (selectedRecord == null)
        {
            MasterDetailManager.getElement(context, BUTTON_OUTBOUNDCALL).setVisible(false);
            MasterDetailManager.getElement(context, INFOTEXT_OUTBOUNDCALL).setVisible(false);
        }
        else
        {
            MasterDetailManager.getElement(context, BUTTON_OUTBOUNDCALL).setVisible(true);
            // determine the correct error message
            //
            IStyledText info =(IStyledText)MasterDetailManager.getElement(context, INFOTEXT_OUTBOUNDCALL);
            IDataTableRecord typeRecord = selectedRecord.getLinkedRecord(Customer_contact_type.NAME);
            boolean hasCallback = typeRecord != null && typeRecord.getValue(Customer_contact_type.java_outbound_handler_class) != null;
            if (hasCallback)
            {
                IDataTableRecord requestRecord = context.getDataTable(Request.NAME).getSelectedRecord();
                if(requestRecord == null)
                {
                    MasterDetailManager.getElement(context, INFOTEXT_OUTBOUNDCALL).setVisible(true);
                    MasterDetailManager.getElement(context, BUTTON_OUTBOUNDCALL).setEnable(false);
                    info.setLabel(I18N.STYLEDTEXT_OUTBOUND_NO_REQUEST.get(context));
                }
                else
                {
                    MasterDetailManager.getElement(context, INFOTEXT_OUTBOUNDCALL).setVisible(false);
                    MasterDetailManager.getElement(context, BUTTON_OUTBOUNDCALL).setEnable(true);
                }
            }
            else
            {
                MasterDetailManager.getElement(context, BUTTON_OUTBOUNDCALL).setEnable(false);
                MasterDetailManager.getElement(context, INFOTEXT_OUTBOUNDCALL).setVisible(true);
                info.setLabel(I18N.STYLEDTEXT_OUTBOUND_NO_CTI.get(context));
            }
        }
        ((GuiHtmlElement)MasterDetailManager.getElement(context, BUTTON_OUTBOUNDCALL)).calculateHTML((ClientContext)context);
        ((GuiHtmlElement)MasterDetailManager.getElement(context, INFOTEXT_OUTBOUNDCALL)).calculateHTML((ClientContext)context);
    }
}

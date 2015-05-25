package jacob.common.masterdetail;

import jacob.model.Company;
import jacob.model.Company_contact;
import jacob.model.Company_contact_type;
import jacob.model.Request;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IStyledText;
import de.tif.jacob.screen.impl.html.ClientContext;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;

public class MasterDetailManager_CompanyContact extends MasterDetailManagerConfiguration
{
    public final static String CONTACT_ADDED ="CompanyContactAdded";

    public final static MasterDetailManager_CompanyContact INSTANCE = new MasterDetailManager_CompanyContact();

    public String getPropagationRelationset(){return IRelationSet.DEFAULT_NAME;}
    public String getButtonDelete() { return "companyDeleteImage"; }
    public String getButtonDown() { return "companyContactDownImage"; }
    public String getButtonNew() { return "companyaddImage"; }
    public String getButtonTop() { return "companyContactTopImage"; }
    public String getButtonUp() { return "companyContactUpImage"; }
    public String getDetailAlias() { return Company_contact.NAME; }
    public String getDetailContainer() { return "companyContactDetailContainer"; }
    public String getFkey() { return  Company_contact.company_key; }
    public String getForeignAlias() { return Company.NAME; }
    public String getMasterList() { return "companyContactMasterListbox"; }
    public String getPkey() { return Company.pkey; }
    public String getSortField() { return Company_contact.sort_id; }
    public String getTransactionProperty() { return CONTACT_ADDED; }

    public boolean isRequired() { return false; }

    public String getButtonUpdate() { return "companyUpdateImage"; }


    private static String BUTTON_OUTBOUNDCALL   = "company_contactOutboundContactButton";
    private static String INFOTEXT_OUTBOUNDCALL = "company_contactOutboundInfoText";

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
            IDataTableRecord typeRecord = selectedRecord.getLinkedRecord(Company_contact_type.NAME);
            boolean hasCallback = typeRecord != null && typeRecord.getValue(Company_contact_type.java_outbound_handler_class)!=null;
            if (hasCallback)
            {
                IDataTableRecord requestRecord = context.getDataTable(Request.NAME).getSelectedRecord();
                if (requestRecord == null)
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

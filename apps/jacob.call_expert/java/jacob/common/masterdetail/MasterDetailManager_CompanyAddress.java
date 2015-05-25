package jacob.common.masterdetail;

import jacob.model.Company;
import jacob.model.Company_address;
import de.tif.jacob.core.definition.IRelationSet;

public class MasterDetailManager_CompanyAddress extends MasterDetailManagerConfiguration
{
    public final static MasterDetailManagerConfiguration INSTANCE = new MasterDetailManager_CompanyAddress();

    public String getPropagationRelationset(){return IRelationSet.DEFAULT_NAME;}

    public String getButtonDelete() { return "company_AddressDeleteImage"; }
    public String getButtonDown() { return "companyAddressDownImage"; }
    public String getButtonNew() { return "company_AddressNewImage"; }
    public String getButtonTop() { return "companyAddressTopImage"; }
    public String getButtonUp() { return "companyAddressUpImage"; }
    public String getDetailAlias() { return Company_address.NAME; }
    public String getDetailContainer() { return "companyAddressDetailContainer"; }
    public String getFkey() { return  Company_address.company_key; }
    public String getForeignAlias() { return Company.NAME; }
    public String getMasterList() { return "companyAddressMasterListbox"; }
    public String getPkey() { return Company.pkey; }
    public String getSortField() { return Company_address.sort_id; }
    public String getTransactionProperty() { return "CompanyAddressAdded"; }

    public boolean isRequired() { return false; }

    public String getButtonUpdate()  { return "company_addressUpdateImage";  }
}

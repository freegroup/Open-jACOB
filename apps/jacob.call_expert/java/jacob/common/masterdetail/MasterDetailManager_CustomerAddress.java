package jacob.common.masterdetail;

import jacob.model.Customer;
import jacob.model.Customer_address;
import de.tif.jacob.core.definition.IRelationSet;

public class MasterDetailManager_CustomerAddress extends MasterDetailManagerConfiguration
{
    public final static MasterDetailManager_CustomerAddress INSTANCE = new MasterDetailManager_CustomerAddress();

    public String getPropagationRelationset(){return IRelationSet.DEFAULT_NAME;}
    public String getButtonDelete() { return "customer_addressDeleteImage"; }
    public String getButtonDown() { return "customerAddressDownImage"; }
    public String getButtonNew() { return "customer_addressNewImage"; }
    public String getButtonTop() { return "customerAddressTopImage"; }
    public String getButtonUp() { return "customerAddressUpImage"; }
    public String getDetailAlias() { return Customer_address.NAME; }
    public String getDetailContainer() { return "customerAddressDetailContainer"; }
    public String getFkey() { return  Customer_address.customer_key; }
    public String getForeignAlias() { return Customer.NAME; }
    public String getMasterList() { return "customerAddressMasterListbox"; }
    public String getPkey() { return Customer.pkey; }
    public String getSortField() { return Customer_address.sort_id; }
    public String getTransactionProperty() { return "CustomerAddressAdded"; }

    public boolean isRequired() { return false; }

    public String getButtonUpdate()  {   return "customer_addressUpdateImage";  }
}

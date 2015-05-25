package jacob.common.masterdetail;

import jacob.model.Customer;
import jacob.model.Customer_bank_account;
import de.tif.jacob.core.definition.IRelationSet;

public class MasterDetailManager_CustomerBankAccount extends MasterDetailManagerConfiguration
{
    public final static MasterDetailManager_CustomerBankAccount INSTANCE = new MasterDetailManager_CustomerBankAccount();

    public String getPropagationRelationset(){return IRelationSet.DEFAULT_NAME;}
    public String getButtonDelete() { return "customer_bank_accountDeleteImage"; }
    public String getButtonDown() { return "customerBankAccountDownImage"; }
    public String getButtonNew() { return "customer_bank_accountNewImage"; }
    public String getButtonTop() { return "customerBankAccountTopImage"; }
    public String getButtonUp() { return "customerBankAccountUpImage"; }
    public String getDetailAlias() { return Customer_bank_account.NAME; }
    public String getDetailContainer() { return "customerBankAccountDetailContainer"; }
    public String getFkey() { return  Customer_bank_account.customer_key; }
    public String getForeignAlias() { return Customer.NAME; }
    public String getMasterList() { return "customerBankAccountMasterListbox"; }
    public String getPkey() { return Customer.pkey; }
    public String getSortField() { return Customer_bank_account.sort_id; }
    public String getTransactionProperty() { return "CustomerBankAccountAdded"; }

    public boolean isRequired() { return false; }

    public String getButtonUpdate()  {   return "customer_bank_accountUpdateImage";  }
}

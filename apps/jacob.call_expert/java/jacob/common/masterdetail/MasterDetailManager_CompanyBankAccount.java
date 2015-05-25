package jacob.common.masterdetail;

import jacob.model.Company;
import jacob.model.Company_bank_account;
import de.tif.jacob.core.definition.IRelationSet;

public class MasterDetailManager_CompanyBankAccount extends MasterDetailManagerConfiguration
{
    public final static MasterDetailManager_CompanyBankAccount INSTANCE = new MasterDetailManager_CompanyBankAccount();

    public String getPropagationRelationset(){return IRelationSet.DEFAULT_NAME;}
    public String getButtonDelete() { return "company_bank_accountDeleteImage"; }
    public String getButtonDown() { return "companyBankAccountDownImage"; }
    public String getButtonNew() { return "company_bank_accountNewImage"; }
    public String getButtonTop() { return "companyBankAccountTopImage"; }
    public String getButtonUp() { return "companyBankAccountUpImage"; }
    public String getDetailAlias() { return Company_bank_account.NAME; }
    public String getDetailContainer() { return "companyBankAccountDetailContainer"; }
    public String getFkey() { return  Company_bank_account.company_key; }
    public String getForeignAlias() { return Company.NAME; }
    public String getMasterList() { return "companyBankAccountMasterListbox"; }
    public String getPkey() { return Company.pkey; }
    public String getSortField() { return Company_bank_account.sort_id; }
    public String getTransactionProperty() { return "CompanyBankAccountAdded"; }

    public boolean isRequired() { return false; }

    public String getButtonUpdate()  {   return "company_bank_accountUpdateImage";  }
}

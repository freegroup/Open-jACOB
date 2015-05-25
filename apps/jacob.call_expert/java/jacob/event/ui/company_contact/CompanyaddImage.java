/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Sep 23 16:50:33 CEST 2009
 */
package jacob.event.ui.company_contact;

import jacob.common.UIUtil;
import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
import jacob.model.Company_contact;
import jacob.model.Contact_type;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the onClick events of the user.
 *
 * @author achim
 */
public class CompanyaddImage extends IStaticImageEventHandler implements IOnClickEventHandler
{

  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    context.setPropertyForWindow("DETAILCHANGED", true);
    ITableListBox listbox = (ITableListBox) context.getForm().findByName("companyContactMasterListbox");
    ITableDefinition tableDef = listbox.getData().getTableAlias().getTableDefinition();
    ITableField field = tableDef.getTableField(Company_contact.contact);
    // Validation
    IForeignField foreignField = (IForeignField) context.getGroup().findByName("company_contactCompany_contact_type");
    IDataTableRecord contactTypeRecord = foreignField.getSelectedRecord(context);
    if (contactTypeRecord != null)
    {
        String input = context.getGroup().getInputFieldValue("company_contactContact");
        input = UIUtil.validateContact(context, input, contactTypeRecord.getSaveStringValue(Contact_type.pkey), field);
        context.getGroup().setInputFieldValue("company_contactContact", input);
    }
    // End Validation
    MasterDetailManager.onNewClicked(MasterDetailManager_CompanyContact.INSTANCE, context);
  }

}

/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Wed Sep 23 17:27:54 CEST 2009
 */
package jacob.event.ui.company_contact;


import jacob.common.UIUtil;
import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManager_CompanyContact;
import jacob.model.Company_contact;
import jacob.model.Company_contact_type;
import jacob.model.Contact_type;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 *
 * @author achim
 */
public class CompanyUpdateImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
    public void onClick(IClientContext context, IGuiElement element) throws Exception
    {
        context.setPropertyForWindow("DETAILCHANGED", true);
        ITableListBox listbox = (ITableListBox) context.getForm().findByName("companyContactMasterListbox");
        IDataBrowserRecord record = listbox.getSelectedBrowserRecord(context);
        if (record == null)
            return;
        //Validation
        IDataTableRecord tableRecord = record.getTableRecord();
        IForeignField foreignField = (IForeignField) context.getGroup().findByName("company_contactCompany_contact_type");
        IDataTableRecord contactTypeRecord = foreignField.getSelectedRecord(context);
        if (contactTypeRecord != null)
        {
            ITableField field = tableRecord.getFieldDefinition(Company_contact.contact);
            String input = context.getGroup().getInputFieldValue("company_contactContact");
            input = UIUtil.validateContact(context, input, contactTypeRecord.getSaveStringValue(Contact_type.pkey), field);
            context.getGroup().setInputFieldValue("company_contactContact", input);
        }
        // End Validation
        MasterDetailManager.beforeSelect(MasterDetailManager_CompanyContact.INSTANCE, context, record);
        int recno = listbox.getSelectedBrowserRecord(context).getBrowser().getSelectedRecordIndex();
        listbox.setSelectedRecordIndex(context, -1);
        listbox.setSelectedRecordIndex(context, recno);
        context.showTransparentMessage("Record Updated");

        IDataTableRecord contactRecord = listbox.getSelectedRecord(context);
        if (contactRecord != null && listbox.getData().getSelectedRecordIndex() == 0)
        {
            ILabel label = (ILabel)context.getForm().findByName("PrimContact");
            String contact = contactRecord.getLinkedRecord(Company_contact_type.NAME).getSaveStringValue(Company_contact_type.type) + " - " +
                             contactRecord.getSaveStringValue(Company_contact.contact);
            label.setLabel(contact);
        }
    }
}

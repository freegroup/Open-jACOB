/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Mon Oct 05 15:04:51 CEST 2009
 */
package jacob.event.ui.customer_contact;

import jacob.common.UIUtil;
import jacob.common.masterdetail.MasterDetailManager;
import jacob.common.masterdetail.MasterDetailManagerConfiguration;
import jacob.common.masterdetail.MasterDetailManager_CustomerContact;
import jacob.model.Contact_type;
import jacob.model.Customer_contact;
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
public class Customer_contactNewImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    MasterDetailManagerConfiguration conf = MasterDetailManager_CustomerContact.INSTANCE;
    context.setPropertyForWindow("DETAILCHANGED", true);
    ITableListBox listbox = (ITableListBox) context.getForm().findByName(conf.getMasterList());
    ITableDefinition tableDef = listbox.getData().getTableAlias().getTableDefinition();
    ITableField field = tableDef.getTableField(Customer_contact.contact);
    // Validation
    IForeignField foreignField = (IForeignField) context.getGroup().findByName("customer_contactCustomer_contact_type");
    IDataTableRecord contactTypeRecord = foreignField.getSelectedRecord(context);
    if (contactTypeRecord != null)
    {
        String input = context.getGroup().getInputFieldValue("customer_contactContact");
        input = UIUtil.validateContact(context, input, contactTypeRecord.getSaveStringValue(Contact_type.pkey), field);
        context.getGroup().setInputFieldValue("customer_contactContact", input);
    }
    // End Validation
    MasterDetailManager.onNewClicked(conf, context);
  }
}

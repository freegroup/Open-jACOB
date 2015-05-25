/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 10 12:16:49 CET 2009
 */
package jacob.event.ui.customer;

import jacob.common.AbstractShowExtendedFormLabel;
import jacob.model.Customer_contact;
import jacob.model.Customer_contact_type;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive
 * the onClick events of the user.
 *
 * @author achim
 */
public class CustomerContactLabel extends AbstractShowExtendedFormLabel
{
    /**
     * The internal revision control system id.
     */
    static public final transient String RCS_ID = "$Id: CustomerContactLabel.java,v 1.4 2009/11/23 11:33:42 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.4 $";

    /**
     * Will be called if the user select a record, pressed the update or new
     * button.
     */
    public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
    {
        label.setVisible(false);
        if (state == IGroup.SELECTED || state == IGroup.UPDATE)
        {
            label.setVisible(true);
            IDataTableRecord rec = null;
            IDataTable contact = context.getDataTable(Customer_contact.NAME);
            for (int i = 0; i < contact.recordCount(); i++)
            {
                if (rec == null || rec.getlongValue(Customer_contact.sort_id) < contact.getRecord(i).getlongValue(Customer_contact.sort_id))
                {
                    rec = contact.getRecord(i);
                }
            }
            label.setLabel(rec == null ? I18N.LABEL_UNSET.get(context) : rec.getSaveStringValue(Customer_contact.displaycontact));
        }
    }
}

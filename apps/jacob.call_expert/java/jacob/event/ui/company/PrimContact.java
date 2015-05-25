/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Aug 11 11:28:16 CEST 2009
 */
package jacob.event.ui.company;



import jacob.browser.Company_contactBrowser;
import jacob.model.Company_contact;
import jacob.model.Company_contact_type;
import jacob.model.Customer_contact;
import jacob.model.Customer_contact_type;
import jacob.relationset.CompanyExtendedSearchRelationset;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataRecord;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;
import de.tif.jacob.util.StringUtil;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive the
 * onClick events of the user.
 *
 * @author achim
 */
public class PrimContact extends ILabelEventHandler  // implements IOnClickEventHandler
{
    /**
     * The internal revision control system id.
     */
    static public final transient String RCS_ID = "$Id: PrimContact.java,v 1.2 2009/11/23 11:33:43 R.Spoor Exp $";
    static public final transient String RCS_REV = "$Revision: 1.2 $";


    /**
     * Will be called if the user select a record, pressed the update or new button.
     */
    public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
    {
        label.setVisible(false);
        if (state == IGroup.SELECTED || state == IGroup.UPDATE)
        {
            label.setVisible(true);
            IDataRecord record = null;
            IDataBrowser browser = context.getDataBrowser(Company_contactBrowser.NAME);
            browser.search(CompanyExtendedSearchRelationset.NAME);
            if (browser.recordCount() > 0)
            {
                record = browser.getRecord(0).getTableRecord();
            }
            label.setLabel(record == null ? I18N.LABEL_UNSET.get(context) : record.getSaveStringValue(Company_contact.displaycontact));
        }
    }

}

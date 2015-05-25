/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Nov 18 15:46:53 CET 2008
 */
package jacob.event.ui.company;

import jacob.model.Company;
import jacob.model.Event;
import jacob.model.Request;
import jacob.resources.I18N;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class CompanyHeaderLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: CompanyHeaderLabel.java,v 1.2 2009/10/02 09:44:46 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";


  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    if(state == IGroup.UPDATE)
      label.setLabel(I18N.LABEL_COMPANY_UPDATE.get(context));
    else if(state == IGroup.NEW)
      label.setLabel(I18N.LABEL_COMPANY_NEW.get(context));
    else
      label.setLabel(I18N.LABEL_COMPANY_NORMAL.get(context));
/*    
    IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
    if (request != null && request.getCurrentTransaction() != null && context.getSelectedRecord() != null)
    {
      IDataTransaction transaction = request.getCurrentTransaction();
      context.getSelectedRecord().setValue(transaction, Company.pkey, context.getSelectedRecord().getValue(Company.pkey));
    }*/
  } 

}

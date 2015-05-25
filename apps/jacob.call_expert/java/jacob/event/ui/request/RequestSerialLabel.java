/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 11 17:19:10 CEST 2009
 */
package jacob.event.ui.request;

import jacob.browser.Company_contactBrowser;
import jacob.model.Request;
import jacob.model.Serial;
import jacob.relationset.CompanyExtendedSearchRelationset;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive
 * the onClick events of the user.
 * 
 * @author achim
 */
public class RequestSerialLabel extends ILabelEventHandler // implements
                                                           // IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: RequestSerialLabel.java,v 1.1 2009/08/12 10:54:02 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * Will be called if the user select a record, pressed the update or new
   * button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    String caption = "";
    // Get Serial
    if (state == IGroup.SELECTED || state == IGroup.UPDATE)
    {

      IDataTableRecord request = context.getDataTable(Request.NAME).getSelectedRecord();
      if (request.hasLinkedRecord(Serial.NAME))
      {
        caption = request.getLinkedRecord(Serial.NAME).getSaveStringValue(Serial.serialno);
      }
    }
    label.setLabel(caption);
    label.setVisible(state == IGroup.SELECTED || state == IGroup.UPDATE);
  }

}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Feb 03 13:57:25 CET 2006
 */
package jacob.event.ui.rcs_branch;

import jacob.model.Rcs_branch;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 * 
 * @author mike
 */
public class Rcs_branchStatus extends IComboBoxEventHandler
{
  static public final transient String RCS_ID = "$Id: Rcs_branchStatus.java,v 1.2 2006/02/24 02:16:15 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * Called, if the user changed the selection during the NEW or UPDATE state of
   * the related table record.
   * 
   * @param context
   *          The current work context of the jACOB application.
   * @param emitter
   *          The emitter of the event.
   */
  public void onSelect(IClientContext context, IComboBox emitter) throws Exception
  {
  }

  /**
   * 
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
  {
    IComboBox comboBox = (IComboBox) emitter;

    // enable/disable combo box
    //
    boolean enable = true;
    IDataTableRecord record = context.getSelectedRecord();
    if (record != null)
    {
      // disable combobox then selected or we have a release record
      if (state == IGuiElement.SELECTED || Rcs_branch.status_ENUM._Release.equals(record.getValue(Rcs_branch.status)))
        enable = false;
    }
    comboBox.setEnable(enable);

    // if enabled and updated, just lets toggle between open and closed for
    // head and branch entries.
    //
    if (enable && record != null)
    {
      // erstmal alle ausschalten
      comboBox.enableOptions(false);
      comboBox.enableOption(Rcs_branch.status_ENUM._Closed, true);
      comboBox.enableOption(Rcs_branch.status_ENUM._Open, true);
    }
    else
      comboBox.enableOptions(true);
  }
}

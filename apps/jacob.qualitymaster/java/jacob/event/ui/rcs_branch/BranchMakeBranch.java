/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 02 15:32:20 CET 2006
 */
package jacob.event.ui.rcs_branch;

import jacob.model.Rcs_branch;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the BranchMakeBranch record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author mike
 */
public class BranchMakeBranch extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: BranchMakeBranch.java,v 1.2 2006/02/24 02:16:16 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context
   *          The current client context
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    // selected Record is Status Release
    // Copy this record and set Status to Open
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IDataTable table = context.getDataTable();
    IDataTransaction trans = table.startNewTransaction();
    try
    {
      IDataAccessor accessor = context.getDataAccessor();
      IDataTableRecord clonedRecord = accessor.cloneRecord(trans, currentRecord, Rcs_branch.NAME);
      clonedRecord.setValue(trans, Rcs_branch.status, Rcs_branch.status_ENUM._Open);
      clonedRecord.setValue(trans, Rcs_branch.version, "BRANCH" + currentRecord.getValue(Rcs_branch.version));
      clonedRecord.setValue(trans, Rcs_branch.rcs_tag, null);
      trans.commit();
      accessor.propagateRecord(clonedRecord, Filldirection.BACKWARD);
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the group
   * state or the selected record.<br>
   * <br>
   * Possible values for the different states are defined in IGuiElement<br>
   * <ul>
   * <li>IGuiElement.UPDATE</li>
   * <li>IGuiElement.NEW</li>
   * <li>IGuiElement.SEARCH</li>
   * <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   * if the button has not the [selected] flag.<br>
   * The selected flag assures that the event can only be fired,<br>
   * if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context
   *          The current client context
   * @param status
   *          The new group state. The group is the parent of the corresponding
   *          event button.
   * @param button
   *          The corresponding button to this event handler
   * @throws Exception
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    if (status == IGuiElement.SELECTED)
    {
      IDataTableRecord record = context.getSelectedRecord();
      button.setEnable(Rcs_branch.status_ENUM._Release.equals(record.getValue(Rcs_branch.status)));
    }
    else
      button.setEnable(false);
  }
}

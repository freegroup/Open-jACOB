/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Feb 16 01:35:39 CET 2006
 */
package jacob.event.ui.rcs_branch;

import jacob.model.Rcs_branch;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;


/**
 *
 * @author andreas
 */
 public class Rcs_branchRcs_tag extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: Rcs_branchRcs_tag.java,v 1.1 2006/02/24 02:16:16 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
    // enable/disable text field
    //
    boolean enable = true;
    IDataTableRecord record = context.getSelectedRecord();
    if (record != null)
    {
      // disable text field then selected or we have NO release record
      if (status == IGuiElement.SELECTED || !Rcs_branch.status_ENUM._Release.equals(record.getValue(Rcs_branch.status)))
        enable = false;
    }
    emitter.setEnable(enable);
	}
}

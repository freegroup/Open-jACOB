/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Aug 15 02:17:06 CEST 2010
 */
package jacob.event.ui.active_menutree;

import jacob.common.MenutreeUtil;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class Active_menutreePathLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: Active_menutreePathLabel.java,v 1.2 2010-10-20 21:00:48 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";


  /**
   * Will be called, if the user selects a record or presses the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    if(context.getSelectedRecord()!=null)
      label.setLabel(MenutreeUtil.calculatePath(context.getSelectedRecord()));
    else
      label.setLabel("");
  }

}

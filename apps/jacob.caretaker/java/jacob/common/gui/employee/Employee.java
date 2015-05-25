/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 14 18:05:05 CEST 2008
 */
package jacob.common.gui.employee;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupListenerEventHandler;


/**
 *
 * @author Andreas
 */
 public class Employee extends IGroupListenerEventHandler
 {
	static public final transient String RCS_ID = "$Id: Employee.java,v 1.1 2008/08/14 17:03:52 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement group) throws Exception
  {
    // Sicherstellen, dass nur der ADMIN und GWSB ADMIN die Role ändern dürfen
    IGuiElement employeeGwsbadmin_role = group.findByName("employeeGwsbadmin_role");
    if (state == IGuiElement.NEW || state == IGuiElement.UPDATE)
      employeeGwsbadmin_role.setEnable(context.getUser().hasRole("gwsbadmin_role") || context.getUser().hasRole("cq_admin"));
    else
      employeeGwsbadmin_role.setEnable(state == IGuiElement.SEARCH);
  }

}

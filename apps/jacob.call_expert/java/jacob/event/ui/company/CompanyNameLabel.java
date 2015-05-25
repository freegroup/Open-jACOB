/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 11:27:19 CET 2008
 */
package jacob.event.ui.company;

import jacob.common.AbstractShowExtendedFormLabel;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class CompanyNameLabel extends AbstractShowExtendedFormLabel
{

  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
     label.setVisible(state == IGroup.SELECTED || state == IGroup.UPDATE);
  }

}

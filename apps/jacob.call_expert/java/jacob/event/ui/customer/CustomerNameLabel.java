/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Oct 30 12:08:56 CET 2008
 */
package jacob.event.ui.customer;

import jacob.common.AbstractShowExtendedFormLabel;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class CustomerNameLabel extends AbstractShowExtendedFormLabel
{
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    label.setVisible(state==IGuiElement.SELECTED||state==IGuiElement.UPDATE);
  }
}

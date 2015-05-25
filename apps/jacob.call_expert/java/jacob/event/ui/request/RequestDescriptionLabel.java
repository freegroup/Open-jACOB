/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 15:41:22 CET 2008
 */
package jacob.event.ui.request;

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
public class RequestDescriptionLabel extends AbstractShowExtendedFormLabel
{
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    label.setVisible(state==IGuiElement.SELECTED||state==IGuiElement.UPDATE);
  }
}

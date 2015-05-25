/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 05 15:39:14 CET 2008
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
public class RequestPkeyLabel extends AbstractShowExtendedFormLabel
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: RequestPkeyLabel.java,v 1.1 2009/02/17 15:12:16 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    label.setVisible(state==IGuiElement.SELECTED||state==IGuiElement.UPDATE);
  }

}

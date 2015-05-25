/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Aug 16 18:42:58 CEST 2009
 */
package jacob.event.ui.customer;

import jacob.common.AbstractShowExtendedFormLabel;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class CustomerPkey extends AbstractShowExtendedFormLabel
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: CustomerPkey.java,v 1.1 2009/08/16 18:09:15 A.Herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    label.setVisible(state==IGuiElement.SELECTED||state==IGuiElement.UPDATE);
  }

}

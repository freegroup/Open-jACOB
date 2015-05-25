/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Nov 18 21:46:22 CET 2008
 */
package jacob.event.ui.request;

import jacob.resources.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you whant receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class RequestHeaderLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: RequestHeaderLabel.java,v 1.1 2009/02/17 15:12:15 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * Will be called if the user select a record, pressed the update or new button.
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
    if(state == IGroup.UPDATE)
      label.setLabel(I18N.LABEL_REQUEST_UPDATE.get(context));
    else if(state == IGroup.NEW)
      label.setLabel(I18N.LABEL_REQUEST_NEW.get(context));
    else
      label.setLabel(I18N.LABEL_REQUEST_NORMAL.get(context));
  }

}

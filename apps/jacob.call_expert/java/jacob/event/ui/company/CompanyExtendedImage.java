/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Nov 26 14:59:09 CET 2008
 */
package jacob.event.ui.company;


import jacob.common.tabcontainer.TabManagerRequest;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class CompanyExtendedImage extends IStaticImageEventHandler  implements IOnClickEventHandler
{
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    String groupAliasName = element.getGroupTableAlias();
    TabManagerRequest.setActive(context, groupAliasName);
  }

  /**
   * The event handler if the group status has been changed.<br>
   * 
   * @param context The current work context of the jACOB application. 
   * @param status  The new state of the group.
   * @param emitter The emitter of the event.
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
  {
     image.setEnable(state == IGroup.SEARCH || state == IGroup.SELECTED );
  }
}


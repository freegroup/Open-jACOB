/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Dec 03 11:59:28 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.BreadCrumbController_RequestCategory;
import jacob.common.GroupManagerRequestHeader;
import jacob.model.Request_category;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler" if you want receive
 * the onClick events of the user.
 * 
 * @author achim
 */
public class RequestBreadCrumbResetImage extends IStaticImageEventHandler implements  IOnClickEventHandler
{
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    BreadCrumbController_RequestCategory.onReset(context);
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IStaticImage image) throws Exception
  {
    image.setVisible(state== IGroup.UPDATE || state==IGroup.NEW);
  }
  
}

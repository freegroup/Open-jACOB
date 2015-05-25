/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:50:19 CEST 2010
 */
package jacob.common.ui;

import jacob.model.Attachment_thumbnail;

import java.util.List;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IContainer;
import de.tif.jacob.screen.IPane;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * @author andreas
 *
 */
public class GalerieArticlePaneEventHandler extends ArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: GalerieArticlePaneEventHandler.java,v 1.1 2010-09-20 19:16:47 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  @Override
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    pane.setVisible(context.getDataTable(Attachment_thumbnail.NAME).recordCount()>0); 

    Pane paneOnShow = getPaneOnShow(context);
    if (paneOnShow != null)
    {
      if (Pane.THUMBNAILS==paneOnShow)
        pane.setActive(context);
    }
    else
    {
      IContainer container = pane.getContainer(context);
      for (IPane child : (List<IPane>) container.getPanes())
      {
        if (child.isVisible())
        {
          child.setActive(context);
          break;
        }
      }
    }
  }
}

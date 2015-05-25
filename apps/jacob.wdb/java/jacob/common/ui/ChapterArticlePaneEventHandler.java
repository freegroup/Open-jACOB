/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:50:19 CEST 2010
 */
package jacob.common.ui;

import jacob.model.Chapter;

import java.util.List;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IContainer;
import de.tif.jacob.screen.IPane;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * 
 * @author andherz
 */
public class ChapterArticlePaneEventHandler extends ArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: ChapterArticlePaneEventHandler.java,v 1.2 2010-10-04 22:18:00 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  @Override
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    IDataTableRecord chapter = context.getDataTable(pane.getPaneTableAlias()).getSelectedRecord();
    pane.setVisible(chapter != null);

    Pane paneOnShow = getPaneOnShow(context);
    if (paneOnShow != null)
    {
      if (pane.getPaneTableAlias().equals(paneOnShow.alias))
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

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    IDataTableRecord chapter = context.getDataTable(pane.getPaneTableAlias()).getSelectedRecord();
    if (chapter != null)
      pane.setLabel(chapter.getSaveStringValue(Chapter.title));
  }
}

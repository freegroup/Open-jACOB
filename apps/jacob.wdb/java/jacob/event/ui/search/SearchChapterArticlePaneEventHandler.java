/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:50:19 CEST 2010
 */
package jacob.event.ui.search;

import jacob.common.ui.ChapterArticlePaneEventHandler;
import jacob.event.data.GlobalcontentTableRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * 
 * @author andherz
 */
public class SearchChapterArticlePaneEventHandler extends ChapterArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: SearchChapterArticlePaneEventHandler.java,v 1.2 2010-10-05 06:55:28 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public final static class SearchHitDisplayAction extends ITabPaneAction
  {
    private final String alias;

    public SearchHitDisplayAction(String alias)
    {
      this.alias = alias;
    }

    @Override
    public void execute(IClientContext context, ITabContainer container, ITabPane pane) throws Exception
    {
      // do nothing
    }

    @Override
    public String getTooltip(IClientContext context)
    {
      return isVisible(context) ? "Enthält Suchbegriff" : "";
    }

    private boolean isVisible(IClientContext context)
    {
      IDataTableRecord chapter = context.getDataTable(alias).getSelectedRecord();
      return chapter != null && GlobalcontentTableRecord.isHighlighted(context, chapter);
    }

    @Override
    public Icon getIcon(IClientContext context) throws Exception
    {
      return isVisible(context) ? Icon.flag_yellow : Icon.blank;
    }
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
//    boolean highlight = false;
//    IDataTableRecord chapter = context.getDataTable(pane.getPaneTableAlias()).getSelectedRecord();
//    if (chapter != null && GlobalcontentTableRecord.isHighlighted(context, chapter))
//    {
//      highlight = true;
//    }
//
//    pane.setBackgroundColor(highlight ? Color.yellow : null);

    super.onGroupStatusChanged(context, state, pane);
  }
}

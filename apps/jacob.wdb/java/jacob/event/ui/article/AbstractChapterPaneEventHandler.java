/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:50:19 CEST 2010
 */
package jacob.event.ui.article;

import jacob.common.ui.ChapterArticlePaneEventHandler;
import jacob.model.Chapter;

import java.util.Date;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * 
 * @author andherz
 */
public class AbstractChapterPaneEventHandler extends ChapterArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: AbstractChapterPaneEventHandler.java,v 1.3 2010-09-20 19:16:46 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  @Override
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    IDataTableRecord chapter = context.getDataTable(pane.getPaneTableAlias()).getSelectedRecord();
    if (state == IGroup.UPDATE)
    {
      // Den Chapter-Rekord in den Updatemodus bringen
      //
      if (chapter != null)
        chapter.setValue(context.getSelectedRecord().getCurrentTransaction(), Chapter.change_date, new Date());
    }
    
    super.onOuterGroupStatusChanged(context, state, pane);
  }
}

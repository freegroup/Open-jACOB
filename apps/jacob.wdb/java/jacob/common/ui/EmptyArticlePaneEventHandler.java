/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:50:19 CEST 2010
 */
package jacob.common.ui;

import jacob.model.Attachment_thumbnail;
import jacob.model.Chapter01;
import jacob.model.Chapter02;
import jacob.model.Chapter03;
import jacob.model.Chapter04;
import jacob.model.Chapter05;
import jacob.model.Chapter06;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * @author andreas
 *
 */
public class EmptyArticlePaneEventHandler extends ArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: EmptyArticlePaneEventHandler.java,v 1.1 2010-09-20 19:16:47 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static boolean isVisible(IClientContext context)
  {
    return context.getDataTable(Attachment_thumbnail.NAME).recordCount() == 0 && //
        context.getDataTable(Chapter01.NAME).getSelectedRecord() == null && //
        context.getDataTable(Chapter02.NAME).getSelectedRecord() == null && //
        context.getDataTable(Chapter03.NAME).getSelectedRecord() == null && //
        context.getDataTable(Chapter04.NAME).getSelectedRecord() == null && //
        context.getDataTable(Chapter05.NAME).getSelectedRecord() == null && //
        context.getDataTable(Chapter06.NAME).getSelectedRecord() == null;
  }

  @Override
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    boolean visible = isVisible(context);
    ((ITabContainer) pane.getContainer(context)).hideTabStrip(visible);
    pane.setVisible(visible);
  }
}

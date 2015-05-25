/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Sep 26 19:46:53 CEST 2010
 */
package jacob.event.ui.search.article;

import jacob.common.MenutreeUtil;
import jacob.model.Active_menutree;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ILabel;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ILabelEventHandler;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andreas
 */
public class ArticleMenuentryValueLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: ArticleMenuentryValueLabel.java,v 1.2 2010-10-20 21:00:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    IDataTableRecord menutreeRecord = context.getDataTable(Active_menutree.NAME).getSelectedRecord();
    ILabel label = (ILabel) element;
    if (menutreeRecord != null)
      label.setLabel(MenutreeUtil.calculatePath(menutreeRecord));
    else
      label.setLabel("");
    element.setVisible(menutreeRecord != null);
  }

  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
  }
}

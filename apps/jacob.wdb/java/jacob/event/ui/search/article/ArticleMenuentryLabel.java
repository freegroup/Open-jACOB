/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Sep 26 19:43:31 CEST 2010
 */
package jacob.event.ui.search.article;

import jacob.model.Active_menutree;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * You must implement the interface "IOnClickEventHandler", if you want to receive the
 * onClick events of the user.
 * 
 * @author andreas
 */
public class ArticleMenuentryLabel extends ILabelEventHandler  // implements IOnClickEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: ArticleMenuentryLabel.java,v 1.1 2010-09-28 16:47:05 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    element.setVisible(context.getDataTable(Active_menutree.NAME).getSelectedRecord()!=null);
  }

  public void onGroupStatusChanged(IClientContext context, GroupState state, ILabel label) throws Exception
  {
  }
}

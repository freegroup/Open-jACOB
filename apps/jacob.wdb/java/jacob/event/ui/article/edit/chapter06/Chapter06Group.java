/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 12:07:33 CEST 2010
 */
package jacob.event.ui.article.edit.chapter06;


import jacob.event.ui.article.AbstractChapterPaneEventHandler;
import jacob.event.ui.article.edit.CloseAction;
import jacob.event.ui.article.edit.RenameAction;
import jacob.model.Article;
import jacob.model.Chapter01;
import jacob.model.Chapter06;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.IGuiElement.GroupState;



/**
 *
 * @author andherz
 */
public class Chapter06Group extends AbstractChapterPaneEventHandler
{
  static public final transient String RCS_ID = "$Id: Chapter06Group.java,v 1.2 2010-08-13 09:45:17 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  CloseAction closeAction = new CloseAction(Chapter06.NAME, Article.chapter06_key);
  RenameAction renameAction = new RenameAction(Chapter06.NAME);
  
  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    super.onGroupStatusChanged(context, state, pane);
    pane.addAction(renameAction);
    pane.addAction(closeAction);
  }

}

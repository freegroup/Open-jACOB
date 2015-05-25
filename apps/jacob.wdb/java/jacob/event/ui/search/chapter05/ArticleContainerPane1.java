/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 21:27:59 CEST 2010
 */
package jacob.event.ui.search.chapter05;


import jacob.event.ui.search.SearchChapterArticlePaneEventHandler;
import jacob.model.Chapter05;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.IGuiElement.GroupState;



/**
 *
 * @author andherz
 */
public class ArticleContainerPane1 extends SearchChapterArticlePaneEventHandler
{
  static public final transient String RCS_ID = "$Id: ArticleContainerPane1.java,v 1.3 2010-10-04 22:18:00 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final ITabPaneAction searchHitDisplayAction = new SearchHitDisplayAction(Chapter05.NAME);

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, ITabPane pane) throws Exception
  {
    super.onGroupStatusChanged(context, state, pane);
    pane.addAction(searchHitDisplayAction);
  }
}

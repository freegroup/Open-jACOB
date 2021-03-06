/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:42:40 CEST 2010
 */
package jacob.event.ui.article;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the ArticleExtendedSearchButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class ArticleExtendedSearchButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: ArticleExtendedSearchButton.java,v 1.3 2010-09-30 09:26:19 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";



  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   *             if the button has not the [selected] flag.<br>
   *             The selected flag assures that the event can only be fired,<br>
   *             if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    context.getGroup().findByName("articleResetFilterLabel1").setVisible(true);
    context.getGroup().findByName("articleResetFilterLabel2").setVisible(true);
    context.getGroup().findByName("articleResetFilterLabel3").setVisible(true);
    context.getGroup().findByName("articleResetFilterLabel4").setVisible(true);
    context.getGroup().clear(context, true);
    context.getGUIBrowser().setEnableTreeView(context, false);
  }
}

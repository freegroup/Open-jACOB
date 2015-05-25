/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Aug 12 10:28:43 CEST 2010
 */
package jacob.event.ui.article;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;


/**
 * The event handler for the ArticleSearchButton search button.<br>
 * 
 * @author andherz
 */
public class ArticleSearchButton extends ISearchActionEventHandler 
{
	static public final transient String RCS_ID = "$Id: ArticleSearchButton.java,v 1.1 2010-08-12 13:41:11 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


  /**
   * This event handler will be called, if the corresponding button has been pressed.
   * You can prevent the execution of the SEARCH action if you return <code>false</code>.<br>
   * 
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
   */
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
  {
    context.getGroup().findByName("articleResetFilterLabel1").setVisible(true);
    context.getGroup().findByName("articleResetFilterLabel2").setVisible(true);
    context.getGroup().findByName("articleResetFilterLabel3").setVisible(true);
    context.getGroup().findByName("articleResetFilterLabel4").setVisible(true);
    return true;
  }

  /**
   * This event method will be called, if the SEARCH action has been successfully executed.<br>
   *  
   * @param context The current context of the application
   * @param button  The action button (the emitter of the event)
   */
  public void onSuccess(IClientContext context, IGuiElement button) 
  {
    try
    {
      if(context.getDataBrowser().recordCount()>0)
        context.showTransparentMessage(context.getDataBrowser().recordCount()+ " Artikel gefunden.");
    }
    catch(Exception e)
    {
      // ignore
    }
  }

}

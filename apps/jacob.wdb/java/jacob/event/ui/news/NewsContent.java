/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Sep 22 10:26:49 CEST 2010
 */
package jacob.event.ui.news;

import java.awt.Color;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.ILinkLabel;
import de.tif.jacob.screen.ILinkRenderStrategy;
import de.tif.jacob.screen.RegExprLinkParser;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IInformLongTextEventHandler;
import de.tif.jacob.screen.event.ILinkEventListener;
import de.tif.jacob.screen.impl.html.HtmlLinkRenderStrategy;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author andherz
 */
 public class NewsContent extends IInformLongTextEventHandler
 {
	static public final transient String RCS_ID = "$Id: NewsContent.java,v 1.3 2010-10-17 14:49:55 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";


	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
   * @param status The new group status
	 * @param emitter The corresponding GUI element of this event handler
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState state, IInFormLongText text) throws Exception
	{
	  // Variante 1: Vordefinierten Parser + eigener Event Handler
	  //
    text.setLinkHandling(RegExprLinkParser.WIKI_PARSER,new ILinkEventListener()
    {
      public void onClick(IClientContext context, IGuiElement element, String link) throws Exception
      {
       context.createMessageDialog(link).show();
      }
    });
    
    // Variante 2: Vordefinerter Parser mit eigenem Pattern + Event Handler
    //
    text.setLinkHandling(new RegExprLinkParser("\\[\\[artikel:([ a-zA-Z0-9\\-+‰ˆ¸ƒ÷‹ﬂ./]*)(?:\\|(.*?))?\\]\\]"),new ILinkEventListener()
    {
      public void onClick(IClientContext context, IGuiElement element, String link) throws Exception
      {
       context.createMessageDialog(link).show();
      }
    });
    
    // Variante 3: eigener Parser + eigener LinkRendererStrategy + eigener Event Handler
    // (Expertenmodus)
    class MyStrategie extends HtmlLinkRenderStrategy
    {
      public void decorateLink(IClientContext context, IGuiElement target, String link, ILinkLabel label)
      {
        label.setStrike(true);
        label.setColor(Color.red);
      }
    }
    
    text.setLinkHandling(new RegExprLinkParser("\\[\\[artikel:([ a-zA-Z0-9\\-+‰ˆ¸ƒ÷‹ﬂ./]*)(?:\\|(.*?))?\\]\\]")
                        {
                           public ILinkRenderStrategy getLinkRenderStrategy(IClientContext context) throws Exception
                           {
                             return  new MyStrategie();
                           }
                        }
                        ,
                        new ILinkEventListener()
                        {
                           public void onClick(IClientContext context, IGuiElement element, String link) throws Exception
                          {
                             context.createMessageDialog(link).show();
                          }
                        });
 	}
}

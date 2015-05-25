/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Jul 30 14:11:29 CEST 2010
 */
package jacob.event.ui.news;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * The event handler for a delete button.<br>
 * 
 * @author andherz
 *
 */
public class NewsDeleteRecordButton extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: NewsDeleteRecordButton.java,v 1.2 2010-08-12 13:41:11 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the DELETE action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

	/**
	 * This event method will be called, if the DELETE action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	   try
    {
      if(context.getDataBrowser().recordCount()>0)
        context.getGUIBrowser().setSelectedRecordIndex(context,0);
      context.showTransparentMessage("News wurde gelöscht");
    }
    catch (Exception e)
    {
     // ignore
    }
	}

}

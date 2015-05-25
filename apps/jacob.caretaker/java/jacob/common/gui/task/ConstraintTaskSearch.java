/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.task;

import jacob.common.Task;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * 
 * Diese Funktion unterbindet die Suche Ohne <br>
 * mindestens ein Suchkrtierium
 * 
 * @author mike
 *
 */
public  class ConstraintTaskSearch extends IActionButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: ConstraintTaskSearch.java,v 1.1 2004/08/17 14:17:49 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	public boolean beforeAction(IClientContext context, IActionEmitter button)throws Exception
  {
  	Task.setGroubmemberConstraint(context);
    button.setSearchMode(true);
    return true;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext arg0, IGuiElement arg1) 
	{
	}
}

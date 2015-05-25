/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.call;

import jacob.common.Call;
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
 * @author Andreas Herz
 *
 */
public  class ConstraintCallSearch extends IActionButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: ConstraintCallSearch.java,v 1.1 2005/06/03 15:18:55 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
  public boolean beforeAction(IClientContext context, IActionEmitter button)throws Exception
  {
  	Call.setGroubmemberConstraint(context);
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

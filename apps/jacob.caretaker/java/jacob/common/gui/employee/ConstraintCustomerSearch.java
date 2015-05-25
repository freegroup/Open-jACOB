/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.employee;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * 
 * Diese Funktion unterbindet die Suche Ohne <br>
 * mindestens ein Suchkrtierium
 * 
 * @author achim
 *
 */
public class ConstraintCustomerSearch extends IActionButtonEventHandler 
{
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
  public boolean beforeAction(IClientContext context, IActionEmitter button)
  {
    button.setSearchMode(true);
    return true;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext arg0, IGuiElement arg1) 
	{
	}
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, int, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext arg0, IGuiElement.GroupState arg1,	IGuiElement arg2) 
	{
	}
}
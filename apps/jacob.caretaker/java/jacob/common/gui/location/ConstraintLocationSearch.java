/*
 * Created on 20.10.2004
 * by mike
 *
 */
package jacob.common.gui.location;

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * 
 * @author mike
 *
 */
public class ConstraintLocationSearch extends IActionButtonEventHandler
{
static public final transient String RCS_ID = "$Id: ConstraintLocationSearch.java,v 1.1 2004/10/20 18:09:29 mike Exp $";
static public final transient String RCS_REV = "$Revision: 1.1 $";
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
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
}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Dec 01 13:21:12 CET 2008
 */
package jacob.event.ui.request;

import jacob.common.BreadCrumbController_RequestCategory;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IHotkeyEventHandler;
import de.tif.jacob.screen.event.ITextFieldEventHandler;
import de.tif.jacob.screen.event.KeyEvent;


/**
 * You must implement the IAutosuggestProvider interface if you want provide autosuggest
 * for the user.
 * 
 * @author andherz
 */
 public class RequestCategoryFilterText extends ITextFieldEventHandler implements IHotkeyEventHandler//, IAutosuggestProvider
 {
	static public final transient String RCS_ID = "$Id: RequestCategoryFilterText.java,v 1.1 2009/02/17 15:12:15 A.Boeken Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";


  public void keyPressed(IClientContext context, KeyEvent e) throws Exception
  {
    String filterText = ((ISingleDataGuiElement)e.getEmitter()).getValue();
    BreadCrumbController_RequestCategory.onSearch(context, filterText);
  }
  
  public int getKeyMask(IClientContext context)
  {
    return KeyEvent.VK_ENTER;
  }  
}

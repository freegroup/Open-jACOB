/*
 * Created on May 5, 2004
 *
 */
package jacob.event.ui.category;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
/**
 * find all toplevel categories category. category.parentcategory_key="NULL"
 * 
 * @author mike
 *
 */
public class CategoryTopAdmin extends IButtonEventHandler 
{
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientBrowserContext, de.tif.jacob.screen.Button)
	 */
	public void onAction(IClientContext context, IGuiElement button)  throws Exception
	{
	  // IBIS: Frage: Kann dies jetzt entfallen, nachdem Du die IDataTable/Browser nur zurücksetzt?!
	// Warning: context.clearDomain() erzeugt/löscht den alten IDataAccessor
	// Also niemals vorher IDataObjekte merken
	  context.clearDomain();
  
	  IDataBrowser  browser          = context.getDataBrowser();   // the current browser
	  IDataTable    categoryTable    = context.getDataTable();     // the table in which the actions performes
      

	  categoryTable.qbeSetValue("parentcategory_key","NULL" );

	  // do the search itself
	  //
	  browser.search("r_category",Filldirection.BOTH);

	  // display the result set
	  //
	  context.getGUIBrowser().setData(context, browser);
  }

  
  /**
   * Enable disable the button when state of the group has been changed
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)
  {
  	button.setEnable( !(status == IGuiElement.NEW || status == IGuiElement.UPDATE ) );
 
  }
}

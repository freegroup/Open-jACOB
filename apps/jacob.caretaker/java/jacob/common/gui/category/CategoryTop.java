/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.category;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * find all toplevel categories category.parentcategory_key = "NULL"
 * @author mike
 *
 */
public class CategoryTop extends IButtonEventHandler 
{
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientBrowserContext, de.tif.jacob.screen.Button)
	 */
	public void onAction(IClientContext context, IGuiElement button)  throws Exception
	{
	  IDataBrowser  browser          = context.getDataBrowser();   // the current browser
	  IDataTable    categoryTable    = context.getDataTable();     // the table in which the actions performes
      
	  categoryTable.qbeClear();
	  categoryTable.qbeSetValue("parentcategory_key","NULL" );	
	  categoryTable.qbeSetValue("categorystatus" ,"Gültig|Keine Zuordnung");
	
	  // do the search itself
	  //
	  
	  browser.search(IRelationSet.LOCAL_NAME);

	  // display the result set
	  //
	  context.getGUIBrowser().setData(context, browser);
  }

  
  /**
   * Enable disable the button when state of the group has been changed
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(context.getSelectedRecord()==null || (context.getSelectedRecord()!=null && context.getSelectedRecord().hasLinkedRecord("parentcategory")));
  }
  
}

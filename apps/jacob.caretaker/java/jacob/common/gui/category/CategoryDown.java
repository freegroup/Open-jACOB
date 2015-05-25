/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.category;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * find all child categories
 * @author mike
 *
 */
public class CategoryDown extends IButtonEventHandler 
{

  /* (non-Javadoc)
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientBrowserContext, de.tif.jacob.screen.Button)
   */
  public void onAction(IClientContext context, IGuiElement button)  throws Exception
  {
    IDataBrowser  browser          = context.getDataBrowser();   // the current browser
    IDataTable    categoryTable    = context.getDataTable();     // the table in which the actions performes
    
    IDataTableRecord category = categoryTable.getSelectedRecord();
    if(category==null)
    {
      alert("Kein Gewerk selektiert!");
      return;
    }
    String parentCategoryKey = category.getStringValue("pkey");
    categoryTable.qbeClear();

    // set  search criteria
	  categoryTable.qbeSetValue("categorystatus" ,"Gültig|Keine Zuordnung");
    categoryTable.qbeSetValue("parentcategory_key",parentCategoryKey );	
    // do the search itself
    //
    browser.search(IRelationSet.LOCAL_NAME);
    if(browser.recordCount()==0)
    {
      //no child found, select old one
      categoryTable.qbeClear();
	    categoryTable.qbeSetValue("categorystatus" ,"Gültig|Keine Zuordnung");
      categoryTable.qbeSetValue("pkey",parentCategoryKey );	
      browser.search(IRelationSet.LOCAL_NAME);
    }

    // display the result set
    //
    context.getGUIBrowser().setData(context, browser);
  }

  
  /**
   * Enable disable the button when state of the group has been changed
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)
  {
  }
  
}

/*
 * Created on May 5, 2004
 *
 */
package jacob.common.gui.category;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
/**
 * find all parencategories
 * 
 * @author mike
 *  
 */
public  class CategoryUpAdmin extends IButtonEventHandler
{
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientBrowserContext,
   *      de.tif.jacob.screen.Button)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
      IDataBrowser browser       = context.getDataBrowser();    // the current data browser
      IDataTable   categoryTable = context.getDataTable(); // the table in which
                                                        // the actions
                                                        // performes
      IDataTableRecord category = categoryTable.getSelectedRecord();
      if (category == null)
      {
        alert("Kein Gewerk selektiert!");
        return;
      }
      
      String parentCategoryKey = category.getStringValue("parentcategory_key");
      
      // set search criteria
      // if parentkey then exit
      if (parentCategoryKey==null || parentCategoryKey.length()==0)
        return;
      context.clearDomain();
      // clearDomain() macht die DataObjekte ungültig-> also neu holen
      browser       = context.getDataBrowser();    // the current data browser
      categoryTable = context.getDataTable(); // the table in which
                                                        // the actions
                                                        // performes
      categoryTable.qbeSetValue("pkey", parentCategoryKey);
      
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
  	button.setEnable( (status == IGuiElement.SELECTED ) );
  }
}
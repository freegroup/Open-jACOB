/*
 * Created on May 5, 2004
 *
 */
package jacob.event.ui.category;


import jacob.common.data.DataUtils;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * open Browser window with URL stored in approfile.knowledgebase and
 *  category.pkey
 * 
 * @author mike
 *
 */


public class CategoryKnowledgebase extends IButtonEventHandler 
{
	
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.ClientBrowserContext, de.tif.jacob.screen.Button)
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
  		IDataTable    categoryTable    = context.getDataTable();     // the table in which the actions performes
  		IDataTableRecord categoryRecord = categoryTable.getSelectedRecord();
        
  		// check if the user has select a call entry
  		// 
      String sKnowledbaseURL = DataUtils.getAppprofileValue(context, "knowledgebase");
  		if (categoryRecord!= null) 
      {
  			sKnowledbaseURL = sKnowledbaseURL +",case=ref(ID" + categoryRecord.getStringValue("pkey") +")";	
  		}
      IUrlDialog dialog = context.createUrlDialog( sKnowledbaseURL);
      dialog.enableNavigation(true);
  		dialog.show(640,480);
	}

  
  /**
   * Enable disable the button when state of the group has been changed
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)
  {
  }
  
}

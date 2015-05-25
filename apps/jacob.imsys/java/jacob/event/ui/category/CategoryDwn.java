package jacob.event.ui.category;
/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Jun 02 14:53:55 CEST 2005
 *
 */
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;



 /**
  * The Event handler for the CategoryDwn-Button.<br>
  * The onAction will be calle if the user clicks on this button<br>
  * Insert your custom code in the onAction-method.<br>
  * 
  * @author mike
  *
  */
public class CategoryDwn extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: CategoryDwn.java,v 1.1 2005/06/02 16:29:44 mike Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The user has been click on the corresponding button.<br>
 	 * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
 	 *             The selected flag warranted that the event can only be fired if the<br>
 	 *             selectedRecord!=null.<br>
 	 *
	 * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
 	public void onAction(IClientContext context, IGuiElement button) throws Exception
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
   * The status of the parent group (TableAlias) has been changed.<br>
   * <br>
   * This is a good place to enable/disable the button on relation to the
   * group state or the selected record.<br>
   * <br>
   * Possible values for the state is defined in IGuiElement<br>
   * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
   * </ul>
   * 
 	 * Be in mind: The currentRecord can be null if the button has not the [selected] flag.<br>
 	 *             The selected flag warranted that the event can only be fired if the<br>
 	 *             selectedRecord!=null.<br>
   *
   * @param context The current client context
   * @param status  The new group state. The group is the parent of the corresponding event button.
   * @param button  The corresponding button to this event handler
	 * @throws Exception
   */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button)throws Exception
	{
		// You can enable/disable the button in relation to your conditions.
	  //
	  //button.setEnable(true/false);
	}
}


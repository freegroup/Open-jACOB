/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Aug 22 14:02:04 CEST 2007
 */
package jacob.common.gui.category;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.ISearchActionEventHandler;
import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * The event handler for the Sc search button.<br>
 * 
 * @author achim
 */
public class Sc extends ISearchActionEventHandler 
{
	static public final transient String RCS_ID = "$Id: Sc.java,v 1.1 2007/08/29 07:24:11 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the SEARCH action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
    IDataTable table = context.getDataTable();
    table.qbeSetValue("categorystatus", "Gültig|Keine Zuordnung");
    return true;
	}

	/**
	 * This event method will be called, if the SEARCH action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) 
	{
	}

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * This is a good place to enable/disable the button on relation to the
	 * group state or the selected record.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
	{
    ISingleDataGuiElement checkbox =(ISingleDataGuiElement) context.getGroup().findByName("categorySubcategory");
    if (checkbox !=null)
    {
      if (status ==IGuiElement.SELECTED )
      {
        IDataAccessor searchAccessor = context.getDataAccessor().newAccessor();
        IDataTable table = searchAccessor.getTable("category"); 
        table.qbeClear();
        table.qbeSetValue("categorystatus", "Gültig|Keine Zuordnung");
        table.qbeSetValue("parentcategory_key", context.getSelectedRecord().getValue("pkey"));
        if (table.search()>0)
        {
          checkbox.setValue("0");
        }
        else
        {
          checkbox.setValue("1");
        }
      }
      else
      {
        checkbox.setValue("0");
      }

    }
	}
}

/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 13 16:06:16 CEST 2005
 */
package jacob.event.ui.orderlineitem;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;


/**
 *
 * @author andreas
 */
public class OrderlineitemOrderOrderpart extends IForeignFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: OrderlineitemOrderOrderpart.java,v 1.1 2005/10/13 23:54:45 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This method will be called, if the search icon of the foreign field has been pressed.<br>
	 * You can avoid the search action, if you return <code>false</code> or you can add QBE search constraints
	 * to the tables to constraint the search result.<br>
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param foreignField The foreign field itself
	 * @return Return <code>false</code>, if you want to avoid the execution of the search action
	 */
	public boolean beforeSearch(IClientContext context, IForeignField foreignField) throws Exception
	{
		return true;
	}
  
	/**
	 * This method is called, if a record has been filled back in the foreign field GUI element.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param orderpart The record which has been field in the foreignField.
	 * @param foreignField The foreign field of the event.
	 */
	public void onSelect(IClientContext context, IDataTableRecord orderpart, IForeignField foreignField) throws Exception
	{
    IGuiElement.GroupState groupState = context.getGroup().getDataStatus(); 
    if (groupState == IGuiElement.NEW || groupState == IGuiElement.UPDATE)
    {
      // attention: pass over user locale to get the correct decimal separator, i.e. 15.5 or 15,5 
      context.getGroup().setInputFieldValue("orderlineitemPrice", orderpart.getSaveStringValue("price", context.getLocale()));
      context.getGroup().setInputFieldValue("orderlineitemDescription", orderpart.getSaveStringValue("description"));
      context.getGroup().setInputFieldValue("orderlineitemUom", orderpart.getSaveStringValue("uom"));
      
      context.getGroup().setInputFieldValue("orderlineitemPosition_amount", "");
    }
	}
  
	/**
	 * This method is called, if the foreign field has been cleared.
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param foreignField The foreign field of the event.
	 */
	public void onDeselect(IClientContext context, IForeignField foreignField) throws Exception
	{
    IGuiElement.GroupState groupState = context.getGroup().getDataStatus(); 
    if (groupState == IGuiElement.NEW || groupState == IGuiElement.UPDATE)
    {
      // clear fields
      context.getGroup().setInputFieldValue("orderlineitemPrice", "");
      context.getGroup().setInputFieldValue("orderlineitemDescription", "");
      context.getGroup().setInputFieldValue("orderlineitemUom", "");
      context.getGroup().setInputFieldValue("orderlineitemPosition_amount", "");
    }
	}
}

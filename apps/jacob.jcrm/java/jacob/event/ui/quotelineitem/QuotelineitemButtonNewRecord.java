/*
 * jACOB event handler created with the jACOB Application Developer
 * 
 * Created on Thu Oct 13 16:42:39 CEST 2005
 */
package jacob.event.ui.quotelineitem;

import jacob.common.AppLogger;
import jacob.model.QuoteOrderpart;
import jacob.model.Quotelineitem;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;


/**
 * The event handler for the QuotelineitemButtonNewRecord new button.<br>
 * 
 * @author andreas
 */
public class QuotelineitemButtonNewRecord extends IActionButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: QuotelineitemButtonNewRecord.java,v 1.5 2006/01/23 12:42:33 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.5 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This event handler will be called, if the corresponding button has been pressed.
	 * You can prevent the execution of the NEW action if you return <code>false</code>.<br>
	 * 
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 * @return Return <code>false</code>, if you want to avoid the execution of the action else return <code>true</code>.
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception 
	{
		return true;
	}

	/**
	 * This event method will be called, if the NEW action has been successfully executed.<br>
	 *  
	 * @param context The current context of the application
	 * @param button  The action button (the emitter of the event)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
    // clear fields
    IDataTableRecord currentRecord = context.getSelectedRecord();
    IDataTransaction transaction = currentRecord.getCurrentTransaction();
    // Attention: quoteOrderpart.clear calls Gui Hook where the fields are cleared. 
    // to make sure that everything works if that Hook changes, we clear the fields here as well
    context.getDataTable(QuoteOrderpart.NAME).clear();
    currentRecord.setValue(transaction,Quotelineitem.description,null);
    currentRecord.setValue(transaction,Quotelineitem.quantity,null);
    currentRecord.setValue(transaction,Quotelineitem.price, null);
    currentRecord.setValue(transaction,Quotelineitem.baseprice, null);
    currentRecord.setValue(transaction,Quotelineitem.discount_amount, null);
    currentRecord.setValue(transaction,Quotelineitem.position_amount, null);
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
		// You can enable/disable the button in relation to your conditions.
		//
		//button.setEnable(true/false);
	}
}

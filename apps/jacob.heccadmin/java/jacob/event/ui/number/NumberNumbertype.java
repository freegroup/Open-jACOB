/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 11 11:11:23 CET 2008
 */
package jacob.event.ui.number;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import jacob.model.Numbertype;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;


/**
 *
 * @author R.Spoor
 */
public class NumberNumbertype extends IForeignFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: NumberNumbertype.java,v 1.1 2009/02/17 15:23:41 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * This hook method will be called, if the search icon of the foreign field
	 * has been pressed. <br>
	 * You can avoid the search action, if you return <code>false</code> or you
	 * can add QBE search constraints to the respective data tables to constraint
	 * the search result. <br>
	 * 
	 * @param context The current client context
	 * @param foreignField The foreign field itself
	 * @return <code>false</code>, if you want to avoid the execution of the
	 *         search action, otherwise <code>true</code>
	 */
	@Override
	public boolean beforeSearch(IClientContext context, IForeignField foreignField) throws Exception
	{
	    return true;
	}

	/**
	 * This hook method will be called, if a record has been filled back
	 * (selected) in the foreign field.
	 * 
	 * @param context The current client context
	 * @param foreignRecord The record which has been filled in the foreign field.
	 * @param foreignField The foreign field itself
	 */
    @Override
	public void onSelect(IClientContext context, IDataTableRecord foreignRecord, IForeignField foreignField) throws Exception
	{
        ISingleDataGuiElement circuits = (ISingleDataGuiElement)context.getGroup().findByName("numberCircuits");
        boolean required = false;
        if (foreignRecord != null)
        {
            Object value = foreignRecord.getValue(Numbertype.requirespassages);
            if (Numbertype.requirespassages_ENUM._yes.equals(value))
            {
                required = true;
            }
        }
        circuits.setRequired(required);
	}

	/**
	 * This hook method will be called, if the foreign field has been cleared
	 * (deselected).
	 * 
	 * @param context The current client context
	 * @param foreignField The foreign field itself
	 */
	@Override
	public void onDeselect(IClientContext context, IForeignField foreignField) throws Exception
	{
        ISingleDataGuiElement circuits = (ISingleDataGuiElement)context.getGroup().findByName("numberCircuits");
        circuits.setRequired(false);
	}
}

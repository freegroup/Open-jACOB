/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Dec 11 11:01:57 CET 2008
 */
package jacob.event.ui.number;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IComboBox;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.event.IComboBoxEventHandler;

/**
 *
 * @author R.Spoor
 */
public class NumberLocaltarif extends IComboBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: NumberLocaltarif.java,v 1.2 2009/02/19 15:02:13 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * Called, if the user changed the selection during the NEW or UPDATE state
	 * of the related table record.
	 *
	 * @param context The current work context of the jACOB application.
	 * @param emitter The emitter of the event.
	 */
	@Override
	public void onSelect(IClientContext context, IComboBox emitter) throws Exception
	{
	    String value = emitter.getValue();
        ISingleDataGuiElement tarif = (ISingleDataGuiElement)context.getGroup().findByName("numberTarif");
        boolean local = jacob.model.Number.localtarif_ENUM._yes.equals(value);
        if (local)
	    {
            tarif.clear(context);
	    }
        tarif.setEnable(!local);
        tarif.setRequired(!local);
	}

	/**
	 * The event handler if the group status has been changed.<br>
	 * This is a good place to enable/disable some combo box entries in relation to the state of the
	 * selected record.<br>
	 * <br>
	 * Note: You can only enable/disable <b>valid</b> enum values of the corresponding table field.<br>
	 * <br>
	 * <code>
	 *	 IComboBox comboBox =(IComboBox)emitter;
	 *	 // remove all combo box entries
	 *	 comboBox.enableOptions(false);
	 *   if(..some condition...)
	 *   {
	 *     comboBox.enableOption("Duplicate",true);
	 *     comboBox.enableOption("Declined",true);
	 *   }
	 *   else if(...another condition...)
	 *   {
	 *     comboBox.enableOption("Proved",true);
	 *     comboBox.enableOption("In progress",true);
	 *     comboBox.enableOption("QA",true);
	 *   }
	 *   else // enable all options
	 *   	comboBox.enableOptions(true);
	 *
	 * </code>
	 *
	 * @param context The current work context of the jACOB application.
	 * @param state   The new state of the group.
	 * @param emitter The emitter of the event.
	 */
    @Override
	public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IGuiElement emitter) throws Exception
	{
        if (state == IGuiElement.NEW || state == IGuiElement.UPDATE)
        {
            IComboBox comboBox = (IComboBox)emitter;
            onSelect(context, comboBox);
        }
        else
        {
            IGuiElement tarif = context.getGroup().findByName("numberTarif");
            tarif.setEnable(true);
        }
	}
}

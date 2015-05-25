/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Tue Feb 24 11:38:47 CET 2009
 */
package jacob.event.ui.contact_type;

import java.util.Set;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IGridTableDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.screen.impl.GuiElement;
import jacob.common.AppLogger;
import jacob.resources.I18N;
import org.apache.commons.logging.Log;
import com.hecc.jacob.validation.contacts.ContactValidators;


/**
 * The event handler for the Contact_typeSelectClassButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author R.Spoor
 */
public class Contact_typeSelectClassButton extends IButtonEventHandler
{
	static public final transient String RCS_ID = "$Id: Contact_typeSelectClassButton.java,v 1.1 2009/02/24 15:56:02 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The name for the class name list box control.
	 */
	private static final String LIST_BOX_NAME = "classListBox";

    /**
     * The name for the submit button.
     */
    private static final String SUBMIT_BUTTON_NAME = "submitButton";

	/**
	 * The user has clicked on the corresponding button.<br>
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param emitter The corresponding button to this event handler
	 */
    @Override
	public void onClick(IClientContext context, IGuiElement emitter) throws Exception
	{
        Set<String> classes = ContactValidators.getValidatorClasses();
        if (classes.isEmpty())
        {
            String message = I18N.CONTACTTYPE_SELECTCLASS_NOCLASSES.get(context);
            context.createMessageDialog(message).show();
            return;
        }
        IGridTableDialog dialog = context.createGridTableDialog(emitter);
        String[] header = {I18N.CONTACTTYPE_VALIDATION_METHOD_CLASS.get(context)};
        dialog.setHeader(header);
        String[][] data = new String[classes.size()][1];
        int index = 0;
        for (String className: classes)
        {
            data[index][0] = className;
            index++;
        }
        dialog.setData(data);
        IGroup group = context.getGroup();
        ISingleDataGuiElement field = (ISingleDataGuiElement)group.findByName("contacttypeValidation_expression");
        dialog.connect(0, field);
        dialog.show();
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
	 * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
	 *             if the button has not the [selected] flag.<br>
	 *             The selected flag assures that the event can only be fired,<br>
	 *             if <code>selectedRecord!=null</code>.<br>
	 *
	 * @param context The current client context
	 * @param status  The new group state. The group is the parent of the corresponding event button.
	 * @param emitter The corresponding button to this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
	{
	    emitter.setEnable(status != GuiElement.SELECTED);
	}
}

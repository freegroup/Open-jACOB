/*
 * jACOB event handler created with the jACOB Application Designer
 *
 * Created on Thu Mar 19 13:04:59 CET 2009
 */
package jacob.event.ui.contact_type;

import jacob.common.AppLogger;
import jacob.model.Contact_type;
import jacob.resources.I18N;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.logging.Log;
import com.hecc.jacob.validation.contacts.ContactValidator;
import com.hecc.jacob.validation.contacts.ContactValidators;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Contact_typeTestValidationButton generic button.<br>
 * The {@link #onClick(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 *
 * @author R.Spoor
 */
public class Contact_typeTestValidationButton extends IButtonEventHandler implements IAskDialogCallback
{
	static public final transient String RCS_ID = "$Id: Contact_typeTestValidationButton.java,v 1.1 2009/03/19 14:55:43 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

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
	    IGroup group = context.getGroup();
	    String method = ((ISingleDataGuiElement)group.findByName("contacttypeValidation_method")).getValue();
	    String expr = ((ISingleDataGuiElement)group.findByName("contacttypeValidation_expression")).getValue();
	    if (expr == null || expr.length() == 0)
	    {
	        return;
	    }
	    if (!Contact_type.validation_method_ENUM._class.equals(method) &&
	        !Contact_type.validation_method_ENUM._regex.equals(method))
	    {
	        return;
	    }
	    String message = I18N.CONTACTTYPE_TESTVALIDATION_QUESTION.get(context);
	    context.createAskDialog(message, this).show();
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
	}

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context, String value) throws UserException
    {
        IGroup group = context.getGroup();
        String method = ((ISingleDataGuiElement)group.findByName("contacttypeValidation_method")).getValue();
        String expr = ((ISingleDataGuiElement)group.findByName("contacttypeValidation_expression")).getValue();
        if (Contact_type.validation_method_ENUM._class.equals(method))
        {
            ClassLoader loader = getClass().getClassLoader();
            ContactValidator validator = ContactValidators.getValidatorSafe(expr, loader);
            if (validator == null)
            {
                // was not loaded before, could not load now
                String message = I18N.CONTACTTYPE_INVALID_CLASS.get(context);
                throw new UserException(message);
            }
            validator.validate(value, context);
            String message = I18N.CONTACTTYPE_TESTVALIDATION_SUCCESS.get(context);
            context.createMessageDialog(message).show();
        }
        else if (Contact_type.validation_method_ENUM._regex.equals(method))
        {
            try
            {
                if (!Pattern.matches(expr, value))
                {
                    String message = I18N.CONTACTTYPE_TESTVALIDATION_REGEXMISMATCH.get(context);
                    throw new UserException(message);
                }
                String message = I18N.CONTACTTYPE_TESTVALIDATION_SUCCESS.get(context);
                context.createMessageDialog(message).show();
            }
            catch (PatternSyntaxException e)
            {
                String message = I18N.CONTACTTYPE_INVALID_REGEX.get(context);
                throw new UserException(message);
            }
        }
    }
}

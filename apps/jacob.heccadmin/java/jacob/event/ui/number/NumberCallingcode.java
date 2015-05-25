/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Jan 07 17:47:11 CET 2009
 */
package jacob.event.ui.number;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.*;

import jacob.common.AppLogger;
import jacob.model.Numbercountry;
import org.apache.commons.logging.Log;
import com.hecc.jacob.util.ForeignFieldUtil;


/**
 * 
 * @author R.Spoor
 */
 public class NumberCallingcode extends ITextFieldEventHandler
 {
	static public final transient String RCS_ID = "$Id: NumberCallingcode.java,v 1.1 2009/02/17 15:23:42 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
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
	 * @param state The new group status
	 * @param text  The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
	{
        text.setEnable(false);
        String value = null;
        IDataTableRecord currentRecord = context.getSelectedRecord();
        IDataTableRecord  record = null;
        if (currentRecord != null)
        {
            record = currentRecord.getLinkedRecord(Numbercountry.NAME);
        }
        else
        {
            IGroup group = context.getGroup();
            record = ForeignFieldUtil.getRecord(context, group, "numberNumbercountry");
        }
        if (record != null)
        {
            value = record.getStringValue(Numbercountry.callingcode);
        }
        text.setValue(value);
	}
}

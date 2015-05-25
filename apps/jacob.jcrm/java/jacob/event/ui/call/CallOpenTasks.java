/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jan 12 16:27:19 CET 2006
 */
package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITextFieldEventHandler;

import jacob.common.AppLogger;
import jacob.model.Call;
import jacob.model.Task;

import org.apache.commons.logging.Log;


/**
 *
 * @author mike
 */
 public class CallOpenTasks extends ITextFieldEventHandler 
 {
	static public final transient String RCS_ID = "$Id: CallOpenTasks.java,v 1.2 2006/11/10 07:51:57 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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
	 * @param emitter The corresponding GUI element of this event handler
	 * 
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		emitter.setEnable(false); // always readonly
    IText field =(IText)emitter;
    if (status == IGuiElement.SEARCH||status == IGuiElement.UNDEFINED)
    {
      field.setValue(null);
    }
    else
    {
      IDataAccessor searchAccessor =context.getDataAccessor().newAccessor();
      IDataTable task = searchAccessor.getTable(Task.NAME);
      task.qbeSetKeyValue(Task.call_key,context.getSelectedRecord().getValue(Call.pkey));
      task.qbeSetValue(Task.taskstatus,"!"+Task.taskstatus_ENUM._Closed);
      
      field.setValue(""+task.search());
    }
	}
  
}

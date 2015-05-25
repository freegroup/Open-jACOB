/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 13:17:05 CEST 2006
 */
package jacob.event.ui.jan_queue;

import jacob.model.Jan_queue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the Jan_queueRecordResetErrorButton record selected button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Jan_queueRecordResetErrorButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: Jan_queueRecordResetErrorButton.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

	/**
	 * The user has clicked on the corresponding button.
	 * 
	 * @param context The current client context
	 * @param button  The corresponding button to this event handler
	 * @throws Exception
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord message = context.getSelectedRecord();

    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      // make sure message is not processed by a send task currently
      //
      trans.lock(message);

      message = message.getTable().reloadSelectedRecord();

      message.setValue(trans, Jan_queue.state, Jan_queue.state_ENUM._New);
      message.setValue(trans, Jan_queue.errormessage, null);

      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }
   
	public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED && Jan_queue.state_ENUM._Error.equals(context.getSelectedRecord().getValue(Jan_queue.state)));
  }
}


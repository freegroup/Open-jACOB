/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 13:17:09 CEST 2006
 */
package jacob.event.ui.jan_queue;

import jacob.model.Jan_queue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the Jan_queueResetAllErrorsButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class Jan_queueResetAllErrorsButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: Jan_queueResetAllErrorsButton.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTable messageTable = context.getDataTable(Jan_queue.NAME);
    messageTable.qbeClear();
    messageTable.qbeSetKeyValue(Jan_queue.state, Jan_queue.state_ENUM._Error);
    if (messageTable.search() == 0)
    {
      context.createMessageDialog("No error messages existing!").show();
      return;
    }

    int num = 0, lockedNum = 0;
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      for (int i = 0; i < messageTable.recordCount(); i++)
      {
        IDataTableRecord message = messageTable.getRecord(i);

        // make sure message is not processed by a send task currently
        //
        try
        {
          trans.lock(message);
        }
        catch (RecordLockedException ex)
        {
          lockedNum++;
          continue;
        }

        num++;
        message.setValue(trans, Jan_queue.state, Jan_queue.state_ENUM._New);
        message.setValue(trans, Jan_queue.errormessage, null);
      }
      trans.commit();
    }
    finally
    {
      trans.close();
    }

    // Create resulting message
    //
    StringBuffer buffer = new StringBuffer();
    buffer.append(num);
    buffer.append(" erroneous messages have been reset");
    if (lockedNum > 0)
      buffer.append(" (").append(lockedNum).append(" messages locked!)");
    context.createMessageDialog(buffer.toString()).show();
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status != IGuiElement.NEW && status != IGuiElement.UPDATE);
  }
}

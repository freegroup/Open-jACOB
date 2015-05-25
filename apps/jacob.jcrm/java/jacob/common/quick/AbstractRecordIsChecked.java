/*
 * Created on 29.04.2005 by mike
 * 
 *
 */
package jacob.common.quick;

import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author mike
 *
 */
public abstract class AbstractRecordIsChecked extends IButtonEventHandler
{
  public String getFieldvalue()
  {
    return "checked";
  }

  /* Aktiviert den Button nur wenn nicht "checked" und Record selektiert
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
  {
    if (status != IGuiElement.SELECTED)
    {
      emitter.setEnable(false);
      return;
    }
    emitter.setEnable(!"checked".equals(context.getSelectedRecord().getValue(getFieldvalue())));
  }

  /* Setzt den entsprechenden Status auf checked
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {

    IDataTransaction currentTransaction = context.getDataAccessor().newTransaction();
    try
    {
      context.getSelectedRecord().setValue(currentTransaction, getFieldvalue(), "checked");
      currentTransaction.commit();
    }
    finally
    {
      currentTransaction.close();
    }
  }
}

package jacob.common.ui;

import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

public class RecyclebinDeleteRecord extends IActionButtonEventHandler
{
  @Override
  public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
  {
    int index = context.getDataBrowser().getSelectedRecordIndex();
    context.setPropertyForWindow("DELETE_INDEX", index);
    return true;
  }

  @Override
  public void onSuccess(IClientContext context, IGuiElement button)
  {
    try
    {
      if (context.getDataBrowser().recordCount() == 0)
        return;

      int index = (Integer) context.getProperty("DELETE_INDEX");

      if (index > 0)
        context.getGUIBrowser().setSelectedRecordIndex(context, index - 1);
      else
        context.getGUIBrowser().setSelectedRecordIndex(context, 0);

    }
    catch (Exception exc)
    {
      ExceptionHandler.handle(context, exc);
    }
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setLabel("Endgültig löschen");
  }
}

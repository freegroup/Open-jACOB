package jacob.common.gui;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

public abstract class RemoveBrowserButtonEventHandler extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: RemoveBrowserButtonEventHandler.java,v 1.1 2006/09/15 11:34:38 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private static class ConfirmCallback implements IOkCancelDialogCallback
  {
    private final IDataBrowser browser;

    private ConfirmCallback(IDataBrowser browser)
    {
      this.browser = browser;
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context) throws Exception
    {
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        for (int i = 0; i < this.browser.recordCount(); i++)
        {
          IDataTableRecord record = this.browser.getRecord(i).getTableRecord();
          record.delete(trans);
        }
        trans.commit();
      }
      finally
      {
        trans.close();
      }
      this.browser.clear();
      context.getGroup().clear(context);
    }
  }
  
  protected abstract String getRecordEntityName(IClientContext context);

  public final void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataBrowser browser = context.getGroup().getBrowser().getData();
    if (browser.recordCount() > 0)
      context.createOkCancelDialog("Alle " + browser.recordCount() + " " + getRecordEntityName(context) + " löschen?", new ConfirmCallback(browser)).show();
  }
   
  public final void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED || status == IGuiElement.SEARCH);
  }

}

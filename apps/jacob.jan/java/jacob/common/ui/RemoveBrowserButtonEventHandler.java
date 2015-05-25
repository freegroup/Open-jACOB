/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Sep 12 02:52:39 CEST 2006
 */
package jacob.common.ui;

import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the RemoveBrowserButtonEventHandler generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public abstract class RemoveBrowserButtonEventHandler extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: RemoveBrowserButtonEventHandler.java,v 1.2 2010-11-17 17:16:03 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
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
      context.createOkCancelDialog("Delete all " + browser.recordCount() + " " + getRecordEntityName(context) + "?", new ConfirmCallback(browser)).show();
  }
   
  public final void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    button.setEnable(status == IGuiElement.SELECTED || status == IGuiElement.SEARCH);
  }
}

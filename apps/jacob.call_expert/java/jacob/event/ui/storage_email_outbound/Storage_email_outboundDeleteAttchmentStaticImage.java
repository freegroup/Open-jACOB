/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jun 18 12:15:01 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;


import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.*;
import de.tif.jacob.screen.impl.GuiElement;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class Storage_email_outboundDeleteAttchmentStaticImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    IDataTransaction transaction = context.getDataTable().getTableTransaction();
    ITableListBox list = (ITableListBox) context.getGroup().findByName("storage_email_outboundAttachmentListbox");
    list.getSelectedRecord(context).delete(transaction);
    IDataBrowser browser = list.getData();
    int index = browser.getSelectedRecordIndex();

    if(index>=0)
    {
      IDataBrowserRecord listBrowserRecord = browser.getRecord(index);
      browser.removeRecord(listBrowserRecord);
      ((GuiElement)list).resetCache();

    }

    
  }

  /**
   * The event handler if the group status has been changed.<br>
   * 
   * @param context The current work context of the jACOB application. 
   * @param status  The new state of the group.
   * @param emitter The emitter of the event.
   */
  public void onGroupStatusChanged( IClientContext context, IGuiElement.GroupState state, IStaticImage image) throws Exception
  {
    image.setEnable(false);
  }
}


/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jun 18 11:30:25 CEST 2009
 */
package jacob.event.ui.storage_email_outbound;


import jacob.model.Storage_email_attachment_outbound;
import jacob.model.Storage_email_outbound;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IStaticImage;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author achim
 */
public class Storage_email_outboundAddAttachmentStaticImage extends IStaticImageEventHandler implements IOnClickEventHandler
{

  static class MyCallback implements IUploadDialogCallback
  {

    public void onCancel(IClientContext context) throws Exception
    {
      // Do nothing
      
    }

    public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
    {
      //TODO Sicher machen (nullpointer...)
      
      //Create new AttRec
      IDataTransaction transaction = context.getDataTable().getTableTransaction();
      IDataTable attachment = context.getDataTable(Storage_email_attachment_outbound.NAME);
      IDataTableRecord attachmentrec = attachment.newRecord(transaction);

      attachmentrec.setDocumentValue(transaction, Storage_email_attachment_outbound.document, DataDocumentValue.create(fileName, fileData));
      attachmentrec.setValue(transaction, Storage_email_attachment_outbound.storage_email_key, context.getDataTable().getSelectedRecord().getValue(Storage_email_outbound.pkey));
      
      // Update Listbox
      ITableListBox list = (ITableListBox)context.getGroup().findByName("storage_email_outboundAttachmentListbox");
      list.append(context, attachmentrec);
      
      
    }
  }
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    context.createUploadDialog(new MyCallback()).show();
    
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
    image.setEnable(state.equals(IGroup.NEW)||state.equals(IGroup.UPDATE));
  }
}


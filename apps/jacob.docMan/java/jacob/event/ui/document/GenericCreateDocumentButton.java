package jacob.event.ui.document;

import jacob.common.BoUtil;
import jacob.common.DocumentUtil;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Parent_bo;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class GenericCreateDocumentButton extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: GenericCreateDocumentButton.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  class UploadDialogCallback implements IUploadDialogCallback
  {
    public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
    {
      IDataTableRecord boRecord = context.getSelectedRecord();
      IDataTableRecord parentRecord = boRecord.getLinkedRecord(Parent_bo.NAME);
      
      boolean isFolder = boRecord.getValue(Bo.folder_key)!=null;
      IDataTableRecord targetFolder = isFolder? boRecord:parentRecord;
      if(BoUtil.exist(context, targetFolder, fileName))
      {
        IAskDialog dlg = context.createAskDialog("Datei mit gleichem Namen bereits vorhanden.\n\nNeuer Dateiname:",fileName, new RenameUploadDocument(fileData));
        dlg.show();
        return;
      }
      IDataTableRecord documentRecord = DocumentUtil.createDocument(context,targetFolder, fileData, fileName);
      if(documentRecord!=null)
      {
        context.getGUIBrowser().add(context, BoUtil.findByPkey(context, documentRecord.getStringValue(Document.pkey)));
      }
    }
    
    public void onCancel(IClientContext context) throws Exception
    {
    }
  }
  
  class RenameUploadDocument implements IAskDialogCallback
  {
    private byte[] content;
    public RenameUploadDocument(byte[] content)
    {
      this.content = content;
    }
    
    public void onCancel(IClientContext context) throws Exception
    {
    }

    public void onOk(IClientContext context, String value) throws Exception
    {
      new UploadDialogCallback().onOk(context, value, content);
      
    }
  }
  
  /**
   * The user has clicked on the corresponding button.<br>
   * Be in mind: The <code>currentRecord</code> can be <code>null</code>,<br>
   *             if the button has not the [selected] flag.<br>
   *             The selected flag assures that the event can only be fired,<br>
   *             if <code>selectedRecord!=null</code>.<br>
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IUploadDialog dialog = context.createUploadDialog("Dokument hochladen", "Dokument auswählen welches hochgeladen werden soll", new UploadDialogCallback());
    dialog.show();
  }
}
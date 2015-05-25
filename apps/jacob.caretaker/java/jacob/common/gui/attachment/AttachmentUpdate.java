/*
 * Created on 29.07.2004
 * by mike
 *
 */
package jacob.common.gui.attachment;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.dialogs.IDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialog;
import de.tif.jacob.screen.dialogs.IOkCancelDialogCallback;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;
import de.tif.jacob.screen.event.IActionButtonEventHandler;

/**
 * Wenn der Attachmentdatensatz geändert wird, dann auch fragen ob das Dokument <br> 
 * neu gespeichert werden soll
 * @author mike
 *
 */
public class AttachmentUpdate extends IActionButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: AttachmentUpdate.java,v 1.4 2004/08/23 17:06:58 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  protected class AttachmentReplaceCallback implements IUploadDialogCallback
  {
    
  	IDataTableRecord record;
  	String attType;
  	public AttachmentReplaceCallback(IDataTableRecord record, String type)
    {
      this.record =record;
      attType =type;
    }
    
    /* 
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onCancel(de.tif.jacob.screen.IClientContext)
     */
    public void onCancel(IClientContext arg0)
    {
      //ignore
    }

    /* 
     * @see de.tif.jacob.screen.dialogs.IUploadDialogCallback#onOk(de.tif.jacob.screen.IClientContext, java.lang.String, byte[])
     */
    public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
    {
      {
        IDataTableRecord attachmentRecord =context.getSelectedRecord();
        
        
        IDataTransaction trans = attachmentRecord.getCurrentTransaction();
        attachmentRecord.setValue(trans, "document",fileData);
        attachmentRecord.setValue(trans, "filename",fileName);
        // Der Dateiname muss auch in die GUI geschrieben werden
        context.getForm().setInputFieldValue("attachmentFilename",fileName);
      }      
    }
  }
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#beforeAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IActionEmitter)
	 */
	public boolean beforeAction(IClientContext context, IActionEmitter button) throws Exception
	{
		if (button.getDataStatus() != IGuiElement.UPDATE) return true;
		
    IDataTableRecord attachmentRecord =context.getSelectedRecord();  
    IDataTransaction trans = attachmentRecord.getCurrentTransaction();
    // Zeitstempel der Änderung setzen
    attachmentRecord.setValue(trans, "datecreated","now");
	
		return true;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IActionButtonEventHandler#onSuccess(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
	 */
	public void onSuccess(IClientContext context, IGuiElement button) throws Exception
	{
		// Wenn ich nicht im Updatemodus bin, dann brauche ich auch keine Datei hochladen
		if (button.getDataStatus() != IGuiElement.UPDATE) return;
		
    IDataTableRecord attachmentRecord =context.getSelectedRecord();  
    IDataTransaction trans = attachmentRecord.getCurrentTransaction();
    // Zeitstempel der Änderung

		// fragen ob neue Datei hochgeladen werden soll
    IOkCancelDialog okCancelDialog = context.createOkCancelDialog("Möchten Sie die Datei aktualisieren",new IOkCancelDialogCallback()
    {
      public void onOk(IClientContext context) throws Exception
      {      
        // wenn OK, dann den Uploaddialog darstellen
      	IDialog uploadDialog=context.createUploadDialog(new AttachmentReplaceCallback(context.getSelectedRecord(),"callattachment"));
        uploadDialog.show();
      }
      public void onCancel(IClientContext context) throws Exception {}
    });
    
    okCancelDialog.show();
    

	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
	 */
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGuiElement emitter) throws Exception
	{
		

	}

	public static void main(String[] args)
	{
	}
}

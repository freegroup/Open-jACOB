/**
 * 
 */
package jacob.event.ui.article.edit.articleattachmentbrowser;


import jacob.model.Article;
import jacob.model.Attachment;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IUploadDialog;
import de.tif.jacob.screen.dialogs.IUploadDialogCallback;

/**
 * @author andherz
 *
 */
public class AddAttachmentSelectionAction extends ISelectionAction
{
  /* (non-Javadoc)
   * @see de.tif.jacob.screen.ISelectionAction#execute(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement, de.tif.jacob.screen.ISelection)
   */
  @Override
  public void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception
  {
    final IBrowser browser = (IBrowser)emitter;
    IUploadDialog dlg= context.createUploadDialog("Dokument auswählen","Dokument welches an den Artikel angehängt werden soll auswählen und hochladen", new IUploadDialogCallback()
    {
      public void onOk(IClientContext context, String fileName, byte[] fileData) throws Exception
      {
        IDataTransaction trans = context.getSelectedRecord().getCurrentTransaction();
        IDataTableRecord attachmentRecord = browser.newRecord(context, trans);
        attachmentRecord.setDocumentValue(trans, Attachment.document, DataDocumentValue.create(fileName, fileData));
        attachmentRecord.setValue(trans, Attachment.article_key, context.getSelectedRecord().getValue(Article.pkey));
        attachmentRecord.setValue(trans, Attachment.title, fileName);
      }
      
      public void onCancel(IClientContext context) throws Exception
      {
      }
    });
    dlg.show();
  }

  @Override
  public Icon getIcon(IClientContext context)
  {
    return Icon.add;
  }

  @Override
  public String getTooltip(IClientContext context)
  {
    return "Anlage hinzufügen";
  }

  @Override
  public boolean isEnabled(IClientContext context, IGuiElement host)
  {
    return true;
  }
}

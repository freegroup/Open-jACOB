package jacob.event.ui.document;

import jacob.model.Document;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IDocumentDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class GenericDownloadDocumentButton extends IButtonEventHandler
{

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord documentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
    IDocumentDialog dialog = context.createDocumentDialog(documentRecord.getDocumentValue(Document.content));
    dialog.show();
  }
}

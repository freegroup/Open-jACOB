/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 01 16:50:46 CEST 2010
 */
package jacob.event.ui.document.verwaltung;

import jacob.model.Document;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;


/**
 * The event handler for the DocumentUpdateDocumentSynonymeButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andherz
 */
public class DocumentUpdateDocumentSynonymeButton extends IButtonEventHandler 
{
	static public final transient String RCS_ID = "$Id: DocumentUpdateDocumentSynonymeButton.java,v 1.1 2010-09-17 08:42:23 achim Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
  @Override
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord currentRecord = context.getDataTable(Document.NAME).getSelectedRecord();
    IAskDialog dialog = context.createAskDialog("Synonyme", currentRecord.getSaveStringValue(Document.synonyme), new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        try
        {
          context.getDataTable(Document.NAME).getSelectedRecord().setValue(trans, Document.synonyme, value);
          trans.commit();
        }
        finally
        {
          trans.close();
        }
      }

      public void onCancel(IClientContext context) throws Exception
      {
      }
    });
    dialog.show();
  }
  
}

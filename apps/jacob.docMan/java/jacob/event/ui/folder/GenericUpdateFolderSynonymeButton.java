package jacob.event.ui.folder;

import jacob.model.Folder;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class GenericUpdateFolderSynonymeButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: GenericUpdateFolderSynonymeButton.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  
  @Override
  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTableRecord currentRecord = context.getDataTable(Folder.NAME).getSelectedRecord();
    IAskDialog dialog = context.createAskDialog("Synonyme", currentRecord.getSaveStringValue(Folder.synonyme), new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        try
        {
          context.getDataTable(Folder.NAME).getSelectedRecord().setValue(trans, Folder.synonyme, value);
          trans.commit();
        }
        finally
        {
          trans.close();
        }
      }

      public void onCancel(IClientContext context) throws Exception
      {
        // TODO Auto-generated method stub
      }
    });
    dialog.show();
  }
}
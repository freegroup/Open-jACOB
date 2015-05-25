package jacob.event.ui.folder;

import org.apache.commons.io.FilenameUtils;

import jacob.common.BoUtil;
import jacob.model.Folder;
import jacob.model.Parent_bo;
import jacob.relationset.BoRelationset;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;

public class GenericRenameFolderButton extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: GenericRenameFolderButton.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  class RenameFolderCallback implements IAskDialogCallback
  {
    private IDataTableRecord recordToRename;
    private String originalName;
    
    public RenameFolderCallback(IDataTableRecord recordToRename) throws Exception
    {
      this.recordToRename = recordToRename;
      this.originalName = recordToRename.getSaveStringValue(Folder.name);
    }
    
    public void onOk(IClientContext context, String value) throws Exception
    {
      IDataTransaction trans = context.getDataAccessor().newTransaction();
      try
      {
        IDataTableRecord boRecord = BoUtil.findByPkey(context, recordToRename.getStringValue(Folder.pkey));
        IDataTableRecord parentBoRecord = boRecord.getLinkedRecord(Parent_bo.NAME);
        
        String name = recordToRename.getStringValue(Folder.name);
        String ext_orig = FilenameUtils.getExtension(name);
        String ext_new = FilenameUtils.getExtension(value);
        // der original prefix einer Datei kann nicht geändert werden
        //
        if(StringUtil.saveEquals(ext_orig, ext_new)==false)
          value = value + "."+ext_orig;
        
        // nothing to do
        if(originalName.equals(value))
          return;
        
        if(BoUtil.exist(context, parentBoRecord, value))
        {
          IAskDialog dialog = context.createAskDialog("Ordner '"+value+"' bereits vorhanden.\n\nNeuer Name",recordToRename.getSaveStringValue(Folder.name),new RenameFolderCallback(recordToRename));
          dialog.show();
        }
        else
        {
          recordToRename.setValue(trans, Folder.name, value);
          trans.commit();
          context.getDataAccessor().propagateRecord(boRecord, BoRelationset.NAME, Filldirection.BOTH);
        }
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
    IDataTableRecord currentRecord = context.getDataTable(Folder.NAME).getSelectedRecord();
    IAskDialog dialog = context.createAskDialog("Name",currentRecord.getSaveStringValue(Folder.name),new RenameFolderCallback(currentRecord));
    dialog.show();
  }
}


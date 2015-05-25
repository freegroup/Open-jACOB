package jacob.event.ui.folder;

import jacob.common.BoUtil;
import jacob.common.FolderUtil;
import jacob.model.Bo;
import jacob.model.Folder;
import jacob.model.Parent_bo;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IAskDialog;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.event.IButtonEventHandler;

public class GenericCreateFolderButton extends IButtonEventHandler 
{
  static public final transient String RCS_ID = "$Id: GenericCreateFolderButton.java,v 1.1 2010-09-17 08:42:24 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    IAskDialog dialog = context.createAskDialog("Ordner Name", new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        IDataTableRecord boRecord = context.getSelectedRecord();
        IDataTableRecord parentRecord = null;
        
        if(boRecord!=null && boRecord.getValue(Bo.folder_key)==null)
           parentRecord = boRecord.getLinkedRecord(Parent_bo.NAME);
        
        IDataTableRecord folderRecord = FolderUtil.createFolder(context,null, parentRecord==null? boRecord:parentRecord, value);
        if(folderRecord!=null)
        {
          context.getGUIBrowser().add(context, BoUtil.findByPkey(context, folderRecord.getStringValue(Folder.pkey)));
        }
        
        if(parentRecord!=null)
        {
          context.getGUIBrowser().refresh(context, parentRecord);
        }
      }
      
      public void onCancel(IClientContext context) throws Exception
      {
      }
    });
    dialog.show();
  }

}


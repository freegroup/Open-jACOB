package jacob.event.ui.folder;

import jacob.common.BoUtil;
import jacob.common.FolderUtil;
import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Parent_bo;
import jacob.model.Recyclebin;
import jacob.relationset.BoRelationset;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;
import de.tif.jacob.util.StringUtil;

public class GenericDeleteFolderButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: GenericDeleteFolderButton.java,v 1.2 2010-09-21 08:43:44 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  /**
   * The user has clicked on the corresponding button.
   * 
   * @param context The current client context
   * @param button  The corresponding button to this event handler
   * @throws Exception
   */
  public void onAction(IClientContext context, IGuiElement button) throws Exception
  {
    IDataTableRecord folderRecord = context.getDataTable(Folder.NAME).getSelectedRecord();
    IDataTableRecord boRecord = BoUtil.findByPkey(context, folderRecord.getStringValue(Folder.pkey));
    IDataTableRecord parentBoRecord = boRecord.getLinkedRecord(Parent_bo.NAME);
    

    byte[] zipData = FolderUtil.createZip(context, folderRecord);
    
    int index = context.getDataBrowser().getSelectedRecordIndex();

    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      String path = BoUtil.calculatePath(context, boRecord);
      IDataTableRecord recycleRecord = Recyclebin.newRecord(context, trans);
      recycleRecord.setValue(trans, Recyclebin.name, folderRecord.getValue(Document.name));
      recycleRecord.setValue(trans, Recyclebin.original_path, path);
      recycleRecord.setValue(trans, Recyclebin.original_pkey, folderRecord.getStringValue(Folder.pkey));
      recycleRecord.setValue(trans, Recyclebin.synonyme, folderRecord.getStringValue(Folder.synonyme));
      recycleRecord.setValue(trans, Recyclebin.type, boRecord.getValue(Bo.type));
      // Vernünftigen Namen für das Zip erstellen. Ganz nett beim Download des Recycle bin
      String zipName = StringUtil.replace(path, " ", "_");
      zipName = StringUtil.replace(zipName, "/", "_");
      zipName = StringUtil.replace(zipName, "*", "_");
      zipName = zipName+".zip";
      
      recycleRecord.setValue(trans, Recyclebin.content, DataDocumentValue.create(zipName, zipData));

      folderRecord.delete(trans);
      trans.commit();
      if(parentBoRecord!=null)
      {
        context.getGUIBrowser().expand(context, parentBoRecord);
        context.getGUIBrowser().refresh(context, parentBoRecord);
        context.getGroup().clear(context, false);
      }
      else
      {
        IDataTable boTable = context.getDataTable(Bo.NAME);
        boTable.qbeSetKeyValue(Bo.parent_bo_key, "null");
        context.getDataBrowser().search(BoRelationset.NAME, Filldirection.BOTH);
      }

      // Neuen Record in dem Papierkorb selektieren
      if(context.getDataBrowser().recordCount()!=0)
      {
        if(index>0)
          context.getGUIBrowser().setSelectedRecordIndex(context, index-1);
        else
          context.getGUIBrowser().setSelectedRecordIndex(context, 0);
      }

    }
    finally
    {
      trans.close();
    }
  }

  @Override
  public void onOuterGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    // Der TopLevel Ordner darf nicht gelöscht werden
    IDataTableRecord boRecord = context.getDataTable(Bo.NAME).getSelectedRecord();
    element.setEnable(boRecord!=null && boRecord.getStringValue(Bo.parent_bo_key)!=null);
  }
   
}

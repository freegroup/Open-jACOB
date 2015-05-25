package jacob.common;

import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Parent_bo;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;

public class FolderUtil
{
  public static IDataTableRecord findByParentBoAndName(Context context, IDataTransaction trans, IDataTableRecord parentBo, String folderName) throws Exception
  {
    String parentBoPkey = parentBo==null?"null":parentBo.getStringValue(Bo.pkey);
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Bo.NAME);
    
    boTable.qbeSetKeyValue(Bo.parent_bo_key, parentBoPkey);
     
    boTable.qbeSetKeyValue(Bo.name, folderName);
    boTable.search(IRelationSet.LOCAL_NAME);
    if(boTable.getSelectedRecord()!=null)
      return boTable.getSelectedRecord().getLinkedRecord(Folder.NAME);
    return null;
  }
  /**
   * Legt ein Dokumentenpfad an. Ist der Pfad bereits vorhanden, so wird dieser verwendet.
   * Es wird immer das Letzte Element des Pfades zurück geliefert.
   * 
   * @param context
   * @param parentBo
   * @param folderPath
   * @return
   * @throws Exception
   */
  public static IDataTableRecord createFolder(Context context,IDataTransaction trans, IDataTableRecord parentBo, String folderPath) throws Exception
  {
    System.out.println("FOLDER: "+folderPath);
    System.out.println("BO:"+parentBo);
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    boolean commitAndClose = false;
    if(trans==null)
    {
      trans = acc.newTransaction();
      commitAndClose = true;
    }
    
    try
    {
      IDataTableRecord folderRecord=null;
      String[] parts = folderPath.split("[/]");
      for (String folder : parts)
      {
        if(folder.length()>0)
        {
          String parentBoPkey = parentBo==null?"null":parentBo.getStringValue(Bo.pkey);

          folderRecord = (IDataTableRecord)trans.getProperty("folder:"+parentBoPkey+":"+folder);
          if(folderRecord==null)
            folderRecord= findByParentBoAndName(context,trans, parentBo, folder);
          
          if(folderRecord == null)
          {
            System.out.println("create bo and folder: "+folder);
            folderRecord = Folder.newRecord(acc, trans);
            IDataTableRecord boRecord = Bo.newRecord(acc, trans);
            
            folderRecord.setValue(trans, Folder.name, folder);
            
            trans.setProperty("folder:"+parentBoPkey+":"+folder, folderRecord);
            trans.setProperty("bo:"+folderRecord.getStringValue(Folder.pkey), boRecord);
            
            //Folder und Dokument haben einen Nummerkreis welcher durch Bo bestimmt wird.
            //
            folderRecord.setValue(trans, Folder.pkey, boRecord.getValue(Bo.pkey));
            
            boRecord.setValue(trans, Bo.name, folder);
            boRecord.setValue(trans, Bo.folder_key, folderRecord.getValue(Folder.pkey));
            if(parentBo!=null)
              boRecord.setValue(trans, Bo.parent_bo_key, parentBo.getValue(Parent_bo.pkey));
            parentBo = boRecord;
          }
          else
          {
            parentBo = (IDataTableRecord)trans.getProperty("bo:"+folderRecord.getStringValue(Folder.pkey));
            if(parentBo==null)
              parentBo =  BoUtil.findByPkey(context, folderRecord.getStringValue(Folder.pkey));
          }
        }
      }
      if(commitAndClose)
        trans.commit();
      
      return folderRecord;
    }
    finally
    {
      if(commitAndClose)
        trans.close();
    }
  }
  
  public static byte[] createZip(IClientContext context, IDataTableRecord folderRecord) throws Exception
  {
    // Create the ZIP file
    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
    ZipOutputStream out = new ZipOutputStream(byteArrayStream);
    addFolder(context, out, folderRecord);
    
    out.close();
    
    return byteArrayStream.toByteArray();
  }
  
  private static void addFolder(Context context, ZipOutputStream out, IDataTableRecord folderRecord) throws Exception
  {
    IDataTableRecord boRecord = BoUtil.findByPkey(context, folderRecord.getSaveStringValue(Folder.pkey));
    String fileName = BoUtil.calculatePath(context, boRecord);
    
 
    // Add ZIP entry to output stream.
    out.putNextEntry(new ZipEntry(fileName+"/"));
  //  out.write(content);
    // Complete the entry
    out.closeEntry();
    
    // alle Kinder von diesem Ordner finden und in das Archiv mit aufnehmen
    //
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Bo.NAME);
    boTable.qbeSetKeyValue(Bo.parent_bo_key, boRecord.getValue(Bo.pkey));
    boTable.search(IRelationSet.LOCAL_NAME);
    for(int i=0; i< boTable.recordCount(); i++)
    {
      IDataTableRecord childBo = boTable.getRecord(i);
      if(childBo.getValue(Bo.folder_key)!=null)
        addFolder(context,out, childBo.getLinkedRecord(Folder.NAME));
      else
        addDocument(context,out,childBo.getLinkedRecord(Document.NAME));
    }

  }

  private static void addDocument(Context context, ZipOutputStream out, IDataTableRecord documentRecord) throws Exception
  {
    IDataTableRecord boRecord = BoUtil.findByPkey(context, documentRecord.getSaveStringValue(Document.pkey));
    String fileName = BoUtil.calculatePath(context, boRecord);
 
    // Add ZIP entry to output stream.
    out.putNextEntry(new ZipEntry(fileName));
    out.write(documentRecord.getDocumentValue(Document.content).getContent());
    // Complete the entry
    out.closeEntry();
  }
}

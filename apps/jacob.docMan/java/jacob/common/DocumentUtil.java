package jacob.common;

import jacob.model.Bo;
import jacob.model.Document;
import jacob.model.Folder;
import jacob.model.Parent_bo;
import de.tif.jacob.core.Bootstrap;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.HTTPClientSession;

public class DocumentUtil
{
  public static IDataTableRecord findByPkey(Context context, String documentPkey) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable documentTable = acc.getTable(Document.NAME);
    documentTable.qbeSetKeyValue(Document.pkey, documentPkey);
    documentTable.search(IRelationSet.LOCAL_NAME);
    return documentTable.getSelectedRecord();
  }
  
  
  public static IDataTableRecord createDocument(Context context, IDataTableRecord parentBo, byte[] data, String name) throws Exception
  {
    return createDocument(context,null, parentBo!=null?parentBo.getStringValue(Parent_bo.pkey):null, data, name);
  }
  
  public static IDataTableRecord createDocument(Context context,IDataTransaction trans, String parentBoPkey, byte[] data, String name) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    boolean commitAndClose = false;
    if(trans==null)
    {
      trans = acc.newTransaction();
      commitAndClose = true;
    }
      
    
    try
    {
      IDataTableRecord documentRecord = Document.newRecord(acc, trans);
      IDataTableRecord boRecord = Bo.newRecord(acc, trans);
      
      documentRecord.setValue(trans, Document.content, DataDocumentValue.create(name,data));
      
      // wird zwar im TableHook berechnet, dann ist es allerdigns nicht für die GenerischenHistory sichtbar
      //
      documentRecord.setValue(trans, Document.name,name);
      documentRecord.setValue(trans, Document.document_size_byte,data.length);
      
      //Folder und Dokument haben einen Nummerkreis welcher durch Bo bestimmt wird.
      //
      documentRecord.setValue(trans, Document.pkey, boRecord.getValue(Bo.pkey));
      
      boRecord.setValue(trans, Bo.name, name);
      boRecord.setValue(trans, Bo.document_key, documentRecord.getValue(Folder.pkey));
      
      if(parentBoPkey!=null)
        boRecord.setValue(trans, Bo.parent_bo_key, parentBoPkey);
      
      if(commitAndClose)
        trans.commit();
      
      return documentRecord;
    }
    finally
    {
      if(commitAndClose)
        trans.close();
    }
  }


  public static String getUrl(IClientContext context, IDataTableRecord documentRecord) throws Exception
  {
    String pkey = documentRecord.getStringValue(Document.pkey);
   
    String server ="";
    if (context.getSession() instanceof HTTPClientSession)
      server = ((HTTPClientSession) context.getSession()).getBaseUrl();
    else
      server = "http://" + context.getSession().getHost() + "/" + Bootstrap.getApplicationName() + "/";

    return server+"document?id="+pkey;
  }

}

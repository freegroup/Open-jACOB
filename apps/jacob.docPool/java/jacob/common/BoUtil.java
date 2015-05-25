package jacob.common;

import jacob.model.Bo;
import jacob.model.Parent_bo;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;

public class BoUtil
{
  

  public static boolean exist(Context context, IDataTableRecord parentBo, String name) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Bo.NAME);
    if(parentBo==null)
      boTable.qbeSetKeyValue(Bo.parent_bo_key, "null");
    else
      boTable.qbeSetKeyValue(Bo.parent_bo_key, parentBo.getStringValue(Bo.pkey));
    boTable.qbeSetKeyValue(Bo.name, name);
    boTable.search(IRelationSet.LOCAL_NAME);
    switch(boTable.recordCount())
    {
    case 0:
      return false;
    case 1:
      return true;
    default:
      throw new Exception("Missing unique key of BO table [parent_bo; name]");
    }
  }
  
  public static String calculatePath(Context context, IDataTableRecord boRecord) throws Exception
  {
    if(boRecord ==null)
      return null;
    
    IDataTableRecord parentBo = boRecord.getLinkedRecord(Parent_bo.NAME);
    if(parentBo==null)
      return "/"+boRecord.getSaveStringValue(Bo.name);

    return calculatePath(context, findByPkey(context,parentBo.getStringValue(Bo.pkey)))+"/"+boRecord.getSaveStringValue(Bo.name);
  }
    
  public static IDataTableRecord findByPkey(Context context, String boPkey) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Bo.NAME);
    boTable.qbeSetKeyValue(Bo.pkey, boPkey);
    boTable.search(IRelationSet.LOCAL_NAME);
    return boTable.getSelectedRecord();
    
  }
}

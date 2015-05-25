package jacob.common;

import jacob.model.Active_menutree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.exception.ExceptionHandler;

public class ActiveMenutreeUtil
{
  public static long count(Context context) throws Exception
  {
    IDataTable newsTable = context.getDataAccessor().newAccessor().getTable(Active_menutree.NAME);
    return newsTable.count();
  }

  public static List<IDataTableRecord> getRootNodes(Context context)
  {
    List<IDataTableRecord> result = new ArrayList<IDataTableRecord>();

    try
    {
      IDataTable menuTable = context.getDataAccessor().newAccessor().getTable(Active_menutree.NAME);
      menuTable.qbeSetValue(Active_menutree.menutree_parent_key, "null");
      menuTable.search(IRelationSet.LOCAL_NAME);
      for (int i = 0; i < menuTable.recordCount(); i++)
      {
        result.add(menuTable.getRecord(i));
      }
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(e);
    }
    
    // and sort the parent menutree entries
    //
    Collections.sort(result, new Comparator<IDataTableRecord>()
    {
      public int compare(IDataTableRecord o1, IDataTableRecord o2)
      {
        try
        {
          return o1.getSaveStringValue(Active_menutree.title).compareTo(o2.getSaveStringValue(Active_menutree.title));
        }
        catch (NoSuchFieldException e)
        {
          // should never occur
          throw new RuntimeException(e);
        }
      }
    });
    
    return result;
  }
}

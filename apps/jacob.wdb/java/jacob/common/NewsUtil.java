package jacob.common;

import jacob.model.News;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;

public class NewsUtil
{
  public static long count(Context context) throws Exception
  {
    IDataTable newsTable = context.getDataAccessor().newAccessor().getTable(News.NAME);
    return newsTable.count();
  }
}

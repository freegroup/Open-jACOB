/*
 * Created on 24.03.2004 by mike
 *  
 */
package jacob.common;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;

/**
 * @author mike
 *  
 */
public class DataUtils
{
  static public final transient String RCS_ID = "$Id: DataUtils.java,v 1.1 2005/10/12 15:23:39 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public static IDataTableRecord getUserRecord() throws Exception
  {
    Context context = Context.getCurrent();
    IDataAccessor searchAccessor = context.getDataAccessor().newAccessor();
    IDataTable table = searchAccessor.getTable("employee");
    table.qbeSetKeyValue("pkey", context.getUser().getKey());
    table.search();
    if (table.search() == 1)
    {
      return table.getRecord(0);
    }
    throw new RuntimeException("Can not access current user record");
  }
}

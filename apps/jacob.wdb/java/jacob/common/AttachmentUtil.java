package jacob.common;

import jacob.model.Attachment;
import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.IRelationSet;

public class AttachmentUtil
{
  public static IDataTableRecord findByPkey(Context context, String boPkey) throws Exception
  {
    IDataAccessor acc = context.getDataAccessor().newAccessor();
    IDataTable boTable = acc.getTable(Attachment.NAME);
    boTable.qbeSetKeyValue(Attachment.pkey, boPkey);
    boTable.search(IRelationSet.LOCAL_NAME);
    return boTable.getSelectedRecord();
  }
}

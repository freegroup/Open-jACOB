package jacob.common;

import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataKeyValue;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;

public final class MenuEntry
{
  private final IDataTableRecord record;
  private final String label;

  public MenuEntry(IDataTableRecord menuRecord) throws Exception
  {
    label = menuRecord.getSaveStringValue(Menutree.title);
    record = menuRecord;
  }

  public void show(IClientContext context) throws Exception
  {
    MenutreeUtil.show(context, record.getStringValue(Menutree.pkey));
  }

  public String getLabel()
  {
    return label;
  }
  
  public IDataKeyValue getMenutreeEntryPkey()
  {
    return this.record.getPrimaryKeyValue();
  }
}

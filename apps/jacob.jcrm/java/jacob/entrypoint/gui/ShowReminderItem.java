/*
 * Created on 26.09.2005
 *
 */
package jacob.entrypoint.gui;

import java.util.Properties;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.entrypoint.IGuiEntryPoint;
import de.tif.jacob.screen.IClientContext;

/**
 * @author andreas
 *
 */
public abstract class ShowReminderItem extends IGuiEntryPoint
{
  protected abstract String getTableAlias();
  
  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.IGuiEntryPoint#enter(de.tif.jacob.screen.IClientContext, java.util.Properties)
   */
  public final void enter(IClientContext context, Properties props) throws Exception
  {
    String tablekey = (String) props.get("tablekey");
    
    IDataTable table = context.getDataAccessor().getTable(getTableAlias());
    table.qbeClear();
    table.qbeSetKeyValue("pkey", tablekey);
    table.search();
    if (table.recordCount()!=1)
    {
      // TODO: Better message
      context.createMessageDialog("Record not found").show();
      return;
    }
    
    context.getDataAccessor().propagateRecord(table.getSelectedRecord(), Filldirection.BOTH);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.IGuiEntryPoint#hasNavigation()
   */
  public final boolean hasNavigation()
  {
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.IGuiEntryPoint#hasSearchBrowser()
   */
  public final boolean hasSearchBrowser()
  {
    return true;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.entrypoint.IGuiEntryPoint#hasToolbar()
   */
  public final boolean hasToolbar()
  {
    return true;
  }
}

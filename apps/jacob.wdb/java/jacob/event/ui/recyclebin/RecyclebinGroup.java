/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Oct 18 12:52:06 CEST 2010
 */
package jacob.event.ui.recyclebin;

import jacob.browser.RecyclebinBrowser;
import jacob.model.Recyclebin;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.event.IGroupEventHandler;

/**
 * 
 * @author andreas
 */
public final class RecyclebinGroup extends IGroupEventHandler
{
  static public final transient String RCS_ID = "$Id: RecyclebinGroup.java,v 1.1 2010-10-20 21:00:49 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public static final class RecycleBinBrowserEventHandler extends IBrowserEventHandler
  {
    @Override
    public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
    {
    }

    @Override
    public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
    {
      if (RecyclebinBrowser.browserObject_path.equals(browser.getDefinition().getBrowserField(column).getName()))
      {
        String type = record.getSaveStringValue(RecyclebinBrowser.browserObject_type);
        if (type.equals(Recyclebin.object_type_ENUM._menutree))
          return Icon.folder;
        else if (type.equals(Recyclebin.object_type_ENUM._article))
        {
          if (record.getintValue(RecyclebinBrowser.browserChildren_count) > 0)
            return Icon.table_multiple;
          else
            return Icon.table;
        }
        return Icon.blank;
      }
      return Icon.NONE;
    }

  }

  @Override
  public Class getSearchBrowserEventHandlerClass()
  {
    return RecycleBinBrowserEventHandler.class;
  }

  public void onShow(IClientContext context, IGroup group) throws Exception
  {
    IDataBrowser browser = group.getBrowser().getData();
    context.getDataAccessor().clear();
    browser.search(IRelationSet.LOCAL_NAME, Filldirection.BOTH);
    if (browser.recordCount() > 0)
      context.getGUIBrowser().setSelectedRecordIndex(context, 0);
  }
}

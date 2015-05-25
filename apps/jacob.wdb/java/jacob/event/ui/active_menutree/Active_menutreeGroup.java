/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Sep 20 21:30:25 CEST 2010
 */
package jacob.event.ui.active_menutree;

import jacob.browser.Active_menutreeBrowser;
import jacob.browser.MenutreeBrowser;
import jacob.model.Menutree;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IBrowserEventHandler;
import de.tif.jacob.screen.event.IGroupEventHandler;

/**
 * 
 * @author andreas
 */
public class Active_menutreeGroup extends IGroupEventHandler
{
  static public final transient String RCS_ID = "$Id: Active_menutreeGroup.java,v 1.2 2010-09-26 01:47:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  public final static class BrowserEventHandler extends IBrowserEventHandler
  {
    public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord record) throws Exception
    {
    }

    @Override
    public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
    {
      if (Active_menutreeBrowser.browserTitle.equals(browser.getDefinition().getBrowserField(column).getName()))
      {
        // different icons for menutree entries with or without attached article
        return record.getValue(Active_menutreeBrowser.browserArticle_key) == null ? Icon.folder : Icon.folder_table;
      }
      return Icon.NONE;
    }
  }

  public Class getSearchBrowserEventHandlerClass()
  {
    return BrowserEventHandler.class;
  }

  public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
  {
  }

  public void onHide(IClientContext context, IGroup group) throws Exception
  {
  }

  public void onShow(IClientContext context, IGroup group) throws Exception
  {
  }
}

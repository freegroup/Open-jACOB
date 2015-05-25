/*
 * Created on 23.02.2009
 *
 */
package de.tif.jacob.components.searchbrowser_contextmenu;

import java.util.List;

import de.tif.jacob.core.SessionContext;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;

public abstract class SearchBrowserContextMenuEventHandler extends IGroupMemberEventHandler
{
  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    onGroupStatusChanged(context,state,(ISearchBrowserContextMenu)element);
  }
  
  public void onGroupStatusChanged(IClientContext context, GroupState state, ISearchBrowserContextMenu element) throws Exception
  {
  }
  
  public abstract List<MenuEntry> getMenuEntries(SessionContext context,IBrowser browser, IDataBrowserRecord record, IBrowserField field) throws Exception;
  public abstract void executeCommand(IClientContext context,IBrowser browser, IDataBrowserRecord record, IBrowserField field, String command) throws Exception;
}

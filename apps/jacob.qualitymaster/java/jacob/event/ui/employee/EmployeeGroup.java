/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Jul 21 17:06:49 CEST 2009
 */
package jacob.event.ui.employee;

import jacob.browser.EmployeeBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;


/**
 *
 * @author andreas
 */
 public final class EmployeeGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: EmployeeGroup.java,v 1.1 2009-07-21 15:44:15 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public final static class BrowserEventHandler extends de.tif.jacob.screen.event.IBrowserEventHandler
  {
    public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
    {
      if (EmployeeBrowser.browserLoginname.equals(record.getBrowser().getBrowserDefinition().getBrowserField(column).getName()))
      {
        if (record.getbooleanValue(EmployeeBrowser.browserLogin_enabled))
          return Icon.bullet_green;
        return Icon.bullet_red;
      }
      return Icon.NONE;
    }

    public String filterCell(IClientContext context, IBrowser browser, int row, int column, String value) throws Exception
    {
      return value;
    }

    public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
    {
    }
  }
  
  @SuppressWarnings("unchecked")
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

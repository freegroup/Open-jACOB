/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Sep 01 17:01:53 CEST 2010
 */
package jacob.common.ui;

import jacob.browser.Inform_attachmentBrowser;
import jacob.browser.Inform_linkBrowser;
import jacob.model.Link;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;

/**
 *
 * @author andreas
 */
public class ArticleLinkBrowserEventHandler extends de.tif.jacob.screen.event.IInformBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: ArticleLinkBrowserEventHandler.java,v 1.2 2010-09-28 11:53:40 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (Inform_linkBrowser.browserTitle.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      String url = record.getSaveStringValue(Inform_linkBrowser.browserUrl).toLowerCase();
      if (url.startsWith("file:") || url.startsWith("ftp:"))
        return Icon.page_white_get;
      else if(url.startsWith("http"))
        return Icon.world_go;
      return Icon.page_green;
    }
    return Icon.NONE;
  }

  @Override
  public boolean beforeAction(IClientContext context, IBrowser browser, IDataBrowserRecord record) throws Exception
  {
    // open URL-Dialog and avoid record selection
    context.createUrlDialog(record.getTableRecord().getStringValue(Link.url)).show();
    return false;
  }

  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
    // TODO: Does not work -> No URL-Dialog opens!
    context.createUrlDialog(selectedRecord.getStringValue(Link.url)).show();
    browser.setSelectedRecordIndex(context, -1);
	}
}

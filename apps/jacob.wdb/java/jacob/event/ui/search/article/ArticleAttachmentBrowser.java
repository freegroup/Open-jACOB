/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 21:44:44 CEST 2010
 */
package jacob.event.ui.search.article;

import jacob.browser.Inform_attachmentSearchBrowser;
import jacob.common.ui.ArticleAttachmentBrowserEventHandler;
import jacob.event.data.GlobalcontentTableRecord;
import jacob.model.Article;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IInFormBrowser;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * 
 * @author andherz
 */
public final class ArticleAttachmentBrowser extends ArticleAttachmentBrowserEventHandler
{
  static public final transient String RCS_ID = "$Id: ArticleAttachmentBrowser.java,v 1.4 2010-10-04 22:18:00 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  @Override
  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (Inform_attachmentSearchBrowser.browserPkey.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      return GlobalcontentTableRecord.isHighlighted(context, record) ? Icon.flag_yellow : Icon.NONE;
    }

    return super.decorateCell(context, browser, row, column, record, value);
  }

  @Override
  public String filterCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (Inform_attachmentSearchBrowser.browserPkey.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      // use pkey field for display of search icon (Flag yellow)
      return "";
    }

    return super.filterCell(context, browser, row, column, record, value);
  }

  @Override
  public void onGroupStatusChanged(IClientContext context, GroupState state, IInFormBrowser informbrowser) throws Exception
  {
    informbrowser.setVisible(context.getDataTable(Article.NAME).getSelectedRecord() != null);

    // HACK: IInFormBrowser erbt nicht von IBrowser. So ein Mist!
//    IBrowser browser = (IBrowser) informbrowser;
//    IDataBrowser databrowser = browser.getData();
//    for (int i = 0; i < databrowser.recordCount(); i++)
//    {
//      IDataBrowserRecord record = databrowser.getRecord(i);
//      browser.setEmphasize(context, record, GlobalcontentTableRecord.isHighlighted(context, record));
//    }

    super.onGroupStatusChanged(context, state, informbrowser);
  }
}

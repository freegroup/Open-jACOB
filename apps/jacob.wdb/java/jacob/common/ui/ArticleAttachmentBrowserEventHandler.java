/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Aug 15 01:44:38 CEST 2010
 */
package jacob.common.ui;

import jacob.browser.Inform_attachmentBrowser;
import jacob.model.Attachment;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;

/**
 *
 * @author andreas
 */
public class ArticleAttachmentBrowserEventHandler extends de.tif.jacob.screen.event.IInformBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: ArticleAttachmentBrowserEventHandler.java,v 1.1 2010-09-26 01:47:20 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  public Icon decorateCell(IClientContext context, IBrowser browser, int row, int column, IDataBrowserRecord record, String value) throws Exception
  {
    if (Inform_attachmentBrowser.browserTitle.equals(browser.getDefinition().getBrowserField(column).getName()))
    {
      String filename = record.getSaveStringValue(Inform_attachmentBrowser.browserDocument).toLowerCase();
      if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".bmp") || filename.endsWith(".gif"))
        return Icon.picture;
      if (filename.endsWith(".doc") || filename.endsWith(".rtf"))
        return Icon.page_word;
      if (filename.endsWith(".xls"))
        return Icon.page_excel;
      if (filename.endsWith(".pps"))
        return Icon.page_white_powerpoint;
      if (filename.endsWith(".zip"))
        return Icon.page_white_compressed;
      if (filename.endsWith(".pdf"))
        return Icon.page_white_acrobat;
      return Icon.page;
    }
    return Icon.NONE;
  }

  public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
    context.createDocumentDialog(selectedRecord.getDocumentValue(Attachment.document)).show();
    browser.setSelectedRecordIndex(context, -1);
	}
}

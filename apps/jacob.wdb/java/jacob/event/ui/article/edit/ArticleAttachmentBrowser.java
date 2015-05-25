/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sun Aug 15 00:35:17 CEST 2010
 */
package jacob.event.ui.article.edit;

import jacob.model.Attachment;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;

/**
 *
 * @author andherz
 */
public class ArticleAttachmentBrowser extends de.tif.jacob.screen.event.IInformBrowserEventHandler
{
	static public final transient String RCS_ID = "$Id: ArticleAttachmentBrowser.java,v 1.1 2010-08-15 00:36:51 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  
  /**
   * This hook method will be called, if the user selects a record in the
   * browser.
   * 
   * @param context
   *          the current client context
   * @param browser
   *          The browser with the click event
   * @param selectedRecord
   *          the record which has been selected
   */
	public void onRecordSelect(IClientContext context, IBrowser browser, IDataTableRecord selectedRecord) throws Exception
	{
	  context.createDocumentDialog(selectedRecord.getDocumentValue(Attachment.document)).show();
    browser.setSelectedRecordIndex(context, -1);
	}
}

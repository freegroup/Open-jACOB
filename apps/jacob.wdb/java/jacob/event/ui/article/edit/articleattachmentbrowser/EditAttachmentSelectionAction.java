/**
 * 
 */
package jacob.event.ui.article.edit.articleattachmentbrowser;

import jacob.model.Attachment;

import java.util.Iterator;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.data.impl.DataBrowserRecord;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;
import de.tif.jacob.screen.impl.html.Browser;
import de.tif.jacob.util.StringUtil;

/**
 * @author andreas
 * 
 */
public class EditAttachmentSelectionAction extends ISelectionAction
{
  private static final class Callback implements IAskDialogCallback
  {
    private final IDataTransaction trans;
    private final IBrowser browser;
    private final IDataBrowserRecord currentRecord;
    private final Iterator<IDataBrowserRecord> nextRecords;

    private Callback(IDataTransaction trans, IBrowser browser, IDataBrowserRecord currentRecord, Iterator<IDataBrowserRecord> nextRecords)
    {
      this.trans = trans;
      this.browser = browser;
      this.currentRecord = currentRecord;
      this.nextRecords = nextRecords;
    }

    public void onOk(IClientContext context, String value) throws Exception
    {
      value = StringUtil.toSaveString(value).trim();
      if (value.length() == 0)
      {
        // repeat asking
        createAskDialog(context, trans, browser, currentRecord, value, nextRecords);
      }
      else
      {
        // TODO: IBIS: Hack um die Änderungen sofort (vor dem Speichern) sichtbar zu machen
        IDataTableRecord tableRecord = ((DataBrowserRecord) this.currentRecord).getTableRecordToUpdate(this.trans);
        tableRecord.setValue(this.trans, Attachment.title, value);
        // this.browser.refresh(context, this.currentRecord);
        ((Browser) this.browser).invalidate();
        
        editTitles(context, this.trans, this.browser, this.nextRecords);
      }
    }

    public void onCancel(IClientContext context) throws Exception
    {
    }
  }

  private static void editTitles(IClientContext context, IDataTransaction trans, IBrowser browser, Iterator<IDataBrowserRecord> nextRecords) throws Exception
  {
    if (nextRecords.hasNext())
    {
      IDataBrowserRecord currentRecord = nextRecords.next();
      createAskDialog(context, trans, browser, currentRecord, null, nextRecords);
    }
  }

  private static void createAskDialog(IClientContext context, IDataTransaction trans, IBrowser browser, IDataBrowserRecord currentRecord, String title, Iterator<IDataBrowserRecord> nextRecords) throws Exception
  {
    if (title == null)
      title = currentRecord.getTableRecord().getSaveStringValue(Attachment.title);
    String document = currentRecord.getTableRecord().getSaveStringValue(Attachment.document);
    context.createAskDialog("Titel von '" + document + "':", title, new Callback(trans, browser, currentRecord, nextRecords)).show();
  }

  @Override
  public void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception
  {
    if (selection.size() == 0)
      throw new UserException("Keine Anlagen markiert");
    IBrowser browser = (IBrowser) emitter;
    editTitles(context, context.getSelectedRecord().getCurrentTransaction(), browser, selection.iterator());
  }

  @Override
  public Icon getIcon(IClientContext context)
  {
    return Icon.page_edit;
  }

}

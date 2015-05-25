/**
 * 
 */
package jacob.event.ui.article.edit.articleattachmentbrowser;

import java.util.Iterator;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISelection;
import de.tif.jacob.screen.ISelectionAction;
import de.tif.jacob.screen.Icon;

/**
 * @author andherz
 *
 */
public class RemoveAttachmentSelectionAction extends ISelectionAction
{
  @Override
  public void execute(IClientContext context, IGuiElement emitter, ISelection selection) throws Exception
  {
    if (selection.size() == 0)
      throw new UserException("Keine Anlagen markiert");
    
    IBrowser browser = (IBrowser)emitter;
    IDataTransaction trans = context.getSelectedRecord().getCurrentTransaction();
    Iterator<IDataBrowserRecord> iter=selection.iterator();
    
    while(iter.hasNext())
    {
      IDataBrowserRecord record = iter.next();
      browser.remove(context, record);
      record.getTableRecord().delete(trans);
    }
  }

  @Override
  public Icon getIcon(IClientContext context)
  {
    return Icon.delete;
  }
}

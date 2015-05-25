package jacob.event.ui.article.edit;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserRecordAction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.Icon;

public class DeleteTextblockAction extends IBrowserRecordAction
{
  @Override
  public void execute(IClientContext context, IBrowser browser, IDataBrowserRecord relatedRecord) throws Exception
  {
    relatedRecord.getTableRecord().delete(context.getSelectedRecord().getCurrentTransaction());
    browser.remove(context, relatedRecord);
  }

  @Override
  public Icon getIcon(IClientContext context) throws Exception
  {
    return Icon.delete;
  }

  @Override
  public String getTooltip(IClientContext context)
  {
    return "Textblock löschen";
  }
 }

package jacob.event.ui.article.edit;
import jacob.common.TextblockEditController;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserRecordAction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.Icon;
public class EditTextblockAction extends IBrowserRecordAction
{
  @Override
  public void execute(IClientContext context, IBrowser browser, IDataBrowserRecord relatedRecord) throws Exception
  {
    ITabContainer container = (ITabContainer) ((ITabPane) browser.getGroup()).getContainer(context);
    TextblockEditController.startEdit(context, container, browser, relatedRecord.getTableRecord());
  }

  @Override
  public Icon getIcon(IClientContext context) throws Exception
  {
    return Icon.page_edit;
  }

  @Override
  public String getTooltip(IClientContext context)
  {
    return "Textblock bearbeiten";
  }
  
}

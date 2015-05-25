package jacob.event.ui.article.edit;

import jacob.model.Chapter;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.Icon;
import de.tif.jacob.screen.dialogs.IAskDialogCallback;

public class RenameAction extends ITabPaneAction
{
  private final String alias;

  public RenameAction(String alias)
  {
    this.alias = alias;
  }

  @Override
  public Icon getIcon(IClientContext context) throws Exception
  {
    return Icon.tab_edit;
  }

  @Override
  public String getTooltip(IClientContext context)
  {
    return "Kapitel umbenennen";
  }

  @Override
  public void execute(IClientContext context,ITabContainer container,final ITabPane pane) throws Exception
  {
    context.createAskDialog("Name des Kapitel",pane.getLabel(), new IAskDialogCallback()
    {
      public void onOk(IClientContext context, String value) throws Exception
      {
        IDataTableRecord chapter = context.getDataTable(alias).getSelectedRecord();
        IDataTransaction trans = chapter.getCurrentTransaction();
        chapter.setValue(trans, Chapter.title, value);
        pane.setLabel(value);
      }
      
      public void onCancel(IClientContext context) throws Exception
      {
      }
    }).show();

  }
}

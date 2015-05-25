package jacob.event.ui.article.edit;

import java.util.List;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IContainer;
import de.tif.jacob.screen.IPane;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITabPaneAction;
import de.tif.jacob.screen.Icon;

public class CloseAction extends ITabPaneAction
{
  private final String alias;
  private final String field;
  
  
  public CloseAction(String alias, String field)
  {
    this.alias = alias;
    this.field = field;
  }

  @Override
  public Icon getIcon(IClientContext context) throws Exception
  {
    return Icon.delete;
  }

  @Override
  public String getTooltip(IClientContext context)
  {
    return "Kapitel löschen";
  }

  @Override
  public void execute(IClientContext context,ITabContainer container, ITabPane pane) throws Exception
  {
    IDataTableRecord article = context.getSelectedRecord();
    IDataTableRecord chapter = context.getDataTable(alias).getSelectedRecord();
    IDataTransaction trans = article.getCurrentTransaction();
    article.resetLinkedRecord(trans, alias);
    chapter.delete(trans);
    context.getDataTable(alias).clearRecords();
    pane.setVisible(false);

    // Wenn noch kein Kapitel hinterlegt ist, dann wird der Erste Tab angezeigt und der TabStrib versteckt.
    boolean atLeastOneVisible=false;
    List<IPane> panes = (List<IPane>) container.getPanes();
    for (int i=1; i<panes.size() && atLeastOneVisible==false;i++)
    {
      atLeastOneVisible = panes.get(i).isVisible();
    }
    ((ITabContainer)pane.getContainer(context)).hideTabStrip(!atLeastOneVisible);
    panes.get(0).setVisible(!atLeastOneVisible);
    if(!atLeastOneVisible)
      container.setActivePane(context,0);
    
    context.showTransparentMessage("Kapitel wurde entfernt");

  }
}

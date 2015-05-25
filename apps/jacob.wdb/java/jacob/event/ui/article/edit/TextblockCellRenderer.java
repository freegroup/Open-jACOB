package jacob.event.ui.article.edit;
import jacob.common.AbstractTextblockRenderer;

import java.util.ArrayList;
import java.util.List;

import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserRecordAction;
import de.tif.jacob.screen.IClientContext;

public class TextblockCellRenderer extends AbstractTextblockRenderer
{
  static public final transient String RCS_ID = "$Id: TextblockCellRenderer.java,v 1.4 2010-10-21 10:35:03 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  List<IBrowserRecordAction> actions = new ArrayList<IBrowserRecordAction>();

  public void addAction(IBrowserRecordAction action)
  {
    actions.add(action);
  }

  public String renderActions(IClientContext context, IBrowser browser, IDataBrowserRecord record, int row)
  {
    // NO Html escape.....write the plain HTML.
    StringBuffer sb = new StringBuffer(1024);
    try
    {
      sb.append("<div>");
      for (IBrowserRecordAction action : actions)
      {
        sb.append("<img style=\"padding-left:15px;\" src=\"");
        sb.append(action.getIcon(context).getPath(true));
        sb.append("\" title=\"");
        sb.append(action.getTooltip(context));
        sb.append("\" onclick=\"");
        sb.append("FireEventData('");
        sb.append(browser.getId());
        sb.append("','");
        sb.append(action.getId());
        sb.append("','");
        sb.append(Integer.toString(row));
        sb.append("')\" >");
      }
      sb.append("</div>");
    }
    catch (Exception e)
    {
      // ignore
      ExceptionHandler.handle(e);
    }

    
    return sb.toString();
  }
}

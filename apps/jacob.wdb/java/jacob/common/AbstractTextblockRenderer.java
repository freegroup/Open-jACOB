package jacob.common;

import jacob.browser.Textblock01Browser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserCellRenderer;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.impl.html.GuiHtmlElement;
import de.tif.jacob.util.StringUtil;

public class AbstractTextblockRenderer extends IBrowserCellRenderer
{
  static public final transient String RCS_ID = "$Id: AbstractTextblockRenderer.java,v 1.7 2010-10-21 10:28:08 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  
  public String renderCell(IClientContext context,IBrowser browser, IDataBrowserRecord record,int row,  String cellContent)
  {
    StringBuilder sb = new StringBuilder(1024);

    sb.append(renderTitle(context, browser, record, row));
    sb.append("<div style=\"width:");
    sb.append(Integer.toString(((GuiHtmlElement)browser).getBoundingRect().width));
    sb.append("px;padding:5px;white-space:normal;\">");
    sb.append(cellContent);
    sb.append("</div>");
    
    return sb.toString();
  }
  
  protected String renderTitle(IClientContext context,IBrowser browser, IDataBrowserRecord record, int row)
  {
    StringBuilder sb = new StringBuilder(1024);
    
    sb.append("<div style=\"background-color:#f3f3f3;width:");
    sb.append(Integer.toString(((GuiHtmlElement)browser).getBoundingRect().width));
    sb.append("px;padding-top:5px;padding-bottom:5px;white-space:normal;\">");
    sb.append("<table style=\"width:");
    sb.append(Integer.toString(((GuiHtmlElement)browser).getBoundingRect().width));
    sb.append("px\"><tr><td style=\"font-size:80%;font-weight:bold\">");
    try
    {
      String title = record.getSaveStringValue(Textblock01Browser.browserTitle);
      if(StringUtil.emptyOrNull(title))
        title ="- -";
      sb.append(StringUtil.htmlEncode(title));
    }
    catch (NoSuchFieldException e)
    {
      ExceptionHandler.handle(context, e);
    }
    sb.append("</td><td align=\"right\" style=\"padding-right:10px;font-size:80%;\">");
    try
    {
      String source = record.getSaveStringValue(Textblock01Browser.browserSource);
      if(StringUtil.emptyOrNull(source))
        source ="-";
      sb.append(StringUtil.htmlEncode("Quelle ["+source+"] "));
      sb.append(record.getSaveStringValue(Textblock01Browser.browserCreate_date,context.getLocale()));
    }
    catch (NoSuchFieldException e)
    {
      ExceptionHandler.handle(context, e);
    }
    sb.append("</td><td style=\"border-left:1px solid #e0e0e0;width:70px;\" align=\"right\">");
    sb.append(renderActions(context, browser, record, row));
    sb.append("</td></tr></table>");
    sb.append("</div>");
   
    return sb.toString();
  }
  
  protected String renderActions(IClientContext context,IBrowser browser, IDataBrowserRecord record, int row)
  {
    return "";
  }
  
 }

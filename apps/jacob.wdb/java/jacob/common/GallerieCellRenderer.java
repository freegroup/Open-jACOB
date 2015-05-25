package jacob.common;


import jacob.browser.Attachment_thumbnailBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IBrowser;
import de.tif.jacob.screen.IBrowserCellRenderer;
import de.tif.jacob.screen.IClientContext;

public class GallerieCellRenderer extends IBrowserCellRenderer
{
  static public final transient String RCS_ID = "$Id: GallerieCellRenderer.java,v 1.1 2010-08-17 20:46:54 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";


  public String renderCell(IClientContext context,IBrowser browser, IDataBrowserRecord record,int row,  String cellContent)
  {
    StringBuilder sb = new StringBuilder(1024);
    
    try
    {
      sb.append(record.getStringValue(Attachment_thumbnailBrowser.browserDocument));
    }
    catch (NoSuchFieldException e)
    {
      ExceptionHandler.handle(e);
    }
    sb.append("<br><a target=\"_new\" href=\"cmdenter?entry=ImageProvider&app=wdb&id=");
    sb.append(cellContent);
    sb.append("\"><img border=\"0\" src=\"cmdenter?entry=ThumbnailProvider&app=wdb&id=");
    sb.append(cellContent);
    sb.append("\"></a><br><br>");
    
    return sb.toString();
  }  
}

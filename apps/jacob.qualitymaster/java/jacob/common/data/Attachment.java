/*
 * Created on Jul 16, 2004
 *
 */
package jacob.common.data;

import java.net.URLEncoder;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.IUrlDialog;

/**
 * Allgemeine UTIL Funktionen für die Tabelle 'Attachment'
 */
public class Attachment
{
  /**
   * Anzeigen des Dokuments in einem neuen Fenster
   * 
   * @param attachmentRecord Das Attachment welches angzeigt werden soll.
   */
  public static void show(IClientContext context, IDataTableRecord attachmentRecord) throws Exception
  {
    String filename = URLEncoder.encode(attachmentRecord.getStringValue("filename"));
    String uri      = URLEncoder.encode(attachmentRecord.getBinaryReference("document"));
    String url = "dialogs/ShowDocument.jsp?uri="+uri+"&filename="+filename;
    IUrlDialog dialog = context.createUrlDialog(url);
    dialog.show(600,500);
  }
}

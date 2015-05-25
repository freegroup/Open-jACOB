/*
 * Created on May 5, 2004
 *
 */
package jacob.event.ui.attachment;

import java.net.URLEncoder;

import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IUrlDialog;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * @author Andreas Herz
 *
 */
public class AttachmentView extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: AttachmentView.java,v 1.1 2005/06/03 15:18:54 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

	/* 
   * Get the selected record (the attachment) and send them to a display
   * dialog.
   * 
	 */
	public void onAction(IClientContext context, IGuiElement button) throws Exception
	{
			IDataTableRecord currentDataRecord = context.getSelectedRecord();
      
      String swapped  = currentDataRecord.getStringValue("fileswapped");
      // Is the file currently in the database or is it archived on tape/disk/trash
      if(swapped.equals("0"))
      {
        String filename = URLEncoder.encode(currentDataRecord.getStringValue("filename"));
        String uri      = URLEncoder.encode(currentDataRecord.getBinaryReference("document"));
        String url = "dialogs/ShowDocument.jsp?uri="+uri+"&filename="+filename;
        IUrlDialog dialog = context.createUrlDialog(url);
        dialog.show(600,500);
      }
      else
      {
        alert("Datei wurde ausgelagert."); 
      }
  }
  
  /**
   * Disable the button if the attachment has swapped out.
   * The attachment has been deleted in the database.
   * 
   */
  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement button) throws Exception
  {
    // only enable the button if the attachment is not swapped out 
    //
    if(status == IGuiElement.SELECTED)
    {
      IDataTableRecord currentDataRecord = context.getSelectedRecord();
      String swapped  = currentDataRecord.getStringValue("fileswapped");
      // Is the file currently in the database or is it archived on tape/disk/trash
      if(!swapped.equals("0"))
      {
        button.setEnable(false);
      }
    }
  }
  
}


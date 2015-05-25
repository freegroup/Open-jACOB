/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 23:03:23 CEST 2010
 */
package jacob.event.ui.menutree;

import jacob.browser.Attachment_thumbnailBrowser;
import jacob.common.GallerieCellRenderer;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;

/**
 *
 * @author andherz
 */
public class ArticleListbox extends ITableListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: ArticleListbox.java,v 1.1 2010-08-17 22:02:39 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";
	
  GallerieCellRenderer renderer = new GallerieCellRenderer();
  
  /**
   * Called, if the user clicks on one entry n the TableListBox
   * 
   * @param context The current work context of the jACOB application. 
   * @param emitter The emitter of the event.
   * @param selectedRecord The entry which the user has been selected
   */
  public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
  {
    // your code here
  }

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
  {
    listBox.installCellRenderer(context, Attachment_thumbnailBrowser.browserPkey, renderer);
  }

}

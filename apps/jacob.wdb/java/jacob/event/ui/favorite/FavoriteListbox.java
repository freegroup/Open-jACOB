/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Mon Aug 16 22:42:52 CEST 2010
 */
package jacob.event.ui.favorite;

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
public class FavoriteListbox extends ITableListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: FavoriteListbox.java,v 1.2 2010-08-17 20:46:53 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.2 $";

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

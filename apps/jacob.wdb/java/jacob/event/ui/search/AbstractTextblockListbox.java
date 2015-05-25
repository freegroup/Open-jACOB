/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed Aug 11 09:27:44 CEST 2010
 */
package jacob.event.ui.search;

import jacob.browser.Textblock01Browser;
import jacob.common.ArticleLinkEventListener;
import jacob.common.ArticleLinkParser;
import jacob.event.data.GlobalcontentTableRecord;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataBrowserRecord;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowserCellRenderer;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.ITableListBoxEventHandler;

/**
 *
 * @author andherz
 */
public class AbstractTextblockListbox extends ITableListBoxEventHandler
{
	static public final transient String RCS_ID = "$Id: AbstractTextblockListbox.java,v 1.3 2010-10-04 22:18:00 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.3 $";

	IBrowserCellRenderer wysiwygRenderer = new TextblockCellRenderer();
	
	/**
	 * Called, if the user clicks on one entry n the TableListBox
	 * 
	 * @param context The current work context of the jACOB application. 
	 * @param emitter The emitter of the event.
   * @param selectedRecord The entry which the user has been selected
	 */
	public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
	{
	}


  public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
	{
    listBox.installCellRenderer(context, Textblock01Browser.browserContent, wysiwygRenderer);
    listBox.setLinkHandling(new ArticleLinkParser(),new ArticleLinkEventListener());
    
    IDataBrowser databrowser = listBox.getData();
    for (int i = 0; i < databrowser.recordCount(); i++)
    {
      IDataBrowserRecord record = databrowser.getRecord(i);
      listBox.setEmphasize(context, record, GlobalcontentTableRecord.isHighlighted(context, record));
    }

	}
}

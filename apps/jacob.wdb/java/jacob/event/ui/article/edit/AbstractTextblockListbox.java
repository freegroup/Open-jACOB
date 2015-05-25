/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Fri Aug 13 18:56:48 CEST 2010
 */
package jacob.event.ui.article.edit;

import jacob.browser.Textblock01Browser;
import jacob.common.ArticleLinkEventListener;
import jacob.common.ArticleLinkParser;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IBrowserRecordAction;
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
	static public final transient String RCS_ID = "$Id: AbstractTextblockListbox.java,v 1.4 2010-09-29 13:29:52 herz Exp $";
	static public final transient String RCS_REV = "$Revision: 1.4 $";

  IBrowserRecordAction editAction = new EditTextblockAction();
  IBrowserRecordAction deleteAction = new DeleteTextblockAction();
  TextblockCellRenderer wysiwygRenderer = new TextblockCellRenderer();

  public AbstractTextblockListbox()
	{
    wysiwygRenderer.addAction(editAction);
    wysiwygRenderer.addAction(deleteAction);
	}
	
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
    listBox.addAction(editAction);
    listBox.addAction(deleteAction);
    listBox.installCellRenderer(context, Textblock01Browser.browserContent, wysiwygRenderer);
    listBox.setLinkHandling(new ArticleLinkParser(),new ArticleLinkEventListener());
	}
}

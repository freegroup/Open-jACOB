/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Aug 17 21:32:11 CEST 2010
 */
package jacob.event.ui.search;

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
  static public final transient String RCS_ID = "$Id: ArticleListbox.java,v 1.1 2010-08-17 20:46:53 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  GallerieCellRenderer renderer = new GallerieCellRenderer();
  
  public void onSelect(IClientContext context, ITableListBox emitter, IDataTableRecord selectedRecord) throws Exception
  {
    // your code here
  }

  public void onGroupStatusChanged(IClientContext context, GroupState state, ITableListBox listBox) throws Exception
  {
    listBox.installCellRenderer(context, Attachment_thumbnailBrowser.browserPkey, renderer);
  }
}

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Aug 14 11:29:47 CEST 2010
 */
package jacob.event.ui.article.edit;


import jacob.common.TextblockEditController;
import jacob.model.Chapter;
import jacob.model.Textblock;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ITabContainer;
import de.tif.jacob.screen.ITabPane;
import de.tif.jacob.screen.ITableListBox;
import de.tif.jacob.screen.event.IOnClickEventHandler;
import de.tif.jacob.screen.event.IStaticImageEventHandler;


/**
 * You must implement the interface "IOnClickEventHandler" if you want receive the
 * onClick events of the user.
 * 
 * @author andherz
 */
public class AbstractCreateTextblockImage extends IStaticImageEventHandler implements IOnClickEventHandler
{
  static public final transient String RCS_ID = "$Id: AbstractCreateTextblockImage.java,v 1.2 2010-09-29 13:29:52 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  public void onClick(IClientContext context, IGuiElement element) throws Exception
  {
    IDataTransaction trans= context.getSelectedRecord().getCurrentTransaction();

    ITableListBox listbox = (ITableListBox)element.getGroup().findByName("chapterTextblockListbox");
    
    IDataTableRecord chapterRecord = element.getGroup().getSelectedRecord(context);
    IDataTableRecord textblockRecord= listbox.newRecord(context, trans);
    textblockRecord.setValue(trans, Textblock.chapter_key, chapterRecord.getValue(Chapter.pkey));
    
    // switch to the edit pane
    ITabContainer container = (ITabContainer)((ITabPane) element.getGroup()).getContainer(context);
    TextblockEditController.startEdit(context, container, listbox, textblockRecord);
  }

}


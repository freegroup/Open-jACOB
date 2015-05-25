/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Sep 09 02:46:03 CEST 2010
 */
package jacob.event.ui.globalcontent;

import jacob.model.Bo;
import jacob.model.Bookmark;
import jacob.model.Document;
import jacob.model.Folder;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 * The event handler for the DeleteallButton generic button.<br>
 * The {@link onAction(IClientContext, IGuiElement)} will be called, if the user
 * clicks on this button.<br>
 * Insert your custom code within this method.<br>
 * 
 * @author andreas
 */
public class DeleteallButton extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: DeleteallButton.java,v 1.1 2010-09-17 08:42:25 achim Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onClick(IClientContext context, IGuiElement emitter) throws Exception
  {
    context.getDataAccessor().qbeClearAll();
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      context.getDataAccessor().getTable(Bookmark.NAME).fastDelete(trans);
      context.getDataAccessor().getTable(Bo.NAME).fastDelete(trans);
      context.getDataAccessor().getTable(Document.NAME).fastDelete(trans);
      context.getDataAccessor().getTable(Folder.NAME).fastDelete(trans);
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  public void onGroupStatusChanged(IClientContext context, IGuiElement.GroupState status, IGuiElement emitter) throws Exception
  {
  }
}

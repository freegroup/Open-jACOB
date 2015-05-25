/*
 * Created on Jul 30, 2004
 *
 */
package jacob.event.ui.call;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IButtonEventHandler;

/**
 *	Legt ein Bookmark an welcher in dem Alert-Fenste angezeigt wird.
 *
 * @author Andreas Herz
 */
public class CallBookmark extends IButtonEventHandler
{
  static public final transient String RCS_ID = "$Id: CallBookmark.java,v 1.1 2005/06/06 12:53:05 mike Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  /* 
   * @see de.tif.jacob.screen.event.IButtonEventHandler#onAction(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement)
   */
  public void onAction(IClientContext context, IGuiElement emitter) throws Exception
  {
    IDataTransaction trans =context.getDataAccessor().newTransaction();
    try
    {
      	IDataTable table=context.getDataTable("qw_alert");
      	IDataTableRecord bookmark=table.newRecord(trans);
      	
      	bookmark.setValue(trans,"addressee",context.getUser().getLoginId());
      	bookmark.setValue(trans,"sender",context.getUser().getLoginId());
      	bookmark.setValue(trans,"tablename","call");
      	bookmark.setValue(trans,"tablekey",context.getSelectedRecord().getValue("pkey"));
      	bookmark.setValue(trans,"message","Lesezeichen für Meldung "+context.getSelectedRecord().getValue("pkey"));
      	bookmark.setValue(trans,"alerttype","Senden");
      	bookmark.setValue(trans,"dateposted","now");
      	bookmark.setValue(trans,"severity","0");
      	trans.commit();
    }
    finally
    {
     	trans.close();
    }
  }

  /* 
   * @see de.tif.jacob.screen.event.IGroupListenerEventHandler#onGroupStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState, de.tif.jacob.screen.IGuiElement)
   */
  public void onGroupStatusChanged( IClientContext context, GroupState state, IGuiElement emitter)  throws Exception
  {
    emitter.setEnable(state==IGuiElement.SELECTED);
  }

}

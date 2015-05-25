package jacob.event.ui.request;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.ISingleDataGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 * @author andherz
 */
public class RequestOwner extends IForeignFieldEventHandler
{
  static public final transient String RCS_ID = "$Id: RequestOwner.java,v 1.1 2005/08/31 21:05:43 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  // use this logger to write messages and NOT the System.out.println(..) ;-)
  static private final transient Log logger = AppLogger.getLogger();

  /**
   * This event handler will be called if the search button of the foreign field has been pressed.<br>
   * You can avoid the search action if you return [false] or you can add QBE search constraints
   * to the tables to constraint the search result.<br>
   * 
   * 
   * @param context The current work context of the jACOB application. 
   * @param foreingField The foreingField of the event.
   * @return Return 'false' if you want to avoid the execution of the search action
   */
  public boolean beforeSearch(IClientContext context, IForeignField foreingField) throws Exception
  {
    return true;
  }
  
  /**
   * This event is called if a record has been filled back in the foreign field GUI element
   * 
   * @param context The current work context of the jACOB application. 
   * @param foreignRecord The record which has been field in the foreingField.
   * @param foreingField The foreingField of the event.
   */
  public void onSelect(IClientContext context, IDataTableRecord foreignRecord, IForeignField foreingField) throws Exception
  {
    
  }
  
  /**
   * This event is called if the foreign field has been cleared.
   * 
   * @param context The current work context of the jACOB application. 
   * @param foreingField The foreingField of the event.
   */
  public void onDeselect(IClientContext context, IForeignField foreingField) throws Exception
  {
    
  }

	public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement emitter) throws Exception 
	{
		ISingleDataGuiElement field = (ISingleDataGuiElement)emitter;
    IDataTableRecord record =context.getSelectedRecord();
    if(record!=null)
    {
      String value =record.getStringValue("state");
     	field.setRequired(!"New".equals(value));
    }
	}
  
}

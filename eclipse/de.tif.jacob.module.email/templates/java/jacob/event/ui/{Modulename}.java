/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Dec 07 11:26:53 CET 2006
 */
package jacob.event.ui;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.event.IFormEventHandler;

import jacob.common.AppLogger;
import org.apache.commons.logging.Log;


/**
 *
 * @author andherz
 */
 public class {Modulename} extends IFormEventHandler 
 {
	static public final transient String RCS_ID = "$Id: {Modulename}.java,v 1.1 2008/12/18 11:30:57 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();


  /**
   * Will be called if the will be change the state from visible=>hidden.
   * 
   * This happends if the user switch the Domain or Form and the end state of this form
   * is "hidden".
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onHide(IClientContext context, IForm form) throws Exception 
  {
    context.getApplication().setSearchBrowserVisible(true);
  }
  
  /**
   * Will be called if the will be change the state from hidden=>visible.
   * 
   * This happends if the user switch to this Form.
   * 
   * @param context The current client context
   * @param group   The corresponding form for this event
   */
  public void onShow(IClientContext context, IForm form) throws Exception 
  {
		IDataTable table = context.getDataTable(jacob.model.{Modulename}.NAME);
  	if(table.recordCount()!=1);
  	{
  		table.qbeClear();
  		table.search();
      table.search(IRelationSet.LOCAL_NAME);
      if(table.recordCount()==0)
      {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        try
        {
          IDataTableRecord configuration = table.newRecord(trans);
          configuration.setStringValue(trans, jacob.model.{Modulename}.smtp_server, "<your SMPT server here>");
          trans.commit();
        }
        finally
        {
          trans.close();
        }
      }
  	}
  	context.getApplication().setSearchBrowserVisible(false);
  }
}

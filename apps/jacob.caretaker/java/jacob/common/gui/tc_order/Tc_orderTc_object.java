/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Jul 27 14:54:48 CEST 2006
 */
package jacob.common.gui.tc_order;

import jacob.common.gui.tc_object.Tc_objectSearchButton;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;


/**
 *
 * @author andreas
 */
public class Tc_orderTc_object extends IForeignFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: Tc_orderTc_object.java,v 1.1 2006/07/28 14:37:30 sonntag Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

  /**
   * This hook method will be called, if the search icon of the foreign field
   * has been pressed. <br>
   * You can avoid the search action, if you return <code>false</code> or you
   * can add QBE search constraints to the respective data tables to constraint
   * the search result. <br>
   * 
   * @param context
   *          The current client context
   * @param foreignField
   *          The foreign field itself
   * @return <code>false</code>, if you want to avoid the execution of the
   *         search action, otherwise <code>true</code>
   */
	public boolean beforeSearch(IClientContext context, IForeignField foreignField) throws Exception
	{
    Tc_objectSearchButton.constrainToExtSystem(context);
		return true;
	}
  
  /**
   * This hook method will be called, if a record has been filled back
   * (selected) in the foreign field.
   * 
   * @param context
   *          The current client context
   * @param foreignRecord
   *          The record which has been filled in the foreign field.
   * @param foreignField
   *          The foreign field itself
   */
	public void onSelect(IClientContext context, IDataTableRecord foreignRecord, IForeignField foreignField) throws Exception
	{
	}
  
  /**
   * This hook method will be called, if the foreign field has been cleared
   * (deselected).
   * 
   * @param context
   *          The current client context
   * @param foreignField
   *          The foreign field itself
   */
	public void onDeselect(IClientContext context, IForeignField foreignField) throws Exception
	{
	}
}

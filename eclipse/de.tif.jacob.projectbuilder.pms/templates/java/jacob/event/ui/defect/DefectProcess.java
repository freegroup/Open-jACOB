/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Tue Oct 24 22:00:04 CEST 2006
 */
package jacob.event.ui.defect;

import org.apache.commons.logging.Log;

import jacob.common.AppLogger;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;


/**
 *
 * @author andherz
 */
public class DefectProcess extends IForeignFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: DefectProcess.java,v 1.1 2007/11/25 22:17:54 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

  @Override
	public void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception 
	{
  	// Falls einmal ein Prozess gesetzt worden ist, dann ist dies nicht mehr änderbar
  	//
  	if(state.equals(IGuiElement.UPDATE)|| state.equals(IGuiElement.SELECTED))
  		element.setEnable(false);
  	else
  		element.setEnable(true);
	}

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

/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Sat Feb 18 01:37:48 CET 2006
 */
package jacob.event.ui.request;

import jacob.model.Category;
import jacob.model.Owner;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForeignField;
import de.tif.jacob.screen.IGroup;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IForeignFieldEventHandler;

/**
 *
 * @author andreas
 */
public class RequestCategory extends IForeignFieldEventHandler
{
	static public final transient String RCS_ID = "$Id: RequestCategory.java,v 1.1 2006/02/24 02:16:15 sonntag Exp $";
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
		return true;
	}
  
  /**
   * This hook method will be called, if a record has been filled back
   * (selected) in the foreign field.
   * 
   * @param context
   *          The current client context
   * @param categoryRecord
   *          The record which has been filled in the foreign field.
   * @param foreignField
   *          The foreign field itself
   */
	public void onSelect(IClientContext context, IDataTableRecord categoryRecord, IForeignField foreignField) throws Exception
	{
    IGroup group = context.getGroup();
    GroupState state = group.getDataStatus();
    if (state == IGuiElement.NEW || state == IGuiElement.UPDATE)
    {
      // something entered for owner?
      IForeignField requestOwner = (IForeignField) group.findByName("requestOwner");
      String value = requestOwner.getValue();
      if (value == null || value.length()==0)
      {
        // backfill owner
        IDataTable ownerTable = context.getDataAccessor().getTable(Owner.NAME);
        ownerTable.qbeClear();
        ownerTable.qbeSetKeyValue(Owner.pkey, categoryRecord.getValue(Category.categoryowner_key));
        ownerTable.search();
        IDataTableRecord ownerRecord = ownerTable.getSelectedRecord();
        if (ownerRecord != null)
          requestOwner.setValue(context, ownerRecord);
      }
    }
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

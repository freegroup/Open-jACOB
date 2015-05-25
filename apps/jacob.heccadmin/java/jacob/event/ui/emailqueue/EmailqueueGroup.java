/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Thu Nov 27 14:21:33 CET 2008
 */
package jacob.event.ui.emailqueue;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.screen.*;
import de.tif.jacob.screen.IGuiElement.GroupState;
import de.tif.jacob.screen.event.IGroupEventHandler;

import jacob.common.AppLogger;
import jacob.model.Emailqueue;
import jacob.model.Emailqueuedetails;
import org.apache.commons.logging.Log;


/**
 *
 * @author R.Spoor
 */
 public class EmailqueueGroup extends IGroupEventHandler 
 {
	static public final transient String RCS_ID = "$Id: EmailqueueGroup.java,v 1.1 2009/02/17 15:23:35 R.Spoor Exp $";
	static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
	 * Use this logger to write messages and NOT the <code>System.out.println(..)</code> ;-)
	 */
	static private final transient Log logger = AppLogger.getLogger();

	/**
	 * The status of the parent group (TableAlias) has been changed.<br>
	 * <br>
	 * Possible values for the different states are defined in IGuiElement<br>
	 * <ul>
	 *     <li>IGuiElement.UPDATE</li>
	 *     <li>IGuiElement.NEW</li>
	 *     <li>IGuiElement.SEARCH</li>
	 *     <li>IGuiElement.SELECTED</li>
	 * </ul>
	 * 
	 * @param context The current client context
	 * @param status  The new status of the group.
	 * @param group   The corresponding GUI element of this event handler
	 */
	@Override
	public void onGroupStatusChanged(IClientContext context, GroupState status, IGroup group) throws Exception
	{
        IDataTableRecord currentRecord = context.getSelectedRecord();
        IDataTable detailsTable = context.getDataAccessor().getTable(Emailqueuedetails.NAME);
        IDataTableRecord detailsRecord = detailsTable.getSelectedRecord();
	    if (status == IGuiElement.SELECTED)
	    {
	        if (detailsRecord == null)
	        {
	            IDataTransaction transaction = detailsTable.startNewTransaction();
	            try
	            {
	                detailsRecord = detailsTable.newRecord(transaction);
	                detailsRecord.setValue(transaction, Emailqueuedetails.queue_key, currentRecord.getValue(Emailqueue.pkey));
	                transaction.commit();
	            }
	            finally
	            {
	                transaction.close();
	            }
	        }
	    }
	    else if (status == IGuiElement.UPDATE)
	    {
	        IDataTransaction transaction = currentRecord.getCurrentTransaction();
	        // detailsRecord != null since SELECTED comes before UPDATE
	        detailsRecord.setValue(transaction, Emailqueuedetails.queue_key, detailsRecord.getValue(Emailqueuedetails.queue_key));
	    }
        ISingleDataGuiElement line = (ISingleDataGuiElement)group.findByName("emailqueuedetailsLine");
        line.setRequired(true);
	}

	/**
	 * Will be called if the will be change the state from visible=>hidden.
	 * 
	 * This happends if the user switch the Domain or Form which contains this group.
	 * 
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onHide(IClientContext context, IGroup group) throws Exception 
	{
	    // insert your code here
	}

	/**
	 * Will be called if the will be change the state from hidden=>visible.
	 * 
	 * This happends if the user switch to a Form which contains this group.
	 * 
	 * @param context The current client context
	 * @param group   The corresponding group for this event
	 */
	@Override
	public void onShow(IClientContext context, IGroup group) throws Exception 
	{
	    ITabContainer tabContainer = (ITabContainer)group.findByName("emailqueueContainer");
	    tabContainer.hideTabStrip(true);
	}
}

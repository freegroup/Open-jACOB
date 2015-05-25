/*
 * Created on 29.07.2004
 *
 */
package jacob.common;



import org.apache.commons.logging.Log;

import jacob.common.AppLogger;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;


/**
 * @author achim
 *  
 */
public class Task
{
    static public final transient String RCS_ID = "$Id: Task.java,v 1.3 2005/09/06 13:29:20 mike Exp $";
    static public final transient String RCS_REV = "$Revision: 1.3 $";

    static protected final transient Log logger = AppLogger.getLogger();



    /**
     * Search calls of the current user
     * 
     * @param context
     *          ClientContext
     * @param statusConstraint
     * @param relationSet
     */
    public static void findByGroupAndState(IClientContext context, String statusConstraint, String relationSet) throws Exception
    {
        IDataAccessor accessor = context.getDataAccessor(); // current data
                                                                                                                // connection
        IDataBrowser browser = context.getDataBrowser(); // the current browser
        IDataTable taskTable = context.getDataTable(); // the table in which the
                                                                                                     // actions performes
        IDataTable groupmemberTable = accessor.getTable("taskgroupmember"); //  the

        accessor.qbeClearAll();

        // set search criteria
        taskTable.qbeSetValue("status", statusConstraint);

        groupmemberTable.qbeSetKeyValue("member_key", context.getUser().getKey());


        // do the search itself
        //
        browser.search(relationSet, Filldirection.BOTH);

        // display the result set
        //
        context.getGUIBrowser().setData(context, browser);
    }
    
    public static void findByState(IClientContext context, String statusConstraint, String relationSet) throws Exception
    {
        IDataAccessor accessor = context.getDataAccessor(); // current data
                                                                                                                // connection
        IDataBrowser browser = context.getDataBrowser(); // the current browser
        IDataTable taskTable = context.getDataTable(); // the table in which the

        accessor.qbeClearAll();

        // set search criteria
        taskTable.qbeSetValue("status", statusConstraint);


        // do the search itself
        //
        browser.search(relationSet, Filldirection.BOTH);

        // display the result set
        //
        context.getGUIBrowser().setData(context, browser);
    }
    public static void findByOwnerAndState(IClientContext context, String statusConstraint, String relationSet) throws Exception
    {
        IDataAccessor accessor = context.getDataAccessor(); // current data
                                                                                                                // connection
        IDataBrowser browser = context.getDataBrowser(); // the current browser
        IDataTable taskTable = context.getDataTable(); // the table in which the
                                                                                                     // actions performes
        IDataTable groupmemberTable = accessor.getTable("taskgroupmember"); //  the

        accessor.qbeClearAll();

        // set search criteria
        taskTable.qbeSetValue("status", statusConstraint);
        taskTable.qbeSetKeyValue("taskowner_key",context.getUser().getKey());

        // do the search itself
        //
        browser.search(relationSet, Filldirection.BOTH);

        // display the result set
        //
        context.getGUIBrowser().setData(context, browser);
    }

    /**
     * Set calls.callstatus = newStatus
     * 
     * @param trans
     *          The transaction to work or null if the function must create its
     *          own transaction
     * @param context
     * @param newStatus
     */
    public static void setStatus(IDataTransaction trans, Context context, IDataTableRecord task, String newStatus) throws Exception
    {
        IDataTransaction currentTransaction = trans != null ? trans : context.getDataAccessor().newTransaction();

        try
        {
            task.setValue(currentTransaction, "status", newStatus);
            if (trans == null)
                currentTransaction.commit();
        }
        finally
        {
            if (trans == null)
                currentTransaction.close();
        }
    }

    

    public static void setDefault(IClientContext context,IDataTableRecord newtask,IDataTransaction newtrans) throws Exception 
     
    {

        newtask.setValue(newtrans,"dateassigned", null);
        newtask.setValue(newtrans,"dateowned", null);
        newtask.setValue(newtrans,"date_qa", null);
        newtask.setValue(newtrans,"datedone", null);

        // Meldung verlinken
        IDataTable calltable = context.getDataTable("call");
        if (calltable.recordCount() == 1)
        {
            IDataTableRecord callRecord = calltable.getRecord(0);
            newtask.setLinkedRecord(newtrans, callRecord);
        }
    }
}

/*
 * Created on Jun 29, 2004
 *
 */
package jacob.scheduler.system.callTaskSynchronizer;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.scheduler.TaskContextSystem;
import jacob.common.AppLogger;
import jacob.common.data.DataUtils;
import jacob.exception.BusinessException;
import jacob.scheduler.system.callTaskSynchronizer.castor.*;

/**
 * 
 */
public class GenericActor implements IActor
{
    static public final transient String RCS_ID = "$Id: GenericActor.java,v 1.10 2006/11/29 15:58:18 sonntag Exp $";

    static public final transient String RCS_REV = "$Revision: 1.10 $";

    static protected final transient Log logger = AppLogger.getLogger();

    /*
     * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskUpdateType)
     */
    public void proceed(TaskContextSystem context, TaskUpdateType update) throws Exception
    {
        throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.update) verweigert.");
    }

    /*
     * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.TaskInsertType)
     */
    public void proceed(TaskContextSystem context, TaskInsertType insert) throws Exception
    {
        throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (task.insert) verweigert.");
    }

    /*
     * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallUpdateType)
     */
    public void proceed(TaskContextSystem context, CallUpdateType update) throws Exception
    {

        throw new BusinessException("Nicht zertifiziertes Fremdsystem! Funktion (call.update) verweigert.");

    }

    /*
     * @see jacob.scheduler.system.IActor#proceed(jacob.scheduler.system.callTaskSynchronizer.CallInsertType)
     */
    public void proceed(TaskContextSystem context, CallInsertType insert) throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("bearbeite GENERIC CallInsertType mit Object Key= " + insert.getObject_key());
        }

        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        accessor.clear();
        IDataTransaction trans = accessor.newTransaction();
        try
        {
            // location anlegen, vielleicht gibt es ein Object
            IDataTableRecord newLocationRecord = null;
            String objectKey = null;
            if (insert.hasObject_key() || (insert.getObjectID() != null && insert.getInstance() != null))
            {
                IDataTable objectTable = accessor.getTable("object");
                IDataTable extSystemTable = context.getDataTable("objectext_system");
                // Object suchen
                if (insert.hasObject_key())
                {
                    objectTable.qbeSetKeyValue("pkey", Long.toString(insert.getObject_key()));
                }
                if (insert.getObjectID() != null)
                {
                    objectTable.qbeSetKeyValue("external_id", insert.getObjectID());
                }
                if (insert.getInstance() != null)
                {
                    extSystemTable.qbeSetKeyValue("name", insert.getInstance().toString());
                }
                IDataBrowser objectBrowser = accessor.getBrowser("objectBrowser");
                objectBrowser.search("r_object");
                // lösche qbe in extSystem
                // ExtSystem ist in der Query von r_call über task und
                // object ohne outher Join
                extSystemTable.qbeClear();
                extSystemTable.clear();

                if (objectBrowser.recordCount() != 1)
                {
                    throw new BusinessException("Objekt ist nicht eindeutig definiert");
                }
                IDataTableRecord objectRecord = objectBrowser.getRecord(0).getTableRecord();
                objectKey = objectRecord.getStringValue("pkey");
                if (objectRecord.hasLinkedRecord("objectlocation"))
                {
                    IDataTableRecord objectLocationRecord = objectRecord.getLinkedRecord("objectlocation");
                    newLocationRecord = accessor.cloneRecord(trans, objectLocationRecord, "location");
                }
            }
            if (newLocationRecord == null)
            {
                newLocationRecord = accessor.getTable("location").newRecord(trans);
                Location location = insert.getLocation();
                if (location != null)
                {
                    newLocationRecord.setValue(trans, "baxis_key", location.getBaxis_key());
                    newLocationRecord.setValue(trans, "building_key", location.getBuilding_key());
                    newLocationRecord.setValue(trans, "buildingpart_key", location.getBuildingpart_key());
                    newLocationRecord.setValue(trans, "buildpartobj_key", location.getBuildpartobj_key());
                    newLocationRecord.setValue(trans, "description", location.getDescription());
                    newLocationRecord.setValue(trans, "floor_key", location.getFloor_key());
                    newLocationRecord.setValue(trans, "gdsbaxis_key", location.getGdsbaxis_key());
                    newLocationRecord.setValue(trans, "gdszaxis_key", location.getGdszaxis_key());
                    newLocationRecord.setValue(trans, "note", location.getNote());
                    if (location.getOrientation() != null)
                        newLocationRecord.setValue(trans, "orientation", location.getOrientation().toString());
                    newLocationRecord.setValue(trans, "plane_key", location.getPlane_key());
                    newLocationRecord.setValue(trans, "room_key", location.getRoom_key());
                    newLocationRecord.setValue(trans, "site_key", location.getSite_key());
                    newLocationRecord.setValue(trans, "sitepart_key", location.getSitepart_key());
                    newLocationRecord.setValue(trans, "baxis_key", location.getZaxis_key());
                    newLocationRecord.setValue(trans, "zaxis_key", location.getBaxis_key());
                }
            }

            IDataTable callTable = accessor.getTable("call");

            IDataTableRecord call = callTable.newRecord(trans);

            call.setStringValueWithTruncation(trans, "problem", insert.getProblem());
            call.setStringValueWithTruncation(trans, "action", insert.getAction());
            if (insert.getProblemtext() != null)
                call.setValue(trans, "problemtext", insert.getProblemtext());
            call.setValue(trans, "callstatus", insert.getCallstatus().toString());
            if (insert.getPriority() != null)
            {
                call.setValue(trans, "priority", insert.getPriority().toString());
            }
            else
            {
                call.setValue(trans, "priority", "1-Normal");
            }
            call.setValue(trans, "datecallconnected", "now");
            call.setDateValue(trans, "datemodified", insert.getDatemodified());
            if (insert.getWarrentystatus() != null)
                call.setValue(trans, "warrentystatus", insert.getWarrentystatus().toString());
            call.setValue(trans, "callbackmethod", insert.getCallbackmethod().toString());
            call.setValue(trans, "externalorderid", insert.getExternalId());
            if (insert.hasProcess_key())
            {
                DataUtils.linkTable(accessor, trans, call, "process_key", "process", "pkey", Long.toString(insert.getProcess_key()));
            }
            else
            {
                throw new BusinessException("Tätigkeit (process_key) ist nicht  definiert");
            }
            if (insert.hasCategorycall())
            {
                DataUtils.linkTable(accessor, trans, call, "categorycall", "category", "pkey", Long.toString(insert.getCategorycall()));
            }
            else
            {
                throw new BusinessException("Gewerk (categorycall) ist nicht  definiert");
            }
            if (insert.hasAgentcall())
            {
                DataUtils.linkTable(accessor, trans, call, "agentcall", "agent", "pkey", Long.toString(insert.getAgentcall()));
            }
            else
            {
                DataUtils.linkTable(accessor, trans, call, "agentcall", "agent", "pkey", DataUtils.getAppprofileValue(context, "webqagent_key"));
            }
            if (insert.hasEmployeecall())
            {
                DataUtils.linkTable(accessor, trans, call, "employeecall", "customerint", "pkey", Long.toString(insert.getEmployeecall()));

            }
            else
            {
                throw new BusinessException("Melder (employeecall) ist nicht  definiert");
            }
            if (insert.hasWorkgroupcall() || insert.getWorkgroup_name() != null)
            {
                if (insert.hasWorkgroupcall())
                {
                    DataUtils.linkTable(accessor, trans, call, "workgroupcall", "callworkgroup", "pkey", Long.toString(insert.getWorkgroupcall()));
                }
                else
                {
                    IDataTable workgrouptable = accessor.getTable("callworkgroup");
                    workgrouptable.clear();
                    workgrouptable.qbeClear();
                    workgrouptable.qbeSetKeyValue("name", insert.getWorkgroup_name());
                    if (workgrouptable.search() == 1)
                    {
                        call.setValue(trans, "workgroupcall", workgrouptable.getRecord(0).getValue("pkey"));
                    }
                }
            }
            else
            {
                throw new BusinessException("AK (callworkgroup) ist nicht  definiert");
            }

            if (objectKey != null)
                DataUtils.linkTable(accessor, trans, call, "object_key", "object", "pkey", Long.toString(insert.getObject_key()));
            call.setValue(trans, "location_key", newLocationRecord.getValue("pkey"));

            trans.commit();
        }
        finally
        {
            trans.close();
        }

    }
}

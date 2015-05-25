/*
 * Created on 15.10.2004
 * by mike
 *
 */
package jacob.scheduler.system;

import jacob.common.AppLogger;
import jacob.common.data.AuftragsKoordinator;
import jacob.common.data.DataUtils;
import jacob.common.data.Routing;
import jacob.config.Config;
import jacob.scheduler.system.callTaskSynchronizer.castor.Call;
import jacob.scheduler.system.callTaskSynchronizer.castor.CallInsertType;
import jacob.scheduler.system.callTaskSynchronizer.castor.Job;
import jacob.scheduler.system.callTaskSynchronizer.castor.Ttsjob;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.ActorSystemType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.CallbackmethodType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.CallstatusType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.PriorityType;
import jacob.scheduler.system.callTaskSynchronizer.castor.types.WarrentystatusType;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import de.tif.jacob.core.data.IDataRecordSet;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.core.exception.RecordLockedException;
import de.tif.jacob.scheduler.ScheduleIterator;
import de.tif.jacob.scheduler.SchedulerTaskSystem;
import de.tif.jacob.scheduler.TaskContextSystem;
import de.tif.jacob.scheduler.iterators.DailyIterator;

/**
 * @author mike
 * 
 */
/**
 * Implementierung der Schnittstelle EdvinResolved Aufträe, die in EDVIN
 * fertig/dokumentiert sind müssen <br>
 * im TTS nachgezogen werden (EIMAN)
 * 
 * @author mike
 * 
 */
public class ObjectWarrenty extends SchedulerTaskSystem
{
    static public final transient String RCS_ID = "$Id: ObjectWarrenty.java,v 1.11 2008/04/29 16:50:29 sonntag Exp $";

    static public final transient String RCS_REV = "$Revision: 1.11 $";

    static protected final transient Log logger = AppLogger.getLogger();

    // Start the task every day at 01:00:00
    final ScheduleIterator iterator = new DailyIterator(1, 0, 0);

    // Start the task every 2 minutes
    // final ScheduleIterator iterator = new MinutesIterator(2);

    public ScheduleIterator iterator()
    {
        return iterator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.scheduler.SchedulerTaskSystem#run(de.tif.jacob.scheduler.TaskContextSystem)
     */
    public void run(TaskContextSystem context) throws Exception
    {
        boolean hasError = false;
        try
        {
            IDataTableRecord parameter = DataUtils.getAppprofileRecord(context);

            List myJobs = getJobs(context, parameter);
            Iterator it = myJobs.iterator();
            while (it.hasNext())
            {
                try
                {
                    IDataTableRecord object = (IDataTableRecord) it.next();
                    processJob(context, object, parameter);
                }
                catch (RecordLockedException e)
                {
                    // ignore and try next time
                    logger.info("Objektdatensatz ist gesperrt. Versuche es später nocheinmal.");
                }
                catch (Exception e)
                {
                    hasError = true;
                    ExceptionHandler.handle(e);
                }
            }
        }
        catch (Exception e)
        {
            hasError = true;
            ExceptionHandler.handle(e);
        }

        finally
        {
            if (hasError)
                DataUtils.notifyAdministrator(context, "ObjectWarrenty ist fehlgeschlagen. Bitte in catalina.out schauen");
        }
    }

    /**
     * Sucht den AK des Calls und wenn keiner gefunden wird den Warrenty
     * Problemmanager
     * 
     * @param context
     * @param object
     * @param parameter
     * @return
     * @throws Exception
     */
    private long getAKKey(TaskContextSystem context, IDataTableRecord object, IDataTableRecord parameter) throws Exception
    {
        String foundAK = "";
        if (object.hasLinkedRecord("objectlocation"))
        {
            // Routing durchführen
            String contract = Routing.getContractKey(object.getLinkedRecord("objectlocation"));
            List aks = Routing.getRoutingAK(context.getDataAccessor(), object.getSaveStringValue("objectcategory_key"), parameter.getSaveStringValue("wprocess_key"), contract);
            Iterator iter = aks.iterator();
            if (iter.hasNext()) // mindestens 1 Routingdatensatz gefunden
            {
                AuftragsKoordinator ak = (AuftragsKoordinator) iter.next();
                foundAK = ak.getPkey();
            }
        }
        // wenn beim Routing nichts oder der Problemmanager gefunden wurde, dann
        // de
        // GW Problemmanager nehmen
        if (foundAK.equals("") || foundAK.equals(parameter.getSaveStringValue("problemmanager_key")))
        {
            return parameter.getlongValue("wmanager_key");
        }
        else
        {
            return Long.parseLong(foundAK);
        }
    }

    /**
     * Erzeugt einen Eintrag in die tts_import Tabelle um einen Auftrag zu
     * aktualiseieren
     * 
     * @param context
     * @param trans
     * @param task
     */
    private void createImportRecord(TaskContextSystem context, IDataTransaction trans, IDataTableRecord object, IDataTableRecord parameter) throws Exception
    {
        // TtsJob zusammenbastelen

        CallInsertType callInsertType = new CallInsertType();
        callInsertType.setProblem("Bitte Gewährleistung überprüfen");
        callInsertType.setCallstatus(CallstatusType.VALUE_2); // AK zugewiesen
        callInsertType.setPriority(PriorityType.VALUE_0); // normal
        callInsertType.setWarrentystatus(WarrentystatusType.VALUE_0); // verfolgen
        callInsertType.setObject_key(object.getlongValue("pkey"));
        callInsertType.setProcess_key(parameter.getlongValue("wprocess_key"));
        callInsertType.setDatemodified(new Date());
        callInsertType.setCallbackmethod(CallbackmethodType.KEINE);
        // Agent und melder aus Appprofile holen.
        long key = 62203; // Default Schwandt ;-)
        try
        {
            key = parameter.getlongValue("warrentyagent_key");
        }
        catch (NumberFormatException e)
        {
            DataUtils.notifyAdministrator(context, "Für ObjectWarrenty sind die Parameter nicht gesetzt.");
        }
        callInsertType.setAgentcall(key);
        callInsertType.setEmployeecall(key);
        // Gewerk setzen
        String noCategoryLocation = "";
        if (object.hasLinkedRecord("objectcategory"))
        {
            callInsertType.setCategorycall(object.getlongValue("objectcategory_key"));
            // AK setzen
            callInsertType.setWorkgroupcall(getAKKey(context, object, parameter));
        }
        else
        {
            // GW Problemmanger setzen
            callInsertType.setWorkgroupcall(parameter.getintValue("wmanager_key"));
            callInsertType.setCategorycall(Long.parseLong(Config.getString("objectWarrentyDefaultCategory")));
            noCategoryLocation = " kein Gewerk";
        }
        if (object.hasLinkedRecord("objectlocation"))
        {
            if (!object.getLinkedRecord("objectlocation").hasLinkedRecord("objfaplissite"))
            {
                noCategoryLocation = noCategoryLocation + " Ort ohne Werk";
            }
        }
        else
        {
            noCategoryLocation = noCategoryLocation + " kein Ort";
        }
        if (noCategoryLocation.length() > 2)
        {
            callInsertType.setProblem("Bitte Gewährleistung überprüfen. Achtung Objekt hat" + noCategoryLocation);
        }
        else
        {
            callInsertType.setProblem("Bitte Gewährleistung überprüfen.");
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("workgroupcall=" + callInsertType.getWorkgroupcall());
            logger.debug("CategoryKey=" + callInsertType.getCategorycall());
            logger.debug("Problem=" + callInsertType.getProblem());
        }

        Call call = new Call();
        call.setInsert(callInsertType);

        Job job = new Job();
        job.setCall(call);
        Ttsjob ttsJob = new Ttsjob();
        ttsJob.setActorSystem(ActorSystemType.GENERIC);
        ttsJob.setJob(job);

        // XML formatieren
        StringWriter sw = new StringWriter();
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(ttsJob, doc);
        org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
        outFormat.setIndenting(true);
        outFormat.setIndent(2);
        outFormat.setLineWidth(200);
        outFormat.setEncoding("ISO-8859-1");
        org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(sw, outFormat);
        xmlser.serialize(doc);

        // tts_import Datensatz speichern
        IDataTable ttsImport = context.getDataTable("tts_import");
        IDataTableRecord ttsImportRecord = ttsImport.newRecord(trans);
        ttsImportRecord.setValue(trans, "origin", "ObjectWarrenty");
        ttsImportRecord.setValue(trans, "xml", sw.toString());

    }

    /**
     * @param context
     * @param task
     * @return
     * @throws Exception
     */
    public void processJob(TaskContextSystem context, IDataTableRecord object, IDataTableRecord parameter) throws Exception
    {
        IDataTransaction trans = context.getDataAccessor().newTransaction();
        try
        {
            object.setValue(trans, "warranty_controled", "1");
            createImportRecord(context, trans, object, parameter);
            trans.commit();

        }

        finally
        {
            trans.close();
        }

    }

    /**
     * @return ArrayList of taskrecords
     * @throws Exception
     */
    public List getJobs(TaskContextSystem context, IDataTableRecord parameter) throws Exception
    {
        // Objekte suchen mit normalen warrenty_end

        IDataTable objectTable = context.getDataTable("object");
        objectTable.clear();
        objectTable.qbeClear();
        String endsInDays = parameter.getSaveStringValue("warrentyend");
        objectTable.qbeSetValue("warranty_controled", "0");
        objectTable.qbeSetValue("warranty_extension", "NULL");
        objectTable.qbeSetValue("objstatus", "in Betrieb|in Reparatur|außer Betrieb|im Depot");

        objectTable.qbeSetValue("warranty_end", "today..today+" + endsInDays + "d");

        objectTable.setMaxRecords(IDataRecordSet.UNLIMITED_RECORDS);
        objectTable.search();
        List jobs = new ArrayList();
        for (int i = 0; i < objectTable.recordCount(); i++)
        {
            jobs.add(objectTable.getRecord(i));

        }
        // Objekte suchen mit warranty_extension

        objectTable.clear();
        objectTable.qbeClear();
        objectTable.qbeSetValue("warranty_controled", "0");
        objectTable.qbeSetValue("warranty_extension", "today..today+" + endsInDays + "d");
        objectTable.qbeSetValue("objstatus", "in Betrieb|in Reparatur|außer Betrieb|im Depot");
        objectTable.search();
        for (int i = 0; i < objectTable.recordCount(); i++)
        {
            jobs.add(objectTable.getRecord(i));

        }

        return jobs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.scheduler.SchedulerTask#hibernatedOnSchedule()
     */
    public boolean hibernatedOnSchedule()
    {
        return false;
    }
}

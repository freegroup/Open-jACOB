/*
 * Created on 12.09.2005 by mike
 * 
 *
 */
package jacob.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * Diese Klasse dient dazu eine Eskalation für call und task zu integrieren
 * 
 * @author mike
 * 
 */
public class Escalation
{
    static public final transient String RCS_ID = "$Id: Escalation.java,v 1.3 2006/05/16 11:50:33 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.3 $";

    static protected final transient Log logger = AppLogger.getLogger();

    // die unterschiedlichen Meldungsstatus
    public static final String RUECKRUF = "Rückruf";

    public static final String DURCHGESTELLT = "Durchgestellt";

    public static final String VERWORFEN = "Verworfen";

    public static final String AK_ZUGEWIESEN = "AK zugewiesen";

    public static final String FEHLGEROUTET = "Fehlgeroutet";

    public static final String ANGENOMMEN = "Angenommen";

    public static final String FERTIG_GEMELDET = "Fertig gemeldet";

    public static final String FERTIG_AKZEPTIERT = "Fertig akzeptiert";

    public static final String DOKUMENTIERT = "Dokumentiert";

    public static final String GESCHLOSSEN = "Geschlossen";

    // taskStatus
    public static final String NEU = "Neu";

    public static final String ANGELEGT = "Angelegt";

    public static final String FREIGEGEBEN = "Freigegeben";

    public static final String STORNIERT = "Storniert";

    public static final String IN_ARBEIT = "In Arbeit";

    public static final String ABGERECHNET = "Abgerechnet";

    public static final String ABGESCHLOSSEN = "Abgeschlossen";

    // Escalation constraints
    public static final String NULL = "NULL";

    public static final String NICHT_ZUGEWIESEN = "nicht zugewiesen|nicht Angenommen|nicht fertig gemeldet";

    public static final String NICHT_ANGENOMMEN = "nicht Angenommen|nicht fertig gemeldet";

    public static final String NICHT_FERITIG = "nicht fertig gemeldet";

    private static final Map statusEvent = new HashMap();

    static
    {
        statusEvent.put(RUECKRUF, NICHT_ZUGEWIESEN);
        statusEvent.put(DURCHGESTELLT, NICHT_ZUGEWIESEN);
        statusEvent.put(VERWORFEN, NULL); // Kein Status Event
        statusEvent.put(AK_ZUGEWIESEN, NICHT_ANGENOMMEN);
        statusEvent.put(FEHLGEROUTET, FEHLGEROUTET);
        statusEvent.put(ANGENOMMEN, NICHT_FERITIG);
        statusEvent.put(FERTIG_GEMELDET, NULL);
        statusEvent.put(FERTIG_AKZEPTIERT, NULL);
        statusEvent.put(DOKUMENTIERT, NULL);
        statusEvent.put(GESCHLOSSEN, NULL);
        statusEvent.put(NEU, NICHT_ZUGEWIESEN);
        statusEvent.put(ANGELEGT, NICHT_ZUGEWIESEN);
        statusEvent.put(FREIGEGEBEN, NICHT_ANGENOMMEN);
        statusEvent.put(STORNIERT, NULL);
        ;
        statusEvent.put(IN_ARBEIT, NICHT_FERITIG);
        statusEvent.put(ABGERECHNET, NULL);
        statusEvent.put(ABGESCHLOSSEN, NULL);

    }

    private static void createEscalationRecord(IDataTransaction trans, IDataTableRecord record, IDataTableRecord escalationRecord, IDataTableRecord eventRecord) throws Exception
    {
        eventRecord.setValue(trans, "tablename", record.getTableAlias().getName());
        eventRecord.setValue(trans, "tablekey", record.getValue("pkey"));
        eventRecord.setValue(trans, "escalationkey", escalationRecord.getValue("pkey"));
        eventRecord.setStringValueWithTruncation(trans, "message", Yan.parseDBFields(record, escalationRecord.getSaveStringValue("subject"), false));
        eventRecord.setValue(trans, "severity", "1");
        eventRecord.setValue(trans, "datemodified", "now");
        // wenn benachrichtige ich?
        // primären Arbeitsgruppe
        // Eskalationsgruppe
        // Owner der Meldung
        String workgroupKey = null;
        String notifyTarget = escalationRecord.getSaveStringValue("notifytarget");
        if ("Eskalationsgruppe".equals(notifyTarget))
        {
            workgroupKey = escalationRecord.getSaveStringValue("escgroupescalation");
        }
        else if ("Owner der Meldung".equals(notifyTarget))
        {
            if ("task".equals(record.getTableAlias().getName()))
            {
                workgroupKey = record.getLinkedRecord("call").getStringValue("workgroupcall");
            }
            else
            {
                workgroupKey = record.getStringValue("workgroupcall");
            }
        }
        else if ("primären Arbeitsgruppe".equals(notifyTarget))
        {
            if ("task".equals(record.getTableAlias().getName()))
            {
                workgroupKey = record.getStringValue("workgrouptask");
            }
            else
            {
                workgroupKey = record.getStringValue("workgroupcall");
            }
        }
        eventRecord.setValue(trans, "workgroupkey", workgroupKey);
        eventRecord.setValue(trans, "workgroupkeyname", "pkey");

        // wann benachrichtige ich
        String dateReported;
        if ("task".equals(record.getTableAlias().getName()))
        {
            dateReported = "daterequested";
        }
        else
        {
            dateReported = "datereported";
        }
        int delay = escalationRecord.getintValue("delay");
        int units = 1; // Einheit in Minuten
        if ("Stunden".equals(escalationRecord.getSaveStringValue("delayunits")))
        {
            units = 60;
        }

        Date date = record.getDateValue(dateReported);
        if ("Planzeit".equals(escalationRecord.getSaveStringValue("basetime")) && record.getValue("estimateddone") != null)
        {
          //MIKE: was ist wenn estimateddone = null??!!
          
            date = record.getDateValue("estimateddone");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, delay * units);
        eventRecord.setDateValue(trans, "when", cal.getTime());
        if (escalationRecord.getintValue("repeated") == 1)
        {
            eventRecord.setIntValue(trans, "repeatinterval", delay * units);
        }

    }

    private static void deleteEscalationRecords(IDataTableRecord record) throws Exception
    {
        IDataTable eventTable = record.getAccessor().getTable("qw_events");
        eventTable.clear();
        eventTable.qbeClear();
        eventTable.qbeSetKeyValue("tablekey", record.getSaveStringValue("pkey"));
        eventTable.qbeSetKeyValue("tablename", record.getTableAlias().getName());
        eventTable.qbeSetValue("escalationkey", ">0");
        eventTable.qbeSetValue("when", ">now+1min");
        IDataTransaction trans = eventTable.startNewTransaction();
        try
        {
            eventTable.searchAndDelete(trans);
            trans.commit();
        }
        finally
        {
            trans.close();
        }
    }

    public static void check(IDataTableRecord record) throws Exception
    {
        // erst die alten Eskalationssätze löschen
        deleteEscalationRecords(record);

        // dann die Eskalationssätze finden und anlegen
        String targetTable = null;
        String status = null;
        String workgroupKey = null;
        String categoryKey = null;
        if ("task".equals(record.getTableAlias().getName()))
        {
            status = record.getSaveStringValue("taskstatus");
            targetTable = "Auftrag";
            workgroupKey = record.getStringValue("workgrouptask");
            categoryKey = record.getLinkedRecord("call").getStringValue("categorycall");
        }
        else
        {
            targetTable = "Meldung";
            status = record.getSaveStringValue("callstatus");
            workgroupKey = record.getStringValue("workgroupcall");
            categoryKey = record.getStringValue("categorycall");
        }
        IDataTable escalationTable = record.getAccessor().getTable("callescalation");
        escalationTable.clear();
        escalationTable.qbeClear();
        // constraint setzen
        escalationTable.qbeSetValue("targettable", targetTable);
        escalationTable.qbeSetValue("type", (String) statusEvent.get(status));
        String priority = record.getStringValue("priority");
        if (workgroupKey != null)
        {
            // Regel für eine bestimmte Gruppe und für alle
            escalationTable.qbeSetValue("groupescalation", workgroupKey + "|NULL");
        }
        else
        {
            escalationTable.qbeSetValue("groupescalation", "NULL");
        }
        if (priority != null)
        {
            escalationTable.qbeSetValue("priority", priority + "|NULL");
        }
        else
        {
            escalationTable.qbeSetValue("priority", "NULL");
        }
        if (categoryKey != null)
        {
            // einschränken auf das Gewerk der Meldung
            escalationTable.qbeSetValue("categoryescalation", categoryKey + "|NULL");
        }
        else
        {
            escalationTable.qbeSetValue("categoryescalation", "NULL");
        }
        // suchen
        escalationTable.search();
        if (escalationTable.recordCount() == 0)
        {
            return;
        }
        IDataTable eventTable = record.getAccessor().getTable("qw_events");
        IDataTransaction trans = eventTable.startNewTransaction();
        try
        {
            for (int i = 0; i < escalationTable.recordCount(); i++)
            {
                IDataTableRecord escalationRecord = escalationTable.getRecord(i);
                if ("Planzeit".equals(escalationRecord.getSaveStringValue("basetime")) && record.getValue("estimateddone") == null)
                {
                  continue;
                }
                IDataTableRecord eventRecord = eventTable.newRecord(trans);
                createEscalationRecord(trans, record, escalationRecord, eventRecord);
            }
            trans.commit();
        }
        finally
        {
            trans.close();
        }

    }
}

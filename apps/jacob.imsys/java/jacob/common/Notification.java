/*
 * Created on 29.06.2005 by mike
 * 
 *
 */
package jacob.common;

import java.util.HashMap;
import java.util.Map;

import jacob.common.data.DataUtils;

import jacob.exception.BusinessException;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;

/**
 * @author mike
 * 
 */
public class Notification
{
    static public final transient String RCS_ID = "$Id: Notification.java,v 1.2 2005/11/17 17:35:15 mike Exp $";

    static public final transient String RCS_REV = "$Revision: 1.2 $";

    static protected final transient Log logger = AppLogger.getLogger();

    // Map[Notificationgroup->Callback]
    private static final Map actionNotificatorCallbacks = new HashMap();

    static
    {
        // -------------------------------------------------------------------------------
        // Aktionshandler für das Versenden von Meldungen
        // -------------------------------------------------------------------------------
        actionNotificatorCallbacks.put("folgende Adresse", new ActionNotificatorAddresse());
        actionNotificatorCallbacks.put("Kunde", new ActionNotificatorKunde());
        actionNotificatorCallbacks.put("Mitarbeiter", new ActionNotificatorMitarbeiter());
        actionNotificatorCallbacks.put("Arbeitsgruppe", new ActionNotificatorArbeitsgruppe());
        actionNotificatorCallbacks.put("NM Meisterei", new ActionNotificatorArbeitsgruppeNM());
        actionNotificatorCallbacks.put("AK des Auftrags", new ActionNotificatorAKdesAuftrags());

    }

    /**
     * erzeugt ein Bookmark in Tabelle qw_allert
     * 
     * @param context
     * @param currentRecord
     * @param transaction
     * @param addressee
     * @param message
     * @throws Exception
     */
    public static void createBookmark(Context context, IDataTableRecord currentRecord, IDataTransaction transaction, String addressee, String message) throws Exception
    {
        IDataTable table = context.getDataTable("qw_alert");
        IDataTableRecord bookmark = table.newRecord(transaction);
        bookmark.setValue(transaction, "addressee", addressee);
        bookmark.setValue(transaction, "sender", Util.getUserLoginID(context));
        bookmark.setStringValueWithTruncation(transaction, "message", message);
        bookmark.setValue(transaction, "tablekey", currentRecord.getStringValue("pkey"));
        bookmark.setValue(transaction, "tablename", currentRecord.getTableAlias().getName());
        bookmark.setValue(transaction, "alerttype", "Warnung");
        bookmark.setValue(transaction, "dateposted", "now");
        bookmark.setIntValue(transaction, "severity", recordSeverity(currentRecord.getSaveStringValue("priority")));
    }

    /**
     * Benachrichtigungen (eMAil, Fax, SMS, ..) auswerten und ausführen für
     * calls
     * 
     * @param callRecord
     * @param transaction
     * @throws Exception
     */
    public static void checkCallAction(IDataTableRecord callRecord) throws Exception
    {
        IDataTransaction transaction = callRecord.getAccessor().newTransaction();

        try
        {
            // Falls sich eines dieser Felder geändert hat, muss die
            // Aktionsregeln
            // ausgewertet werden.
            //
            if (callRecord.hasChangedValue("callstatus") || callRecord.hasChangedValue("workgroupcall"))
            {
                // 1. Aktionsregel finden
                //
                IDataTable action = callRecord.getAccessor().getTable("callaction");
                action.qbeClear();
                action.qbeSetValue("targettable", "Meldung");

                // wenn Meldung autodokumentiert, dann muss Status Fertig
                // gemeldet auch
                // berückdichtigt werden
                String callstatus = callRecord.getSaveStringValue("callstatus");
                if ("Dokumentiert".equals(callstatus) && "Angenommen".equals(callRecord.getOldValue("callstatus")))
                {
                    action.qbeSetValue("statusevent", "Fertig gemeldet|Dokumentiert");
                }
                else
                {
                    action.qbeSetValue("statusevent", callstatus);
                }

                if (callRecord.getValue("priority") != null)
                    action.qbeSetValue("priority", "NULL|" + callRecord.getSaveStringValue("priority"));
                else
                    action.qbeSetValue("priority", "NULL");

                if (callRecord.getValue("categorycall") != null)
                {
                    action.qbeSetValue("excludecategory", "!" + callRecord.getSaveStringValue("categorycall") + ",|NULL");
                    action.qbeSetValue("categoryaction", "NULL|" + callRecord.getSaveStringValue("categorycall"));
                }
                else
                    action.qbeSetValue("categoryaction", "NULL");

                if (callRecord.getValue("workgroupcall") != null)
                {
                    action.qbeSetValue("groupaction", "NULL|" + callRecord.getSaveStringValue("workgroupcall"));
                    IDataTableRecord workgroup = callRecord.getLinkedRecord("callworkgroup");
                    action.qbeSetValue("migration", workgroup.getSaveStringValue("migration"));
                }
                else
                    action.qbeSetValue("groupaction", "NULL");

                if (action.search() > 0)
                {
                    if (logger.isDebugEnabled())
                        logger.debug(action.recordCount() + " Aktionsregeln gefunden.!!!!!");
                    for (int i = 0; i < action.recordCount(); i++)
                    {
                        IDataTableRecord currentAction = action.getRecord(i);
                        String recipienType = currentAction.getSaveStringValue("recipient");
                        ActionNotificator notificator = (ActionNotificator) actionNotificatorCallbacks.get(recipienType);
                        if (notificator != null)
                            notificator.send(callRecord, currentAction, transaction);
                        else
                            throw new BusinessException("Unbekannte (nicht implementierte) Benachrichtigungsart [" + recipienType + "]");
                    }
                }
            }
            transaction.commit();
        }
        finally
        {
            transaction.close();
        }
    }

    /**
     * Benachrichtigungen (eMAil, Fax, SMS, ..) auswerten und ausführen für
     * tasks
     * 
     * @param taskRecord
     * @param transaction
     * @throws Exception
     */
    public static void checkTaskAction(IDataTableRecord taskRecord) throws Exception
    {
        IDataTransaction transaction = taskRecord.getAccessor().newTransaction();

        try
        {
            // Falls sich eines dieser Felder geändert hat, muss die
            // Aktionsregeln
            // ausgewertet werden.
            //
            if (taskRecord.hasChangedValue("taskstatus") || taskRecord.hasChangedValue("workgrouptask"))
            {
                // 1. Aktionsregel finden
                //
                IDataTable action = taskRecord.getAccessor().getTable("callaction");
                action.qbeClear();
                action.qbeSetValue("targettable", "Auftrag");

                String taskstatus = taskRecord.getSaveStringValue("taskstatus");

                action.qbeSetValue("statusevent", taskstatus);

                if (taskRecord.getValue("priority") != null)
                    action.qbeSetValue("priority", "NULL|" + taskRecord.getSaveStringValue("priority"));
                else
                    action.qbeSetValue("priority", "NULL");
                if (taskRecord.hasLinkedRecord("call"))
                {
                    if (taskRecord.getLinkedRecord("call").getValue("categorycall") != null)
                    {
                        action.qbeSetValue("excludecategory", "!" + taskRecord.getLinkedRecord("call").getSaveStringValue("categorycall") + ",|NULL");
                        action.qbeSetValue("categoryaction", "NULL|" + taskRecord.getLinkedRecord("call").getSaveStringValue("categorycall"));
                    }
                    else
                    {
                        action.qbeSetValue("categoryaction", "NULL");
                    }
                }
                if (taskRecord.getValue("workgrouptask") != null)
                {
                    action.qbeSetValue("groupaction", "NULL|" + taskRecord.getSaveStringValue("workgrouptask"));
                }
                else
                {
                    action.qbeSetValue("groupaction", "NULL");
                }
                if (taskRecord.getValue("ext_system_key") != null)
                {
                    action.qbeSetValue("ext_system_key", "NULL|" + taskRecord.getSaveStringValue("ext_system_key"));

                }
                else
                {
                    action.qbeSetValue("ext_system_key", "NULL");
                }
                if (taskRecord.getValue("tasktype_key") != null)
                {
                    action.qbeSetValue("tasktype_key", "NULL|" + taskRecord.getSaveStringValue("tasktype_key"));

                }
                else
                {
                    action.qbeSetValue("tasktype_key", "NULL");
                }

                if (action.search() > 0)
                {
                    // Wegen dem Trigger auf taskno muss der taskRecord neu geladen werden
                    IDataTableRecord refreshedRecord = taskRecord.getTable().loadRecord(taskRecord.getPrimaryKeyValue());
                    
                    if (logger.isDebugEnabled())
                        logger.debug(action.recordCount() + " Aktionsregeln gefunden.!!!!!");
                    for (int i = 0; i < action.recordCount(); i++)
                    {
                        IDataTableRecord currentAction = action.getRecord(i);
                        String recipienType = currentAction.getSaveStringValue("recipient");
                        ActionNotificator notificator = (ActionNotificator) actionNotificatorCallbacks.get(recipienType);
                        if (notificator != null)
                            notificator.send(refreshedRecord, currentAction, transaction);
                        else
                            throw new BusinessException("Unbekannte (nicht implementierte) Benachrichtigungsart [" + recipienType + "]");
                    }
                }
            }
            transaction.commit();
        }
        finally
        {
            transaction.close();
        }
    }

    /**
     * Verschickt eine Nachricht über die Fertigmeldung des Auftrages an den
     * Owner der Meldung, sofern Feedback erwünscht ist.
     * 
     * @param taskRecord
     *            der Auftrag
     * @param transaction
     *            die Transaktion, in deren Kontext die Fertigmeldung erfolgt
     * @throws Exception
     *             bei einem schwerwiegenden Fehler
     */
    public static void notifyOwner(IDataTableRecord taskRecord, IDataTransaction transaction) throws Exception
    {
        if ("Ja".equals(taskRecord.getValue("feedback")))
        {
            String sMsg = "Auftrag " + taskRecord.getValue("taskno") + " wurde " + taskRecord.getValue("taskstatus") + ".";
            // Documentenforlage suchen
            IDataTable doc_templateTable = taskRecord.getAccessor().getTable("doc_template");
            doc_templateTable.qbeClear();
            doc_templateTable.qbeSetValue("use_in", "Auftragrückmeldung");
            if (doc_templateTable.search() > 0)
            {
                IDataTableRecord docTemplate = doc_templateTable.getRecord(0);
                IDataTable workgroupTable = taskRecord.getAccessor().getTable("callworkgroup");
                if (workgroupTable.recordCount() != 1)
                    return;
                ActionNotificatorArbeitsgruppe.send(taskRecord, workgroupTable.getRecord(0), docTemplate, transaction, sMsg);
            }

        }
    }

    /**
     * Dies klasse wird in Abhängigkeit der Benachrichtungsart erzeugt und
     * gerufen. <br>
     * Die erzeugte Instance (singleton) ist dann für das versenden der
     * Benachrichtigungen verantwortlich.
     * 
     * @author Andreas Herz
     */
    private static interface ActionNotificator
    {
        public void send(IDataTableRecord callRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception;
    }

    private static int recordSeverity(String priority)
    {
        if (priority.equals("1-Normal"))
            return 0;
        if (priority.equals("2-Kritisch"))
            return 1;

        if (priority.equals("3-Produktion"))
            return 2;
        if (priority.equals("4-Notfall"))
            return 3;
        return 0;

    }

    /**
     * Es muss derjenige welche benachrichtigt werden der in der Aktionsregel
     * steht.
     * 
     */
    static class ActionNotificatorAddresse implements ActionNotificator
    {
        public void send(IDataTableRecord currentRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
        {
            Context context = Context.getCurrent();
            // Dokumentenvorlage zu der Actionsregel holen
            //
            IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = callAction.getSaveStringValue("method");
            String stylesheet;
            String recipienAddress;
            boolean confirmation = "Ja".equals(callAction.getSaveStringValue("confirmation"));
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + callAction.getSaveStringValue("notificationaddr");
                stylesheet = docTemplate.getStringValue("email_xsl");
            }
            else if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + callAction.getSaveStringValue("notificationaddr");
                stylesheet = docTemplate.getStringValue("fax_xsl");
            }
            else if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + callAction.getSaveStringValue("notificationaddr");
                stylesheet = docTemplate.getStringValue("sms_xsl");
            }
            else if ("Signal".equals(channel))
            {
                String document = callAction.getSaveStringValue("subject");
                document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
                createBookmark(context, currentRecord, transaction, callAction.getSaveStringValue("notificationaddr"), document);
                return;
            }
            else
            {
                throw new BusinessException("Unbekannte (nicht implementierte) Rückmeldungsart [" + channel + "] für Meldung.");
            }

            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String document = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
            Yan.createActionNotification(context, currentRecord, docTemplate, document, recipienAddress, stylesheet, true, null, confirmation);

        }
    }

    /**
     * Der Kunde muss benachrichtigt werden, dass sich an seinem Ticket etwas
     * getan hat.
     * 
     */
    static class ActionNotificatorKunde implements ActionNotificator
    {
        public void send(IDataTableRecord currentRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
        {

            Context context = Context.getCurrent();
            // Dokumentenvorlage zu der Actionsregel holen
            //
            IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
            IDataTableRecord customer = null;
            if (currentRecord.getTableAlias().getName().startsWith("call"))
            {
                customer = currentRecord.getLinkedRecord("customerint");
            }
            else
            {
                customer = currentRecord.getLinkedRecord("call").getLinkedRecord("customerint");
            }
            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = callAction.getSaveStringValue("method");
            String stylesheet;
            String recipienAddress;
            boolean confirmation = "Ja".equals(callAction.getSaveStringValue("confirmation"));
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + customer.getSaveStringValue("emailcorr");
                stylesheet = docTemplate.getStringValue("email_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);

                return;
            }
            if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + customer.getSaveStringValue("faxcorr");
                stylesheet = docTemplate.getStringValue("fax_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);

                return;
            }
            if ("Keine".equals(channel))
            {
                return;
            }
            if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + customer.getSaveStringValue("pager");
                stylesheet = docTemplate.getStringValue("sms_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }

            String document = callAction.getSaveStringValue("subject");
            document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
            createBookmark(context, currentRecord, transaction, DataUtils.getAppprofileValue(context, "custfeedbacklogin"), document);
        }
    }

    /**
     * Nachricht an den Agenten der Meldung
     * 
     */
    static class ActionNotificatorMitarbeiter implements ActionNotificator
    {
        public void send(IDataTableRecord currentRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
        {
            Context context = Context.getCurrent();
            // Dokumentenvorlage zu der Actionsregel holen
            //
            IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
            IDataTableRecord agent = null;
            if (currentRecord.getTableAlias().getName().startsWith("call"))
            {
                agent = currentRecord.getLinkedRecord("agent");
            }
            else
            {
                agent = currentRecord.getLinkedRecord("call").getLinkedRecord("agent");
            }
            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = agent.getSaveStringValue("communicatepref");
            String stylesheet;
            String recipienAddress;
            boolean confirmation = "Ja".equals(callAction.getSaveStringValue("confirmation"));
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + agent.getSaveStringValue("emailcorr");
                stylesheet = docTemplate.getStringValue("email_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);

                return;
            }
            if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + agent.getSaveStringValue("faxcorr");
                stylesheet = docTemplate.getStringValue("fax_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + agent.getSaveStringValue("pager");
                stylesheet = docTemplate.getStringValue("sms_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }

            if ("Keine".equals(channel))
            {
                return;
            }

            String document = callAction.getSaveStringValue("subject");
            document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
            String address = agent.getStringValue("loginname");
            if (address == null)
                return;
            createBookmark(context, currentRecord, transaction, address, document);

        }
    }

    /**
     * Es soll vom Auftrag der Owner der dazugehörigen Meldung benachrichtigt
     * werden
     * 
     */
    static class ActionNotificatorAKdesAuftrags implements ActionNotificator
    {

        public void send(IDataTableRecord currentTaskRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
        {
            Context context = Context.getCurrent();
            // Dokumentenvorlage zu der Actionsregel holen
            //
            IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
            IDataTableRecord workgroup = currentTaskRecord.getLinkedRecord("call").getLinkedRecord("callworkgroup");

            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = workgroup.getSaveStringValue("notifymethod");
            String stylesheet;
            String recipienAddress;
            String Address = workgroup.getSaveStringValue("notificationaddr");
            boolean confirmation = "Ja".equals(callAction.getSaveStringValue("confirmation"));
            if ("Bearbeiter".equals(channel))
            {

                ActionNotificatorArbeitsgruppe.notifyGroupmember(context, currentTaskRecord, workgroup, docTemplate, callAction, transaction);
                return;
            }
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + Address;
                stylesheet = docTemplate.getStringValue("email_xsl");
                Yan.createActionNotification(context, currentTaskRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + Address;
                stylesheet = docTemplate.getStringValue("fax_xsl");
                Yan.createActionNotification(context, currentTaskRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + Address;
                stylesheet = docTemplate.getStringValue("sms_xsl");
                Yan.createActionNotification(context, currentTaskRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Keine".equals(channel))
            {
                return;
            }
            if ("Signal".equals(channel))
            {
                if (Address != null)
                {
                    String document = callAction.getSaveStringValue("subject");
                    document = Yan.fillDBFields(context, currentTaskRecord, docTemplate, document, true, null);
                    createBookmark(context, currentTaskRecord, transaction, Address, document);
                }
                return;
            }
            // if something wrong notify special Agent

            String document = workgroup.getSaveStringValue("name") + " konnte nicht benachrichtigt werden";
            document = Yan.fillDBFields(context, currentTaskRecord, docTemplate, document, true, null);
            createBookmark(context, currentTaskRecord, transaction, DataUtils.getAppprofileValue(context, "custfeedbacklogin"), document);
        }
    }

    /**
     * Es soll die Arbeitsgruppe der aktuellen Meldung/Auftrag benachrichtigt
     * werden
     * 
     */
    static class ActionNotificatorArbeitsgruppe implements ActionNotificator
    {

        static void notifyGroupmember(Context context, IDataTableRecord currentRecord, IDataTableRecord workgroupRecord, IDataTableRecord docTemplate, IDataTableRecord callAction,
                IDataTransaction transaction) throws Exception
        {
            // Überprüfen ob gegebenfalls Eigenbenachrichtigung der Gruppe
            // vorliegt
            String UserKey = Util.getUserKey(context);
            IDataTable groupmemberTable = context.getDataTable("groupmember");
            if ("Nein".equals(workgroupRecord.getValue("notifyowngroup")) && UserKey != null)
            {
                groupmemberTable.qbeClear();
                groupmemberTable.clear();
                groupmemberTable.qbeSetKeyValue("workgroupgroup", workgroupRecord.getValue("pkey"));
                groupmemberTable.qbeSetKeyValue("employeegroup", UserKey);
                groupmemberTable.search();
                if (groupmemberTable.recordCount() > 0)
                    return;
            }
            groupmemberTable.qbeClear();
            groupmemberTable.clear();
            groupmemberTable.qbeSetKeyValue("workgroupgroup", workgroupRecord.getValue("pkey"));
            // Eigenbenachrichtigung bei Aufträgen zulassen
            if (currentRecord.getTableAlias().getName().startsWith("call"))
            {
                groupmemberTable.qbeSetValue("employeegroup", "!" + UserKey);
            }

            groupmemberTable.qbeSetValue("notifymethod", "!Keine");

            groupmemberTable.search();
            if (groupmemberTable.recordCount() < 1)
                return;

            boolean confirmation = "Ja".equals(callAction.getSaveStringValue("confirmation"));
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
            String recipienAddress;
            for (int i = 0; i < groupmemberTable.recordCount(); i++)
            {
                IDataTableRecord groupmember = groupmemberTable.getRecord(i);
                IDataTableRecord employee = groupmember.getLinkedRecord("employee");
                String channel = groupmember.getSaveStringValue("notifymethod");
                String stylesheet = groupmember.getStringValue("xsl_stylesheet");
                if ("Email".equals(channel))
                {
                    recipienAddress = "email://" + employee.getSaveStringValue("emailcorr");
                    if (stylesheet == null)
                        stylesheet = docTemplate.getStringValue("email_xsl");
                    Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                    continue;
                }
                if ("FAX".equals(channel))
                {
                    recipienAddress = "rightfax://" + employee.getSaveStringValue("faxcorr");
                    if (stylesheet == null)
                        stylesheet = docTemplate.getStringValue("fax_xsl");
                    Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                    continue;
                }
                if ("Funkruf".equals(channel) || "SMS".equals(channel))
                {
                    recipienAddress = "sms://" + employee.getSaveStringValue("pager");
                    ;
                    if (stylesheet == null)
                        stylesheet = docTemplate.getStringValue("sms_xsl");
                    Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                    continue;
                }
                if ("Signal".equals(channel))
                {
                    String document = callAction.getSaveStringValue("subject");
                    document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
                    IDataTable table = context.getDataTable("qw_alert");
                    IDataTableRecord bookmark = table.newRecord(transaction);
                    String address = employee.getStringValue("loginname");
                    if (address == null)
                        continue;
                    createBookmark(context, currentRecord, transaction, address, document);
                }
            }

        }

        /**
         * Benachrichtigung der Gruppenmitglieder des Owners der Meldung über
         * Fertigmeldung des Auftrags
         * 
         * @param context
         * @param taskRecord
         * @param workgroupRecord
         * @param docTemplate
         * @param transaction
         * @param subject
         * @throws Exception
         */
        static void notifyGroupmember(Context context, IDataTableRecord taskRecord, IDataTableRecord workgroupRecord, IDataTableRecord docTemplate, IDataTransaction transaction,
                String subject) throws Exception
        {
            // Überprüfen ob gegebenfalls Eigenbenachrichtigung der Gruppe
            // vorliegt
            // MIKE: Workaround mit UserKey lösen!
            String UserKey = Util.getUserKey(context); // context.getUser().getKey();
            IDataTable groupmemberTable = context.getDataTable("groupmember");
            if ("Nein".equals(workgroupRecord.getValue("notifyowngroup")) && UserKey != null)
            {
                groupmemberTable.qbeClear();
                groupmemberTable.clear();
                groupmemberTable.qbeSetKeyValue("workgroupgroup", workgroupRecord.getValue("pkey"));
                groupmemberTable.qbeSetKeyValue("employeegroup", UserKey);
                groupmemberTable.search();
                if (groupmemberTable.recordCount() > 0)
                    return;
            }
            groupmemberTable.qbeClear();
            groupmemberTable.clear();
            groupmemberTable.qbeSetKeyValue("workgroupgroup", workgroupRecord.getValue("pkey"));
            groupmemberTable.qbeSetValue("employeegroup", "!" + UserKey);
            groupmemberTable.qbeSetValue("notifymethod", "!Keine");

            groupmemberTable.search();
            if (groupmemberTable.recordCount() < 1)
                return;

            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
            template = Yan.fillDBFields(context, taskRecord, docTemplate, template, true, null);
            String recipienAddress;
            for (int i = 0; i < groupmemberTable.recordCount(); i++)
            {
                IDataTableRecord groupmember = groupmemberTable.getRecord(i);
                IDataTableRecord employee = groupmember.getLinkedRecord("employee");
                String channel = groupmember.getSaveStringValue("notifymethod");
                String stylesheet = groupmember.getStringValue("xsl_stylesheet");
                if ("Email".equals(channel))
                {
                    recipienAddress = "email://" + employee.getSaveStringValue("emailcorr");
                    if (stylesheet == null)
                        stylesheet = docTemplate.getStringValue("email_xsl");
                    Yan.createInstance(context, template, recipienAddress, stylesheet);
                    continue;
                }
                if ("FAX".equals(channel))
                {
                    recipienAddress = "rightfax://" + employee.getSaveStringValue("faxcorr");
                    if (stylesheet == null)
                        stylesheet = docTemplate.getStringValue("fax_xsl");
                    Yan.createInstance(context, template, recipienAddress, stylesheet);
                    continue;
                }
                if ("Funkruf".equals(channel) || "SMS".equals(channel))
                {
                    recipienAddress = "sms://" + employee.getSaveStringValue("pager");
                    ;
                    if (stylesheet == null)
                        stylesheet = docTemplate.getStringValue("sms_xsl");
                    Yan.createInstance(context, template, recipienAddress, stylesheet);
                    continue;
                }
                if ("Signal".equals(channel))
                {

                    subject = Yan.fillDBFields(context, taskRecord, docTemplate, subject, true, null);
                    String address = employee.getStringValue("loginname");
                    if (address == null)
                        continue;
                    createBookmark(context, taskRecord, transaction, address, subject);
                }
            }
        }

        public void send(IDataTableRecord currentRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
        {
            Context context = Context.getCurrent();
            // Dokumentenvorlage zu der Actionsregel holen
            //
            IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");

            IDataTableRecord workgroup = null;
            if (currentRecord.getTableAlias().getName().startsWith("call"))
            {
                workgroup = currentRecord.getLinkedRecord("callworkgroup");
            }
            else
            {
                workgroup = currentRecord.getLinkedRecord("taskworkgroup");
            }

            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = workgroup.getSaveStringValue("notifymethod");
            String stylesheet;
            String recipienAddress;
            String Address = workgroup.getSaveStringValue("notificationaddr");
            boolean confirmation = "Ja".equals(callAction.getSaveStringValue("confirmation"));
            if ("Bearbeiter".equals(channel))
            {
                notifyGroupmember(context, currentRecord, workgroup, docTemplate, callAction, transaction);
                return;
            }
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + Address;
                stylesheet = docTemplate.getStringValue("email_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + Address;
                stylesheet = docTemplate.getStringValue("fax_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + Address;
                stylesheet = docTemplate.getStringValue("sms_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Keine".equals(channel))
            {
                return;
            }

            if ("Signal".equals(channel))
            {
                String document = callAction.getSaveStringValue("subject");
                document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
                createBookmark(context, currentRecord, transaction, Address, document);
                return;
            }
            // if something wrong notify special Agent

            String document = workgroup.getSaveStringValue("name") + " konnte nicht benachrichtigt werden";
            document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
            createBookmark(context, currentRecord, transaction, DataUtils.getAppprofileValue(context, "custfeedbacklogin"), document);

        }

        /**
         * Benachrichtigung des Owners der Meldung über Fertigmeldung des
         * Auftrags
         * 
         * @param taskRecord
         * @param workgroup
         * @param docTemplate
         * @param transaction
         * @param subject
         * @throws Exception
         */
        static public void send(IDataTableRecord taskRecord, IDataTableRecord workgroup, IDataTableRecord docTemplate, IDataTransaction transaction, String subject)
                throws Exception
        {
            Context context = Context.getCurrent();

            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");
            template = Yan.fillDBFields(context, taskRecord, docTemplate, template, true, null);

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = workgroup.getSaveStringValue("notifymethod");
            String stylesheet;
            String recipienAddress;
            String Address = workgroup.getSaveStringValue("notificationaddr");

            if ("Bearbeiter".equals(channel))
            {
                notifyGroupmember(context, taskRecord, workgroup, docTemplate, transaction, subject);
                return;
            }
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + Address;
                stylesheet = docTemplate.getStringValue("email_xsl");
                Yan.createInstance(context, template, recipienAddress, stylesheet);
                return;
            }
            if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + Address;
                stylesheet = docTemplate.getStringValue("fax_xsl");
                Yan.createInstance(context, template, recipienAddress, stylesheet);
                return;
            }
            if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + Address;
                stylesheet = docTemplate.getStringValue("sms_xsl");
                Yan.createInstance(context, template, recipienAddress, stylesheet);
                return;
            }
            if ("Keine".equals(channel))
            {
                return;
            }
            if ("Signal".equals(channel))
            {

                subject = Yan.fillDBFields(context, taskRecord, docTemplate, subject, true, null);
                createBookmark(context, taskRecord, transaction, Address, subject);
                return;
            }
            // if something wrong notify special Agent

            String document = workgroup.getSaveStringValue("name") + " konnte nicht benachrichtigt werden";
            document = Yan.fillDBFields(context, taskRecord, docTemplate, document, true, null);
            createBookmark(context, taskRecord, transaction, DataUtils.getAppprofileValue(context, "custfeedbacklogin"), document);
        }
    }

    /**
     * 
     * 
     */
    static class ActionNotificatorArbeitsgruppeNM implements ActionNotificator
    {
        public void send(IDataTableRecord currentRecord, IDataTableRecord callAction, IDataTransaction transaction) throws Exception
        {
            Context context = Context.getCurrent();
            // Dokumentenvorlage zu der Actionsregel holen
            //
            IDataTableRecord docTemplate = callAction.getLinkedRecord("doc_template");
            IDataTableRecord workgroup = null;
            if (currentRecord.getTableAlias().getName().startsWith("call"))
            {
                workgroup = currentRecord.getLinkedRecord("callworkgroup");
            }
            else
            {
                workgroup = currentRecord.getLinkedRecord("taskworkgroup");
            }

            // XML-Vorlage für die Meldung holen und mit den Datenbankfeldern
            // auffüllen
            //
            String template = docTemplate.getLinkedRecord("xml_template").getStringValue("xmltext");

            // Entsprechend des Meldungskanals das Stylesheet und die
            // Empfängeradresse holen
            //
            String channel = workgroup.getSaveStringValue("notifymethod");
            String stylesheet;
            String recipienAddress;
            String Address = workgroup.getSaveStringValue("notificationaddr");
            boolean confirmation = true; // NM Meisterei wird immer
                                            // bestätigt!
            if ("Email".equals(channel))
            {
                recipienAddress = "email://" + Address;
                stylesheet = docTemplate.getStringValue("email_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("FAX".equals(channel))
            {
                recipienAddress = "rightfax://" + Address;
                stylesheet = docTemplate.getStringValue("fax_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Funkruf".equals(channel) || "SMS".equals(channel))
            {
                recipienAddress = "sms://" + Address;
                stylesheet = docTemplate.getStringValue("sms_xsl");
                Yan.createActionNotification(context, currentRecord, docTemplate, template, recipienAddress, stylesheet, true, null, confirmation);
                return;
            }
            if ("Keine".equals(channel))
            {
                return;
            }
            if ("Signal".equals(channel))
            {
                String document = callAction.getSaveStringValue("subject");
                document = Yan.fillDBFields(context, currentRecord, docTemplate, document, true, null);
                createBookmark(context, currentRecord, transaction, Address, document);
                return;
            }
            // if something wrong notify special Agent
            String document = workgroup.getSaveStringValue("name") + " konnte nicht benachrichtigt werden!";

            createBookmark(context, currentRecord, transaction, DataUtils.getAppprofileValue(context, "custfeedbacklogin"), document);

        }

    }

}

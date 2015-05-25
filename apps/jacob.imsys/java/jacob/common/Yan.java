/*
 * Created on Jul 26, 2004
 *
 */
package jacob.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.DataDocumentValue;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.data.IDataTransaction;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.xml.Converter;

/**
 * Interface für das Versenden von Nachrichten aus dem Caretaker heraus.
 * 
 */
public class Yan
{
    transient private static final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");

    transient private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

    /**
     * @author mike
     * 
     * siehe auch GenericSendAs.AskCallback
     */
    static class NotificationAskCallback implements IFormDialogCallback
    {
        String document;

        String address;

        String xslStylesheet;

        boolean cofirmation = false;

        NotificationAskCallback(String document, String address, String xslStylesheet, boolean confirmation)
        {
            this.document = document;
            this.address = address;
            this.xslStylesheet = xslStylesheet;
            this.cofirmation = confirmation;

        }

        public void onSubmit(IClientContext context, String buttonId, Map formValues) throws Exception
        {
            Iterator iter = formValues.keySet().iterator();
            boolean hasAttachments = false;
            String attachmentList = "";
            while (iter.hasNext())
            {
                String key = (String) iter.next();

                if (key.startsWith("db_field(*ASK:"))
                {
                    String value = Converter.encode((String) formValues.get(key));
                    document = StringUtils.replace(document, key, value);
                }
                if (key.startsWith("address"))
                    address = (String) formValues.get(key);
                if (key.startsWith("db_field(*ATTACHMENT)"))
                {
                    DataDocumentValue attachment = (DataDocumentValue) formValues.get(key);
                    attachmentList = attachmentList + Yan.addAttachment(context, attachment);
                    hasAttachments = true;
                }
            }
            if (hasAttachments)
                document = StringUtils.replace(document, "db_field(*ATTACHMENT)", attachmentList);
            Yan.createInstance(context, document, address, xslStylesheet);
        }
    }

    /**
     * Parst ein Dokument und füllt dieses mit den Werten des übergebenen
     * Records (und seinen gelinkten Records aus). Das Übergebene Template muss
     * kein gültiges XML Dokument sein. Es kann auch RTF, plain text oder sont
     * etwas sein. Der Parser ersetzt die mit <code>db_field</code>
     * gekennzeichneten Felder. Falls das Flag <code>doXmlEncoding</code>
     * gesetzt ist, wird dafür gesorgt das Sonderzeichen wie öäü... in das
     * entsprechende XML-Encoding überführt werden.
     * 
     * Beispiel: Vor dem parsen:
     * 
     * <docXYZ><name>db_field(employee.fullname) </name>
     * <id>db_field(employee.pkey) </id> <werk>db_field(emploseesite.pkey)
     * </werk> </docXYZ>
     * 
     * 
     * Nach dem parsen:
     * 
     * <docXYZ><name>Andreas Schneider </name> <id>123 </id> <werk>050 </werk>
     * </docXYZ>
     * 
     * @param context
     *            Der Runtime context von der jACOB Applikation
     * @param dataRecord
     *            Der Datenrecord (samt seiner LinkedRecordds) welche in das
     *            docTemplate eingefüllt werden soll.
     * @param doXmlEncoding
     *            Falls dieser true ist werden die ersetzten Werte vor dem
     *            einsetzten in das docTemplete xml-dekodiert.
     * @param replacementCR
     *            Das Steuerzeichen für CrLf z.B. bei RTF \par
     * @param templateRecord
     *            Record des DocTempaltes.
     * @param docTempalate
     *            Das template welches mit den Werten von
     *            <code>dataRecord</code> gefüllt werden müssen.
     * 
     * @return das geparste Dokument
     * @throws Exception
     */
    public static String fillDBFields(Context context, IDataTableRecord dataRecord, IDataTableRecord docRecord, String docTemplate, boolean doXmlEncoding, String replacementCR)
            throws Exception
    {
        IDataAccessor accessor = context.getDataAccessor().newAccessor();
        Locale locale = context.getUser().getLocale();
        Map replacements = new HashMap();

        replacements.put("db_field(*Date)", date.format(new Date()));
        replacements.put("db_field(*Time)", time.format(new Date()));

        PatternMatcher matcher = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile("db_field\\(([a-zA-Z_]*)\\.([a-zA-Z_]*)(?:-([a-zA-Z_]*)\\.([a-zA-Z_]*))*(?:-([a-zA-Z_]*)\\.([a-zA-Z_]*))*\\)");

        PatternMatcherInput input = new PatternMatcherInput(docTemplate);
        MatchResult result;

        accessor.propagateRecord(dataRecord, Filldirection.BACKWARD);

        // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
        // ermitteln
        //
        int index = 0;
        while (matcher.contains(input, pattern))
        {
            result = matcher.getMatch();
            // letztes Element in der
            // dc_field(table_x.field_y-table_x2.field_y2-...) finden.
            //
            for (index = result.groups(); result.group(index) == null || result.group(index).length() == 0; index--)
                ;
            String table = result.group(index - 1);
            String field = result.group(index);
            // Tabelle bestimmen
            IDataTableRecord record = docRecord;
            if (!table.equals("doc_template"))
                record = accessor.getTable(table).getSelectedRecord();
            if (record != null)
            {
                // Wert des Feldes bestimmen und 'XML-Tauglich' machen
                String value;
                try
                {
                    value = StringUtil.toSaveString(record.getStringValue(field,locale));
                }
                catch (NoSuchFieldException e)
                {
                    value = table +"."+field +" not defined!";
                }
                if (doXmlEncoding)
                {    
                    value = Converter.encode(value);
                }
                else
                {
                    if (replacementCR != null)
                    {
                        value = StringUtil.replace(value, "\n", replacementCR);
                    }
                }
                replacements.put(result.group(0), value);
            }
            else
                replacements.put(result.group(0), "");
        }
 

        // Sclüsselwort db_field(*USERRECORD.FELDNAME) suchen ersetzen
        pattern = compiler.compile("db_field\\(\\*USERRECORD.([a-zA-Z_]+)\\)");

        input = new PatternMatcherInput(docTemplate);

        // Userfelder/Properties aus dem XML-Template auslesen und Werte dazu
        // ermitteln
        //
        while (matcher.contains(input, pattern))
        {
            result = matcher.getMatch();
            String field = result.group(1);
            String value = StringUtil.toSaveString((String)context.getUser().getProperty(field));
            if (doXmlEncoding)
                value = Converter.encode(value);
            else if (replacementCR != null)
                value = StringUtil.replace(value, "\n", replacementCR);
            replacements.put(result.group(0), value);
        }

        // Werte in dem XML-Template eintragen
        //
        Iterator iter = replacements.keySet().iterator();
        while (iter.hasNext())
        {
            String key = (String) iter.next();
            String value = (String) replacements.get(key);
            docTemplate = StringUtils.replace(docTemplate, key, value);
        }
        return docTemplate;
    }

    /**
     * parst einen String nach den Schlüsselwörter db_field und ersetzt den Inhalt
     * @param dataRecord
     * @param docTemplate
     * @param doXmlEncoding
     * @param replacementCR
     * @return parsed docTemplate
     * @throws Exception
     */
    public static String parseDBFields(IDataTableRecord dataRecord, String docTemplate, boolean doXmlEncoding) throws Exception
    {
        IDataAccessor accessor = dataRecord.getAccessor().newAccessor();
        Locale locale = Context.getCurrent().getUser().getLocale();
        Map replacements = new HashMap();

        replacements.put("db_field(*Date)", date.format(new Date()));
        replacements.put("db_field(*Time)", time.format(new Date()));

        PatternMatcher matcher = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile("db_field\\(([a-zA-Z_]*)\\.([a-zA-Z_]*)(?:-([a-zA-Z_]*)\\.([a-zA-Z_]*))*(?:-([a-zA-Z_]*)\\.([a-zA-Z_]*))*\\)");

        PatternMatcherInput input = new PatternMatcherInput(docTemplate);
        MatchResult result;

        accessor.propagateRecord(dataRecord, Filldirection.BACKWARD);

        // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
        // ermitteln
        //
        int index = 0;
        while (matcher.contains(input, pattern))
        {
            result = matcher.getMatch();
            // letztes Element in der
            // dc_field(table_x.field_y-table_x2.field_y2-...) finden.
            //
            for (index = result.groups(); result.group(index) == null || result.group(index).length() == 0; index--)
                ;
            String table = result.group(index - 1);
            String field = result.group(index);

            IDataTableRecord record = accessor.getTable(table).getSelectedRecord();
            if (record != null)
            {
                // Wert des Feldes bestimmen und 'XML-Tauglich' machen
                String value = StringUtil.toSaveString(record.getStringValue(field,locale));
                if (doXmlEncoding)
                    value = Converter.encode(value);
                replacements.put(result.group(0), value);
            }
            else
                replacements.put(result.group(0), "");
        }

        // Werte in dem XML-Template eintragen
        //
        Iterator iter = replacements.keySet().iterator();
        while (iter.hasNext())
        {
            String key = (String) iter.next();
            String value = (String) replacements.get(key);
            docTemplate = StringUtils.replace(docTemplate, key, value);
        }
        return docTemplate;
    }

    /**
     * 
     * @param context
     * @param document
     * @param address
     * @param xslStylesheet
     * @throws Exception
     */
    public static void fillAskFields(IClientContext context, String document, IFormDialogCallback callback) throws Exception
    {
        Map replacements = new HashMap();

        PatternMatcher matcher = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile("(db_field\\(\\*ASK:)([äöüÄÖÜ&ßa-zA-Z_ ?!.:]*)\\)");

        // Askfelder aus dem XML-Template auslesen und Werte dazu ermitteln
        //
        PatternMatcherInput input = new PatternMatcherInput(document);
        MatchResult result;
        while (matcher.contains(input, pattern))
        {
           
            result = matcher.getMatch();
            
            replacements.put(result.group(0), result.group(2));
        }
        // Attachment?
        boolean askAttachment = (document.indexOf("db_field(*ATTACHMENT)") > -1);

        // Falls Felder bei dem Benutzer nachgefragt werden müssen, dann wird
        // ein Dialog angezeigt
        // bei dem der Benutzer diese Werte eingeben muss/kann
        //
        if (replacements.size() > 0 || askAttachment)
        {
            String rowsString = "10dlu,p,10dlu,";
            for (int i = 0; i < replacements.size(); i++)
            {
                rowsString += "20,6dlu,";
            }

            if (askAttachment)
            {
                for (int i = 0; i < 15; i++)
                    // 15 AttachmentUpload Dialoge einfügen
                    rowsString += "20dlu,6dlu,";
            }
            rowsString += "20dlu";
            
            FormLayout layout = new FormLayout("10dlu,p,6dlu,350dlu,10dlu", // columns
                    rowsString); // rows

            IFormDialog dialog = context.createFormDialog("Fragenkatalog", layout, callback);
            CellConstraints cc = new CellConstraints();
            if (replacements.size() == 0 && askAttachment)
            {
                dialog.addHeader("Anhang hinzufügen", cc.xywh(1, 1, 3, 1)); 
            }
            else
            {    
                dialog.addHeader("Fragen bitte beantworten", cc.xywh(1, 1, 3, 1));
            }
            Iterator iter = replacements.keySet().iterator();
            int i = 3;
            while (iter.hasNext())
            {
                String askKey = (String) iter.next();
                String ask = (String) replacements.get(askKey);

                dialog.addLabel(ask, cc.xy(1, i));
                dialog.addTextField(askKey, "", cc.xy(3, i));
                i = i + 2;
            }

            if (askAttachment)
            {
                for (int j = 0; j < 15; j++) // 20 AttachmentUpload Dialoge
                                                // einfügen
                {
                    dialog.addLabel("Anhang " + Integer.toString(j + 1) + ":", cc.xy(1, i + 2 * j));
                    dialog.addFileUpload("db_field(*ATTACHMENT)" + Integer.toString(j + 1), cc.xy(3, i + 2 * j));
                }
            }
            dialog.addSubmitButton("ok", "Speichern");
            dialog.setCancelButton("Abbruch");
            // dialog.setDebug(true);
            dialog.show(650, 650);
        }
        else
        {
            callback.onSubmit(context, "ok", new HashMap());
        }
    }

    /**
     * 
     * @param context
     * @param document
     * @param address
     * @param xslStylesheet
     * @throws Exception
     */
    public static void fillNotificationAskFields(IClientContext context, String document, NotificationAskCallback callback) throws Exception
    {

        Map replacements = new HashMap();

        PatternMatcher matcher = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile("(db_field\\(\\*ASK:)([äöüÄÖÜ&ßa-zA-Z_ ?!.:]*)\\)");

        // Askfelder aus dem XML-Template auslesen und Werte dazu ermitteln
        //
        PatternMatcherInput input = new PatternMatcherInput(document);
        MatchResult result;

        while (matcher.contains(input, pattern))
        {
            result = matcher.getMatch();
            //
            replacements.put(result.group(0), result.group(2));
        }

        boolean askAttachment = (document.indexOf("db_field(*ATTACHMENT)") > -1);

        // Falls Felder bei dem Benutzer nachgefragt werden müssen, dann wird
        // ein Dialog angezeigt
        // bei dem der BNenutzer diese Werte eingeben muss/kann
        //
        if (replacements.size() > 0 || askAttachment || callback.cofirmation)
        {
            String rowsString = "10dlu,p,10dlu,";
            for (int i = 0; i < replacements.size(); i++)
            {
                rowsString += "20dlu,6dlu,";
            }
            if (callback.cofirmation)
                rowsString += "20dlu,6dlu,";
            
            if (askAttachment)
            {
                for (int i = 0; i < 15; i++)
                    // 15 AttachmentUpload Dialoge einfügen
                    rowsString += "20dlu,6dlu,";
            }
            rowsString += "20dlu";

            FormLayout layout = new FormLayout("10dlu,p,6dlu,350dlu,10dlu", // columns
                    rowsString); // rows

            IFormDialog dialog = context.createFormDialog("Fragenkatalog", layout, callback);
            CellConstraints cc = new CellConstraints();
            if (replacements.size() == 0 && askAttachment)
            {
                dialog.addHeader("Anhang hinzufügen", cc.xywh(1, 1, 3, 1)); 
            }
            else
            {    
                dialog.addHeader("Fragen bitte beantworten", cc.xywh(1, 1, 3, 1));
            }

            Iterator iter = replacements.keySet().iterator();
            int i = 5;
            while (iter.hasNext())
            {
                String askKey = (String) iter.next();
                String ask = (String) replacements.get(askKey);
                
                dialog.addLabel(ask, cc.xy(1, i));
                dialog.addTextField(askKey, "", cc.xy(3, i));
                i = i + 2;
            }

            // Wenn eine Adressenvalidierung stattfindensoll, dann muss der
            // CallBack vom Typ NotificationAskCallback
            // sein, dort wird die Info confirmation hinterlegt
            
            if (callback.cofirmation)
            {         
                dialog.addLabel("Ist die Adresse richtig?", cc.xy(1, i));
                dialog.addTextField("address", callback.address, cc.xy(3, i));
                i = i + 2;
            }
            
            if (askAttachment)
            {
                for (int j = 0; j < 15; j++) // 20 AttachmentUpload Dialoge
                                                // einfügen
                {
                    dialog.addLabel("Anhang " + Integer.toString(j + 1) + ":", cc.xy(1, i + 2 * j));
                    dialog.addFileUpload("db_field(*ATTACHMENT)" + Integer.toString(j + 1), cc.xy(3, i + 2 * j));
                }
            }

            dialog.addSubmitButton("ok", "Speichern");
            dialog.setCancelButton("Abbruch");
            // dialog.setDebug(true);
            dialog.show(650, 650);
        }
        else
        {
            callback.onSubmit(context, "ok", new HashMap());
        }
    }

    /**
     * 
     * @param context
     * @param document
     * @param address
     * @param xslStylesheet
     * @throws Exception
     */
    public static void createInstance(Context context, String document, String address, String xslStylesheet) throws Exception
    {
        // Eintrag in der Tablle yan_task machen
        //
        IDataTable table = context.getDataTable("yan_task");
        IDataTransaction trans = table.startNewTransaction();
        try
        {
            // Hack vom Quintus Caretaker in "original_to url=" musste
            // die Zieladresse von Hand in das XML-Dokument geschrieben werden
            // MIKE: bitte überprüfen ob das anders geht!
            document = StringUtils.replace(document, "original_to url=\"\"", "original_to url=\"" + address + "\"");
            IDataTableRecord yan = table.newRecord(trans);
            yan.setValue(trans, "xmltext", document);
            yan.setValue(trans, "address", address);
            yan.setValue(trans, "datecreated", "now");
            yan.setValue(trans, "xsl_stylesheet", xslStylesheet);
            trans.commit();
        }
        finally
        {
            trans.close();
        }
    }

    public static void createActionNotification(Context context, IDataTableRecord dataRecord, IDataTableRecord docRecord, String docTemplate, String address, String xslStylesheet,
            boolean doXmlEncoding, String replacementCR, boolean confirmation) throws Exception
    {
        docTemplate = fillDBFields(context, dataRecord, docRecord, docTemplate, doXmlEncoding, replacementCR);
        // Wenn ich im ClientContext bin, dann kann ich auch *ASK parsen
        if (context instanceof IClientContext)
        {
            IClientContext clientContext = (IClientContext) context;
            // Felder welche mit ask() gekennzeichnet sind bei dem Anwender
            // nachfragen
            //
            Yan.fillNotificationAskFields(clientContext, docTemplate, new NotificationAskCallback(docTemplate, address, xslStylesheet, confirmation));

            return;

        }
        createInstance(context, docTemplate, address, xslStylesheet);
    }

    /**
     * Legt ein Attachment für den zugehörigen Datensatz an und gibt den
     * entsprechenden XML Eintrag zurück
     * 
     * @param context
     * @param attachment
     * @return
     * @throws Exception
     */
    static public String addAttachment(IClientContext context, DataDocumentValue attachment) throws Exception
    {
        // Kein Attachment also raus ..
        if (attachment == null)
            return "";

        IDataTable callTable = context.getDataTable("call");
        // Call muss da sein!
        String callKey = null;
        if (callTable.recordCount() == 1)
        {
            callKey = callTable.getRecord(0).getStringValue("pkey");
        }
        else
        {
            return "";
        }
        IDataTable taskTable = context.getDataTable("task");
        String taskKey = null;
        if (taskTable.recordCount() == 1)
        {
            taskKey = taskTable.getRecord(0).getStringValue("pkey");
        }
        IDataTable attachmentTable = context.getDataTable("attachment");
        IDataTransaction trans = attachmentTable.startNewTransaction();
        String attachmentKey = null;

        try
        {
            String filename = getFilename(attachment.getName());

            IDataTableRecord attachmentRec = attachmentTable.newRecord(trans);
            attachmentRec.setValue(trans, "datecreated", "now");
            attachmentRec.setValue(trans, "callattachment", callKey);
            attachmentRec.setValue(trans, "filename", filename);
            attachmentRec.setValue(trans, "docname", filename);
            attachmentRec.setValue(trans, "docdescr", "Anhang einer Nachricht");
            attachmentRec.setValue(trans, "task_key", taskKey);
            attachmentRec.setValue(trans, "document", attachment.getContent());
            trans.commit();
            attachmentKey = attachmentRec.getSaveStringValue("pkey");
        }
        finally
        {
            trans.close();
        }

        return "<attachment pkey=\"" + attachmentKey + "\" />";
    }

    /**
     * Hack da ich nicht weiss welchen File Separator ich bekomme
     * 
     * @param name
     * @return
     */
    private static String getFilename(String name)
    {
        for (int i = name.length() - 1; i >= 0; i--)
        {
            if ("\\".equals(name.substring(i, i + 1)) || ("/".equals(name.substring(i, i + 1))))
            {
                return name.substring(i + 1);
            }
        }
        return name;
    }
    
}

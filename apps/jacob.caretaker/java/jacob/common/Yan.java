/*
 * Created on Jul 26, 2004
 *
 */
package jacob.common;

import jacob.common.xml.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

/**
 * Interface für das Versenden von Nachrichten aus dem Caretaker heraus.
 * 
 */
public class Yan
{
    transient private static final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");

    transient private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

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
        // plausibilty check
        if (docTemplate == null)
          throw new NullPointerException("docTemplate is null");
        
        IDataAccessor accessor = context.getDataAccessor().newAccessor();

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
                String value = StringUtil.toSaveString(record.getStringValue(field));
                if (doXmlEncoding)
                    value = Converter.encode(value);
                replacements.put(result.group(0), value);
            }
            else
                replacements.put(result.group(0), "");
        }
        // Für Dokumente, die Felder mir CR/LF im im Datensatzfeld muss dieses
        // CR/LF dur ein entsprechendes Steuerzeichen ersetzt werden

        pattern = compiler.compile("db_field_with_CR\\(([a-zA-Z_]*)\\.([a-zA-Z_]*)(?:-([a-zA-Z_]*)\\.([a-zA-Z_]*))*(?:-([a-zA-Z_]*)\\.([a-zA-Z_]*))*\\)");

        input = new PatternMatcherInput(docTemplate);

        accessor.propagateRecord(dataRecord, Filldirection.BACKWARD);

        // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
        // ermitteln
        //
        index = 0;
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
                String value = StringUtil.toSaveString(record.getStringValue(field));
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
            //
            replacements.put(result.group(0), result.group(2));
        }

        // Falls Felder bei dem Benutzer nachgefragt werden müssen, dann wird
        // ein Dialog angezeigt
        // bei dem der BNenutzer diese Werte eingeben muss/kann
        //
        if (replacements.size() > 0)
        {
            String rowsString = "10dlu,p,10dlu,";
            for (int i = 0; i <= replacements.size(); i++)
                rowsString += "p,6dlu,";
            rowsString += "30dlu";

            FormLayout layout = new FormLayout("10dlu,p,6dlu,350dlu,10dlu", // columns
                    rowsString); // rows

            IFormDialog dialog = context.createFormDialog("Fragenkatalog", layout, callback);
            CellConstraints cc = new CellConstraints();
            dialog.addHeader("Fragen bitte beantworten", cc.xywh(1, 1, 3, 1));

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
            dialog.addSubmitButton("ok", "Speichern");
            dialog.setCancelButton("Abbruch");
            // dialog.setDebug(true);
            dialog.show(450, 350);
        }
        else
        {
            callback.onSubmit(context, "ok", new HashMap());
        }
    }

  /**
   * @param context
   * @param document
   * @param address
   * @param xslStylesheet
   * @throws Exception
   */
  public static void createInstance(Context context, String document, String address, String xslStylesheet) throws Exception
  {
    IDataTransaction trans = context.getDataAccessor().newTransaction();
    try
    {
      createInstance(context, trans, document, address, xslStylesheet);
      trans.commit();
    }
    finally
    {
      trans.close();
    }
  }

  /**
   * @param context
   * @param trans
   * @param document
   * @param address
   * @param xslStylesheet
   * @throws Exception
   */
  public static void createInstance(Context context, IDataTransaction trans, String document, String address, String xslStylesheet) throws Exception
  {
    // Eintrag in der Tabelle yan_task machen
    //
    IDataTable table = context.getDataTable("yan_task");

    // Hack vom Quintus Caretaker in "original_to url=" musste
    // die Zieladresse von Hand in das XML-Dokument geschrieben werden
    // MIKE: bitte überprüfen ob das anders geht!
    document = StringUtils.replace(document, "original_to url=\"\"", "original_to url=\"" + address + "\"");
    IDataTableRecord yan = table.newRecord(trans);
    yan.setValue(trans, "xmltext", document);
    yan.setValue(trans, "address", address);
    yan.setValue(trans, "datecreated", "now");
    yan.setValue(trans, "xsl_stylesheet", xslStylesheet);
  }
}

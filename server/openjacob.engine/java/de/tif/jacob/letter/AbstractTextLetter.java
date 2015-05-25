/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.letter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.dialogs.form.CellConstraints;
import de.tif.jacob.screen.dialogs.form.FormLayout;
import de.tif.jacob.screen.dialogs.form.IFormDialog;
import de.tif.jacob.screen.dialogs.form.IFormDialogCallback;

public abstract class AbstractTextLetter implements ILetter
{
    // MUST be public for the jACOB Designer
    // AbstractTextLetter is not an ofical interface ILetter is the one.
    //
    public final static String PATTERN_SIMPLE_DB_FIELD = "db_field\\(([a-zA-Z_][a-zA-Z_0-9]*)\\)";
    public final static String PATTERN_DB_FIELD        = "db_field\\(([a-zA-Z_][a-zA-Z_0-9]*)\\.([a-zA-Z_][a-zA-Z_0-9]*)\\)";
    public final static String PATTERN_FOR_EACH        = "{?\\s*startForEach\\(([a-zA-Z_][a-zA-Z_0-9]*)\\)\\s*}?([.\\\\\\w\\n)(\\n\\[\\]?&%$§/\"!<>|#'+*äöüÄÖÜ' =\\._\\}{\\0-9;,.:]*?){?\\s*endForEach\\(([a-zA-Z_][a-zA-Z_0-9]*)\\)\\s*}?";
    public final static String PATTERN_ASK             = "(db_field\\(\\*ASK:)([äöüÄÖÜ&ßa-zA-Z_ ?!.]*)\\)";
    
    public abstract String encode(String value);
    
    public byte[] transform(Context context, IDataTableRecord dataRecord, byte[] data) throws Exception
    {
      IDataAccessor accessor = context.getDataAccessor().newAccessor();
      accessor.propagateRecord(dataRecord, Filldirection.BOTH);
      
      return transform(context, accessor, data);
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
     *            Der Datenrecord (samt seiner LinkedRecords) welche in das
     *            docTemplate eingefüllt werden soll.
     * @param tempalate
     *            Das template welches mit den Werten von
     *            <code>dataRecord</code> gefüllt werden müssen.
     * 
     * @return das geparste Dokument
     * @throws Exception
     */
    public byte[] transform(Context context, IDataAccessor accessor, byte[] data) throws Exception
    {
      	String template = new String(data);
      	

        Map replacements = new HashMap();

        LetterFactory.insertCommonSubstitution(context, replacements);
        

        PatternMatcher matcher = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(PATTERN_DB_FIELD);

        PatternMatcherInput input = new PatternMatcherInput(template);
        MatchResult result;


        // Ersetzten von db_field(alias.fieldname)
        //
        
        // Als standard string Ausgabeformat die Einstellung der Applicationlocale verwenden
        Locale locale = context.getApplicationLocale();
        
        int index = 0;
        while (matcher.contains(input, pattern))
        {
            result = matcher.getMatch();
            // letztes Element in der
            // db_field(table_x.field_y-table_x2.field_y2-...) finden.
            // NUR AUS CAMPATIBILITÄT ZUM ALTEN CARETAKER!!!!
            for (index = result.groups(); result.group(index) == null || result.group(index).length() == 0; index--)
            {
              // do nothing
            }
            String table = result.group(index - 1);
            String field = result.group(index);
            //System.out.println("table:"+table);
            //System.out.println("fields:"+field);
            // Tabelle bestimmen
            IDataTableRecord record = accessor.getTable(table).getSelectedRecord();
            if (record != null)
            {
                String value = record.getSaveStringValue(field, locale);
                value = encode(value);// StringUtil.replace(value, "\n", getNewLineString());
                replacements.put(result.group(0), value);
            }
            else
                replacements.put(result.group(0), "");
        }

        // Iterationen im letter template expandieren
        //
        pattern = compiler.compile(PATTERN_FOR_EACH);
        input   = new PatternMatcherInput(template);

        // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
        // ermitteln
        //
        while (matcher.contains(input, pattern))
        {
            result = matcher.getMatch();

            String table = result.group(1);
            String fields = result.group(2);
            replacements.put("startForEach("+table+")","");
            replacements.put("endForEach("+table+")","");

            String fieldResult="";
            
            PatternMatcher matcher2 = new Perl5Matcher();
            PatternCompiler compiler2 = new Perl5Compiler();
            Pattern pattern2 = compiler2.compile(PATTERN_SIMPLE_DB_FIELD);

            PatternMatcherInput input2 = new PatternMatcherInput(fields);
            MatchResult result2;
            // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
            // ermitteln
            //
            List f = new ArrayList();
            while (matcher2.contains(input2, pattern2))
            {
              result2 = matcher2.getMatch();
              String field = result2.group(1);
              f.add(field);
            }
            
            int count = accessor.getTable(table).recordCount();
            for(int i=0;i<count;i++)
            {
              String iterPart=fields;
              IDataTableRecord record = accessor.getTable(table).getRecord(i);
              iterPart=StringUtils.replace(iterPart, "db_recordIndex", Integer.toString(i+1));
              Iterator iter=f.iterator();
              while (iter.hasNext())
              {
                String obj = (String) iter.next();
                String value = record.getSaveStringValue(obj, locale);
            //    System.out.println("replacing db_field("+obj+") -> "+value);
                iterPart=StringUtils.replace(iterPart, "db_field("+obj+")", value);
              }
              fieldResult = fieldResult+iterPart;
            }
            replacements.put(fields,fieldResult);
            //System.out.println("\n\n");
        }
       
        
        // Werte in dem Template eintragen
        //
        Iterator iter = replacements.keySet().iterator();
        while (iter.hasNext())
        {
            String key = (String) iter.next();
            String value = (String) replacements.get(key);
            template = StringUtils.replace(template, key, value);
        }
        return template.getBytes();
    }

    /**
     * 
     * @param context
     * @param document
     * @param address
     * @param xslStylesheet
     * @throws Exception
     */
    public void fillAskFields(IClientContext context, String document, IFormDialogCallback callback) throws Exception
    {
        Map replacements = new HashMap();

        PatternMatcher matcher = new Perl5Matcher();
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(PATTERN_ASK);

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
        // bei dem der Benutzer diese Werte eingeben muss/kann
        //
        if (replacements.size() > 0)
        {
            String rowsString = "10dlu,p,10dlu,";
            for (int i = 0; i <= replacements.size(); i++)
                rowsString += "p,6dlu,";
            rowsString += "30dlu";

            FormLayout layout = new FormLayout("10dlu,p,6dlu,350dlu,10dlu", // columns
                    rowsString); // rows

            IFormDialog dialog = context.createFormDialog(I18N.getLocalized("LABEL_LETTERFACTORY_ASKTITLE",context), layout, callback);
            CellConstraints cc = new CellConstraints();
            dialog.addHeader(I18N.getLocalized("LABEL_LETTERFACTORY_ASKINFORMATION",context), cc.xywh(1, 1, 3, 1));

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
            dialog.addSubmitButton("ok", I18N.getLocalized("BUTTON_COMMON_SAVE",context), true);
            dialog.setCancelButton(I18N.getLocalized("BUTTON_COMMON_CANCEL",context));
            // dialog.setDebug(true);
            dialog.show(450, 350);
        }
        else
        {
            callback.onSubmit(context, "ok", new HashMap());
        }
    }


}

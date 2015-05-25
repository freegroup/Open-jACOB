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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import de.tif.jacob.core.Context;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;

/**
 *
 */
public class PDFGenerator implements ILetter
{
//  transient private static final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
//  transient private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
  public byte[] transform(Context context, IDataTableRecord dataRecord, byte[] template) throws Exception
  {
    IDataAccessor accessor = context.getDataAccessor().newAccessor();
    accessor.propagateRecord(dataRecord, Filldirection.BACKWARD);

    return transform(context, accessor, template);
  }
  
  public byte[] transform(Context context, IDataAccessor accessor, byte[] template) throws Exception
  {

    Map replacements = new HashMap();

    LetterFactory.insertCommonSubstitution(context, replacements);
    
    PatternMatcher matcher = new Perl5Matcher();
    PatternCompiler compiler = new Perl5Compiler();
    Pattern pattern = compiler.compile("db_field\\(([a-zA-Z_]*)[.]([a-zA-Z_]*)\\)");


    // Als standard string Ausgabeformat die Einstellung der Applicationlocale verwenden
    Locale locale = context.getApplicationLocale();
    
    // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
    // ermitteln
    //
    ByteArrayOutputStream out = new ByteArrayOutputStream(30*1024);
  	PdfReader reader = new PdfReader(template);
    PdfStamper stamp = new PdfStamper(reader, out);
    AcroFields form = stamp.getAcroFields();
                
    Iterator iter = form.getFields().keySet().iterator();
    while (iter.hasNext())
    {
      String textfield = (String)iter.next();
//      System.out.println("Textfield:"+textfield);
      PatternMatcherInput input = new PatternMatcherInput(textfield);
      MatchResult result;
	    while (matcher.contains(input, pattern))
	    {
	        result = matcher.getMatch();
	        String table = result.group(1);
	        String field = result.group(2);
	        
//	        System.out.println("Table:"+table);
//	        System.out.println("Field:"+field);
//	        System.out.println("\n\n");
	        // Tabelle bestimmen
	        IDataTableRecord record = accessor.getTable(table).getSelectedRecord();
	        if (record != null)
	        {
	            String value = record.getSaveStringValue(field, locale);
	            value = encode(value);
	            replacements.put(textfield, value);
	        }
	        else
	            replacements.put(textfield, "");
	    }
    }
    
    // Werte in dem Template eintragen
    //
    iter = replacements.keySet().iterator();
    while (iter.hasNext())
    {
        String key = (String) iter.next();
        String value = (String) replacements.get(key);
        form.setField(key,value);
    }
    stamp.setFormFlattening(true);
    stamp.close();
    return out.toByteArray();  
  }
  
  public String encode(String value)
  {
    return value;
  }
  
  public String getMimeType()
  {
    return LetterFactory.MIMETYPE_PDF;
  }
}

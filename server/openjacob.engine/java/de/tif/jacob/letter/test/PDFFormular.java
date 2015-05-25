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

package de.tif.jacob.letter.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

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

/**
 *
 */
public class PDFFormular
{
  public static void main(String[] args)
  {
//  Reads in the pdf Template
        
    try
    {
      PatternMatcher matcher = new Perl5Matcher();
      PatternCompiler compiler = new Perl5Compiler();
      Pattern pattern = compiler.compile("db_field\\(([a-zA-Z_]*)[.]([a-zA-Z_]*)\\)");

      // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
      // ermitteln
      //

    	PdfReader reader = new PdfReader(new FileInputStream( "forms.pdf"));
      PdfStamper stamp = new PdfStamper(reader, new FileOutputStream("forms_out.pdf"));
      AcroFields form = stamp.getAcroFields();
                  
      Iterator iter = form.getFields().keySet().iterator();
      while (iter.hasNext())
      {
        String obj = (String)iter.next();
        System.out.println(obj);
        PatternMatcherInput input = new PatternMatcherInput(obj);
        MatchResult result;
  	    int index = 0;
  	    while (matcher.contains(input, pattern))
  	    {
  	        result = matcher.getMatch();
  	        System.out.println(result.groups());
  	        String table = result.group(index+1);
  	        String field = result.group(index+2);
  	        
  	        System.out.println("Table:"+table);
  	        System.out.println("Field:"+field);
  	        System.out.println("\n\n");
  	    }
      }
      form.setField("Text1","aValue");
      stamp.setFormFlattening(true);
      stamp.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }    
  }
}

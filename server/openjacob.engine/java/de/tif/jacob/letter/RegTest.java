/*
 * Created on 06.11.2009
 *
 */
package de.tif.jacob.letter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class RegTest
{
  public static void main(String[] args)
  {
    try
    {
      String template = IOUtils.toString(RegTest.class.getResourceAsStream("test.txt"));

      PatternMatcher matcher = new Perl5Matcher();
      PatternCompiler compiler = new Perl5Compiler();
      Pattern pattern = compiler.compile(AbstractTextLetter.PATTERN_DB_FIELD);

      PatternMatcherInput input = new PatternMatcherInput(template);
      MatchResult result;


      // Ersetzten von db_field(alias.fieldname)
      //
      
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
          System.out.println(result.group(0));
          System.out.println("table:"+table);
          System.out.println("fields:"+field);
          System.out.println("-----------------------------------------------");
      }

      System.out.println("==================================================");
      // Iterationen im letter template expandieren
      //
      pattern = compiler.compile(AbstractTextLetter.PATTERN_FOR_EACH);
      input   = new PatternMatcherInput(template);

      System.out.println("Parsing forEach");
      // Datenbankfelder aus dem XML-Template auslesen und Werte dazu
      // ermitteln
      //
      while (matcher.contains(input, pattern))
      {
          result = matcher.getMatch();

          String table = result.group(1);
          String fields = result.group(2);
          System.out.println("table:"+table);

          String fieldResult="";
          
          PatternMatcher matcher2 = new Perl5Matcher();
          PatternCompiler compiler2 = new Perl5Compiler();
          Pattern pattern2 = compiler2.compile(AbstractTextLetter.PATTERN_SIMPLE_DB_FIELD);

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
            System.out.println("\t"+field);
            f.add(field);
          }
          
      }
    }
    catch(Exception exc)
    {
      exc.printStackTrace();
    }
  }
}

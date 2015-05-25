/*
 * Created on 27.09.2010
 *
 */
package de.tif.jacob.screen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import de.tif.jacob.util.StringUtil;


public class RegExprLinkParser extends ILinkParser
{
  public static final ILinkParser WIKI_PARSER = new RegExprLinkParser("\\[\\[([ a-zA-Z0-9:\\-+‰ˆ¸ƒ÷‹ﬂ./]*)(?:\\|(.*?))?\\]\\]");
  public static final ILinkParser WIKI_HTTP_FTP_PARSER = new RegExprLinkParser("\\[\\[([ a-zA-Z0-9:\\-+‰ˆ¸ƒ÷‹ﬂ./]*)(?:\\|(.*?))?\\]\\]|((?:(?:ftp|https?){1}://)([-a-zA-Z0-9@:%_\\+.~#?&//=]+))");

  private String pattern;

  public RegExprLinkParser(String pattern )
  {
     this.pattern = pattern;
  }
  
  public void setPattern(String pattern) throws Exception
  {
    // compile and check the valid syntax of the regexpre pattern
    PatternCompiler compiler2 = new Perl5Compiler();
    compiler2.compile(pattern);
    
    this.pattern = pattern;
  }
  
  public String parse(IClientContext context, IGuiElement target, String value) throws Exception
  {
    if(this.pattern==null)
      return value;

    
    ILinkRenderStrategy renderer = null;
    // for debugging
    if(context !=null)
      renderer = this.getLinkRenderStrategy(context);
    
     PatternMatcher matcher2 = new Perl5Matcher();
     PatternCompiler compiler2 = new Perl5Compiler();
     Pattern pattern2 = compiler2.compile(this.pattern);
     PatternMatcherInput input2 = new PatternMatcherInput(value);
     MatchResult result2;

     Map replacement = new HashMap();
     while (matcher2.contains(input2, pattern2))
     {
       result2 = matcher2.getMatch();
       String all = result2.group(0);
       // die erste kombination von link und display finden bei den mind einer !=null ist.
       //
       String link=null;
       String display=null;
       for(int i=1;i<result2.groups(); i+=2)
       {
         link = result2.group(i);
         display = result2.group(i+1);
         if(!StringUtil.emptyOrNull(link))
           break;
       }
       if(StringUtil.emptyOrNull(display))
         display = link;
       // for debug...
       if(context==null)
       {
         for(int i=1;i<result2.groups(); i++)
         {
           System.out.println("group_"+i+": "+result2.group(i));
         }
      
         System.out.println("Link: "+link);
         System.out.println("Display: "+display);
         System.out.println("");
       }
       
       if(renderer!=null)
         replacement.put(all, renderer.buildLink(context, target, link, display));
     }
     Iterator iter = replacement.entrySet().iterator();
     while (iter.hasNext())
     {
       Map.Entry obj = (Map.Entry) iter.next();
       value = StringUtil.replace(value,(String)obj.getKey(),  (String)obj.getValue());
     }
     return value;
  }
  
  
  public static void main(String[] args)
  {
    try
    {
      String test = "Dies [[https://www.google.de|Google]] ist l [[link123|Link]] nach ein test [[mit:123]] ein paar [falsch] links [[linkToAny|ANY2Link]] und http://www.google.de  ein paar\nrichtigen [[mit: 123]]  [[linkToAny|ANY Link]] . Mal sehen\n ob dies geht";
      RegExprLinkParser parser = new RegExprLinkParser("\\[\\[(ftp|https?://[-a-zA-Z0-9@:%_\\+.~#?&//=]+)(?:\\|(.*?))?\\]\\]");
      System.out.println(parser.parse(null, null,test));
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}

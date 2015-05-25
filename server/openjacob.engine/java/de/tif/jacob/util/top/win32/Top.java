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

package de.tif.jacob.util.top.win32;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import de.tif.jacob.util.top.ITop;

/**
 *
 */
public class Top implements ITop
{
  
  public List getProcessInformation() throws Exception
  {
    List processInfos = new ArrayList();
      PatternMatcher  matcher = new Perl5Matcher();
      PatternCompiler compiler= new Perl5Compiler();
      Pattern         pattern = compiler.compile("jACOB Windows Process Information\\s*Total:\\s*(\\d*.\\d*)%\\s*Kernel:\\s*(\\d*.\\d*)%\\s*User:\\s*(\\d*.\\d*)%\\s*Total\\s*Kernel\\s*User\\s*PID\\s*Process\\s*([\\s\\W\\w]*)");
      
      Top top=new Top();

      PatternMatcherInput input=new PatternMatcherInput(getOutput());
      MatchResult result;
      
      if(matcher.contains(input, pattern)) 
      {
        result = matcher.getMatch();
//        String completeTotal  = result.group(1);
//        String completeKernel = result.group(2);
        int pid = -1;
        BigDecimal totalTime  = new BigDecimal(result.group(1));
        BigDecimal kernelTime = new BigDecimal(result.group(2));
        BigDecimal userTime   = new BigDecimal(result.group(3));
        String process    = "<ALL>";
        String user       = "System";

        processInfos.add(new ProcessInformation(process,totalTime,userTime,kernelTime,pid,user));
        
        String processes = result.group(4);
        Pattern  pattern2 = compiler.compile("(\\d*.\\d*)%\\s*(\\d*.\\d*)%\\s*(\\d*.\\d*)%\\s*(\\d*)\\s*([~\\\\ \\ta-zA-Z0-9:._-]*)");
        PatternMatcherInput input2=new PatternMatcherInput(processes);
        while(matcher.contains(input2, pattern2))
        {
          result =matcher.getMatch();
          totalTime  = new BigDecimal(result.group(1));
          kernelTime = new BigDecimal(result.group(2));
          userTime   = new BigDecimal(result.group(3));
          pid        = Integer.parseInt(result.group(4));
          process    = result.group(5);
          user       = "unknown";
          if(pid!=0)
            processInfos.add(new ProcessInformation(process,totalTime, userTime,kernelTime,pid,user));
        }
      }
    return processInfos;
  }
  
  private String getOutput() throws Exception
  {
//    if(true)return FileUtils.readFileToString(new File(getClass().getResource("testinput.txt").getPath()),"ISO-8859-1");
    
    URL topUrl =getClass().getResource("top.exe");
    Process pr =Runtime.getRuntime().exec(topUrl.getFile().substring(1));
    InputStream is = pr.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    String str;
    String output ="";
    while ((str = br.readLine()) != null) {
        output=output +"\n"+str;
    }
    is.close();
    pr.waitFor();
    return output;
  }
}

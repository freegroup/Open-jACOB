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

package de.tif.jacob.util.top.unix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
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
public abstract class Top implements ITop
{
  private static final BigDecimal DECIMAL0 = new BigDecimal("0.00");
  
  public List getProcessInformation() throws Exception
  {
    List processInfos = new ArrayList();
      PatternMatcher  matcher = new Perl5Matcher();
      PatternCompiler compiler= new Perl5Compiler();

//      Pattern         pattern = compiler.compile("CPU states:\\s*(\\d*.\\d*)%\\s*idle,\\s*(\\d*.\\d*)%\\s*user,\\s*(\\d*.\\d*)%\\s*kernel,\\s*(\\d*.\\d*)%\\s*iowait,\\s*\\d*.\\d*%\\s*swap\\s*.*\\s*.*\\s*([\\s\\W\\w]*)");
      Pattern         pattern = compiler.compile("COMMAND\\s*([\\s\\W\\w]*)");
      

      PatternMatcherInput input=new PatternMatcherInput(getOutput());
      MatchResult result;
      
      if(matcher.contains(input, pattern)) 
      {
        result = matcher.getMatch();
        
        int pid = -1;
        /*
        float  totalTime  = 100-Float.parseFloat(result.group(1));
        float  userTime   = Float.parseFloat(result.group(2));
        float  kernelTime = Float.parseFloat(result.group(3));
        float  ioWait     = Float.parseFloat(result.group(4));
        String process    = "<ALL>";
        String user       = "System";
        */
        BigDecimal totalTime  = DECIMAL0;
        BigDecimal userTime   = DECIMAL0;
        BigDecimal kernelTime = DECIMAL0;
        BigDecimal ioWait     = DECIMAL0;
        BigDecimal common_totalTime  = DECIMAL0;
        BigDecimal common_userTime   = DECIMAL0;
        BigDecimal common_kernelTime = DECIMAL0;
        BigDecimal common_ioWait     = DECIMAL0;
        String process    = "<ALL>";
        String user       = "System";

        ProcessInformation all=new ProcessInformation(process,DECIMAL0,ioWait, userTime,kernelTime,pid,user);
        processInfos.add(all);
        
        String processes = result.group(1).trim();
        Pattern  pattern2 = compiler.compile("(\\d*)\\s+([\\w-_]*)\\s*\\d*\\s*\\d*\\s*\\d*\\s*\\d*\\w\\s*\\d*\\w\\s*\\w*\\s*\\d*:\\d*\\s*(\\d*[.]\\d*)%\\s*([~\\\\ \\ta-zA-Z0-9:._-]*)\\s*");
        PatternMatcherInput input2=new PatternMatcherInput(processes);
        while(matcher.contains(input2, pattern2))
        {
          result =matcher.getMatch();
         
          kernelTime = DECIMAL0;
          userTime   = new BigDecimal(result.group(3));
          pid        = Integer.parseInt(result.group(1));
          process    = result.group(4);
          user       = result.group(2);
          
          
          if(pid!=0)
          {
            String procName=getProcessName(pid);
            if(procName!=null)
            {
              common_userTime   =common_userTime.add(userTime);
              common_kernelTime = common_kernelTime.add(kernelTime);
              ProcessInformation x = new ProcessInformation(procName, userTime.add(kernelTime), DECIMAL0, userTime, kernelTime, pid, user);
              processInfos.add(x);
            }
          }
        }
        all.setIOWait(DECIMAL0);
        all.setKernelTime(common_kernelTime);
        all.setUserTime(common_userTime);
        all.setTotalTime(common_kernelTime.add(common_userTime));
      }
    return processInfos;
  }

  /**
   * 
   * @param pid
   * @return
   * @throws Exception
   */
  private String getProcessName(int pid) throws Exception
  {
    //
    // IMPORTANT: Read first the article before you change any line of this code:
    // ==========
    //
    // http://forums.java.sun.com/thread.jsp?thread=533029&forum=31&message=2572282
    //
    Process pr = Runtime.getRuntime().exec("ps -p" + pid + " -f -oargs");

    InputStream p_i_s = pr.getInputStream();
    try
    {
      OutputStream p_o_s = pr.getOutputStream();
      try
      {
        InputStream p_e_s = pr.getErrorStream();
        try
        {

          int status = -1;
          try
          {
            status = pr.waitFor();
          }
          catch (InterruptedException err)
          {
          } // this shouldn't happen, but ignore it anyway
          if (0 != status)
          {
            return null;
          }
          BufferedReader br = new BufferedReader(new InputStreamReader(p_i_s));
          try
          {
            br.readLine();
            return br.readLine();
          }
          finally
          {
            br.close();
            br = null;
          }
        }
        finally
        {
          p_e_s.close();
          p_e_s = null;
        }
      }
      finally
      {
        p_o_s.close();
        p_o_s = null;
      }
    }
    finally
    {
      p_i_s.close();
      p_i_s = null;
      pr = null;
    }
  }
  
  public abstract Process getTopProcess() throws Exception;
  
  private String getOutput() throws Exception
  {
    //
    // IMPORTANT: Read first the article before you change any line of this code:
    // ==========
    //
    // http://forums.java.sun.com/thread.jsp?thread=533029&forum=31&message=2572282
    // 
    Process pr = getTopProcess();

    InputStream p_i_s = pr.getInputStream();
    try
    {
      OutputStream p_o_s = pr.getOutputStream();
      try
      {
        InputStream p_e_s = pr.getErrorStream();
        try
        {
          int status = -1;
          try
          {
            status = pr.waitFor();
          }
          catch (InterruptedException err)
          {
          } // this shouldn't happen, but ignore it anyway
          BufferedReader br = new BufferedReader(new InputStreamReader(p_i_s));
          if (0 != status)
          {
             br = new BufferedReader(new InputStreamReader(p_e_s));
          //  return null;
          }
          try
          {
            String str;
            StringBuffer output = new StringBuffer(1000);
            while ((str = br.readLine()) != null)
            {
              output.append("\n");
              output.append(str);
            }
//            System.out.println("OUT:"+output.toString().trim());
            return output.toString().trim();
          }
          finally
          {
            br.close();
            br = null;
          }
        }
        finally
        {
          p_e_s.close();
          p_e_s = null;
        }
      }
      finally
      {
        p_o_s.close();
        p_o_s = null;
      }
    }
    finally
    {
      p_i_s.close();
      p_i_s = null;
      pr = null;
    }
  }
}

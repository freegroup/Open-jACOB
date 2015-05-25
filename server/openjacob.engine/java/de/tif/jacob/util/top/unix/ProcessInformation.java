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

import java.math.BigDecimal;

import de.tif.jacob.util.top.IProcessInformation;

/**
 *
 */
public class ProcessInformation implements IProcessInformation
{
  private final String processInformation;
  private final int    pid;
  private final String processUserName;
  private BigDecimal ioWait;
  private BigDecimal userTime;
  private BigDecimal kernelTime;
  private BigDecimal totalTime;
  
  public ProcessInformation(String processInformation,BigDecimal totalTime, BigDecimal ioWait, BigDecimal userTime, BigDecimal kernelTime, int pid, String processUserName)
  {
    this.processInformation = processInformation;
    this.userTime           = userTime;
    this.kernelTime         = kernelTime;
    this.ioWait             = ioWait;
    this.pid                = pid;
    this.processUserName    = processUserName;
    this.totalTime          = totalTime;
  }
  
  public final BigDecimal getIOWait()
  {
    return ioWait;
  }
  
  public final BigDecimal getTotalTime()
  {
    return totalTime;
  }
 
  public final BigDecimal getKernelTime()
  {
    return kernelTime;
  }
  
  public final BigDecimal getUserTime()
  {
    return userTime;
  }

  public final String getProcessInformation()
  {
    return processInformation;
  }
  
  public final int getPid()
  {
    return pid;
  }
  public final String getProcessUserName()
  {
    return processUserName;
  }

  public String toString()
  {
    return "userTime:"+userTime+"  kernelTime:"+kernelTime+" ioWait:"+ioWait+" process:"+processInformation+" user:"+processUserName;
  }
  
  public final void setIOWait(BigDecimal ioWait)
  {
    this.ioWait = ioWait;
  }
  
  public final void setKernelTime(BigDecimal kernelTime)
  {
    this.kernelTime = kernelTime;
  }
  
  public final void setUserTime(BigDecimal userTime)
  {
    this.userTime = userTime;
  }
  public final void setTotalTime(BigDecimal totalTime)
  {
    this.totalTime = totalTime;
  }
}

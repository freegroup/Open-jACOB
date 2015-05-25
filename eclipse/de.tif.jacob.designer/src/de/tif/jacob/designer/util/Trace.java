/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
package de.tif.jacob.designer.util;

import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import de.tif.jacob.util.StringUtil;

public class Trace {

	private static boolean enabled   = true;
	private static long    timePoint  = System.currentTimeMillis();
  private static int     startCount = 0;
  private static String filler ="";
  private static Stack  timerStack = new Stack();
  
	public static void start(String label)
	{
		if(enabled)
    {
      System.out.println(filler+"Starting measuring for ["+label+"]");
      startCount++;
      filler = StringUtils.repeat("\t",startCount);
      timePoint=System.currentTimeMillis();
      timerStack.push(new Long(timePoint));
    }
	}

  public static void mark()
  {
    if(enabled)
      timePoint=System.currentTimeMillis();
  }

  public static void print(String label)
	{
		if(enabled)
    {
      long now = System.currentTimeMillis();
			System.out.println(filler+"duration:"+(now-timePoint)+":"+label);
      timePoint=now;
    }
	}

	public static void stop(String label)
	{
		if(enabled)
    {
      long now = System.currentTimeMillis();
      startCount--;
      filler = StringUtils.repeat("\t",startCount);
      timePoint = ((Long)timerStack.pop()).longValue();
      System.out.println(filler+"Stopping "+(now-timePoint)+" ["+label+"]");
    }
	}
}

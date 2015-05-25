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

package de.tif.jacob.util.top.linux;

import java.net.URL;


/**
 *
 */
public class Top extends de.tif.jacob.util.top.unix.Top
{
  
	public Process getTopProcess() throws Exception
	{
    URL topUrl =getClass().getResource("top.sh");
    Runtime.getRuntime().exec("chmod 755 "+topUrl.getFile());
    Process pr =Runtime.getRuntime().exec(topUrl.getFile());
    return pr;
    //return Runtime.getRuntime().exec(new String[]{"/usr/bin/top", "-n1","-b"},new String[]{"TERM=xterm"});
	}
  
}

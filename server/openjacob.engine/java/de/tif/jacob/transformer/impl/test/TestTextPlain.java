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

package de.tif.jacob.transformer.impl.test;

import java.io.IOException;

import de.tif.jacob.transformer.impl.TextPlain;

/**
 *
 */
public class TestTextPlain
{
  
	public static void main(String[] args)
	{
	  try
    {
	    String[] header=new String[]{"Header1","HEaaader2","H3","hhhhhdddddddd6"};
	    String[][] data = new String[3][];
	    data[0] = new String[]{"Header1","HEaaader2","ddddddddddH3","hhhhhdddddddd6"};
	    data[1] = new String[]{"Her1","HEasdfaaader2","H3","hhhddddd6"};
	    data[2] = new String[]{"Hddddeadfffer1","HEaer2","H3","hhhhhdddddddd6"};
	    
	    TextPlain plain=new TextPlain();
      plain.transform(System.out,header,data);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
	}
}

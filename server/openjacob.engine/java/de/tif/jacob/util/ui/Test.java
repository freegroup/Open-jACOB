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

package de.tif.jacob.util.ui;

import java.io.File;
import javax.imageio.ImageIO;

public class Test
{
  public static void main(String[] args)
  {
    try
    {
      File file = new File("progress.png");
//      GenericChart chart = new LineChart(600,300);
      ProgressBar chart = new ProgressBar(300,20);
      ImageIO.write(chart.createImage(0.82f), "png", file);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.exit(1);
  }
}

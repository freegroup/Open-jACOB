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

package de.tif.jacob.util.gif;

import javax.imageio.ImageIO;

/**
 * RWtypes.java - a class to display available ImageReaders and ImageWriters by
 * image format and MIME type
 */
public class RWType {
  public static void main(String[] args) {
    String[] readers, writers;

    System.out.println("For Reading:");
    readers = ImageIO.getReaderFormatNames();
    System.out.println("\tBy format:");
    for (int i = 0; i < readers.length; i++)
      System.out.println("\t\t" + readers[i]);

    readers = ImageIO.getReaderMIMETypes();
    System.out.println("\tBy MIME Types:");
    for (int i = 0; i < readers.length; i++)
      System.out.println("\t\t" + readers[i]);

    System.out.println("For Writing:");
    writers = ImageIO.getWriterFormatNames();
    System.out.println("\tBy format:");
    for (int i = 0; i < writers.length; i++)
      System.out.println("\t\t" + writers[i]);

    writers = ImageIO.getWriterMIMETypes();
    System.out.println("\tBy MIME Types:");
    for (int i = 0; i < writers.length; i++)
      System.out.println("\t\t" + writers[i]);
  }
}

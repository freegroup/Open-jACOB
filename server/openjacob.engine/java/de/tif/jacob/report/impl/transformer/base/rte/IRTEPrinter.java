/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.jacob.report.impl.transformer.base.rte;


/**
 * Report text expression printer.
 * 
 * @since 2.9
 * @author Andreas Sonntag
 */
public interface IRTEPrinter
{
  public void print(String text);
  
  public void printLinefeed();

  public void printPagebreak();
  
  public void printPageNumber();
  
  /**
   * Mark that the following text should be printed out left aligned.
   */
  public void markLeftAligned();

  /**
   * Mark that the following text should be printed out centered.
   */
  public void markCentered();

  /**
   * Mark that the following text should be printed out right aligned.
   */
  public void markRightAligned();
}

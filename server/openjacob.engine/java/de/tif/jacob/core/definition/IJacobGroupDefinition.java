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

package de.tif.jacob.core.definition;

import java.awt.Color;
import java.awt.Rectangle;

import de.tif.jacob.core.definition.guielements.ResizeMode;


/**
 * This interface represents a html group definition.
 *  
 * @author Andreas Herz
 */
public interface IJacobGroupDefinition extends IGroupDefinition
{

  public Rectangle getRectangle();

  public boolean hasBorder();
  
  /**
   * @since 2.8.6
   */
  public int getBorderWidth();
  
  /**
   * @since 2.8.6
   */
  public Color getBorderColor();
  
  /**
   * @since 2.8.6
   */
  public Color getBackgroundColor();
  
  /**
   * Gibt an wie sich der Container/Gruppe verhalten soll wenn Elemente (Kinder) ausserhalb des 
   * Containers positioniert sind.<br>
   * <br>
   * <i>
   * Anmerkung: Ist hier und nicht bei IGroupDefinition deklariert, da ein "resize" bei einer IHtmlGroup 
   *                     oder IMutableGroup keinen Sinn macht.
   * </i>
   * @since 2.10
   * 
   * @return
   */
  public ResizeMode getResizeMode();
}

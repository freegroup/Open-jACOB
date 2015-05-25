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
/*
 * Created on 03.04.2006
 *
 */
package de.tif.jacob.designer.model;

public interface IFontProviderModel
{
  public int getFontSize();

  public void setFontSize(int size);

  public String getFontFamily();

  public void setFontFamily(String family);

  public String getFontWeight();

  public void setFontWeight(String weight);

  public String getFontStyle();

  public void setFontStyle(String style);

  public String getAlign();

  public void setAlign(String align);
}
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
 * Created on Nov 2, 2004
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.draw2d.geometry.Point;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;

/**
 *
 */
public interface UIICaptionProviderModel
{
  /** Liefert niemals NULL zurück */
  public UICaptionModel getCaptionModel();
  
  public UIGroupModel   getGroupModel();
  public Point          getLocation();
  public String         getName();
  public String         getDefaultCaption();
  public String         getDefaultCaptionAlign(); // left or right (see CastorHorizontalAlignment)
  public String         getCaption();
  
  // for event handling
  public String         getHookClassName();
  
  // Der CaptionProvider schlägt dem Anwender ein I18N Key vor. Es ist dabei egal ob dieser
  // bereits im ResourceBundle vorhanden ist. Wird benötigt falls man eine Anwendung
  // auf I18N umstellen möchte.
  //
  public String         suggestI18NKey();
  
  // return true if the model should handle the caption as external element
  // return false if 'this' paint the caption.
  public boolean        isCaptionExtern();
  
  //
  public void          setCastorCaption(CastorCaption caption);
  public CastorCaption getCastorCaption();
}


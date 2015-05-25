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
 * Created on 28.02.2005
 *
 */
package de.tif.jacob.designer.editor.diagram.activity.editpart.anchor;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 *
 */
public class TopBottomAnchor extends ChopboxAnchor
{
  public TopBottomAnchor( IFigure owner) 
  {
  	super(owner);
  }

  public Point getLocation(Point reference)
  {
		Point ref = getBox().getCenter();
		getOwner().translateToAbsolute(ref);
		
		if(reference.y<ref.y)
		  ref.translate(0,-getBox().height/2);
		else
		  ref.translate(0,getBox().height/2);
		return ref;
  }
}

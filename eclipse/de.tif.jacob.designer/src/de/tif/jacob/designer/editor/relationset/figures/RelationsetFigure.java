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
 * Created on Jul 13, 2004
 */
package de.tif.jacob.designer.editor.relationset.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;

/**
 * Figure which represents the whole diagram - the view which corresponds to the
 * Schema model object
 * @author Phil Zoio
 */
public class RelationsetFigure extends FreeformLayer
{

  public static int LAST_X;
  public static int LAST_Y;
  
	public RelationsetFigure()
	{
		setOpaque(true);
    
    addMouseMotionListener(new MouseMotionListener()
    {
      public void mouseMoved(MouseEvent me)
      {
        // TODO Auto-generated method stub
//        System.out.println(me);
        LAST_X = me.x;
        LAST_Y  = me.y;
      }
    
      public void mouseHover(MouseEvent me)
      {
        // TODO Auto-generated method stub
      }
    
      public void mouseExited(MouseEvent me)
      {
        // TODO Auto-generated method stub
      }
    
      public void mouseEntered(MouseEvent me)
      {
        // TODO Auto-generated method stub
      }
    
      public void mouseDragged(MouseEvent me)
      {
        // TODO Auto-generated method stub
      }
    });
	}

}
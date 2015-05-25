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
 * Created on 20.07.2005
 *
 */
package de.tif.jacob.designer.util;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.internal.OverlayIcon;
import org.eclipse.ui.internal.util.Util;
/**
 * An OverlayIcon consists of a main icon and an overlay icon
 */
public class OverlayImageDescriptor extends CompositeImageDescriptor
{
  private ImageData base    = null;
  private ImageData overlay = null;

  public OverlayImageDescriptor(ImageDescriptor base, ImageDescriptor overlay)
  {
    this.base    = base.getImageData();
    this.overlay = overlay.getImageData();
    if(this.base==null)
      this.base=ImageDescriptor.getMissingImageDescriptor().getImageData();
  }

  protected void drawCompositeImage(int width, int height) 
  {
    drawImage(base, 0, 0);
    int y = base.height - overlay.height;
    drawImage(overlay, 0, y);
 } 
  

  protected Point getSize()
  {
    return new Point(base.width, base.height);
  }
}
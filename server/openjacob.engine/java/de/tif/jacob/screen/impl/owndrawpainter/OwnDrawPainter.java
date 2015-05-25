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

package de.tif.jacob.screen.impl.owndrawpainter;

import java.awt.Dimension;
import java.awt.Graphics;
import de.tif.jacob.screen.IClientContext;

public interface OwnDrawPainter
{
  /**
   * This hook method will be called, if the own draw area has to be refreshed.
   * Therefore, the image within the own draw area has to be painted completely
   * by means of this method.
   * 
   * @param context
   *          The current client context
   * @param graphics
   *          The graphics object to be used for all draw operations
   * @param dimension
   *          The dimension of the own draw area
   */
  public abstract void paint(IClientContext context, Graphics graphics, Dimension dimension) throws Exception;

}

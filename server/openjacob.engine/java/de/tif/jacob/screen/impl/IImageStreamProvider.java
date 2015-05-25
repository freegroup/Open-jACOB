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

package de.tif.jacob.screen.impl;

import java.io.OutputStream;

import de.tif.jacob.screen.IClientContext;

/**
 * @author Andreas Sonntag
   * @since 2.8.7
 */
public interface IImageStreamProvider
{
  static public final String RCS_ID = "$Id: IImageStreamProvider.java,v 1.1 2009/07/02 14:18:51 ibissw Exp $";
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Returns the content type of the image.
   * 
   * @param context
   *          the client context
   * @param imageId
   *          the optional image id
   * @return the content type (e.g. "image/png") or <code>null</code>, if no
   *         content type could be determined
   * @throws Exception
   *           on any problem
   */
  public String getImageContentType(IClientContext context, String imageId) throws Exception;

  /**
   * Writes the image of the given id to the stream.
   * 
   * @param context
   *          the client context
   * @param out
   *          the output stream to write the image to
   * @param imageId
   *          the optional image id
   * @return <code>true</code> if the image was written or <code>false</code> if
   *         no image should be displayed
   * @throws Exception
   *           on any problem
   */
  public boolean writeImage(IClientContext context, OutputStream out, String imageId) throws Exception;
}

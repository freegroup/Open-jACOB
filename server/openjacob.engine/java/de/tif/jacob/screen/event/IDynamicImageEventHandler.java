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

package de.tif.jacob.screen.event;

import java.io.OutputStream;

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IDynamicImage;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.IGuiElement.GroupState;

/**
 * Abstract event handler class for dynamic images. Derived implementations of this
 * event handler class have to be used to "hook" application-specific business
 * logic to dynamic images.
 * 
 * @author Andreas Sonntag
 * @since 2.8.7
 */
public abstract class IDynamicImageEventHandler extends IGroupMemberEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IDynamicImageEventHandler.java,v 1.1 2009/07/02 13:50:58 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  /**
   * Returns the content type of the image.
   * 
   * @param context
   *          the client context
   * @param imageId
   *          the optional image id
   * @return the content type (default is "image/png") or <code>null</code>, if no
   *         content type could be determined
   * @throws Exception
   *           on any problem
   */
  public String getImageContentType(IClientContext context, String imageId) throws Exception
  {
    return "image/png";
  }

  /**
   * Writes the image of the given id to the stream.
   * 
   * @param context
   *          the client context
   * @param out
   *          the output stream to write the image to
   * @param imageId
   *          the optional image id
   * @param width
   *          the width of the image to generate
   * @param height
   *          the height of the image to generate
   * @return <code>true</code> if the image was written or <code>false</code> if
   *         no image should be displayed
   * @throws Exception
   *           on any problem
   */
  public abstract boolean writeImage(IClientContext context, OutputStream out, String imageId, int width, int height) throws Exception;

  public final void onGroupStatusChanged(IClientContext context, GroupState state, IGuiElement element) throws Exception
  {
    this.onGroupStatusChanged(context, state, (IDynamicImage) element);
  }

  public void onGroupStatusChanged(IClientContext context, GroupState state, IDynamicImage image) throws Exception
  {
  }
}

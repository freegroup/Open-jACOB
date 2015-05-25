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

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IForm;
import de.tif.jacob.screen.IMutableForm;

/**
 * 
 * FOR INTERNAL USE AT THE MOMENT
 * 
 * @author Andreas Herz
 */
public abstract class IMutableFormEventHandler extends IFormEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IMutableFormEventHandler.java,v 1.1 2007/06/22 11:25:44 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public final void onHide(IClientContext context, IForm form) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onHide(context, (IMutableForm)form);
  }
  public final void onShow(IClientContext context, IForm form) throws Exception
  {
    // redirect to more confortable function signature
    // => No casting required for the application programmer
    //
    onShow(context,(IMutableForm)form);
  }

  public void onHide(IClientContext context, IMutableForm form) throws Exception
  {
  }
  
  public void onShow(IClientContext context, IMutableForm form) throws Exception
  {
  }

}

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

package jacob.event.ui.request;

import jacob.common.BreadCrumbController_RequestCategory;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IBreadCrumb;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IBreadCrumbEventHandler;

/**
 * 
 * @author achim
 **/
public class RequestBreadCrumb extends IBreadCrumbEventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: RequestBreadCrumb.java,v 1.1 2009/02/17 15:12:16 A.Boeken Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public void onClick(IClientContext context, String pathToSegment, String segment, IBreadCrumb emitter)
  {
    try
    {
      BreadCrumbController_RequestCategory.onNavigate(context, pathToSegment, segment);
    }
    catch(Exception exc )
    {
      ExceptionHandler.handle(exc);
    }
  }
}

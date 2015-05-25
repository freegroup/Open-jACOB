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

package de.tif.jacob.screen;

import de.tif.jacob.core.Session;
import de.tif.jacob.core.data.IDataBrowser;

/**
 * @author Andreas Herz
 *
 */
public interface IApplication extends IGuiElement
{
  static public final String RCS_ID = "$Id: IApplication.java,v 1.4 2009/12/23 10:48:51 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.4 $";

  /**
   * 
   * @param context
   * @param domain
   * @throws Exception
   */
  public void setActiveDomain(IClientContext context, IDomain domain) throws Exception;
  
  public String  getVersion();
  public String  getName();
  public Session getSession();
  
  /** 
   * Returns <code>true</code> whether one form is in update or new mode.<br>
   * This means that the user has unsaved content.
   * 
   * @throws Exception 
   *
   **/
  public boolean isDirty(IClientContext context) throws Exception;

  /**
   * Close the application immediately.
   *
   */
  public void close();
  

  /**
   * Reset all DataAccessor and GUI Elemetns of the Application instance
   * 
   * @param context
   * @throws Exception
   */
  public void    clear(IClientContext context) throws Exception;
  
  /**
   * Returns the current state of the application object. 
   * 
   * @return <code>true</code> if closed, otherwise <code>false</code> 
   */
  public boolean isClosed();
  
  /**
   * 
   * @return true if the application currently in the reporting creation/modifiy mode
   */
  public boolean     isInReportMode(IClientContext context);
  public IDataBrowser getReportDataBrowser(IClientContext context);
  public void        setReportDataBrowser(IClientContext context, IDataBrowser browser);
  
  public void     setToolbar(IToolbar toolbar);
  public IToolbar  getToolbar();
  public void     setToolbarVisible(boolean flag);
  public boolean  isToolbarVisible();
  
  public void setSearchBrowserVisible(boolean searchBrowserVisible);
  public boolean isSearchBrowserVisible(IClientContext context);
  
  public void     setNavigationVisible(boolean flag);
  public boolean isNavigationVisible();
}

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

import de.tif.jacob.core.ManagedResource;
import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.definition.DataScope;
import de.tif.jacob.core.definition.impl.AbstractApplicationDefinition;
import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.deployment.ClassProvider;
import de.tif.jacob.report.IReport;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.dialogs.IExcelDialog;
import de.tif.jacob.screen.event.IApplicationEventHandler;

/**
 *
 */
public interface HTTPApplication extends IApplication
{
  /**
   * Gets the unique application browser id.
   */
  public String getHTTPApplicationId();
  
  /**
   * Bietet das Excel Dokument zum download an OHNE einen Seitenrefresh oder
   * einen JacobDialog zu öffnen!
   */
  public void doForward(IExcelDialog dialog);
  
  public void addReportColumn(String alias, String column, String label) throws IllegalStateException, NoSuchFieldException;
  public void startReportMode(IClientContext context, IReport report);
  public void startReportMode(IClientContext context);
  public void stopReportMode(IClientContext context);

  public void setSession(HTTPClientSession session) throws Exception;
  
  public void init(HTTPClientContext context) throws Exception;
  
  public int  getSecondsBeforeDestroy();
  public void setSecondsBeforeDestroy(int sec);
  
  public boolean hasChildInDataStatus(IClientContext context, IGuiElement.GroupState state) throws Exception;
  
  public void register(ManagedResource resource);
  public void unregister(ManagedResource resource);
  
  public void   setPropertyForWindow(Object key, Object value);
  public Object getPropertyForWindow(Object key);

  public int    getDividerPos();
  
  public boolean isLookupEventHandlerByReference();
  public IApplicationEventHandler getEventHandler() throws Exception;
  
  public HTTPDomain getActiveDomain();
  
  // Verwaltungsfunktionen um zu prüfen ob der einmalige Aufruf des LoginHook
  // schon erledigt wurde.
  //
  public boolean hasLoginHookCalled();
  public void setLoginHookCalled();
  
  // Verwaltungsfunktionen um zu prüfen ob der pro Fenster einmalige Aufruf des CreatedHook
  // aufgerufen wurde.
  //
  public boolean hasCreatedHookCalled();
  public void setCreatedHookCalled();
  
  // Wird nur aufgerufen wenn sich alle Domains EINEN IDataAcccessor teilen sollen
  // Dies kann in de AdminConsole unter den Properties eingestellt werden
  //
  public IDataAccessor getDataAccessor();

  public DataField[] getDataFields();
  
  public DataScope getDataScope();

  // Stück für Stück alle allgemeinen Funktionen von HTML/XUL auf diese Ebenen heben
  //
  static class Command
  {
    public static IApplicationEventHandler getEventHandler(HTTPApplication app) throws Exception
    {
      AbstractApplicationDefinition appDef = (AbstractApplicationDefinition)app.getApplicationDefinition();
      String eventHandler = appDef.getEventHandler();
      Object obj = ClassProvider.getInstance(appDef,eventHandler);
      if(obj!=null)
      {
        if(!(obj instanceof IApplicationEventHandler))
          throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+IApplicationEventHandler.class.getName()+"]");
      }
      return (IApplicationEventHandler)obj;
    }

  }
  /**
   * Sends an ABORT message to the current running context related 
   * to the "application". The current running context can ignore 
   * this event if the didn't provide a "abort" mechanism.<br>
   *  
   * @since 2.8.8
   */
  public void requestAbort();
  
  /**
   * Return true if the current running context should abort and rollback
   * all done operations. 
   *  
   * @since 2.8.8
   */
  public boolean shouldAbort();
  
  /**
   * Set the flag that all running context related to the window 
   * can be aborted.
   * 
   * @param flag
   * 
   * @since 2.8.8
   */
  public void canAbort(boolean flag);
  
  /**
   * Check whenever the context provides the "abort" feature.
   *  
   * @since 2.8.8
   */
  public boolean canAbort();
  
 }

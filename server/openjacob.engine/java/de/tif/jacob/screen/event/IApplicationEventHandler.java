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

import de.tif.jacob.core.definition.IApplicationDefinition;
import de.tif.jacob.event.EventHandler;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;

/**
 * Abstract event handler class for the application object. Derived
 * implementations of this event handler class have to be used to "hook"
 * application-specific business logic to the application object.
 * 
 * @author Andreas Herz
 */
public abstract class IApplicationEventHandler extends EventHandler
{
  /**
   * The internal revision control system id.
   */
  static public final transient String RCS_ID = "$Id: IApplicationEventHandler.java,v 1.5 2010/08/19 09:33:33 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final transient String RCS_REV = "$Revision: 1.5 $";

  /**
   * This hook method will be called, if the application is started up (i.e.
   * during server run up) or the state of the application is changed from
   * 'inactive' to 'productive'.
   * <p>
   * You can startup some additional worker threads. Be in mind - you have NO
   * access to your application database at this point.
   * <p>
   * 
   * If you throw an exception, the application will not be activated and hence
   * does not appear in the login screen. The application will be marked as
   * undeployed in the administration application.<br><br>
   * <b>Be in mind - you didn't have access to your application database at this point.</b>
   *
   * @deprecated use {@link #onStartup(IApplicationDefinition)} instead
   */
  public  void onStartup() throws Exception
  {
  }

  /**
   * This hook method will be called, if the application is started up (i.e.
   * during server run up) or the state of the application is changed from
   * 'inactive' to 'productive'.
   * <p>
   * You can startup some additional worker threads. Be in mind - you have NO
   * access to your application database at this point.
   * <p>
   * 
   * If you throw an exception, the application will not be activated and hence
   * does not appear in the login screen. The application will be marked as
   * undeployed in the administration application.
   * <br>
   * <br>
   * <b>Be in mind - you didn't have access to your application database at this point.</b>
   * @since 2.10
   */
  public  void onStartup(IApplicationDefinition appDef) throws Exception
  {
    // redirect to the old interface if the Application hook didn't override the new method.
    // Be backward compatible.
    this.onStartup();
  }

  /**
   * This hook method will be called, if the application is shutdowned or the
   * state of the application is changed from 'productive' to 'inactive'.
   * <p>
   * 
   * You should shutdown and free your additional allocated resources here.
   * <p>
   * 
   * <b>Be in mind - you didn't have access to your application database at this point.</b>
   * @deprecated use {@link #onShutdown(IApplicationDefinition)} instead
   */
  public void onShutdown() throws Exception
  {
  }

  /**
   * This hook method will be called, if the application is shutdowned or the
   * state of the application is changed from 'productive' to 'inactive'.
   * <p>
   * 
   * You should shutdown and free your additional allocated resources here.
   * <p>
   * 
   * <b>Be in mind - you didn't have access to your application database at this point.</b>
   * @since 2.10
   */
  public void onShutdown(IApplicationDefinition appDef) throws Exception
  {
    // redirect to the old interface if the Application hook didn't override the new method.
    // Be backward compatible.
    this.onShutdown();
 }

  /**
   * Action events will be fired before the application will be closed. The close
   * event must be trigged by an user event. So that they may be vetoed or display 
   * additional dialogs or informations to the user.
   * 
   * @param context
   *          The current client context of the application
   * @param app
   *          The application UI instance.
   * @return <code>true</code> if the normal close procedure can be continued.
   * @since 2.8.0
   **/
  public boolean canClose(IClientContext context, IApplication app)
  {
    return true;
  }

  /**
   * This hook method will be called, if the user logs into an application.
   * <p>
   * You can access the application database at this point.
   * <p>
   * Note: In contrast to {@link #onCreate(IClientContext, IApplication)} this
   * hook method will be called once upon logon, when a new user session and,
   * therefore the first application GUI instance (i.e. the application main
   * window) is created.
   * 
   * @param context
   *          The current client context of the application
   * @param app
   *          The application UI instance which has been created on logon.
   */
  public void onLogin(IClientContext context, IApplication app)
  {
  }

  /**
   * This hook method will be called, if the user logs out from the application.
   * <p>
   * You can access the application database at this point.
   * <p>
   * 
   * @param context
   *          The current client context of the application
   * @param app
   *          The application GUI instance which has been created on logon.
   */
  public void onLogout(IClientContext context, IApplication app)
  {
  }

  /**
   * This hook method will be called, if an application GUI instance (i.e. the
   * application main window) has been created. <br>
   * For <b>each </b> browser window an application instance will be
   * instantiated.
   * <p>
   * 
   * You can access the application database at this point.
   * <p>
   * 
   * Note: It is possible that a logged in user has more than one
   * application instance. This method will be called for each instance.
   * 
   * @param context
   *          The current client context of the application
   * @param app
   *          The application GUI instance itself.
   */
  public abstract void onCreate(IClientContext context, IApplication app);
}

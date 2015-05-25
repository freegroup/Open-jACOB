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

import de.tif.jacob.screen.IActionEmitter;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGuiElement;

/**
 * Abstract event handler class for action buttons, i.e search, new, delete and
 * update buttons. Derived implementations of this event handler class have to
 * be used to "hook" application-specific business logic to action buttons.
 * 
 * @author Andreas Herz
 */
public abstract class IActionButtonEventHandler extends IGroupMemberEventHandler
{
	/**
	 * The internal revision control system id.
	 */
  static public final transient String RCS_ID = "$Id: IActionButtonEventHandler.java,v 1.1 2007/01/19 09:50:40 freegroup Exp $";
  
	/**
	 * The internal revision control system id in short form.
	 */
  static public final transient String RCS_REV = "$Revision: 1.1 $";

	/**
   * This event method will be called, if the corresponding button has been
   * pressed. You can prevent the execution of the action, if you return
   * <code>false</code>. Nevertheless, you should inform the user by means of
   * a proper notification, e.g. create and show a message dialog.
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @return Return <code>false</code>, if you want to avoid the execution of
   *         the action else return <code>true</code>.
   */
  public abstract boolean beforeAction(IClientContext context,IActionEmitter button) throws Exception;
  
	/**
   * This event method will be called, if the action has been successfully
   * executed.
   * <p>
   * <b>Note for new buttons</b>: You can use this method to initialize or reset GUI
   * input fields which are connected to a data field after a new record action
   * has been performed. Nevertheless, you should do this by modifying the data
   * fields of the selected record instead of modifying the GUI field directly.
   * The later has <b>no</b> effect, since the application server will update
   * the GUI fields according to the values of the respective data fields after
   * this method has been invoked.<br>
   * Example:
   * 
   * <pre>
   * public void onSuccess(IClientContext context, IGuiElement button) throws Exception
   * {
   *   // clear generated project number which might still be set by previous (template) record
   *   IDataTableRecord currentRecord = context.getSelectedRecord();
   *   currentRecord.setValue(currentRecord.getCurrentTransaction(), &quot;projectnumber&quot;, null);
   * }
   * </pre>
   * instead of
   * <pre>
   * public void onSuccess(IClientContext context, IGuiElement button) throws Exception
   * {
   *   // clear generated project number which might still be set by previous (template) record
   *   context.getGroup().setInputFieldValue(&quot;salesprojectProjectnumber&quot;, &quot;&quot;);
   * }
   * </pre>
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @throws Exception
   *           If an exception is thrown,
   *           {@link #onError(IClientContext, IGuiElement, Exception)} will be
   *           called as well.
   */
  public abstract void onSuccess(IClientContext context, IGuiElement button) throws Exception;
  
  /**
   * This event method will be called, if the execution of the action fails by
   * any reason. You should not try to rethrow the reason exception, since this
   * exception is already handled by the jACOB application server. Nevertheless,
   * you could add additional code for error handling, e.g. do some kind of
   * application logging.
   * 
   * @param context
   *          The current context of the application
   * @param button
   *          The action button (the emitter of the event)
   * @param reason
   *          The reason why the action has failed
   */
  public void onError(IClientContext context, IGuiElement button, Exception reason)
  {
  	// default implementation: just do nothing!
  }
}

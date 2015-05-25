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

package de.tif.jacob.screen.impl.html;

import java.awt.Rectangle;
import java.util.Map;
import java.util.Vector;

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearch;
import de.tif.jacob.core.definition.actiontypes.ActionTypeSearchUpdateRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IActionButtonEventHandler;
import de.tif.jacob.screen.event.IGroupMemberEventHandler;
import de.tif.jacob.screen.impl.ActionTypeHandler;
import de.tif.jacob.screen.impl.HTTPActionEmitter;

/**
 * @author Andreas Herz
 *
 */
public abstract class ActionEmitter extends GuiHtmlElement implements HTTPActionEmitter
{
  static public final transient String RCS_ID = "$Id: ActionEmitter.java,v 1.7 2010/10/15 08:04:46 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.7 $";

  private ActionType action;
  
  private boolean safeSearch;
  
  protected ActionEmitter(IApplication app, String name, String label, boolean isVisible, Rectangle boundingRect, ActionType action, Map properties)
  {
    super(app, name, label, isVisible, boundingRect, properties);
    this.action = action;
    // IBIS: Was soll safeSearch an einem ExitButton/CancelButton/Clear......
    if( action instanceof ActionTypeSearch )
      this.safeSearch = ((ActionTypeSearch) action).isSafeMode();
    else if( action instanceof ActionTypeSearchUpdateRecord )
      this.safeSearch = ((ActionTypeSearchUpdateRecord) action).isSafeMode();
  }

  /**
   * The framework call this method if this event comes from the client guiBrowser.
   * 
   */
  public boolean processEvent(IClientContext context, int guid, String event, String value) throws Exception
  {
    // The event is not for this object
    //
    if(guid!=this.getId())
      return false;

    if(action==null)
      return true;
    
    action.execute(ActionTypeHandler.INSTANCE, context, this);
    
    return true;
  }
  
  /**
   * Calls the eventhandler for the Button event handler
   * 
   */
  public void onOuterGroupDataStatusChanged(de.tif.jacob.screen.IClientContext context, GroupState groupStatus) throws Exception
  {
    try
    {
      if(action!=null)
        action.onOuterGroupStatusChanged(ActionTypeHandler.INSTANCE , context, this, groupStatus);
      // call the eventhandler if any exists
      // The event handler can override the status (enable/disable) of the button
      //
      Object obj = getEventHandler(context);
      if(obj instanceof IActionButtonEventHandler)
        ((IActionButtonEventHandler)obj).onOuterGroupStatusChanged(context,groupStatus, this);
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context,e);
    }
    super.onOuterGroupDataStatusChanged(context,groupStatus);
  }

  /**
   * Calls the eventhandler for the Button event handler
   * 
   */
  public void onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext context, GroupState groupStatus) throws Exception
  {
    try
    {
      if(action!=null)
        action.onGroupStatusChanged(ActionTypeHandler.INSTANCE , context, this, groupStatus);
      
      // call the eventhandler if any exists
      // The event handler can override the status (enable/disable) of the button
      //
      Object obj = getEventHandler(context);
      if(obj instanceof IGroupMemberEventHandler)
         ((IGroupMemberEventHandler)obj).onGroupStatusChanged(context,groupStatus, this);
    }
    catch (Exception e)
    {
      ExceptionHandler.handle(context,e);
    }
    super.onGroupDataStatusChanged(context,groupStatus);
  }
  
  /* 
   * @see de.tif.jacob.screen.GUIElement#addDataFields(java.Util.Vector)
   * @author Andreas Herz
   */
  protected final void addDataFields(Vector fields)
  {
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.screen.IActionEmitter#setSearchMode(boolean)
	 */
	public void setSearchMode(boolean safeSearch)
	{
    this.safeSearch = safeSearch;
	}

	/**
	 * @return Returns the safeSearch.
	 */
	public final boolean isSafeSearch()
	{
		return safeSearch;
	}
	
  /**
   * Returns <b>true</b> if the handsover Action are the same type of action of the
   * button.
   * @param type
   * @return
   */
  protected boolean isTypeOf(ActionType type)
  {
    return type.getClass() == action.getClass();
  }
  
	/**
	 * @return Returns the action.
	 */
	public ActionType getAction(IClientContext context)
	{
		// if the action is not executable, do not return it
		if (action != null && !action.isExecutable(context, this))
		{
		  // since ActionTypeNavigateToForm.isExecutable() is a expensive operation -> calculate once
		  // Note: This only works for actions which do not dynamically change within the same context!
		  action = null;
		}
		return action;
	}

}

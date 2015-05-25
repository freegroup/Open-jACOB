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
import de.tif.jacob.screen.IInFormLongText;
import de.tif.jacob.screen.IText;
import de.tif.jacob.screen.IGuiElement.GroupState;


/**
 * Abstract event handler class for inform long text fields. Derived implementations of
 * this event handler class have to be used to "hook" application-specific
 * business logic to text fields.
 * 
 * @author Andreas Herz
 * @since 2.10
 */
public abstract class IInformLongTextEventHandler extends ITextFieldEventHandler// IGroupMemberEventHandler
{
  static public final transient String RCS_ID = "$Id: IInformLongTextEventHandler.java,v 1.3 2010/10/13 09:12:15 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

 /**
   * To be backward compatible. Called by the engine. Old jACOB application did have implement the eventhandler
   * of the class ITextFieldEventHandler. 
   **/
  final public  void onGroupStatusChanged(IClientContext context, GroupState state, IText text) throws Exception
  {
    // redirect to the standard implementation
    onGroupStatusChanged(context, state, (IInFormLongText) text);
  }  
  
  /**
   * Method wrapper for the generic onGroupStatus changed event handling. <br>
   * Only to enforce a propper interface for the user.<br>
   * 
   * @since 2.10
   * @param context
   * @param state
   * @param text
   * @throws Exception
   * 
   * 
   */
  public void onGroupStatusChanged(IClientContext context, GroupState state, IInFormLongText text) throws Exception
  {
  }
}

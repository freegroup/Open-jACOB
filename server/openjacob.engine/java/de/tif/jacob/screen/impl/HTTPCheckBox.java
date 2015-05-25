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

import java.io.IOException;

import de.tif.jacob.core.exception.UserException;
import de.tif.jacob.screen.ICheckBox;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.ICheckBoxEventHandler;

/**
 *
 */
public interface HTTPCheckBox extends ICheckBox
{

  // Da es in Java keine Mehrfachvererbung gibt, erben die Klassem alle Eigenschaften
  // von GuiElement und delegieren Ihre objektspezifischen Aufrufe an die statische Klasse
  // 'Command'. Command ist somit nur für die objektspezifischen Eigenschaften zuständig - nicht
  // für die Allgemeinen!!
  //
  static class Command
  {
    /**
     * The state of the checkbox will handled in the processEvent method.
     * 
     */
    public static boolean processParameter(HTTPCheckBox self, int guid, String data) throws IOException, NoSuchFieldException
    {
      // The value will be set/received from the processEvent-method
      // do nothing here.
      if(self.getId()!=guid)
        return false;
      
      self.setValue(data);
      
      return true;
    }

    public static boolean processEvent(HTTPCheckBox self, IClientContext context, int guid, String event, String value) throws Exception
    {
      ICheckBoxEventHandler handler = (ICheckBoxEventHandler)((GuiElement)self).getEventHandler(context);
      
      if(handler==null)
        return true;
      
      if (self.isChecked())
        handler.onCheck(context, self);
      else
        handler.onUncheck(context, self);
      
      return true;
    }
    
    /* 
     * @see de.tif.jacob.screen.impl.html.GuiHtmlElement#onGroupDataStatusChanged(de.tif.jacob.screen.IClientContext, de.tif.jacob.screen.IGuiElement.GroupState)
     */
    public static void onGroupDataStatusChanged(GuiElement self, IClientContext context,GroupState groupStatus)  throws Exception
    {
      // call the eventhandler if any exists
      // The event handler can override the status (enable/disable) of the textfield
      // or it can calculate the new value if the element a non DB bounded element.
      //
      Object obj = self.getEventHandler(context);
      if(obj!=null)
      {
        if(obj instanceof ICheckBoxEventHandler)
          ((ICheckBoxEventHandler)obj).onGroupStatusChanged(context,groupStatus, self);
        else
          throw new UserException("Class ["+obj.getClass().getName()+"] does not implement required interface/class ["+ICheckBoxEventHandler.class.getName()+"]");
      }
    }
  }
}

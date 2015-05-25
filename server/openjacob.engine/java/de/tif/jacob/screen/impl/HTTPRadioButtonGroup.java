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

import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IMutableRadioButtonGroup;
import de.tif.jacob.screen.event.IRadioButtonGroupEventHandler;

/**
 *
 */
public interface HTTPRadioButtonGroup extends IMutableRadioButtonGroup
{
  // Da es in Java keine Mehrfachvererbung gibt, erben die Klassem alle Eigenschaften
  // von GuiElement und delegieren Ihre objektspezifischen Aufrufe an die statische Klasse
  // 'Command'. Command ist somit nur für die objektspezifischen Eigenschaften zuständig - nicht
  // für die Allgemeinen!!
  //
  static class Command
  {
    public static boolean processEvent(HTTPRadioButtonGroup self, IClientContext context, int guid, String event, String value) throws Exception
    {
      IRadioButtonGroupEventHandler handler = (IRadioButtonGroupEventHandler)((GuiElement)self).getEventHandler(context);
      if(handler!=null) 
        handler.onSelect(context, self);

      return true;
    }
   
    public static final void onGroupDataStatusChanged(HTTPRadioButtonGroup self, IClientContext context, GroupState status) throws Exception
    {
      // call the eventhandler if any exists
      // The event handler can override the status (enable/disable) of the comboBox
      //
      IRadioButtonGroupEventHandler buttonHandler = (IRadioButtonGroupEventHandler)((GuiElement)self).getEventHandler(context);
      if(buttonHandler!=null)
        buttonHandler.onGroupStatusChanged(context,status, self);
    }
  }
}

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

import java.util.Locale;
import de.tif.jacob.screen.ICaption;
import de.tif.jacob.screen.IClientContext;

/**
 *
 */
public interface HTTPCaption extends ICaption
{
  public String getI18NLabel(Locale locale);
  
  // Da es in Java keine Mehrfachvererbung gibt, erben die Klassem alle Eigenschaften
  // von GuiElement und delegieren Ihre objektspezifischen Aufrufe an die statische Klasse
  // 'Command'. Command ist somit nur für die objektspezifischen Eigenschaften zuständig - nicht
  // für die Allgemeinen!!
  //
  static class Command
  {
    public static boolean processEvent(HTTPCaption self, IClientContext context, int guid, String event, String value) throws Exception
    {
      if(guid == self.getId() && event.equals("click") )
      {
        Object obj = self.getParent();
        // Eine Caption kann auch an einem Image hängen...ist ein bischen unsauber 
        if(obj instanceof HTTPSingleDataGuiElement)
        {
	        HTTPSingleDataGuiElement e =(HTTPSingleDataGuiElement)obj;
	        // Das eingabefeld muss nicht an die Datenbank gebunden sein...
	        if(e.getDataField()!=null)
	        {
		        String table =e.getDataField().getTableAlias().getName();
		        String field =e.getDataField().getField().getName();
		
		        HTTPApplication app = (HTTPApplication)context.getApplication();
		        app.addReportColumn(table, field, self.getI18NLabel(context.getLocale()));
	        }
        }
      }
      return true;
    }
    
  }
}

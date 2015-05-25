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

import java.io.Writer;

import de.tif.jacob.i18n.I18N;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.IListBoxEventHandler;
import de.tif.jacob.screen.impl.html.ClientSession;
import de.tif.jacob.screen.impl.html.ListBox;
import de.tif.jacob.util.StringUtil;

public abstract class ListBoxAction
{
  private String i18nTooltip=null;
  
  public abstract String getJavaScriptEventHandler(HTTPListBox listbox);
  public abstract String getImageName();
  public abstract String getTooltipMessageId();
  
  public final void calculateHTML(IClientContext context,HTTPListBox listbox,  Writer w) throws Exception
  {
      boolean hasHandler = (((ListBox)listbox).getEventHandler(context) instanceof IListBoxEventHandler);
      w.write("<img onClick=\"");
      w.write(getJavaScriptEventHandler(listbox));
      if (hasHandler)
        w.write("; FireEvent('" + Integer.toString(listbox.getId()) + "','change');");
      w.write("\" src=\"");
      w.write(((ClientSession)context.getSession()).getTheme().getImageURL(getImageName()));
      w.write("\" title=\"");
      w.write(StringUtil.htmlEncode(getI18NTooltip(context)));
      w.write("\"/>");

  }  
  
  private final String getI18NTooltip(IClientContext context)
  {
    if(i18nTooltip==null)
      return i18nTooltip = I18N.getCoreLocalized(getTooltipMessageId(), context, context.getLocale());
    else
      return i18nTooltip;
  }
   
}

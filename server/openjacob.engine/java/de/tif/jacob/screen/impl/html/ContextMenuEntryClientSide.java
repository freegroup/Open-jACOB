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

import java.io.Writer;

import de.tif.jacob.screen.IApplication;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public abstract class ContextMenuEntryClientSide extends ContextMenuEntry
{
  public abstract String getJavaScriptEventHandlerName();
  

  protected ContextMenuEntryClientSide(IApplication app, String label)
  {
    super(app, "", label, true, null, null);
  }
  
	/* 
	 * @see de.tif.jacob.screen.GUIElement#renderHTML(java.io.Writer)
	 */
	public final void calculateHTML(ClientContext c) throws Exception
	{
	  if(getCache()==null)
	  {  
	    Writer w = newCache();
	    w.write("<tr><td class='contextMenuItem' ><a class='contextMenuItem' href=# onClick=\"");
	    w.write(getJavaScriptEventHandlerName());
	    w.write("();\" >");
	    w.write(StringUtil.htmlEncode(getI18NLabel(c.getLocale())));
	    w.write("</a></td></tr>\n");
	  }
  }  
}
